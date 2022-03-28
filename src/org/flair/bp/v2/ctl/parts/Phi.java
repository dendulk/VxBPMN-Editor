package org.flair.bp.v2.ctl.parts;

import org.flair.bp.v2.ctl.impl.CTLNode;

public abstract class Phi
{
	
	public Phi()
	{}
	
	public abstract boolean validate(CTLNode e, int depth);
	public abstract String toString();
	public abstract boolean equals(Phi t);
}
