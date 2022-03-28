package org.flair.bp.v2.ctl.util;

import java.util.ArrayList;
import java.util.List;

import org.flair.bp.v2.ctl.impl.CTLNode;
import nl.rug.sasleg.editor.model.Flow;

public class CTLLoopNode implements CTLNode {

	@Override
	public List<Flow> getOutgoingFlows()
	{
		return new ArrayList<Flow>();
	}

}
