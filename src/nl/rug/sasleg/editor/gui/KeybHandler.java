package nl.rug.sasleg.editor.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeybHandler extends KeyAdapter 
{
	private DiagramOperation operation;
	
	public KeybHandler(DiagramOperation operation)
	{
		this.operation = operation;	
	}
	
	public void keyPressed(KeyEvent event) 
	{
		if (event.getKeyCode() == KeyEvent.VK_DELETE)
			operation.removeSelectedShapes();
	}
}
