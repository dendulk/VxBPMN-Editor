package nl.rug.sasleg.editor.util;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class Intersection 
{
	public static Point2D getIntersection(Line2D line, Shape shape)
	{
		if (shape instanceof GeneralPath)
			return lineRect(line, (GeneralPath)shape);
		else if (shape instanceof Rectangle2D)
			return lineRect(line, new GeneralPath(shape));
		else if (shape instanceof Ellipse2D)
			return lineCircle(line, (Ellipse2D)shape);
		else if (shape instanceof RoundRectangle2D)
			return lineRect(line, new GeneralPath(shape));
		
		return null;
	}	
	
	private static Point2D lineRect(Line2D line, GeneralPath rect)
	{		
		PathIterator pi = rect.getPathIterator(null, 0);
		
		while (!pi.isDone())
		{
			double coords[] = new double[6];
			
			pi.currentSegment(coords);
			Point2D p1 = new Point2D.Double(coords[0], coords[1]);
			
			pi.next();
			if (pi.isDone())
				return null;
			
			pi.currentSegment(coords);
			Point2D p2 = new Point2D.Double(coords[0], coords[1]);
			
			Line2D lineSegment = new Line2D.Double(p1, p2);
			
			if (line.intersectsLine(lineSegment))
				return lineLine(line, lineSegment);
		}
		
		return null;
	}
		
	private static Point2D lineLine(Line2D l1, Line2D l2)
	{
		double x1 = l1.getX1(); double y1 = l1.getY1();
		double x2 = l1.getX2(); double y2 = l1.getY2();
		double x3 = l2.getX1(); double y3 = l2.getY1();
		double x4 = l2.getX2(); double y4 = l2.getY2();
		
		double uA = ( (x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3) ) / 
					( (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1) );

		double xPos = x1 + uA * (x2 - x1);
		double yPos = y1 + uA * (y2 - y1);
		
		return new Point2D.Double(xPos, yPos);
	}
	
	private static Point2D lineCircle(Line2D line, Ellipse2D circle)
	{
		double x1 = line.getX1(); double y1 = line.getY1();
		double x2 = line.getX2(); double y2 = line.getY2();
		double x3 = circle.getCenterX(); double y3 = circle.getCenterY();
		double r = circle.getWidth() / 2; 
	
		double a = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
		double b = 2 * ((x2 - x1) * (x1 - x3) + (y2 - y1) * (y1 - y3));
		double c = x3 * x3 + y3 * y3 + x1 * x1 + y1 * y1 - 
								2 * (x3 * x1 + y3 * y1) - r * r;
		
		double d = b * b - 4 * a * c;
		
		double u;
		if (line.getP1().distance(x3, y3) < line.getP2().distance(x3, y3))
			u = ( -b + Math.sqrt(d) ) / (2 * a);
		else 
			u = ( -b - Math.sqrt(d) ) / (2 * a);
		
		double xPos = x1 + u * (x2 - x1);
		double yPos = y1 + u * (y2 - y1);
		
		return new Point2D.Double(xPos, yPos);
	}
}
