package org.flair.bp.v2.ctl.parts.state;

import org.flair.bp.v2.ctl.impl.CTLNode;
import org.flair.bp.v2.ctl.parts.Phi;
import org.flair.bp.v2.ctl.parts.path.PathQuantifier;

public abstract class StateQuantifier extends Phi
{
	protected PathQuantifier q;
	
	public StateQuantifier()
	{}
	
	public StateQuantifier(PathQuantifier q)
	{
		this.q = q;
	}
	
	public void setPathQuantifier(PathQuantifier q)
	{
		this.q = q;
	}
	
	public PathQuantifier getPathQuantifier()
	{
		return q;
	}

	@Override
	public abstract boolean validate(CTLNode e, int depth);
	
	public abstract String toString();
	
	public abstract boolean equals(Phi t);
}
