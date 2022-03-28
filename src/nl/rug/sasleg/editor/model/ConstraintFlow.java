package nl.rug.sasleg.editor.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import org.flair.bp.v2.ctl.impl.CTLConstraintFlow;

public class ConstraintFlow extends Flow implements CTLConstraintFlow
{
	public enum StateQuantifier
	{
		ALL,
		EXIST
	}
	
	public enum PathQuantifier
	{
		NEXT,
		FINALLY,
	}
	
	private StateQuantifier sqState = StateQuantifier.EXIST;
	private PathQuantifier pqState = PathQuantifier.FINALLY;
	private boolean isNegated = false;

	public ConstraintFlow(Node source, Node target) 
	{
		super(source, target);
	}
	
	public StateQuantifier getStateQuantifier()
	{
		return sqState;
	}
	
	public void setStateQuantifier(StateQuantifier sq)
	{
		sqState = sq;
	}
	
	public PathQuantifier getPathQuantifier()
	{
		return pqState;
	}
	
	public void setPathQuantifier(PathQuantifier pq)
	{
		pqState = pq;
	}
	
	public boolean isNegated()
	{
		return isNegated;
	}
	
	public void setNegation(boolean value)
	{
		isNegated = value;
	}
	
	public void draw(Graphics2D gfx2D)
	{
		gfx2D.setColor(Color.LIGHT_GRAY);
		
		shape.setLine(getP1(), getP2());
		
		if (getP1().distance(getP2()) != 0)
		{
			clipLine();
		
			if(!isBehindSourceTargetNodes())
			{
				if (pqState == PathQuantifier.NEXT)
					gfx2D.fill(new BasicStroke(4.0f).createStrokedShape(shape));
				else	
					gfx2D.draw(shape);
				
				drawArrowHead(gfx2D);
				drawLabel(gfx2D);
			}
		}
	}
}
