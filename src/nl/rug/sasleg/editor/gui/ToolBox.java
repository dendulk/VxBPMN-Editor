package nl.rug.sasleg.editor.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

public class ToolBox extends JToolBar //Singleton class
{
	private static final long serialVersionUID = 1L;
	private static final ToolBox INSTANCE = new ToolBox();
	
	private JButton pathFlowButton;
	private JButton mandatoryButton;
	private JButton ctlGroupButton;
	private JButton parallelButton;
	
	private ToolState toolState = ToolState.instance();
	
	private ToolBox()
	{
		super(JToolBar.VERTICAL);
		
		createWidgets();
	}
	
	private void createWidgets()
	{
		JButton connectorButton = new JButton("Connector");
		add(connectorButton);
		connectorButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent evt) 
			{
				toolState.setState(ToolState.Mode.FLOW);
			}
		});
		
		JButton startButton = new JButton("Start");
		add(startButton);
		startButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent evt) 
			{
				toolState.setState(ToolState.Mode.START_EVENT);
			}
		});
		
		JButton endButton = new JButton("End");
		add(endButton);
		endButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent evt) 
			{
				toolState.setState(ToolState.Mode.END_EVENT);
			}
		});
		
		JButton activityButton = new JButton("Activity");
		add(activityButton);
		activityButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent evt) 
			{
				toolState.setState(ToolState.Mode.ACTIVITY);
			}
		});
		
		JButton gatewayButton = new JButton("Gateway");
		add(gatewayButton);
		gatewayButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent evt) 
			{
				toolState.setState(ToolState.Mode.GATEWAY);
			}
		});
		
		JButton groupButton = new JButton("Group");
		add(groupButton);
		groupButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent evt) 
			{
				toolState.setState(ToolState.Mode.GROUP);
			}
		});
				
		addSeparator();
		
		pathFlowButton = new JButton("pathFlow");
		add(pathFlowButton);
		pathFlowButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent evt) 
			{
				toolState.setState(ToolState.Mode.PATHFLOW);
			}
		});
		
		parallelButton = new JButton("Parallel");
		add(parallelButton);
		parallelButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent evt) 
			{
				toolState.setState(ToolState.Mode.PARALLEL);
			}
		});	
		
		mandatoryButton = new JButton("Mandatory");
		add(mandatoryButton);
		mandatoryButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent evt) 
			{
				toolState.setState(ToolState.Mode.MANDATORY);
			}
		});
		
		ctlGroupButton = new JButton("CTL Group");
		add(ctlGroupButton);
		ctlGroupButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent evt) 
			{
				toolState.setState(ToolState.Mode.CTLGROUP);
			}
		});
	}
	
	public void setTemplateMode(boolean value)
	{
		pathFlowButton.setVisible(value);
		mandatoryButton.setVisible(value);
		ctlGroupButton.setVisible(value);
		parallelButton.setVisible(value);
	}
	
	public Object clone() throws CloneNotSupportedException
    {
		throw new CloneNotSupportedException(); 
    }

	public static ToolBox instance()
	{
		return INSTANCE;
	}
}
