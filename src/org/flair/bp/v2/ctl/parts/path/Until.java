package org.flair.bp.v2.ctl.parts.path;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.flair.bp.v2.ctl.impl.CTLNode;
import org.flair.bp.v2.ctl.parts.Phi;
import org.flair.bp.v2.ctl.util.CTLLoopNode;

public class Until extends PathQuantifier {
	protected Phi q;
	
	public Until()
	{}

	//p U q
	public Until(Phi p, Phi q)
	{
		super(p);
		this.q = q;
	}
	
	public void setPhi(Phi p, Phi q)
	{
		this.setPhi(p);
		this.q = q;
	}
	
	public List<Phi> getPhis()
	{
		List<Phi> l = new ArrayList<Phi>();
		l.add(p);
		l.add(q);
		return l;
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
		
		return ret;
	}
	
	@Override
	public String toString()
	{
		if(p==null || q==null) return "[D/E U D/E]";
		
		return "[" + p.toString() + " U " + q.toString() + "]";
	}

	@Override
	public boolean equals(PathQuantifier t)
	{
		if(t==null || p==null || q==null) return false;
		
		if(t instanceof Until  && !(t instanceof Unless))
		{
			List<Phi> l = ((Until)t).getPhis();
			return (p.equals(l.get(0)) && q.equals(l.get(1)));
		}
		else return false;
	}
}
