package nl.rug.sasleg.editor.gui;

import java.awt.geom.Point2D;
import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.wfmc._2008.xpdl2.Activity;
import org.wfmc._2008.xpdl2.Artifact;
import org.wfmc._2008.xpdl2.Artifacts;
import org.wfmc._2008.xpdl2.ConstraintFlow;
import org.wfmc._2008.xpdl2.ConstraintGroup;
import org.wfmc._2008.xpdl2.NodeGraphicsInfo;
import org.wfmc._2008.xpdl2.ObjectFactory;
import org.wfmc._2008.xpdl2.PackageType;
import org.wfmc._2008.xpdl2.ParallelConstraint;
import org.wfmc._2008.xpdl2.ProcessType;
import org.wfmc._2008.xpdl2.Transition;

import nl.rug.sasleg.editor.model.ConstraintFlow.PathQuantifier;
import nl.rug.sasleg.editor.model.ConstraintFlow.StateQuantifier;
import nl.rug.sasleg.editor.model.Element;
import nl.rug.sasleg.editor.model.Flow;
import nl.rug.sasleg.editor.model.Group;
import nl.rug.sasleg.editor.model.Node;
import nl.rug.sasleg.editor.model.NormalFlow;
import nl.rug.sasleg.editor.model.Parallel;
import nl.rug.sasleg.editor.model.Process;

public class XPDLUnmarshaller 
{
	private JAXBElement<PackageType> xmlRoot;
	
