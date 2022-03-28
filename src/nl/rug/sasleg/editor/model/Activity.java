package nl.rug.sasleg.editor.model;

import java.awt.Color;
import java.awt.geom.RoundRectangle2D;

public class Activity extends Element 
{
	private static final int WIDTH = 60;	//For now the shapes have fixed size
	private static final int HEIGHT = 40;
	private static final int ARC_WH = 10;
	
	public Activity(int xPos, int yPos)
	{
		super();
		
		shape = new RoundRectangle2D.Double(
						xPos, yPos, WIDTH, HEIGHT, ARC_WH, ARC_WH);
		interiorColor = Color.LIGHT_GRAY;
		strokeColor = Color.BLACK;
	}
}
