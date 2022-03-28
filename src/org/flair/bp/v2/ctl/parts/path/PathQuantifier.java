package org.flair.bp.v2.ctl.parts.path;

import java.util.List;

import org.flair.bp.v2.ctl.impl.CTLNode;
import org.flair.bp.v2.ctl.parts.Phi;

public abstract class PathQuantifier
{
	protected Phi p;
	
	public PathQuantifier()
	{}
	
	public PathQuantifier(Phi p)
	{
		this.p = p;
	}
	
	public void setPhi(Phi p)
	{
		this.p = p;
	}
	
	public Phi getPhi()
	{
		return p;
	}
	
	public abstract int validate(List<CTLNode> path, int depth);
	
	public abstract String toString();
	
	public abstract boolean equals(PathQuantifier t);
}
