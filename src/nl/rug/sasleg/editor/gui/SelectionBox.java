package nl.rug.sasleg.editor.gui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import nl.rug.sasleg.editor.model.Element;
import nl.rug.sasleg.editor.model.Flow;
import nl.rug.sasleg.editor.model.Group;
import nl.rug.sasleg.editor.model.Process;

public class SelectionBox 
{
	private ToolState toolState = ToolState.instance();
	
	private Process model;
	private Selection selection;
	
	private boolean isDragging = false;
	private Point2D fromPoint = new Point2D.Double();
	private Point2D toPoint = new Point2D.Double();
	private Rectangle2D box = new Rectangle2D.Double();
	
	private final float dash[] = {12.0f};
	private BasicStroke selectionStroke = 
							new BasicStroke(1.0f, BasicStroke.CAP_BUTT, 
									BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
	
	public SelectionBox(Process pModel, Selection sel)
	{
		model = pModel;
		selection = sel;
	}
	
	public void setDragging(boolean dragging)
	{
		isDragging = dragging;
	}
	
	public boolean isDragging()
	{
		return isDragging;
	}
	
	public Point2D getFromPoint()
	{
		return fromPoint;
	}
	
	public Point2D getToPoint()
	{
		return toPoint;
	}
	
	public void setFromPoint(int xPos, int yPos)
	{
		fromPoint.setLocation(xPos, yPos);
	}
	
	public void setToPoint(int xPos, int yPos)
	{
		toPoint.setLocation(xPos, yPos);
	}
	
	public void doSelection()
	{
		for (Element element : model.getElements())
		{
			for (Flow flow : element.getOutgoingFlows())
				if (box.contains(flow.getP1()) && box.contains(flow.getP2()))
					selection.addOrRemove(flow);
			
			if (box.contains(element.getShape().getBounds2D()))
				selection.addOrRemove(element);
		}
		
		for (Group group : model.getGroups())
			if (box.contains(group.getShape().getBounds2D()))
				selection.addOrRemove(group);
	}
	
	public void draw(Graphics2D gfx2D)
	{
		if (isDragging)
		{
			box.setFrameFromDiagonal(fromPoint, toPoint);
			
			if (toolState.getState() == ToolState.Mode.CTLGROUP)
				drawAsFrozenGroupBox(gfx2D);
			else
				drawSelectionBox(gfx2D);
		}
	}
	
	private void drawAsFrozenGroupBox(Graphics2D gfx2D)
	{
		gfx2D.setColor(Color.YELLOW);
		Composite composite = gfx2D.getComposite();
		gfx2D.setComposite(AlphaComposite.getInstance(
									AlphaComposite.SRC_OVER, 0.1f));
		
		gfx2D.fill(box);
		gfx2D.setComposite(composite);
		drawSelectionBox(gfx2D);
	}
	
	private void drawSelectionBox(Graphics2D gfx2D)
	{
		gfx2D.setColor(Color.BLACK);
		gfx2D.setStroke(selectionStroke);
		
		gfx2D.draw(box);
	}
}
