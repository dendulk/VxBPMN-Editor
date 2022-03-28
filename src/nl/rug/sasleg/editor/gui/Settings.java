package nl.rug.sasleg.editor.gui;

public class Settings //Singleton class
{
	private static final Settings INSTANCE = new Settings();
	private boolean templateState = true;
	
	private Settings() //Singleton class, empty private constructor
	{}
	
	public void setTemplateState(boolean value)
	{
		templateState = value;
	}
	
	public boolean isTemplateMode()
	{
		return templateState;
	}
	
	public static Settings instance() 
	{
		return INSTANCE;
	}
	
	public Object clone() throws CloneNotSupportedException
    {
		throw new CloneNotSupportedException(); 
    }
}
