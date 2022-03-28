package org.flair.bp.v2.ctl.impl;

import java.util.List;

import nl.rug.sasleg.editor.model.Flow;

public interface CTLNode {

	List<Flow> getOutgoingFlows();
}
