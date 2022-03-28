package nl.rug.sasleg.editor.model;

import java.awt.Color;
import java.awt.geom.Ellipse2D;

public class StartEvent extends Element 
{
	private static final int DIAMETER = 25;	//For now the shapes have fixed size
	
	public StartEvent(int xPos, int yPos)
	{
		super();
		
		shape = new Ellipse2D.Double(xPos, yPos, DIAMETER, DIAMETER);
		interiorColor = Color.GREEN;
		strokeColor = Color.BLACK;
	}
}
