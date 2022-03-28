package nl.rug.sasleg.editor.gui;

import java.awt.Container;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import nl.rug.sasleg.editor.model.Process;

public class FileOperation 
{
	public static void openXPDL(Process model, Container parent)
	{
		JFileChooser fc = new JFileChooser();
		fc.addChoosableFileFilter(
				new FileNameExtensionFilter("xpdl files", "xpdl"));
		
		if (fc.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
		{
			XPDLUnmarshaller unmarshall = 
				new XPDLUnmarshaller(fc.getSelectedFile());
			
			unmarshall.importXPDL(model);
		}
	}
	
	public static void saveXPDL(Process model, Container parent)
	{
		JFileChooser fc = new JFileChooser();
		fc.addChoosableFileFilter(
				new FileNameExtensionFilter("xpdl files", "xpdl"));
		
		if (fc.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION)
		{
			XPDLMarshaller marshaller = new XPDLMarshaller(model);
			
			marshaller.Marshall(fc.getSelectedFile());
		}
	}
}
