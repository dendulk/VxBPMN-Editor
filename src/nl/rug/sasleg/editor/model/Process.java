package nl.rug.sasleg.editor.model;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Process 
{
	private String name = "";
	private ArrayList<Element> elemList = new ArrayList<Element>();
	private ArrayList<Group> groupList = new ArrayList<Group>();
	
	public Group addGroup(Point2D p1, Point2D p2, boolean automaticContainment)
	{
		Group group = new Group(p1, p2);
		groupList.add(group);
		
		if (automaticContainment)
			doContainment(group);
		
		return group;
	}
	
	public Group addFrozenGroup(Point2D p1, Point2D p2, 
									boolean automaticContainment)
	{
		Group group = new FrozenGroup(p1, p2);
		groupList.add(group);
		
		if (automaticContainment)
			doContainment(group);
		
		return group;
	}
	
	private void doContainment(Group group)
	{
		for (Element element : elemList)
			if (!isElementGrouped(element))
				if (group.getShape().contains(element.getShape().getBounds2D()))
				{
					element.setMandatory(true);
					element.setFloating(false);
	
					group.add(element);
				}
	}
	
	public boolean isElementGrouped(Element element)
	{
		for (Group group : groupList)
			if (group.getElements().contains(element))
				return true;
		
		return false;
	}
	
	public ArrayList<Group> getGroups()
	{
		return groupList;
	}
		
	public void removeGroup(Group group)
	{	
		for (Node sourceElement : getNodes())	//First remove incoming flows
			sourceElement.removeFlowsToTarget(group);	
		
		for (int idx = group.getElements().size() - 1; idx >= 0; --idx)
			removeElement(group.getElements().get(idx));
		
		groupList.remove(group);
	}
	
	public void toGroup(Element element)
	{
		for (Group group : groupList)
			if (group.getShape().contains(element.getShape().getBounds2D()))
			{
				group.add(element);
				return;
			}
	}
	
	public Element addStartEvent(int xPos, int yPos)
	{
		StartEvent startEvent = new StartEvent(xPos, yPos);
		elemList.add(startEvent);
		
		return startEvent;
	}
	
	public Element addEndEvent(int xPos, int yPos)
	{
		EndEvent endEvent = new EndEvent(xPos, yPos);
		elemList.add(endEvent);
		
		return endEvent;
	}
	
	public Element addActivity(int xPos, int yPos)
	{
		Element activity = new Activity(xPos, yPos);
		elemList.add(activity);
		
		return activity;
	}
	
	public Element addGate(int xPos, int yPos)
	{
		Gate gate = new Gate(xPos, yPos);
		elemList.add(gate);
		
		return gate;
	}
	
	public Node getNode(String id)
	{
		for (Node node : getNodes())
			if (node.getId().equals(id))
				return node;
			
		return null;
	}
	
	public ArrayList<Element> getElements()
	{
		return elemList;
	}
	
	public ArrayList<Node> getNodes()
	{
		ArrayList<Node> nodeList = new ArrayList<Node>();
		nodeList.addAll(elemList);
		nodeList.addAll(groupList);
		
		return nodeList; 
	}
	
	public void placeElementBehind(int idx)
	{
		elemList.add(elemList.get(idx));
		elemList.remove(idx);
	}
	
	public void removeElement(Element element)
	{
		for (Node sourceNode : getNodes())	//First remove incoming flows
			sourceNode.removeFlowsToTarget(element);
		
		for (Group group : groupList)		//remove possible reference in group
			group.getElements().remove(element);
		
		elemList.remove(element);
	}
	
	public void addNormalFlow(Node source, Node target)
	{
		source.addNormalFlow(target);
	}
	
	public void addConstraintFlow(Node source, Node target)
	{
		source.addConstraintFlow(target);
	}
	
	public void addParallel(Node source, Node target)
	{
		source.addParallel(target);
	}
	
	public Dimension getProcessDimension()
	{
		Dimension graphDim = new Dimension();
		
		for (Node node : getNodes())
		{
			if (node.getX() + node.getWidth() > graphDim.getWidth())
				graphDim.width = (int)(node.getX() + node.getWidth());
			if (node.getY() + node.getHeight() > graphDim.getHeight())
				graphDim.height = (int)(node.getY() + node.getHeight());
		}	
		
		return graphDim;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
}
