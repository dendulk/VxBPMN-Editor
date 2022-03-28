package org.flair.bp.v2.ctl.util;

import org.flair.bp.v2.ctl.CTLImplies;
import org.flair.bp.v2.ctl.impl.CTLNode;

public class CTLError extends CTLImplies
{
	private String s = " >CTLError< \n";
	
	public CTLError()
	{}
	
	public CTLError(String s)
	{
		this.s = " >CTLError: " + s + "< \n";
	}
	
	@Override
	public boolean validate(CTLNode state)
	{
		return false;
	}
	
	@Override
	public String toString()
	{
		return s;
	}
	
	public void setString(String s)
	{
		this.s = " >CTLError: " + s + "< \n";
	}
}
