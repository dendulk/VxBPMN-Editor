package org.flair.bp.v2.ctl.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.flair.bp.v2.ctl.impl.CTLConstraintFlow;
import org.flair.bp.v2.ctl.impl.CTLFlow;
import org.flair.bp.v2.ctl.impl.CTLNode;
import nl.rug.sasleg.editor.model.Activity;
import nl.rug.sasleg.editor.model.Flow;
import nl.rug.sasleg.editor.model.Gate;

public class CTLUtil
{

	public static List<CTLNode> getOutgoingNodes(CTLNode e)
	{
		Iterator<Flow> flows = e.getOutgoingFlows().iterator();
		List<CTLNode> nodes = new ArrayList<CTLNode>();
		
		while(flows.hasNext())
		{
			CTLFlow f = (CTLFlow)flows.next();
			//root out dead ends and ConstraintFlows
			if(!(f instanceof CTLConstraintFlow) && f.getTarget()!=null) nodes.add((CTLNode) f.getTarget());
		}
		
		return nodes;
	}
	
	/*public static List<Element> getGroupContents(Element g)
	{
		List<Element> returnList = new ArrayList<Element>();
		
		List<Element> ele = ((Group)g).getElements();
		Iterator<Element> elit = ele.iterator();
		
		while(elit.hasNext())
		{
			Element next = elit.next(); 
			
			returnList.add(next);
			if(next instanceof Group) returnList.addAll(getGroupContents(next));
		}
		
		return returnList;
	}*/

	public static List<List<CTLNode>> getAllPathsFromNode(CTLNode n)
	{
		return getAllPathsFromNode(n, -1);
	}
		
	public static List<List<CTLNode>> getAllPathsFromNode(CTLNode n, int maxDepth)
	{
		List<List<CTLNode>> paths = new ArrayList<List<CTLNode>>();
		List<CTLNode> path = new ArrayList<CTLNode>();
		path.add(n);
		paths.add(path);
		
		CTLUtil.getPaths(n, 0, maxDepth, paths, paths.get(0));
		
		System.out.println(CTLUtil.getPathsOutput(n, paths));
		return paths;
	}
	
	private static void getPaths(CTLNode n, int depth, int maxDepth, List<List<CTLNode>> paths, List<CTLNode> path)
	{
		if(depth < maxDepth || maxDepth == -1)
		{
			Iterator<CTLNode> outgoingNodes = CTLUtil.getOutgoingNodes(n).iterator();
			
			if(outgoingNodes.hasNext()) paths.remove(path);
			while(outgoingNodes.hasNext())
			{
				CTLNode next = outgoingNodes.next();
				List<CTLNode> pa = new ArrayList<CTLNode>(path);
				paths.add(pa);
				
				boolean existsAlready = pa.contains(next);
				if(!existsAlready)
				{
					pa.add(next);
					CTLUtil.getPaths(next, depth + 1, maxDepth, paths, pa);
				}
				else
					pa.add(new CTLLoopNode());
			}
		}
	}
	
	public static boolean isCyclicPath(CTLNode from, List<CTLNode> path)
	{
		boolean ret = path.contains(from);
		
		Iterator<CTLNode> pathIt = path.iterator();
		while(pathIt.hasNext() && !ret)
		{
			CTLNode next = pathIt.next();
			ret = path.indexOf(next) != path.lastIndexOf(next);
		}
		
		return ret;
	}
	
	public static String getPathsOutput(CTLNode n, List<List<CTLNode>> paths)
	{
		String out = "";
		
		Iterator<List<CTLNode>> pit = paths.iterator();
		while(pit.hasNext())
		{
			if(n instanceof Activity) out = out + ((Activity)n);
			else if(n instanceof Gate) out = out + ((Gate)n);
			else out = out + "Unknown";
			
			out = out + " ---> ";
			
			Iterator<CTLNode> nodeit = pit.next().iterator();
			while(nodeit.hasNext())
			{
				CTLNode next = nodeit.next();

				if(next instanceof Activity) out = out + ((Activity)next);
				else if(next instanceof Gate) out = out + ((Gate)next);
				else out = out + "Unknown";
				
				if(nodeit.hasNext()) out = out + " => ";
			}
			out = out + "\n";
		}
		
		return out;
	}
}
