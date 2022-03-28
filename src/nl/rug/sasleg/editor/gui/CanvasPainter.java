package nl.rug.sasleg.editor.gui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import nl.rug.sasleg.editor.model.Element;
import nl.rug.sasleg.editor.model.Flow;
import nl.rug.sasleg.editor.model.Group;
import nl.rug.sasleg.editor.model.Node;
import nl.rug.sasleg.editor.model.Process;

public class CanvasPainter
{
	private Process model;
	private Selection selection;
	private SelectionBox selectionBox;
	
	private final Color ANCHOR_COLOR = Color.MAGENTA;
	private final float dash[] = {12.0f};
	private BasicStroke anchorStroke = 
							new BasicStroke(4.0f, BasicStroke.CAP_BUTT, 
								BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
	
	public CanvasPainter(Process pModel, Selection sel, SelectionBox sb)
	{
		model = pModel;
		selection = sel;
		selectionBox = sb;
	}
	
	public void drawModel(Graphics gfx) 
	{
		Graphics2D gfx2D = (Graphics2D) gfx;
		
		drawGroups(gfx2D);
		drawGroupAnchors(gfx2D);
		drawFlows(gfx2D);
		drawFlowAnchors(gfx2D);
		drawElements(gfx2D);
		selectionBox.draw(gfx2D);
		drawHover(gfx2D);

		gfx2D.dispose();
	}
	
	private void drawGroups(Graphics2D gfx2D)
	{
		for (Group group : model.getGroups())
			group.draw(gfx2D);
	}
	
	private void drawGroupAnchors(Graphics2D gfx2D)
	{
		gfx2D.setColor(Color.RED);
		
		for (Group group : selection.getGroups())
			gfx2D.draw(anchorStroke.createStrokedShape(group.getShape()));
	}
	
	private void drawFlows(Graphics2D gfx2D)
	{
		for (Node node : model.getNodes())
			for (Flow flow : node.getOutgoingFlows())
				flow.draw(gfx2D);
	}
	
	private void drawFlowAnchors(Graphics2D gfx2D)
	{
		gfx2D.setPaint(Color.RED);
		
		for (Flow flow : selection.getFlows())
			gfx2D.draw(anchorStroke.createStrokedShape(flow.getShape()));
	}
	
	private void drawElements(Graphics2D gfx2D)
	{
		for (Element element : model.getElements())
		{
			element.draw(gfx2D);
			
			if (element.isMandatory() || 
					(element.isFloating() && model.isElementGrouped(element)))
				drawSigns(gfx2D, element);
				
			if (selection.isSelected(element))
				drawElementAnchor(gfx2D, element);
		}
	}
	
	private void drawSigns(Graphics2D gfx2D, Element element)
	{
		int ovalWidth = 22;
		if (element.isMandatory() && (element.isFloating() && 
				model.isElementGrouped(element)))
			ovalWidth = 30;
		
		Composite composite = gfx2D.getComposite();
		gfx2D.setComposite(
				AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
		gfx2D.setColor(Color.BLUE);
		gfx2D.fillOval((int)element.getX() - 8, 
				(int)element.getY() - 20, ovalWidth, 22);
		gfx2D.setComposite(composite);		
		
		gfx2D.setColor(Color.BLACK);
		gfx2D.drawOval((int)element.getX() - 8, 
				(int)element.getY() - 20, ovalWidth, 22);
		
		gfx2D.setFont(new Font("Arial", Font.BOLD, 22));
		gfx2D.setColor(Color.RED);
		
		String signs = element.isMandatory()? "!" : "";
		if (element.isFloating() && model.isElementGrouped(element))
			signs = signs + "f";
		
		gfx2D.drawString(signs, (int)element.getX(), (int)element.getY());	
	}
	
	private void drawElementAnchor(Graphics2D gfx2D, Element element)
	{
		gfx2D.setPaint(ANCHOR_COLOR);
		gfx2D.draw(anchorStroke.createStrokedShape(element.getShape()));
	}
	
	private void drawHover(Graphics2D gfx2D)
	{
		if (selection.getHoveredElement() != null)
		{
			gfx2D.setColor(Color.PINK);
			BasicStroke stroke = new BasicStroke(6.0f);
			gfx2D.draw(stroke.createStrokedShape(
							selection.getHoveredElement().getShape()));
		}
	}
}
