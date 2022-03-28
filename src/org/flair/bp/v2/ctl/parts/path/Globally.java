package org.flair.bp.v2.ctl.parts.path;

import java.util.Iterator;
import java.util.List;

import org.flair.bp.v2.ctl.impl.CTLNode;
import org.flair.bp.v2.ctl.parts.Phi;
import org.flair.bp.v2.ctl.util.CTLLoopNode;

public class Globally extends PathQuantifier {

	public Globally()
	{}
	
	public Globally(Phi p)
	{
		super(p);
	}
	
	@Override
	public int validate(List<CTLNode> path, int depth)
	{
		Iterator<CTLNode> pathIt = path.iterator();
		CTLNode n = null;
		int ret = (pathIt.hasNext() ? 1 : 0);
		
		while(pathIt.hasNext() && ret == 1)
		{
			n = pathIt.next();
			if(!(n instanceof CTLLoopNode))
				ret = (p.validate(n, depth) ? 1 : 0);
		}
		
		if(ret == 1 && n instanceof CTLLoopNode)
			ret = 2;
		
		return ret;
	}

	@Override
	public String toString()
	{
		if(p==null) return "G D/E";
		
		return "G" + p.toString();
	}

	@Override
	public boolean equals(PathQuantifier t)
	{
		if(t==null || p==null) return false;
		
		if(t instanceof Globally) return p.equals(((Globally)t).getPhi());
		else return false;
	}
}
