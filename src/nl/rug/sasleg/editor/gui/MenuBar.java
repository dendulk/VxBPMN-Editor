package nl.rug.sasleg.editor.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import nl.rug.sasleg.editor.gui.ToolState.Mode;
import nl.rug.sasleg.editor.model.Process;

public class MenuBar extends JMenuBar
{
	private static final long serialVersionUID = 1L;
	private ModelChecker checker;
	
	public MenuBar(final Process model, Canvas canvas)
	{
		createFileMenu(model , canvas);
		createEditMenu();
		createToolsMenu(model);
		createWindowMenu(model);
		createHelpMenu();
	}
	
	private void createFileMenu(final Process model, final Canvas canvas)
	{
		JMenu fileMenu = new JMenu("File");
		add(fileMenu);
		fileMenu.setMnemonic(KeyEvent.VK_F);
		
		
		JMenuItem openItem = new JMenuItem("Open File...");
		fileMenu.add(openItem);
		openItem.setMnemonic(KeyEvent.VK_O);
		openItem.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		
		openItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt)
			{
				FileOperation.openXPDL(model, getParent());
				
				canvas.setPreferredSize(model.getProcessDimension());
				canvas.revalidate();
				getRootPane().repaint();
			}
		});
		
		
		JMenuItem saveAsItem = new JMenuItem("Save As...");
		fileMenu.add(saveAsItem);
		saveAsItem.setMnemonic(KeyEvent.VK_A);
		saveAsItem.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		
		saveAsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt)
			{
				FileOperation.saveXPDL(model, getParent());
			}
		});
		
		
		fileMenu.addSeparator();
		
		
		JMenuItem exitItem = new JMenuItem("Exit");
		fileMenu.add(exitItem);
		exitItem.setMnemonic(KeyEvent.VK_X);
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt)
			{
				System.exit(0);
			}
		});
	}
	
	private void createEditMenu()
	{
		JMenu editMenu = new JMenu("Edit");
		add(editMenu);
		editMenu.setMnemonic(KeyEvent.VK_E);
	}
	
	private void createToolsMenu(final Process model)
	{
		JMenu toolsMenu = new JMenu("Tools");
		add(toolsMenu);
		toolsMenu.setMnemonic(KeyEvent.VK_T);
		
		
		JMenuItem checkItem = new JMenuItem("Check Model");
		toolsMenu.add(checkItem);
		checkItem.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_F11, ActionEvent.CTRL_MASK));
		
		checkItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt)
			{
				if (Settings.instance().isTemplateMode())
					checker = new ModelChecker(model);
				
				checker.check(model);
			}
		});
	}
	
	private void createWindowMenu(final Process model)
	{
		JMenu windowMenu = new JMenu("Window");
		add(windowMenu);
		windowMenu.setMnemonic(KeyEvent.VK_W);
		
		
		final JCheckBoxMenuItem design = new JCheckBoxMenuItem("Template Mode");
		windowMenu.add(design);
		design.setState(true);
		design.setMnemonic(KeyEvent.VK_T);
		
		design.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt)
			{
				Settings.instance().setTemplateState(design.getState());
				ToolBox.instance().setTemplateMode(design.getState());
				Gui.instance().setTemplateMode(design.getState());
				ToolState.instance().setState(Mode.SELECT);
				
				if (!Settings.instance().isTemplateMode())
					checker = new ModelChecker(model);
			}
		});
	}
	
	private void createHelpMenu()
	{
		JMenu helpMenu = new JMenu("Help");
		add(helpMenu);
		helpMenu.setMnemonic(KeyEvent.VK_H);
		
		
		JMenuItem about = new JMenuItem("About VxBPMN");
		helpMenu.add(about);
		about.setMnemonic(KeyEvent.VK_A);
		
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt)
			{
				JOptionPane.showMessageDialog(
						getParent(),
						"This is the VxBPMN Editor!\n It's currently under " +
						"development. It's goal is to provide additional\n " +
						"expressive power to the Business Process Modeling " +
						"Notation (BPMN) standard.\n" +
						"\n Powered by: BPMN, JAXB2, XPDL2.1",
					    "About VxBPMN",
					    JOptionPane.PLAIN_MESSAGE);
			}
		});
	}
}
