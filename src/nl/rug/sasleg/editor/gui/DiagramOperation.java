package nl.rug.sasleg.editor.gui;

import java.awt.event.MouseEvent;

import org.flair.bp.v2.ctl.impl.CTLConstraintFlow;

import nl.rug.sasleg.editor.gui.ToolState.Mode;
import nl.rug.sasleg.editor.model.Element;
import nl.rug.sasleg.editor.model.Flow;
import nl.rug.sasleg.editor.model.GraphicalElement;
import nl.rug.sasleg.editor.model.Group;
import nl.rug.sasleg.editor.model.Node;
import nl.rug.sasleg.editor.model.Process;

public class DiagramOperation
{
	private ToolState toolState = ToolState.instance();
	private boolean shapeGrabbed = false;
	private Canvas canvas;
	private Process process;
	private Selection selection;
	private SelectionBox selectionBox;
	
	public DiagramOperation(Canvas c, Process p, Selection s, SelectionBox sb)
	{
		canvas = c;
		process = p;
		selection = s;
		selectionBox = sb;
	}

	public void moveSelection(int dx, int dy)
	{
		for (Group group : selection.getGroups())
			group.move(dx, dy);
		
		for (Element el : selection.getElements())
		{
			boolean processMode = !Settings.instance().isTemplateMode();
			boolean isFrozen = process.isElementGrouped(el) && !el.isFloating();
			
			if (!(processMode && isFrozen))
			{
				el.move(dx, dy);
				addOrRemoveElementToGroup(el);
			}
		}
		
		updateCanvas();
	}
	
	private void addOrRemoveElementToGroup(Element element)
	{
		for (Group group : process.getGroups())
		{
			if (group.getShape().contains(element.getShape().getBounds2D()))
			{	
				if (!process.isElementGrouped(element))
				{
					group.add(element);
					return;
				}
			}
			else
				group.removeElement(element);
		}
	}
	
	private void updateCanvas()
	{
		canvas.setPreferredSize(process.getProcessDimension());
		canvas.revalidate();
		canvas.repaint();
	}
	
	public boolean isShapeGrabbed()
	{
		return shapeGrabbed;
	}
	
	public GraphicalElement detectShape(int xPos, int yPos, boolean multiSelect)
	{
		Element elem = detectElement(xPos, yPos, multiSelect);
		if (elem != null)
			return elem;
		
		Flow flow = detectFlow(xPos, yPos, multiSelect);
		if (flow != null)
			return flow;
		
		return detectGroup(xPos, yPos, multiSelect);
	}
	
	public Node detectNode(int xPos, int yPos, boolean multiSelect)
	{
		Node elem = detectElement(xPos, yPos, multiSelect);
		if (elem != null)
			return elem;
		
		if (!(toolState.getState() == Mode.FLOW))
			return detectGroup(xPos, yPos, multiSelect);
		
		return null;
	}
	
	public Element detectElement(int xPos, int yPos, boolean isMultiSelect)
	{
		//because of the Z-order rendering, the last shape is checked first
		for (int idx = process.getElements().size() - 1; idx >= 0; --idx)
		{
			Element element = process.getElements().get(idx);
			
			if (element.hit(xPos, yPos))
			{
				if (isMultiSelect)
					selection.addOrRemove(element);
				else if (!selection.isSelected(element))
					selection.selectSingle(element);
				
				if (selection.isSelected(element))
					shapeGrabbed = true;
					
				process.placeElementBehind(idx);
				canvas.repaint();
				return element;
			}
		}
		
		return null;
	}
	
	public Flow detectFlow(int xPos, int yPos, boolean isMultiSelect)
	{
		for (int idx = process.getNodes().size() - 1; idx >= 0; --idx)
		{
			Node node = process.getNodes().get(idx);
			
			for (Flow flow : node.getOutgoingFlows())
			{	
				if (flow.hit(xPos, yPos))
				{
					if (isMultiSelect)
						selection.addOrRemove(flow);
					else if (!selection.isSelected(flow))
						selection.selectSingle(flow);
					
					if (selection.isSelected(flow))
						shapeGrabbed = true;
						
					canvas.repaint();
					return flow;
				}
			}
		}
		
		return null;
	}
	
	public Group detectGroup(int xPos, int yPos, boolean isMultiSelect)
	{
		for (Group group : process.getGroups())
		{
			if (group.hit(xPos, yPos))
			{
				if (isMultiSelect)
					selection.addOrRemove(group);
				else if (!selection.isSelected(group))
					selection.selectSingle(group);
				
				if (selection.isSelected(group))
					shapeGrabbed = true;
				
				canvas.repaint();
				return group;
			}
		}
		
		return null;
	}
	
