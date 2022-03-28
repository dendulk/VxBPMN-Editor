package org.flair.bp.v2.ctl.parts.state;

import java.util.Iterator;
import java.util.List;

import org.flair.bp.v2.ctl.impl.CTLNode;
import org.flair.bp.v2.ctl.parts.Phi;
import org.flair.bp.v2.ctl.parts.path.Next;
import org.flair.bp.v2.ctl.parts.path.PathQuantifier;
import org.flair.bp.v2.ctl.util.CTLUtil;

public class All extends StateQuantifier
{
	public All()
	{}
	
	public All(PathQuantifier q) {
		super(q);
	}

	@Override
	public boolean validate(CTLNode e, int depth)
	{
		if(q==null || e==null) return false;
		
		Iterator<List<CTLNode>> paths;
		
		if(q instanceof Next)
			paths = CTLUtil.getAllPathsFromNode(e, 1).iterator();
		else
			paths = CTLUtil.getAllPathsFromNode(e, depth).iterator();
		
		boolean ret = paths.hasNext();
		int t = 0;
		
		while(paths.hasNext() && ret)
		{
			int i = q.validate(paths.next(), depth);
			if(i == 0) ret = false;
			else if(i == 1) t++;
		}
		
		if(ret && t == 0)     //no false, all maybe
			ret = false;
		
		return ret;
	}

	@Override
	public String toString()
	{
		if(q==null) return "A D/E";
		
		return "A" + q.toString();
	}

	@Override
	public boolean equals(Phi t)
	{
		if(t==null || q==null) return false;
		
		if(t instanceof All) return q.equals(((All)t).getPathQuantifier());
		else return false;
	}
}
