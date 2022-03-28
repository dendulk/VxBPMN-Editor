package nl.rug.sasleg.editor.model;

import java.awt.Shape;
import java.awt.geom.RectangularShape;
import java.util.ArrayList;

import org.flair.bp.v2.ctl.impl.CTLNode;

public abstract class Node implements GraphicalElement, CTLNode
{
	protected RectangularShape shape;
	protected String id = "";
	protected String name = "";
	
	private ArrayList<Flow> outgoingFlows = new ArrayList<Flow>();
	
	public ArrayList<Flow> getOutgoingFlows()
	{
		return outgoingFlows;
	}
	
	public ArrayList<NormalFlow> getNormalFlows()
	{
		ArrayList<NormalFlow> nfList = new ArrayList<NormalFlow>();
		
		for (Flow fl : outgoingFlows)
			if (fl instanceof NormalFlow)
				nfList.add((NormalFlow)fl);
		
		return nfList;
	}
	
	public Flow addNormalFlow(Node target)
	{
		Flow newFlow = new NormalFlow(this, target);
		outgoingFlows.add(newFlow);
		
		return newFlow;
	}
	
	public ConstraintFlow addConstraintFlow(Node target)
	{
		ConstraintFlow newFlow = new ConstraintFlow(this, target);
		outgoingFlows.add(newFlow);
		
		return newFlow;
	}
	
	public Parallel addParallel(Node target)
	{
		Parallel parallel = new Parallel(this, target); 
		outgoingFlows.add(parallel);
		
		return parallel;
	}
	
	public void removeFlow(Flow flow)
	{
		outgoingFlows.remove(flow);
	}
	
	public void removeFlowsToTarget(Node target)
	{		
		for (int idx = outgoingFlows.size() - 1; idx >= 0; --idx)
			if (outgoingFlows.get(idx).getTarget() == target)
				outgoingFlows.remove(idx);
	}
	
	public Shape getShape()
	{
		return shape;
	}
	
	public boolean hit(int xPos, int yPos) 
	{
		return getShape().contains(xPos, yPos);
	}
	
	public void move(double dx, double dy) 
	{
		shape.setFrame(getX() + dx, getY() + dy, getWidth(), getHeight());
	}
	
	public double getX()
	{
		return shape.getX();
	}
	
	public double getY()
	{
		return shape.getY();
	}
	
	public double getWidth()
	{
		return shape.getWidth();
	}
	
	public double getHeight()
	{
		return shape.getHeight();
	}
	
	public double getCenterX()
	{
		return shape.getCenterX();
	}
	
	public double getCenterY()
	{
		return shape.getCenterY();
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
}
