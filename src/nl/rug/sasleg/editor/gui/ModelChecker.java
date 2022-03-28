package nl.rug.sasleg.editor.gui;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Vector;

import nl.rug.sasleg.editor.model.ConstraintFlow;
import nl.rug.sasleg.editor.model.Element;
import nl.rug.sasleg.editor.model.Flow;
import nl.rug.sasleg.editor.model.FrozenGroup;
import nl.rug.sasleg.editor.model.Group;
import nl.rug.sasleg.editor.model.Node;
import nl.rug.sasleg.editor.model.NormalFlow;
import nl.rug.sasleg.editor.model.Parallel;
import nl.rug.sasleg.editor.model.Process;

import org.flair.bp.v2.ctl.CTLGroup;
import org.flair.bp.v2.ctl.CTLImplies;
import org.flair.bp.v2.ctl.model.ElementAt;
import org.flair.bp.v2.ctl.parts.Phi;
import org.flair.bp.v2.ctl.parts.path.Finally;
import org.flair.bp.v2.ctl.parts.path.Globally;
import org.flair.bp.v2.ctl.parts.path.Next;
import org.flair.bp.v2.ctl.parts.path.PathQuantifier;
import org.flair.bp.v2.ctl.parts.path.Unless;
import org.flair.bp.v2.ctl.parts.state.All;
import org.flair.bp.v2.ctl.parts.state.Exists;
import org.flair.bp.v2.ctl.parts.state.Negation;
import org.flair.bp.v2.ctl.parts.state.Or;
import org.flair.bp.v2.ctl.parts.state.StateQuantifier;

public class ModelChecker
{
	private ArrayList<CTLGroup> groupFormulas = new ArrayList<CTLGroup>();
	
	private ArrayList<ConstraintFlow> cfList = new ArrayList<ConstraintFlow>();
	private CTLGroup cfFormulas = new CTLGroup();
	
	private ArrayList<Parallel> parList = new ArrayList<Parallel>();
	private CTLGroup parFormulas1 = new CTLGroup();
	private CTLGroup parFormulas2 = new CTLGroup();
	
	public ModelChecker(Process model)
	{
		createCTLFlowFormulas(model);
		createCTLGroupFormulas(model);
	}
	
	public void check(Process model)
	{
		Gui.instance().clearMessageTable();
		
		checkConstraintFlows(model);
		checkParallelFlows(model);
		checkGroupConstraints(model);
		
		Gui.instance().repaint();
	}
	
	private void createCTLFlowFormulas(Process model)
	{
		for (Node node : model.getNodes())
			for (Flow fl : node.getOutgoingFlows())
				if (fl instanceof ConstraintFlow)
					createCTLConstraintFlowFormula((ConstraintFlow)fl);
				else if (fl instanceof Parallel)
					createParallelConstraintFormula((Parallel)fl);
	}
	
	private void createCTLGroupFormulas(Process model)
    {
        for (Group gr : model.getGroups())
        	if (gr instanceof FrozenGroup && gr.getEndElements().size() == 1)
        	{	
        		if (gr.hasWeakLink())
        			createCTLWeakenLinkFormulas(gr, model);
        		else
        			createCTLFrozenGroupFormulas(gr, model);
        	}
    }
	
	private void createCTLConstraintFlowFormula(ConstraintFlow cf)
	{	
		Or orSource = createOrSet(cf.getSource());
		Or orTarget = createOrSet(cf.getTarget());
		
		Phi target = cf.isNegated()? new Negation(orTarget) : orTarget;

		PathQuantifier pq;
		if (cf.getPathQuantifier() == ConstraintFlow.PathQuantifier.NEXT)
			pq = new Next(target);
		else if (cf.isNegated() && 
				cf.getPathQuantifier() == ConstraintFlow.PathQuantifier.FINALLY)
			pq = new Globally(target);
		else
			pq = new Finally(target);
		
		StateQuantifier sq =
				cf.getStateQuantifier() == ConstraintFlow.StateQuantifier.ALL?
			new All(pq) : new Exists(pq);
		
		cfFormulas.add(new CTLImplies(orSource, sq));
		cfList.add(cf);
	}
	
	private Or createOrSet(Node node)
	{
		Or orSource = new Or();
		
		if (node instanceof Element)
			orSource.addPhi(new ElementAt(node));
		else if (node instanceof Group)
			for (Element setElem : ((Group)node).getElements())
				orSource.addPhi(new ElementAt(setElem));
		
		return orSource;
	}
	
	private void createParallelConstraintFormula(Parallel par)
	{
		Or orSource = createOrSet(par.getSource());
		Or orTarget = createOrSet(par.getTarget());
		
		All all1 = new All(new Globally(new Negation(orTarget)));
		All all2 = new All(new Globally(new Negation(orSource)));
		
		parFormulas1.add(new CTLImplies(orSource, all1));
		parFormulas2.add(new CTLImplies(orTarget, all2));
		parList.add(par);
	}
	
