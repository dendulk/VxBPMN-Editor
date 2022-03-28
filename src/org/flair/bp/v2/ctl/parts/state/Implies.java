package org.flair.bp.v2.ctl.parts.state;

import java.util.ArrayList;
import java.util.List;

import org.flair.bp.v2.ctl.CTLGroup;
import org.flair.bp.v2.ctl.CTLImplies;
import org.flair.bp.v2.ctl.impl.CTLNode;
import org.flair.bp.v2.ctl.parts.Phi;

public class Implies extends StateQuantifier
{
	protected StateQuantifier p,q;
	
	public Implies()
	{
	}

	public Implies(StateQuantifier p, StateQuantifier q)
	{
		this.p = p;
		this.q = q;
	}
	
	public void setStateQuantifier(StateQuantifier p, StateQuantifier q)
	{
		this.p = p;
		this.q = q;
	}
	
	public List<StateQuantifier> getStateQuantifier()
	{
		List<StateQuantifier> l = new ArrayList<StateQuantifier>();
		l.add(p);
		l.add(q);
		return l;
	}
	
	@Override
	public boolean validate(CTLNode e, int depth)
	{
		if(!p.validate(e, depth)) return true; //False implies everything
		else return q.validate(e, depth);
	}

	@Override
	public String toString()
	{
		if(p==null || q==null) return "(D/E => D/E)";
		
		return "(" + p.toString() + " => " + q.toString() + ")";
	}

	@Override
	public boolean equals(Phi t)
	{
		if(t==null || p==null || q==null) return false;
		
		if(t instanceof Implies  && !(t instanceof CTLImplies) && !(t instanceof CTLGroup))
		{
			List<StateQuantifier> l = ((Implies)t).getStateQuantifier();
			return (p.equals(l.get(0)) && q.equals(l.get(1)));
		}
		else return false;
	}
}
