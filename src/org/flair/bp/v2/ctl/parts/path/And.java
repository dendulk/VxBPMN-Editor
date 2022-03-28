package org.flair.bp.v2.ctl.parts.path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.flair.bp.v2.ctl.impl.CTLNode;

public class And extends PathQuantifier {
	protected List<PathQuantifier> p;

	public And()
	{
		this.p = new ArrayList<PathQuantifier>();
	}
	
	public And(PathQuantifier p)
	{
		this.p = new ArrayList<PathQuantifier>();
		this.p.add(p);
	}
	
	public And(List<PathQuantifier> p) {
		this.p = p;
	}
	
	public void addPathQuantifier(PathQuantifier p)
	{
		if(this.p==null) this.p = new ArrayList<PathQuantifier>();
		this.p.add(p);
	}
	
	public boolean removePathQuantifier(PathQuantifier p)
	{
		if(this.p==null) return false;
		return this.p.remove(p);
	}
	
	public int size()
	{
		return p.size();
	}

	public List<PathQuantifier> getCopy()
	{
		List<PathQuantifier> r = new ArrayList<PathQuantifier>(p);
		Collections.copy(r,p);
		return r;
	}

	public List<PathQuantifier> getPathQuantifier()
	{
		return p;
	}
	
	public boolean contains(PathQuantifier s)
	{
		Iterator<PathQuantifier> i = p.iterator();
		boolean val = false;
		
		while(i.hasNext() && !val)
			val = i.next().equals(s);
		
		return val;
	}

	@Override
	public int validate(List<CTLNode> path, int depth)
	{
		Iterator<PathQuantifier> pit = p.iterator();
		int ret = (pit.hasNext() ? 1 : 0);
		int t = 0;
		int m = 0;
		
		while(pit.hasNext() && ret != 0)
		{
			ret = pit.next().validate(path, depth);
			if(ret == 1) t++;
			else if(ret == 2) m++;
		}
		
		if(t == p.size())           //all true; return true
			ret = 1;
		else if(t + m == p.size())  //all maybe or true; return maybe
			ret = 2;
		else                        //some false; return false
			ret = 0;
		
		return ret;
	}

	@Override
	public String toString()
	{
		String ret = "(";
		
		Iterator<PathQuantifier> phiIt = p.iterator();
		
		while(phiIt.hasNext())
		{
			ret = ret + phiIt.next().toString();
			if(phiIt.hasNext()) ret = ret + " /\\ ";
		}
		
		return ret + ")";
	}
	
	@Override
	public boolean equals(PathQuantifier t)
	{
		if(t==null || p==null) return false;
		
		boolean ret = true;
		if(t instanceof org.flair.bp.v2.ctl.parts.path.And && !(t instanceof org.flair.bp.v2.ctl.parts.path.Or))
		{
			List<PathQuantifier> l = ((org.flair.bp.v2.ctl.parts.path.And)t).getPathQuantifier();
			Iterator<PathQuantifier> i = l.iterator();
			
			if(p.size() != l.size()) ret = false;
			
			while(i.hasNext() && ret)
				ret = contains(i.next());
		}
		
		return ret;
	}
}
