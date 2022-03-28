package nl.rug.sasleg.editor.model;

import java.awt.Color;
import java.awt.geom.Point2D;

public class FrozenGroup extends Group
{
	public FrozenGroup(Point2D p1, Point2D p2)
	{
		super(p1, p2);
		
		interiorColor = Color.YELLOW;
	}
}
