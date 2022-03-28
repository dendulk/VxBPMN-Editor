package org.flair.bp.v2.ctl.parts.state;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.flair.bp.v2.ctl.impl.CTLNode;
import org.flair.bp.v2.ctl.parts.Phi;

public class And extends StateQuantifier {
	protected List<StateQuantifier> p;

	public And()
	{
		this.p = new ArrayList<StateQuantifier>();
	}
	
	public And(Phi p)
	{
		this.p = new ArrayList<StateQuantifier>();
		this.p.add((StateQuantifier) p);
	}
	
	public And(List<StateQuantifier> p) {
		this.p = p;
	}
	
	public void addPhi(Phi p)
	{
		if(this.p==null) this.p = new ArrayList<StateQuantifier>();
		this.p.add((StateQuantifier) p);
	}
	
	public boolean removePhi(Phi p)
	{
		if(this.p==null) return false;
		return this.p.remove(p);
	}
	
	public int size()
	{
		return p.size();
	}
	
	public List<StateQuantifier> getCopy()
	{
		List<StateQuantifier> r = new ArrayList<StateQuantifier>(p);
		Collections.copy(r,p);
		return r;
	}
	
	public List<StateQuantifier> getPhi()
	{
		return p;
	}
	
	public boolean contains(StateQuantifier s)
	{
		Iterator<StateQuantifier> i = p.iterator();
		boolean val = false;
		
		while(i.hasNext() && !val)
			val = i.next().equals(s);
		
		return val;
	}

	@Override
	public boolean validate(CTLNode e, int depth)
	{
		if(p==null || e==null) return false;
		
		Iterator<StateQuantifier> i = p.iterator();
		boolean val = true;
		
		while(i.hasNext() && val)
			val = i.next().validate(e, depth);
		
		return val;
	}

	@Override
	public String toString()
	{
		String ret = "(";
		
		Iterator<StateQuantifier> phiIt = p.iterator();
		
		while(phiIt.hasNext())
		{
			ret = ret + phiIt.next().toString();
			if(phiIt.hasNext()) ret = ret + " /\\ ";
		}
		
		return ret + ")";
	}

	@Override
	public boolean equals(Phi t)
	{
		if(t==null || p==null) return false;
		
		boolean ret = true;
		if(t instanceof org.flair.bp.v2.ctl.parts.state.And && !(t instanceof org.flair.bp.v2.ctl.parts.state.Or))
		{
			List<StateQuantifier> l = ((org.flair.bp.v2.ctl.parts.state.And)t).getPhi();
			Iterator<StateQuantifier> i = l.iterator();
			
			if(p.size() != l.size()) ret = false;
			
			while(i.hasNext() && ret)
				ret = contains(i.next());
		}
		
		return ret;
	}
}
