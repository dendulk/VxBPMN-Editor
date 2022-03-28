package nl.rug.sasleg.editor.model;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

public class Gate extends Element
{
	private final static int SIZE = 30;	//For now the shapes have fixed size
	
	public Gate(int xPos, int yPos)
	{
		super();
		
		shape = new Rectangle2D.Double(xPos, yPos, SIZE, SIZE);
		interiorColor = Color.ORANGE;
		strokeColor = Color.BLACK;
	}
	
	public Shape getShape()
	{
		AffineTransform af = new AffineTransform();
		af.rotate(Math.toRadians(45), getCenterX(), getCenterY());
		
		return new GeneralPath(shape).createTransformedShape(af);
	}
}
