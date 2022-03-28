package org.flair.bp.v2.ctl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nl.rug.sasleg.editor.model.Group;

import org.flair.bp.v2.ctl.impl.CTLNode;
import org.flair.bp.v2.ctl.parts.Phi;
import org.flair.bp.v2.ctl.parts.state.StateQuantifier;

public class CTLGroup extends CTLImplies
{
	private List<CTLImplies> group;
	private Group groupReference;
	
	//added by Piet
	public CTLGroup(Group gr)
	{
		groupReference = gr;
		
		group = new ArrayList<CTLImplies>();
	}
	
	//added by Piet
	public void setGroupReference(Group groupReference) {
		this.groupReference = groupReference;
	}

	//added by Piet
	public Group getGroupReference() {
		return groupReference;
	}
	public CTLGroup()
	{
		group = new ArrayList<CTLImplies>();
	}
	
	public CTLGroup(StateQuantifier p, StateQuantifier q)
	{
		group = new ArrayList<CTLImplies>();
		group.add(new CTLImplies(p, q));
	}
	
	public CTLGroup(CTLImplies i)
	{
		group = new ArrayList<CTLImplies>();
		if(i!=null)	group.add(i);
	}
	
	public CTLGroup(List<CTLImplies> group)
	{
		this.group = group;
	}
	
	public void add(StateQuantifier p, StateQuantifier q)
	{
		group.add(new CTLImplies(p, q));
	}
	
	public void add(CTLImplies i)
	{
		group.add(i);
	}
	
	public boolean remove(CTLImplies i)
	{
		return group.remove(i);
	}
	
	public CTLImplies get(int index)
	{
		if(index < group.size()) return group.get(index);
		else return null;
	}
	
	public List<CTLImplies> getAll()
	{
		return group;
	}
	
	public int size()
	{
		return group.size();
	}
	
	public boolean contains(CTLImplies t)
	{
		Iterator<CTLImplies> i = group.iterator();
		boolean val = false;
		
		while(i.hasNext() && !val)
			val = i.next().equals(t);
		
		return val;
	}
	
	@Override
	public boolean validate(CTLNode state)
	{
		boolean val = true;
		
		Iterator<CTLImplies> ctls = group.iterator();
		while(ctls.hasNext() && val)
		{
			CTLImplies ctli = ctls.next();
			ctli.setDepth(depth);
			val = ctli.validate(state);
		}
		
		return val;
	}
	
	@Override
	public String toString()
	{
		String ret = "";
		ret = ret + "==Group Start==\n";
		
		Iterator<CTLImplies> cit = group.iterator();
		
		while(cit.hasNext())
			ret = ret + (cit.next()).toString();
		
		ret = ret + "===Group End===\n";
		return ret;
	}
	
	@Override
	public boolean equals(Phi t)
	{
		if(t==null || p==null) return false;
		
		boolean ret = true;
		if(t instanceof CTLGroup)
		{
			List<CTLImplies> l = ((CTLGroup)t).getAll();
			Iterator<CTLImplies> i = l.iterator();
			
			if(group.size() != l.size()) ret = false;
			
			while(i.hasNext() && ret)
				ret = contains(i.next());
		}
		
		return ret;
	}
}