	private void checkConstraintFlows(Process model)
	{
		for (CTLImplies  impl : cfFormulas.getAll())
		{
			ConstraintFlow cf = cfList.get(cfFormulas.getAll().indexOf(impl));
			
			impl.setDepth(model.getNodes().size());
			
			if (!validateCTL(cf.getSource(), impl))
			{
				String pqStr = cf.getPathQuantifier() == 
										ConstraintFlow.PathQuantifier.NEXT?
					"directly" : "finally";
				
				if (cf.getStateQuantifier() == 
							ConstraintFlow.StateQuantifier.ALL)
				{
					Vector<String> row = new Vector<String>();
					
					String negStr = cf.isNegated()?
								"There shouldn't be a path" : "All paths";
					
					row.add(negStr + " from: " + cf.getSource().getId() + ":(" + 
							cf.getSource().getName() + ") " + " to " + 
							cf.getTarget().getId() + ":(" +
							cf.getTarget().getName() + ") should " + pqStr +
							" end" + " in " + cf.getTarget().getId() + ":(" +
							cf.getTarget().getName() + ")");
					row.add(cf.getId() + ": " + cf.getName());
					
					Gui.instance().addErrorMessage(row);
				}
				else
				{
					Vector<String> row = new Vector<String>();
					
					String negStr = cf.isNegated()? "A Path" : "No path";
					
					row.add(negStr + " " + pqStr + " exists from: " + 
							cf.getSource().getId() + ":(" + 
							cf.getSource().getName() + ") " + " to " + 
							cf.getTarget().getId() + ":(" +
							cf.getTarget().getName() + ") ");
					row.add(cf.getId() + ": " + cf.getName());
					
					Gui.instance().addErrorMessage(row);
				}
			}
		}
	}
	
	private boolean validateCTL(Node node, CTLImplies impl)
	{
		boolean validation = true;
		
		if (node instanceof Element)
		{
			if (!impl.validate(node))
				validation = false;
		}
		else if (node instanceof Group)
			for (Element el : ((Group)node).getElements())
				if(!impl.validate(el))
				{
					validation = false;
					break;
				}
		
		return validation;
	}
	
	private void checkParallelFlows(Process model)
	{
		for (Parallel par : parList)
		{
			CTLImplies impl1 = parFormulas1.get(parList.indexOf(par));
			CTLImplies impl2 = parFormulas2.get(parList.indexOf(par));
			
			impl1.setDepth(model.getNodes().size());
			impl2.setDepth(model.getNodes().size());
			
			if (!(validateCTL(par.getSource(), impl1) && 
						validateCTL(par.getTarget(), impl2)))
			{
				Vector<String> row = new Vector<String>();
				
				row.add(" from: " + par.getSource().getId() + ":(" + 
						par.getSource().getName() + ") " + " to " + 
						par.getTarget().getId() + ":(" +
						par.getTarget().getName() + ") should exclude" + 
						" each other in " + par.getTarget().getId() + ":(" +
						par.getTarget().getName() + ")");
				row.add(par.getId() + ": " + par.getName());
				
				Gui.instance().addErrorMessage(row);
			}
		}
	}

	private void creatAllNextEnd(Element elem, Node end, CTLGroup ctlGroup)
	{
		for (Node target : elem.getTargetElements())
    		if (target == end)
    		{
    			for (NormalFlow nf : elem.getWeakLinks())
				{
    				if (nf.getTarget() == end)
    				{
    					All all = new All(new Finally(new All(new Next(
    													new ElementAt(end)))));
    					ctlGroup.add(new CTLImplies(new ElementAt(elem), all));
    					
    					return;
    				}
				}
    			
    			ctlGroup.add(new CTLImplies(new ElementAt(elem), 
    									new All(new Next(new ElementAt(end)))));
    		}
	}
	
	private boolean isWeakLinkInPath(ArrayList<Element> path)
	{
		ListIterator<Element> li = path.listIterator();
		
		while (li.hasNext())
		{
			Element currentElem = li.next();
			if(currentElem.hasWeakLink())
			{
				for (NormalFlow nf : currentElem.getWeakLinks())
				{
					if (li.hasNext())
					{
						if (nf.getTarget() == li.next())
							return true;
						else
							li.previous();
					}
				}	
			}
		}
		
		return false;
	}
	
	private boolean isweakLinkBetween(Element elem, ArrayList<Element> path)
	{
		if(elem.hasWeakLink())
		{
			for (NormalFlow nf : elem.getWeakLinks())
			{
				int idx = path.indexOf(elem);
				
				if (path.size() > idx && nf.getTarget() == path.get(idx + 1))
					return true;
			}	
		}
		
		return false;
	}
	
