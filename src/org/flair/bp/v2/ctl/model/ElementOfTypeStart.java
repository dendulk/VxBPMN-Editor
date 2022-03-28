package org.flair.bp.v2.ctl.model;

import org.flair.bp.v2.ctl.impl.CTLNode;
import org.flair.bp.v2.ctl.parts.Phi;
import org.flair.bp.v2.ctl.parts.state.Proposition;
import nl.rug.sasleg.editor.model.StartEvent;

public class ElementOfTypeStart extends Proposition {

	public ElementOfTypeStart()
	{}

	@Override
	public boolean validate(CTLNode e, int depth)
	{
		if(e == null) return false;
		boolean ret = false;
		
		if(e instanceof StartEvent)
			ret = true;
		
		return ret;
	}

	@Override
	public String toString()
	{
		return "Start";
	}

	@Override
	public boolean equals(Phi t)
	{
		if(t == null) return false;
		boolean ret = false;
		
		if(t instanceof ElementOfTypeStart)
			ret = true;
		
		return ret;
	}
}
