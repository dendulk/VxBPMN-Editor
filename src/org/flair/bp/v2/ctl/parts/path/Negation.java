package org.flair.bp.v2.ctl.parts.path;

import java.util.List;

import org.flair.bp.v2.ctl.impl.CTLNode;
import org.flair.bp.v2.ctl.parts.path.PathQuantifier;

public class Negation extends PathQuantifier
{
	private PathQuantifier p;

	public Negation()
	{}
	
	public Negation(PathQuantifier p)
	{
		this.p = p;
	}
	
	public void setPathQuantifier(PathQuantifier p)
	{
		this.p = p;
	}

	public PathQuantifier getPathQuantifier()
	{
		return p;
	}

	@Override
	public int validate(List<CTLNode> path, int depth)
	{
		int ret = p.validate(path, depth);
		if(ret == 0) ret = 1;
		else if(ret == 1) ret = 0;
		
		return ret;
	}

	@Override
	public String toString()
	{
		if(p==null) return "¬(D/E)";
		
		return "¬(" + p.toString() + ")";
	}
	
	@Override
	public boolean equals(PathQuantifier t)
	{
		if(t==null || p==null) return false;
		
		if(t instanceof Negation) return p.equals(((Negation)t).getPathQuantifier());
		else return false;
	}
}