	private Unless createInnerWeakenFormula(ArrayList<Element> path, Node end, 
												CTLGroup ctlGroup)
	{
		Unless unless = null;
		
		Or leftPart = new Or();
		
		if (!isWeakLinkInPath(path))
		{
			for (Node pathElem : path)
				leftPart.addPhi(new ElementAt(pathElem));
			
			unless = new Unless(leftPart, new ElementAt(end));
		}
		else
		{
			for (Element pathElem : path)
			{
				leftPart.addPhi(new ElementAt(pathElem));
				
				ArrayList<Element> copyList = new ArrayList<Element>(path);
				copyList.retainAll(
						path.subList(path.indexOf(pathElem) + 1, path.size()));
				
				if(isweakLinkBetween(pathElem, path))
				{
					Unless unless2 = 
							createInnerWeakenFormula(copyList, end, ctlGroup);
					
					All all = new All(new Finally(new All(unless2)));
					unless = new Unless(leftPart, all);
					
					break;
				}
			}
		}
			
		return unless;
	}
	
	private void createCTLWeakenLinkFormulas(Group group, Process model)
	{
		CTLGroup ctlGroup = new CTLGroup(group);

		Element end = null;
    	if (group.getEndElements().size() == 1)
    		end = group.getEndElements().get(0);
    	
    	for (Element elem : group.getElements())
        {
    		if (!elem.isFloating())
    		{
	    		ArrayList<ArrayList<Element>> allPaths = 
	    									group.getPaths(elem, end);
	
	    		if (allPaths.isEmpty())
	            	creatAllNextEnd(elem, end, ctlGroup);
	    		else
	    		{
	    			org.flair.bp.v2.ctl.parts.path.Or orPathSplit = 
										new org.flair.bp.v2.ctl.parts.path.Or();
	    			
	    			for (ArrayList<Element> path : allPaths)
	                {
	    				if (!isWeakLinkInPath(path))
	    				{
	                    	Or OrPath = new Or();
	                    	
	                    	for (Node pathElem : path)
	                    		OrPath.addPhi(new ElementAt(pathElem));
	                    	
	                    	Unless unless = 
	                    				new Unless(OrPath, new ElementAt(end));
	                    	orPathSplit.addPathQuantifier(unless);
	    				}
	    				else
	    					orPathSplit.addPathQuantifier(
	    							createInnerWeakenFormula(
	    												path, end, ctlGroup));
	                }
	    			
	    			if (elem.hasWeakLink())
	        		{
	    				All all = new All(new Finally(new All(orPathSplit)));
	                    ctlGroup.add(new CTLImplies(new ElementAt(elem), all));
	        		}
	        		else
	        		{
	        			All all = new All(orPathSplit);
	                    ctlGroup.add(new CTLImplies(new ElementAt(elem), all));
	        		}
	    		}
    		}
        }
    	
    	groupFormulas.add(ctlGroup);
	}
	
	private void createCTLFrozenGroupFormulas(Group group, Process model)
	{
		CTLGroup ctlGroup = new CTLGroup(group);
		System.out.println(ctlGroup);
		
    	Element end = null;
    	if (group.getEndElements().size() == 1)
    		end = group.getEndElements().get(0);
    	
    	for (Element elem : group.getElements())
        {
    		ArrayList<ArrayList<Element>> allPaths = group.getPaths(elem, end);
            
            if (allPaths.isEmpty())
            	creatAllNextEnd(elem, end, ctlGroup);
            else
            {
                org.flair.bp.v2.ctl.parts.path.Or orPathSplit = 
                					new org.flair.bp.v2.ctl.parts.path.Or();
                
                for (ArrayList<Element> path : allPaths)
                {
                	Or OrPath = new Or();
                	
                	for (Node pathElem : path)
                		OrPath.addPhi(new ElementAt(pathElem));
                	
                	Unless unless = new Unless(OrPath, new ElementAt(end));
                	orPathSplit.addPathQuantifier(unless);
                }
                	
                All all = new All(orPathSplit);
                ctlGroup.add(new CTLImplies(new ElementAt(elem), all));
            }
        }
    	
    	groupFormulas.add(ctlGroup);
	}
	
	private void checkGroupConstraints(Process model)
	{
    	for (CTLGroup ctlGroup : groupFormulas)
    	{
    		System.out.println(ctlGroup.toString());
    		ctlGroup.setDepth(model.getElements().size());
    		boolean validation = true;
	    	
    		for (Node grElem : ctlGroup.getGroupReference().getElements())
	    		if (!ctlGroup.validate(grElem))
	    		{
	    			validation = false;
	    			break;
	    		}
	    	
	    	if (!validation)
	    	{
		    	Vector<String> row = new Vector<String>();
				
				row.add("Frozen group has changed content.");
				row.add(ctlGroup.getGroupReference().getId() + ": " + 
						ctlGroup.getGroupReference().getName());
				
				Gui.instance().addErrorMessage(row);
	    	}
    	}
	}
}
