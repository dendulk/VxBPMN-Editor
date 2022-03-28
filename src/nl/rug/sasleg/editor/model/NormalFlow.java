package nl.rug.sasleg.editor.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import org.flair.bp.v2.ctl.impl.CTLFlow;

public class NormalFlow extends Flow implements CTLFlow
{
	private boolean weaken = false;
	
	public NormalFlow(Node source, Node target) 
	{
		super(source, target);
	}
	
	public void setWeaken(boolean weaken) 
	{
		this.weaken = weaken;
	}

	public boolean isWeaken() 
	{
		return weaken;
	}
	
	public void draw(Graphics2D gfx2D)
	{
		gfx2D.setColor(Color.BLACK);
		
		shape.setLine(getP1(), getP2());
		
		if (getP1().distance(getP2()) != 0)
		{
			clipLine();
			drawArrowHead(gfx2D);
		}
		
		if (weaken)
		{
			BasicStroke stroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, 
					BasicStroke.JOIN_MITER, 10.0f, new float[] {6.0f}, 0.0f);
			
			gfx2D.fill(stroke.createStrokedShape(shape));
		}
		else 
			gfx2D.draw(shape);
		
		drawLabel(gfx2D);
	}
}
