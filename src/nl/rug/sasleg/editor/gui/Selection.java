package nl.rug.sasleg.editor.gui;

import java.util.ArrayList;

import nl.rug.sasleg.editor.model.Element;
import nl.rug.sasleg.editor.model.Flow;
import nl.rug.sasleg.editor.model.Group;
import nl.rug.sasleg.editor.model.Node;

public class Selection
{
	private ArrayList<Element> elementSelection = new ArrayList<Element>();
	private ArrayList<Flow> flowSelection = new ArrayList<Flow>();
	private ArrayList<Group> groupSelection = new ArrayList<Group>();
	private Node elementHovered;
	
	public void setHover(Node element)
	{
		elementHovered = element;
	}
	
	public Node getHoveredElement()
	{
		return elementHovered;
	}
	
	public void clearHover()
	{
		elementHovered = null;
	}
	
	public void selectSingle(Group group)
	{
		clear();
		groupSelection.add(group);
	}
	
	public void addOrRemove(Group group)
	{
		if (groupSelection.contains(group))
			groupSelection.remove(group);
		else
		{
			groupSelection.add(group);
			deSelectElementsInGroup(group);
		}
	}
	
	private void deSelectElementsInGroup(Group group)
	{
		for (Element element : group.getElements())
		{
			for (int idx = flowSelection.size() - 1; idx >= 0; --idx)
			{
				Flow flow = flowSelection.get(idx);
				
				if (flow.getSource() ==  element || flow.getTarget() == element)
					flowSelection.remove(flow);
			}
				
			elementSelection.remove(element);	
		}
	}
	
	public ArrayList<Group> getGroups()
	{
		return groupSelection;
	}
	
	public boolean isSelected(Group group)
	{
		return groupSelection.contains(group);
	}
	
	public void selectSingle(Flow flow)
	{
		clear();
		flowSelection.add(flow);
	}
	
	public void addOrRemove(Flow flow)
	{
		if (flowSelection.contains(flow))
			flowSelection.remove(flow);
		else
		{
			flowSelection.add(flow);
			deselectGroup(flow);
		}
	}
	
	private void deselectGroup(Flow flow)
	{
		for (Group group : groupSelection)
		{
			if (group.getElements().contains(flow.getSource()) || 
					group.getElements().contains(flow.getTarget()))
			{
				groupSelection.remove(group);
				return;
			}
		}
	}
	
	public ArrayList<Flow> getFlows()
	{
		return flowSelection;
	}
	
	public boolean isSelected(Flow flow)
	{
		return flowSelection.contains(flow);
	}
	
	public void selectSingle(Element element)
	{
		clear();
		elementSelection.add(element);
	}
	
	public void addOrRemove(Element element)
	{
		if (elementSelection.contains(element))
			elementSelection.remove(element);
		else
		{
			elementSelection.add(element);
			deselectGroup(element);
		}
	}
	
	private void deselectGroup(Element element)
	{
		for (Group group : groupSelection)
			if (group.getElements().contains(element))
			{
				groupSelection.remove(group);
				return;
			}
	}
	
	public Node getNode()
	{
		if (!elementSelection.isEmpty())
			return elementSelection.get(0);
		else if (!groupSelection.isEmpty())
			return groupSelection.get(0);
		
		return null;
	}
	
	public ArrayList<Element> getElements()
	{
		return elementSelection;
	}
	
	public boolean isSelected(Node element)
	{
		return elementSelection.contains(element);
	}
	
	public boolean isSingleThingSelected()
	{
		return elementSelection.size() + flowSelection.size() + 
					groupSelection.size() == 1;
	}
	
	public void clear()
	{
		elementSelection.clear();
		flowSelection.clear();
		groupSelection.clear();
	}
}
