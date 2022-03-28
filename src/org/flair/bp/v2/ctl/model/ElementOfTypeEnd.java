package org.flair.bp.v2.ctl.model;

import org.flair.bp.v2.ctl.impl.CTLNode;
import org.flair.bp.v2.ctl.parts.Phi;
import org.flair.bp.v2.ctl.parts.state.Proposition;
import nl.rug.sasleg.editor.model.EndEvent;

public class ElementOfTypeEnd extends Proposition {

	public ElementOfTypeEnd()
	{}

	@Override
	public boolean validate(CTLNode e, int depth)
	{
		if(e == null) return false;
		boolean ret = false;
		
		if(e instanceof EndEvent)
			ret = true;
		
		return ret;
	}

	@Override
	public String toString()
	{
		return "End";
	}

	@Override
	public boolean equals(Phi t)
	{
		if(t == null) return false;
		boolean ret = false;
		
		if(t instanceof ElementOfTypeEnd)
			ret = true;
		
		return ret;
	}
}
