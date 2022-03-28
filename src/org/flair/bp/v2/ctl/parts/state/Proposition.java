package org.flair.bp.v2.ctl.parts.state;

import org.flair.bp.v2.ctl.impl.CTLNode;
import org.flair.bp.v2.ctl.parts.Phi;

public abstract class Proposition extends StateQuantifier
{
	public Proposition()
	{}

	@Override
	public abstract boolean validate(CTLNode e, int depth);
	public abstract String toString();
	public abstract boolean equals(Phi t);
}
