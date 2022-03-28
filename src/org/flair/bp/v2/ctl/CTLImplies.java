package org.flair.bp.v2.ctl;

import java.util.List;

import org.flair.bp.v2.ctl.impl.CTLNode;
import org.flair.bp.v2.ctl.parts.Phi;
import org.flair.bp.v2.ctl.parts.state.Implies;
import org.flair.bp.v2.ctl.parts.state.StateQuantifier;

public class CTLImplies extends Implies
{
	protected int depth = -1;
	
	public CTLImplies()
	{
	}

	public CTLImplies(StateQuantifier p, StateQuantifier q)
	{
		super(p,q);
	}
	
	public void setDepth(int depth)
	{
		this.depth = depth;
	}
	
	public boolean validate(CTLNode state)
	{
		if(!p.validate(state, depth)) return true; //False implies everything
		else return q.validate(state, depth);
	}

	@Override
	public String toString()
	{
		if(p==null || q==null) return "D/E => D/E\n";
		
		return p.toString() + " => " + q.toString() + "\n";
	}
	
	@Override
	public boolean equals(Phi t)
	{
		if(t==null || p==null || q==null) return false;
		
		if(t instanceof CTLImplies && !(t instanceof CTLGroup))
		{
			List<StateQuantifier> l = ((CTLImplies)t).getStateQuantifier();
			return (p.equals(l.get(0)) && q.equals(l.get(1)));
		}
		else return false;
	}
}