	public boolean hoverShape(int xPos, int yPos)
	{
		for (int idx = process.getElements().size() - 1; idx >= 0; --idx)
		{
			Element element = process.getElements().get(idx);
			
			if (element.hit(xPos, yPos))
			{
				if (!selection.isSelected(element))
					selection.setHover(element);
				
				process.placeElementBehind(idx);
				canvas.repaint();
				return true;
			}
		}
		
		if (!(toolState.getState() == Mode.FLOW))
		{
			for (Group group : process.getGroups())
			{
				if (group.hit(xPos, yPos))
				{
					if (!selection.isSelected(group))
						selection.setHover(group);
					
					canvas.repaint();
					return true;
				}
			}
		}
		
		selection.clearHover();
		canvas.repaint();
		return false;
	}
	
	public void addElement(int xPos, int yPos)
	{
		Element element = null;
		
		switch (toolState.getState())
		{
			case ACTIVITY:
				element = process.addActivity(xPos, yPos); break;
			case START_EVENT:
				element = process.addStartEvent(xPos, yPos); break;
			case END_EVENT:
				element = process.addEndEvent(xPos, yPos); break;
			case GATEWAY:
				element = process.addGate(xPos, yPos); break;
			case MANDATORY:
				element = process.addActivity(xPos, yPos); 
				element.setMandatory(true); break;
			default: System.out.println("In a state I shouldn't be in!!!");
		}
		
		if (element != null)
		{
			process.toGroup(element);
			selection.selectSingle(element);
		}
		
		toolState.setState(ToolState.Mode.SELECT);
		updateCanvas();
	}
	
	public void removeSelectedShapes()
	{
		boolean processMode = !Settings.instance().isTemplateMode();
		
		if (!processMode)
			for (Group group : selection.getGroups())
				process.removeGroup(group);
		
		for (Flow flow : selection.getFlows())
			if (!(processMode && flow instanceof CTLConstraintFlow))
				flow.getSource().removeFlow(flow);
			
		for (Element element : selection.getElements())
			if (!(processMode && element.isMandatory()))
				process.removeElement(element);
		
		selection.clear();
		updateCanvas();
	}
	
	public void addConnector(MouseEvent evt)
	{
		Node source = selection.getNode();
		Node target = detectElement(evt.getX(), evt.getY(), false);
		if (target == null && !(toolState.getState() == Mode.FLOW))
			target = detectGroup(evt.getX(), evt.getY(), false);
		if (target == null)
			return;

		if (target != source)
		{
			if (toolState.getState() == ToolState.Mode.FLOW)
				process.addNormalFlow(source, target);
			else if (toolState.getState() == ToolState.Mode.PATHFLOW)
				process.addConstraintFlow(source, target);
			else if (toolState.getState() == ToolState.Mode.PARALLEL)
				process.addParallel(source, target);
		}
	}
	
	public void startDragSelection(int xPos, int yPos)
	{
		selectionBox.setDragging(true);
		selectionBox.setFromPoint(xPos, yPos);
		selectionBox.setToPoint(xPos, yPos);
		clearSelection();
	}
	
	public void mouseDragSelection(int xPos, int yPos)
	{
		selectionBox.setToPoint(xPos, yPos);
		canvas.repaint();
	}
	
	public void clearSelection()
	{
		selection.clear();
		canvas.repaint();
	}
	
	public void stopSelection()
	{
		makeGroupOrDoSelection();
		shapeGrabbed = false;
		selectionBox.setDragging(false);
		selection.clearHover();
		canvas.repaint();
	}
	
	private void makeGroupOrDoSelection()
	{
		boolean isDragging = selectionBox.isDragging();
		
		if (isDragging && toolState.getState() == ToolState.Mode.GROUP)
		{
			process.addGroup(selectionBox.getFromPoint(), 
							selectionBox.getToPoint(), true);
			
			toolState.setState(ToolState.Mode.SELECT);
		}
		else if (isDragging && toolState.getState() == ToolState.Mode.CTLGROUP)
		{
			process.addFrozenGroup(selectionBox.getFromPoint(), 
							selectionBox.getToPoint(), true);
			
			toolState.setState(ToolState.Mode.SELECT);
		}
		else if (isDragging)
			selectionBox.doSelection();
	}
}
