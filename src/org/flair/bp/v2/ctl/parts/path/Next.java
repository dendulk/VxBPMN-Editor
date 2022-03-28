package org.flair.bp.v2.ctl.parts.path;

import java.util.List;

import org.flair.bp.v2.ctl.impl.CTLNode;
import org.flair.bp.v2.ctl.parts.Phi;

public class Next extends PathQuantifier {

	public Next()
	{}
	
	public Next(Phi p)
	{
		super(p);
	}
	
	@Override
	public int validate(List<CTLNode> path, int depth)
	{
		return (path.size() > 1 && p.validate(path.get(1), depth) ? 1 : 0);
	}

	@Override
	public String toString()
	{
		if(p==null) return "X D/E";
		
		return "X" + p.toString();
	}

	@Override
	public boolean equals(PathQuantifier t)
	{
		if(t==null || p==null) return false;
		
		if(t instanceof Next) return p.equals(((Next)t).getPhi());
		else return false;
	}
}
