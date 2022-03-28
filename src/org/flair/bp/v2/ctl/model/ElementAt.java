package org.flair.bp.v2.ctl.model;

import java.util.Iterator;

import org.flair.bp.v2.ctl.impl.CTLNode;
import org.flair.bp.v2.ctl.parts.Phi;
import org.flair.bp.v2.ctl.parts.state.Proposition;
import nl.rug.sasleg.editor.model.Activity;
import nl.rug.sasleg.editor.model.Element;
import nl.rug.sasleg.editor.model.EndEvent;
import nl.rug.sasleg.editor.model.Gate;
import nl.rug.sasleg.editor.model.Group;
import nl.rug.sasleg.editor.model.StartEvent;

public class ElementAt extends Proposition
{
	private CTLNode n;
	
	public ElementAt(CTLNode n)
	{
		this.n = n;
	}

	public CTLNode getNode()
	{
		return n;
	}
	
	@Override
	public boolean validate(CTLNode e, int depth)
	{
		if(n==null || e==null) return false;
		boolean val = n==e;
		
		if(n instanceof Group)
		{
			Group g = (Group)n;
			Iterator<Element> nodes = g.getElements().iterator();
			
			while(nodes.hasNext() && !val)
				val = nodes.next() == e;
		}
		
		return val;
	}

	@Override
	public String toString()
	{
		if(n==null) return "D/E";
		
		if(n instanceof Activity) return " " + ((Activity)n) + " ";
		else if(n instanceof Gate) return " " + ((Gate)n) + " ";
		else if(n instanceof StartEvent) return " Start ";
		else if(n instanceof EndEvent) return " End ";
		else return " Unknown ";
	}
	
	@Override
	public boolean equals(Phi t)
	{
		boolean ret = false;
		if(t instanceof ElementAt)
			if(((ElementAt)t).getNode() == n) ret = true;
		
		return ret;
	}
}
