package nl.rug.sasleg.editor.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import nl.rug.sasleg.editor.model.ConstraintFlow;
import nl.rug.sasleg.editor.model.ConstraintFlow.PathQuantifier;
import nl.rug.sasleg.editor.model.ConstraintFlow.StateQuantifier;
import nl.rug.sasleg.editor.model.GraphicalElement;

public class PropertiesPage extends JDialog
{
	private static final long serialVersionUID = 1L;
	private static final int DIALAG_WIDTH = 300;
	private static final int DIALAG_HEIGHT = 240;
	
	private JComboBox stateQuantCmBox;
	private JComboBox pathQuantCmBox;
	private JComboBox negCmBox;
	
	public PropertiesPage(GraphicalElement element, JFrame parentFrame)
	{
		super(parentFrame, "Properties Page", true);
		
		createPageLayout(parentFrame);
		createWidgets(element);
		
		setVisible(true);
	}
	
	private void createPageLayout(JFrame parentFrame)
	{
		setMinimumSize(new Dimension(DIALAG_WIDTH, DIALAG_HEIGHT));
		setLayout(new FlowLayout(FlowLayout.RIGHT, 50, 10));
		
		Point center = new Point(
			(int)parentFrame.getLocation().getX() + 
					parentFrame.getWidth() / 2 - DIALAG_WIDTH / 2, 
			(int)parentFrame.getLocation().getY() + 
					parentFrame.getHeight() / 2 - DIALAG_HEIGHT / 2
		);
		
		setLocation(center);
	}
	
	private void createWidgets(final GraphicalElement gElem)
	{
		JLabel idLabel = new JLabel("Id:");
		add(idLabel);
		
		final JTextField idField = new JTextField(10);
		add(idField);
		idField.setEditable(false);
		idField.setText(gElem.getId());	
		
		JLabel nameLabel = new JLabel("Name:");
		add(nameLabel);
		
		final JTextField nameField = new JTextField(10);
		add(nameField);
		nameField.setText(gElem.getName());				
		
		createElementSpecificWidgets(gElem);
		
		JButton closeButton = new JButton("Close");
		add(closeButton);
		closeButton.setMnemonic(KeyEvent.VK_C);
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				dispose();
			}
		});
		
		JButton okButton = new JButton("Ok");
		add(okButton);
		okButton.setMnemonic(KeyEvent.VK_O);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				gElem.setId(idField.getText());
				gElem.setName(nameField.getText());
				
				storeElementSpecificData(gElem);
				
				dispose();
			}
		});
	}
	
	private void storeElementSpecificData(GraphicalElement gElem)
	{
		if (gElem instanceof ConstraintFlow)
		{
			ConstraintFlow cf = (ConstraintFlow) gElem;
			
			if (stateQuantCmBox.getSelectedIndex() == 0)
				cf.setStateQuantifier(StateQuantifier.ALL);
			else
				cf.setStateQuantifier(StateQuantifier.EXIST);
			
			if (pathQuantCmBox.getSelectedIndex() == 0)
				cf.setPathQuantifier(PathQuantifier.NEXT);
			else
				cf.setPathQuantifier(PathQuantifier.FINALLY);
			
			cf.setNegation(negCmBox.getSelectedIndex()== 0? false : true);
		}
	}
	
	private void createElementSpecificWidgets(GraphicalElement gElem)
	{
		if (gElem instanceof ConstraintFlow && 
				Settings.instance().isTemplateMode())
		{
			ConstraintFlow cf = (ConstraintFlow) gElem;
			
			JLabel sqLabel = new JLabel("State Quantifier");
			add(sqLabel);
			
			String[] sq = {"ALL", "Exist"};
			stateQuantCmBox = new JComboBox(sq);
			add(stateQuantCmBox);
			
			if (cf.getStateQuantifier() == StateQuantifier.ALL)
				stateQuantCmBox.setSelectedIndex(0);
			else
				stateQuantCmBox.setSelectedIndex(1);
			
			JLabel pqLabel = new JLabel("Path Quantifier");
			add(pqLabel);
			
			String[] pq = {"neXt", "Finally"};
			pathQuantCmBox = new JComboBox(pq);
			add(pathQuantCmBox);
			
			if (cf.getPathQuantifier() == PathQuantifier.NEXT)
				pathQuantCmBox.setSelectedIndex(0);
			else
				pathQuantCmBox.setSelectedIndex(1);
			
			JLabel negLabel = new JLabel("Not");
			add(negLabel);
			
			String[] neg = {"False", "True"};
			negCmBox = new JComboBox(neg);
			add(negCmBox);
			
			negCmBox.setSelectedIndex(cf.isNegated()? 1 : 0);
		}
	}
}
