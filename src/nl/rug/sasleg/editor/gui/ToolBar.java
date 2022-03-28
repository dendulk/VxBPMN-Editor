package nl.rug.sasleg.editor.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

import nl.rug.sasleg.editor.model.Process;

public class ToolBar extends JToolBar
{
	private static final long serialVersionUID = 1L;

	public ToolBar(Process model, Canvas canvas)
	{
		super();
		
		createWidgets(model, canvas);
	}
	
	private void createWidgets(final Process model, final Canvas canvas)
	{
		JButton newButton = new JButton("New");
		add(newButton);
		
		JButton openButton = new JButton("Open");
		add(openButton);
		openButton.setToolTipText("Open (Ctrl + O)");
		openButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt)
			{
				FileOperation.openXPDL(model, getParent());
				
				canvas.setPreferredSize(model.getProcessDimension());
				canvas.revalidate();
				getRootPane().repaint();
			}
		});
		
		JButton saveButton = new JButton("Save");
		add(saveButton);
		saveButton.setToolTipText("Save (Ctrl + S)");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt)
			{
				FileOperation.saveXPDL(model, getParent());
			}
		});
		
		addSeparator();
		
		JButton selectButton = new JButton("Select");
		add(selectButton);
		selectButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent evt) 
			{
				ToolState.instance().setState(ToolState.Mode.SELECT);
			}
		});
	}
}