	@SuppressWarnings("unchecked")
	public XPDLUnmarshaller(File file)
	{
		try
		{
			JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);  
			
			Unmarshaller unmarshaller = context.createUnmarshaller();  
			unmarshaller.setProperty("com.sun.xml.internal.bind.ObjectFactory", 
										new ObjectFactory());
			
			xmlRoot = (JAXBElement<PackageType>) unmarshaller.unmarshal(file);
		}
		catch (JAXBException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void importXPDL(Process model)
	{	      
		importGroups(model);
		
		List<ProcessType> wfp = 
	    	xmlRoot.getValue().getWorkflowProcesses().getWorkflowProcess();
	    
	    ProcessType pt = wfp.get(0);
	      
	    importFrozenGroups(model, pt);
	    importActivities(model, pt);
	    importTransitions(model, pt);
	}
	
	private void importGroups(Process model)
	{
		Artifacts artifacts = xmlRoot.getValue().getArtifacts();
		
		if (artifacts == null)
			return;
		
		for (int i = artifacts.getArtifactAndAny().size() - 1; i >= 0; --i)
		{
			if (artifacts.getArtifactAndAny().get(i) instanceof Artifact)
			{
				Artifact artifact = 
					(Artifact) artifacts.getArtifactAndAny().get(i);
				
				if (artifact.getArtifactType().equalsIgnoreCase("Group"))
				{
					List<NodeGraphicsInfo> gfxInfo = 
	    				artifact.getNodeGraphicsInfos().getNodeGraphicsInfo();
	    	
					double xPos = 
							gfxInfo.get(0).getCoordinates().getXCoordinate();
					double yPos = 
							gfxInfo.get(0).getCoordinates().getYCoordinate();
					
					double width = gfxInfo.get(0).getWidth();
					double height = gfxInfo.get(0).getHeight();
					
					Point2D p1 = new Point2D.Double(xPos, yPos);
					Point2D p2 = 
								new Point2D.Double(xPos + width, yPos + height);
					
					Group group = model.addGroup(p1, p2, false);
					group.setId(artifact.getGroup().getId());
					group.setName(artifact.getGroup().getName());
				}
			}
		}
	}
	
	private void importFrozenGroups(Process model, ProcessType pt)
	{
		if (pt.getConstraintGroups() != null)
		{
			for (ConstraintGroup cg : 
					pt.getConstraintGroups().getConstraintGroup())
			{				
				List<NodeGraphicsInfo> gfxInfo = 
    				cg.getNodeGraphicsInfos().getNodeGraphicsInfo();
    	
				double xPos = gfxInfo.get(0).getCoordinates().getXCoordinate();
				double yPos = gfxInfo.get(0).getCoordinates().getYCoordinate();
				double width = gfxInfo.get(0).getWidth();
				double height = gfxInfo.get(0).getHeight();
				
				Point2D p1 = new Point2D.Double(xPos, yPos);
				Point2D p2 = new Point2D.Double(xPos + width, yPos + height);
				
				Group group = model.addFrozenGroup(p1, p2, false);
				group.setId(cg.getId());
				group.setName(cg.getName());
			}
		}
	}
	
	private void importActivities(Process model, ProcessType pt)
	{
		for (Activity actvity : pt.getActivities().getActivity())
	    {
	    	List<NodeGraphicsInfo> gfxInfo = 
	    				actvity.getNodeGraphicsInfos().getNodeGraphicsInfo();
	    	
	    	double xPos = gfxInfo.get(0).getCoordinates().getXCoordinate();
	    	double yPos = gfxInfo.get(0).getCoordinates().getYCoordinate();
	    	
	    	Element elem = null;
	    	
	    	if (actvity.getRoute() != null)
	    		elem = model.addGate((int)xPos, (int)yPos);
	    	else if (actvity.getEvent() != null)
	    	{
	    		if (actvity.getEvent().getStartEvent() != null)
	    			elem = model.addStartEvent((int)xPos, (int)yPos);
	    		else if (actvity.getEvent().getEndEvent() != null)
	    			elem = model.addEndEvent((int)xPos, (int)yPos);
	    	}
	    	else
	    		elem = model.addActivity((int)xPos, (int)yPos);
	    	
	    	if (elem != null)
	    	{
	    		elem.setId(actvity.getId());
	    		elem.setName(actvity.getName());
	    		elem.setMandatory(actvity.isMandatory());
	    		elem.setFloating(actvity.isFloating());
	    		
	    		for (Group group : model.getGroups())
	    			if (group.getId() != null &&
	    				group.getId().equals(
	    								gfxInfo.get(0).getConstraintGroupId()))
	    			{
	    				group.add(elem);
	    				break;
	    			}
	    	}
	    }
	}

	private void importTransitions(Process model, ProcessType pt)
	{
		if (pt.getTransitions() != null)
	    {    
		    for (Transition transition : pt.getTransitions().getTransition())
		    {	    	
		    	Node source = model.getNode(transition.getFrom());
		    	Node target = model.getNode(transition.getTo());
		    	
		    	Flow fl = source.addNormalFlow(target);
		    	fl.setName(transition.getName());
		    	((NormalFlow)fl).setWeaken(transition.isWeaken());
		    }
		    
		    for (ConstraintFlow cf : pt.getTransitions().getConstraintFlow())
		    {	    	
		    	Node source = model.getNode(cf.getFrom());
		    	Node target = model.getNode(cf.getTo());
		    	
		    	nl.rug.sasleg.editor.model.ConstraintFlow cfl = 
		    							source.addConstraintFlow(target);
		    	cfl.setName(cf.getName());
		    	
		    	cfl.setNegation(cf.isNegation());
		    	
		    	if (cf.getStateQuantifier() != null)
		    	{
			    	if (cf.getStateQuantifier().equalsIgnoreCase("All"))
			    		cfl.setStateQuantifier(StateQuantifier.ALL);
			    	else
			    		cfl.setStateQuantifier(StateQuantifier.EXIST);
		    	}
			    	
		    	if (cf.getPathQuantifier() != null)
		    	{
			    	if (cf.getPathQuantifier().equalsIgnoreCase("Next"))
			    		cfl.setPathQuantifier(PathQuantifier.NEXT);
			    	else
			    		cfl.setPathQuantifier(PathQuantifier.FINALLY);
		    	}
		    }
		    
		    for (ParallelConstraint xpdlPar : 
		    		pt.getTransitions().getParallelConstraint())
		    {	    	
		    	Node source = model.getNode(xpdlPar.getFrom());
		    	Node target = model.getNode(xpdlPar.getTo());
		    	
		    	Parallel par = source.addParallel(target);
		    	par.setName(xpdlPar.getName());
		    }
	    }
	}
}
