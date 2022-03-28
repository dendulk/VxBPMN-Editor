package org.flair.bp.v2.ctl.parts.path;

import java.util.Iterator;
import java.util.List;

import org.flair.bp.v2.ctl.impl.CTLNode;
import org.flair.bp.v2.ctl.parts.Phi;
import org.flair.bp.v2.ctl.util.CTLLoopNode;

public class Finally extends PathQuantifier {

	public Finally()
	{}
	
	public Finally(Phi p)
	{
		super(p);
	}
	
	@Override
	public int validate(List<CTLNode> path, int depth)
	{
		int ret = 0;
		Iterator<CTLNode> pathIt = path.iterator();
		CTLNode n = null;
		
		while(pathIt.hasNext() && ret == 0)
		{
			n = pathIt.next();
			ret = (p.validate(n, depth) ? 1 : 0);
		}
		
		if(ret == 0 && n instanceof CTLLoopNode)
			ret = 2;
		
		return ret;
	}

	@Override
	public String toString()
	{
		if(p==null) return "F D/E";
		
		return "F" + p.toString();
	}

	@Override
	public boolean equals(PathQuantifier t)
	{
		if(t==null || p==null) return false;
		
		if(t instanceof Finally) return p.equals(((Finally)t).getPhi());
		else return false;
	}
}
