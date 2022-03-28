package nl.rug.sasleg.editor.model;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Group extends Node
{
	private static int groupCount;
	private ArrayList<Element> elementList = new ArrayList<Element>();	
	
	private final float dash[] = {6.0f};
	private BasicStroke contour = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, 
								BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
	
	protected Color interiorColor = Color.WHITE;
	
	public Group(Point2D p1, Point2D p2)
	{
		shape = new Rectangle2D.Double();
		
		createShape(p1, p2);
		
		groupCount++;
		id = "g" + groupCount;
	}
	
	private void createShape(Point2D p1, Point2D p2)
	{
		double distX = Math.abs(p2.getX() - p1.getX());
		double distY = Math.abs(p2.getY() - p1.getY());
		
		if (distX > 100 && distY > 100)
			shape.setFrameFromDiagonal(p1, p2);
		else
		{
			Point2D newP2 = new Point2D.Double(
					p1.getX() + (p2.getX() - p1.getX() >= 0 ? 100 : -100), 
					p1.getY() +	(p2.getY() - p1.getY() >= 0 ? 100 : -100));
			
			shape.setFrameFromDiagonal(p1, newP2);
		}
	}
	
	public boolean hasWeakLink()
	{
		for (Element elem : elementList)
			if (elem.hasWeakLink())
				return true;
					
		return false;
	}
	
	public void add(Element element)
	{
		if (!elementList.contains(element))
			elementList.add(element);
	}
	
	public ArrayList<Element> getElements()
	{
		return elementList;
	}
	
	public void removeElement(Element element)
	{
		elementList.remove(element);
	}
	
	public boolean hasElement(Element element)
	{
		return elementList.contains(element);
	}
	
	public ArrayList<Element> getEndElements()
	{
		ArrayList<Element> endList = new ArrayList<Element>();
		
		for (Element el : elementList)
			if (el.isSink() || !elementList.containsAll(el.getTargetElements()))
				endList.add(el);
		
		return endList;
	}
	
	public boolean isEndElement(Element elem)
	{
		return getEndElements().contains(elem);
	}
	
	public ArrayList<ArrayList<Element>> getPaths(Element start, Element end)
	{
		ArrayList<ArrayList<Element>> allPaths = 
										new ArrayList<ArrayList<Element>>();
		
		ArrayList<Element> visitedList = new ArrayList<Element>();
		visitedList.add(start);
		
		if (start != end)
			dfs(start, end, visitedList, 
					new ArrayList<Element>(), allPaths);
		
		return allPaths;
	}
	
	private void dfs(Element elem, Element end, 
			ArrayList<Element> visitedList, ArrayList<Element> cycledList, 
			ArrayList<ArrayList<Element>> allPaths)
	{	
		for (Element next : elem.getTargetElements())
		{
			if (next == end)
			{
				if (!allPaths.contains(visitedList) && !visitedList.isEmpty())
					allPaths.add(new ArrayList<Element>(visitedList));
			}
			else 
			{
				if (!visitedList.equals(elementList))
				{
					if (visitedList.contains(next))
					{
						if (!cycledList.contains(next))
						{
							cycledList.add(next);
							dfs(next, end, visitedList, cycledList, allPaths);
							cycledList.remove(next);
						}
					}
					else
					{
						visitedList.add(next);
						ArrayList<Element> copyCycleList = 
										new ArrayList<Element>(cycledList);
						
						cycledList.clear();
						
						dfs(next, end, visitedList, cycledList, allPaths);
						
						cycledList.addAll(copyCycleList);
						visitedList.remove(next);
					}
				}
			}
		}
	}
	
	public void move(double dx, double dy)
	{
		super.move(dx, dy);
		
		for (Element element : elementList)
			element.move(dx, dy);
	}
	
	public void draw(Graphics2D gfx2D)
	{
		Composite composite = gfx2D.getComposite();
		gfx2D.setComposite(
				AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
		
		gfx2D.setColor(interiorColor);
		gfx2D.fill(shape);
		gfx2D.setComposite(composite);
		gfx2D.setColor(Color.BLACK);
		gfx2D.draw(contour.createStrokedShape(shape));
		
		if (name != null)
			gfx2D.drawString(name, (int)getX() + 20, (int)getY() - 5);
	}
}
