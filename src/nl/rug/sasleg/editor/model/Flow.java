package nl.rug.sasleg.editor.model;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import nl.rug.sasleg.editor.util.Intersection;

public abstract class Flow implements GraphicalElement
{
	protected Line2D shape = new Line2D.Double();
	protected Node source;
	protected Node target;
	private String id = "";
	private String name = "";
	
	private static int flowCount;
	
	public Flow(Node source, Node target)
	{
		this.source = source;
		this.target = target;
		
		flowCount++;
		id = "f" + flowCount;
	}
	
	public Node getSource() 
	{
		return source;
	}
	
	public Node getTarget() 
	{
		return target;
	}
	
	public Point2D getP1()
	{
		return new Point2D.Double(source.getCenterX(), source.getCenterY());
	}
	
	public Point2D getP2()
	{
		return new Point2D.Double(target.getCenterX(), target.getCenterY());
	}
	
	public String getId()
	{
		return id;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String toString()
	{
		return "id: " + id + " name: " + name;
	}
	
	public Shape getShape()
	{
		return shape;
	}
	
	public boolean hit(int xPos, int yPos) 
	{	
		BasicStroke stroke = new BasicStroke(6.0f);
		
		return stroke.createStrokedShape(shape).contains(xPos, yPos);
	}
	
	protected void clipLine()
	{
		Point2D iP1 = Intersection.getIntersection(shape, source.getShape());
		Point2D iP2 = Intersection.getIntersection(shape, target.getShape());

		if (iP1 != null && iP2 != null)
			shape.setLine(iP1, iP2);
	}
	
	protected void drawArrowHead(Graphics2D gfx2D)
	{
		double x1 = shape.getX1(); double y1 = shape.getY1();
		double x2 = shape.getX2(); double y2 = shape.getY2();
		double xx = x2 - x1;
		double yy = y2 - y1; 
		
		double angle = Math.atan(yy / xx); //returns angle between 0 and half pi
		if (xx < 0)
			angle += Math.PI;
		if (yy < 0)
			angle += 2 * Math.PI;
		
		Polygon arrowHead = new Polygon();
		arrowHead.addPoint(0, 0);
		arrowHead.addPoint(-8, -8);
		arrowHead.addPoint(-8, 8);
		
		AffineTransform af = new AffineTransform();
		af.rotate(angle, shape.getX2(), shape.getY2());
		af.translate(shape.getX2(), shape.getY2());
		
		gfx2D.fill(af.createTransformedShape(arrowHead));
	}
	
	protected void drawLabel(Graphics2D gfx2D)
	{
		double midX = shape.getX1() + (shape.getX2() - shape.getX1()) / 2;
		double midY = shape.getY1() + (shape.getY2() - shape.getY1()) / 2;
		
		if (getName() != null)
		{
			int sWidth = gfx2D.getFontMetrics().stringWidth(getName());
			
			gfx2D.drawString(getName(), (int)midX - sWidth / 2, (int)midY - 3);
		}
	}
	
	protected boolean isBehindSourceTargetNodes()
	{
		return source.getShape().contains(shape.getP1()) && 
				source.getShape().contains(shape.getP2()) || 
				target.getShape().contains(shape.getP1()) && 
				target.getShape().contains(shape.getP2());
	}
	
	public abstract void draw(Graphics2D gfx2D);
}
