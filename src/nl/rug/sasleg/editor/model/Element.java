package nl.rug.sasleg.editor.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

public abstract class Element extends Node
{
	protected Color interiorColor;
	protected Color strokeColor;
	
	private boolean mandatory = false;
	private boolean floating = true;
	
	private static int elementCount;
	
	public Element()
	{
		elementCount++;
		id = "n" + elementCount;
	}
	
	public ArrayList<Element> getTargetElements()
	{
		ArrayList<Element> targets = new ArrayList<Element>(); 
		
		for (Flow fl : getOutgoingFlows())
			if (fl instanceof NormalFlow && fl.getTarget() instanceof Element)
				targets.add((Element)fl.getTarget());
		
		return targets;
	}
	
	public boolean isSink()
	{
		return getOutgoingFlows().isEmpty();
	}
	
	public boolean hasWeakLink()
	{
		for (Flow fl : getOutgoingFlows())
			if (fl instanceof NormalFlow && ((NormalFlow)fl).isWeaken())
				return true;
		
		return false; 
	}
	
	public ArrayList<NormalFlow> getWeakLinks()
	{
		ArrayList<NormalFlow> weakLinks = new ArrayList<NormalFlow>(); 
		
		for (Flow fl : getOutgoingFlows())
			if (fl instanceof NormalFlow && ((NormalFlow)fl).isWeaken())
				weakLinks.add((NormalFlow)fl);
		
		return weakLinks;
	}
	
	public void setMandatory(boolean value) 
	{
		mandatory = value;
	}

	public boolean isMandatory() 
	{
		return mandatory;
	}
	
	public void setFloating(boolean value) 
	{
		floating = value;
	}

	public boolean isFloating() 
	{
		return floating;
	}
	
	public void draw(Graphics2D gfx2D)
	{
		gfx2D.setColor(interiorColor);
		gfx2D.fill(getShape());
		gfx2D.setColor(strokeColor);
		gfx2D.draw(getShape());
		
		drawLabel(gfx2D);
	}
	
	private void drawLabel(Graphics2D gfx2D)
	{
		gfx2D.setFont(new Font("Arial", Font.ITALIC, 12));
		gfx2D.drawString(name, (int)getCenterX() - 25, (int)getCenterY());
	}
}
