package nl.rug.sasleg.editor.gui;

import javax.swing.SwingUtilities;

public class Main 
{
	public static void main(String[] args) 
	{
	    SwingUtilities.invokeLater(new Runnable() {
	    	public void run() {
	    		Gui.instance();
	    	}
	    });
	}
}
