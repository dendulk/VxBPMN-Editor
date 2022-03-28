package org.flair.bp.v2.ctl.parts.state;

import java.util.Iterator;
import java.util.List;

import org.flair.bp.v2.ctl.impl.CTLNode;
import org.flair.bp.v2.ctl.parts.Phi;

public class Or extends And 
{
	public Or()
	{
		super();
	}
	
	public Or(Phi p)
	{
		super(p);
	}
	
	public Or(List<StateQuantifier> p)
	{
		super(p);
	}

	@Override
	public boolean validate(CTLNode e, int depth)
	{
		if(p==null || e==null) return false;
		
		Iterator<StateQuantifier> i = p.iterator();
		boolean val = false;
		
		while(i.hasNext() && !val)
			val = i.next().validate(e, depth);
		
		return val;
	}


	@Override
	public String toString()
	{
		String ret = "(";
		
		Iterator<StateQuantifier> phiIt = p.iterator();
		
		while(phiIt.hasNext())
		{
			ret = ret + phiIt.next().toString();
			if(phiIt.hasNext()) ret = ret + " \\/ ";
		}
		
		return ret + ")";
	}

	@Override
	public boolean equals(Phi t)
	{
		if(t==null || p==null) return false;
		
		boolean ret = true;
		if(t instanceof org.flair.bp.v2.ctl.parts.state.Or)
		{
			List<StateQuantifier> l = ((org.flair.bp.v2.ctl.parts.state.And)t).getPhi();
			Iterator<StateQuantifier> i = l.iterator();
			
			if(p.size() != l.size()) ret = false;
			
			while(i.hasNext() && ret)
				ret = contains(i.next());
		}
		
		return ret;
	}
}
