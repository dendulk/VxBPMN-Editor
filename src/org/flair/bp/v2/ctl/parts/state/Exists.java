package org.flair.bp.v2.ctl.parts.state;

import java.util.Iterator;
import java.util.List;

import org.flair.bp.v2.ctl.impl.CTLNode;
import org.flair.bp.v2.ctl.parts.Phi;
import org.flair.bp.v2.ctl.parts.path.Next;
import org.flair.bp.v2.ctl.parts.path.PathQuantifier;
import org.flair.bp.v2.ctl.util.CTLUtil;

public class Exists extends StateQuantifier {

	public Exists()
	{}
	
	public Exists(PathQuantifier q)
	{
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
		
		boolean ret = false;
		
		while(paths.hasNext() && !ret)
		{
			ret = (q.validate(paths.next(), depth) == 1);
		}
		
		return ret;
	}

	@Override
	public String toString()
	{
		if(q==null) return "E D/E";
		
		return "E" + q.toString();
	}

	@Override
	public boolean equals(Phi t)
	{
		if(t==null || q==null) return false;
		
		if(t instanceof Exists) return q.equals(((Exists)t).getPathQuantifier());
		else return false;
	}
}
