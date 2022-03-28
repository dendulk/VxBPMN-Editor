package org.flair.bp.v2.ctl.parts.path;

import java.util.Iterator;
import java.util.List;

import org.flair.bp.v2.ctl.impl.CTLNode;
import org.flair.bp.v2.ctl.parts.Phi;
import org.flair.bp.v2.ctl.util.CTLLoopNode;

public class Unless extends Until 
{
	public Unless()
	{}

	//p W q
	public Unless(Phi p, Phi q)
	{
		super(p,q);
	}
	
	@Override
	public int validate(List<CTLNode> path, int depth)
	{
		Iterator<CTLNode> pathIt = path.iterator();
		CTLNode n = null;
		int ret = 0;
		boolean ok = pathIt.hasNext();
		
		while(pathIt.hasNext() && ret == 0 && ok)
		{
			n = pathIt.next();
			
			if(!(n instanceof CTLLoopNode))
			{
				ok = p.validate(n, depth);
				ret = (q.validate(n, depth) ? 1 : 0);
			}
		}
		
		if(ret == 0 && ok && n instanceof CTLLoopNode)
			ret = 2;
		else if(ret == 0 && ok)
			ret = 1;
		
		return ret;
	}
	
	@Override
	public String toString()
	{
		if(p==null || q==null) return "[D/E W D/E]";
		
		return "[" + p.toString() + " W " + q.toString() + "]";
	}
	
	@Override
	public boolean equals(PathQuantifier t)
	{
		if(t==null || p==null || q==null) return false;
		
		if(t instanceof Unless)
		{
			List<Phi> l = ((Unless)t).getPhis();
			return (p.equals(l.get(0)) && q.equals(l.get(1)));
		}
		else return false;
	}
}
