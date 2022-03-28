package nl.rug.sasleg.editor.gui;

public class ToolState //Singleton class
{
	public enum Mode 
	{
		SELECT,
		FLOW,
		START_EVENT,
		END_EVENT,
		ACTIVITY,
		GATEWAY,
		PATHFLOW,
		PARALLEL,
		MANDATORY,
		GROUP,
		CTLGROUP
	}
	
	private static final ToolState INSTANCE = new ToolState();
	
	private Mode state = Mode.SELECT;
	
	private ToolState() //Singleton class, empty private constructor
	{}
	
	public Mode getState()
	{
		return state;
	}
	
	public void setState(Mode mode) 
	{
		state = mode;
	}
	
	public boolean isConnectorMode()
	{
		return state == Mode.FLOW || state == Mode.PATHFLOW || 
				state == Mode.PARALLEL;
	}
	
	public static ToolState instance() 
	{
		return INSTANCE;
	}
	
	public Object clone() throws CloneNotSupportedException
    {
		throw new CloneNotSupportedException(); 
    }
}
