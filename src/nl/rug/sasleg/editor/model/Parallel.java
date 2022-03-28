package nl.rug.sasleg.editor.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import org.flair.bp.v2.ctl.impl.CTLConstraintFlow;

public class Parallel extends Flow implements CTLConstraintFlow
{
	private final float dash[] = {6.0f};
	private BasicStroke stroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, 
									BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
	
	public Parallel(Node source, Node target)
	{
		super(source, target);
	}

	public void draw(Graphics2D gfx2D)
	{
		gfx2D.setColor(Color.GRAY);
		
		shape.setLine(getP1(), getP2());
		
		if (shape.getP1().distance(shape.getP2()) != 0)
			clipLine();
		
		if (!isBehindSourceTargetNodes())
		{
			gfx2D.draw(stroke.createStrokedShape(shape));
			gfx2D.draw(new Rectangle2D.Double(shape.getP1().getX() - 5, 
											shape.getP1().getY() - 5, 10, 10));
			gfx2D.draw(new Rectangle2D.Double(shape.getP2().getX() - 5, 
											shape.getP2().getY() - 5, 10, 10));
			
			drawLabel(gfx2D);
		}
	}
}
