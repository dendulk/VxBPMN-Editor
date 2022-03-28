package org.flair.bp.v2.ctl.parts.path;

import java.util.Iterator;
import java.util.List;

import org.flair.bp.v2.ctl.impl.CTLNode;
import org.flair.bp.v2.ctl.parts.path.PathQuantifier;

public class Or extends And
{

	public Or()
	{
		super();
	}
	
	public Or(PathQuantifier p)
	{
		super(p);
	}
	
	public Or(List<PathQuantifier> p)
	{
		super(p);
	}
	
	@Override
	public int validate(List<CTLNode> path, int depth)
	{
		Iterator<PathQuantifier> pit = p.iterator();
		int ret = 0;
		int m = 0;
		
		while(pit.hasNext() && ret != 1)
		{
			ret = pit.next().validate(path, depth);
			if(ret == 2) m = 1;
		}
		
		if(ret != 1 && m == 1)
			ret = 2;
		
		return ret;
	}

	@Override
	public String toString()
	{
		String ret = "(";
		
		Iterator<PathQuantifier> phiIt = p.iterator();
		
		while(phiIt.hasNext())
		{
			ret = ret + phiIt.next().toString();
			if(phiIt.hasNext()) ret = ret + " \\/ ";
		}
		
		return ret + ")";
	}
	
	@Override
	public boolean equals(PathQuantifier t)
	{
		if(t==null || p==null) return false;
		
		boolean ret = true;
		if(t instanceof org.flair.bp.v2.ctl.parts.path.Or)
		{
			List<PathQuantifier> l = ((org.flair.bp.v2.ctl.parts.path.Or)t).getPathQuantifier();
			Iterator<PathQuantifier> i = l.iterator();
			
			if(p.size() != l.size()) ret = false;
			
			while(i.hasNext() && ret)
				ret = contains(i.next());
		}
		
		return ret;
	}
}
