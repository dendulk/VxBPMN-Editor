package nl.rug.sasleg.editor.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;

import nl.rug.sasleg.editor.model.Process;

public class Gui extends JFrame //Singleton class
{
	private static final long serialVersionUID = 1L;
	private static final Gui INSTANCE = new Gui();
	
	private JLabel statusBar;
	private Vector<Vector<String>> errorMessages = new Vector<Vector<String>>();
	
	private Gui()
	{
		super("VxBPMN Editor: 0.0.170");
				
		createGui();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	private void createGui()
	{
		Process model = new Process();
		Canvas canvas = new Canvas(model);
		
		setJMenuBar(new MenuBar(model, canvas));
		getContentPane().add(new ToolBar(model, canvas), BorderLayout.NORTH);
		getContentPane().add(ToolBox.instance(), BorderLayout.WEST);
		
		Vector<String> columnNames = new Vector<String>();
		columnNames.add("Description");
		columnNames.add("Resource");
		
		JTable messageTable = new JTable(errorMessages, columnNames);
		messageTable.setPreferredScrollableViewportSize(new Dimension(500, 40));
		messageTable.setFillsViewportHeight(true);
		
		JTabbedPane tab = new JTabbedPane();
		tab.add("Check Results", new JScrollPane(messageTable));
		
		add(new JSplitPane(JSplitPane.VERTICAL_SPLIT, 
					new JScrollPane(canvas), tab));
		
		statusBar = new JLabel("  Mode: Template Design");
		add(statusBar, BorderLayout.SOUTH);
		statusBar.setBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	}
	
	public void setTemplateMode(boolean value)
	{
		if (value)
			statusBar.setText("  Mode: Template Design");
		else
			statusBar.setText("  Mode: Process Design");
	}
	
	public void clearMessageTable()
	{
		errorMessages.clear();
	}
	
	public void addErrorMessage(Vector<String> errorMessage)
	{
		errorMessages.add(errorMessage);
	}
	
	public Object clone() throws CloneNotSupportedException
    {
		throw new CloneNotSupportedException(); 
    }

	public static Gui instance()
	{
		return INSTANCE;
	}
}
