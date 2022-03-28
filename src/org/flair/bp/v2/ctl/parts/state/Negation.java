package org.flair.bp.v2.ctl.parts.state;

import org.flair.bp.v2.ctl.impl.CTLNode;
import org.flair.bp.v2.ctl.parts.Phi;

public class Negation extends StateQuantifier {

	private StateQuantifier q;
	
	public Negation()
	{}
	
	public Negation(StateQuantifier q)
	{
		this.q = q;
	}
	
	public void setStateQuantifier(StateQuantifier q)
	{
		this.q = q;
	}

	public StateQuantifier getStateQuantifier()
	{
		return q;
	}
	
	@Override
	public boolean validate(CTLNode e, int depth)
	{
		if(q==null || e==null) return false;
		return !q.validate(e, depth);
	}

	@Override
	public String toString()
	{
		if(q==null) return "¬(D/E)";
		
		return "¬(" + q.toString() + ")";
	}

	@Override
	public boolean equals(Phi t)
	{
		if(t==null || q==null) return false;
		
		if(t instanceof Negation) return q.equals(((Negation)t).getStateQuantifier());
		else return false;
	}
}
