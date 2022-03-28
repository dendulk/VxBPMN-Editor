package nl.rug.sasleg.editor.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

import nl.rug.sasleg.editor.model.GraphicalElement;

public class MouseHandler extends MouseAdapter
{
	private ToolState toolState = ToolState.instance();
	private Canvas canvas;
	private DiagramOperation operation;
	private Selection selection;
	
	private int lastX = 0;
	private int lastY = 0;
	
	public MouseHandler(Canvas canv, DiagramOperation op, Selection sel)
	{		
		canvas = canv;
		operation = op;
		selection = sel;
	}
	
	public void mousePressed(MouseEvent evt)
	{	
		if (evt.getButton() == MouseEvent.BUTTON1)
			determineAction(evt);
	}
	
	private void determineAction(MouseEvent evt)
	{
		switch (toolState.getState())
		{
			case SELECT: 
				handleSelectAction(evt); break;
			case FLOW: 
				startConnectorAction(evt); break;
			case PATHFLOW:
				startConnectorAction(evt); break;
			case PARALLEL:
				startConnectorAction(evt); break;
			case GROUP:
				handleSelectAction(evt); break;
			case CTLGROUP:
				handleSelectAction(evt); break;
			default:
				operation.addElement(evt.getX(), evt.getY());
		}
	}
	
	private void handleSelectAction(MouseEvent evt)
	{
		lastX = evt.getX();
		lastY = evt.getY();
		
		if (evt.getClickCount() >= 2)
			selection.clear();
		
		GraphicalElement gElem = 
			operation.detectShape(evt.getX(), evt.getY(), evt.isControlDown());
		
		if (gElem == null)
			operation.startDragSelection(evt.getX(), evt.getY());
		
		if (evt.getClickCount() >= 2 && gElem != null)
		{
			new PropertiesPage(gElem, (JFrame)canvas.getTopLevelAncestor());
			
			operation.stopSelection();
		}
	}
	
	private void startConnectorAction(MouseEvent evt)
	{
		operation.clearSelection();
		operation.detectNode(evt.getX(), evt.getY(), false);
	}
	
	private void endConnectorAction(MouseEvent evt)
	{
		if (toolState.isConnectorMode() && operation.isShapeGrabbed())
			operation.addConnector(evt);
		
		operation.stopSelection();
	}
		
	public void mouseReleased(MouseEvent evt)
	{
		canvas.requestFocusInWindow();
		
		if (evt.getButton() == MouseEvent.BUTTON1)
			endConnectorAction(evt);
	}
	
	public void mouseDragged(MouseEvent evt)
	{
		switch (toolState.getState())
		{
			case SELECT: 
				performSelectOperation(evt); break;
			case FLOW: 
				detectSecondElement(evt); break;
			case PATHFLOW:
				detectSecondElement(evt); break;
			case PARALLEL:
				detectSecondElement(evt); break;
			case GROUP: 
				operation.mouseDragSelection(evt.getX(), evt.getY()); break;
			case CTLGROUP: 
				operation.mouseDragSelection(evt.getX(), evt.getY()); break;
			default: break;
		}
	}
	
	private void performSelectOperation(MouseEvent evt)
	{
		if (operation.isShapeGrabbed())
		{
			int dx = evt.getX() - lastX;
			int dy = evt.getY() - lastY;
			
			operation.moveSelection(dx, dy);
			
			lastX += dx;
			lastY += dy;
		}
		else 
			operation.mouseDragSelection(evt.getX(), evt.getY());
	}
	
	private void detectSecondElement(MouseEvent evt)
	{
		if (operation.isShapeGrabbed())
			operation.hoverShape(evt.getX(), evt.getY());
	}
}
