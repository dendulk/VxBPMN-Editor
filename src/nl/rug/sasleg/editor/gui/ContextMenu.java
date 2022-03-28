package nl.rug.sasleg.editor.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import nl.rug.sasleg.editor.model.Element;
import nl.rug.sasleg.editor.model.GraphicalElement;
import nl.rug.sasleg.editor.model.NormalFlow;

public class ContextMenu extends MouseAdapter implements ActionListener
{
	private Canvas canvas;
	private DiagramOperation operation;
	private Selection selection;
	
	private JPopupMenu contextMenu;
	private JMenuItem removeItm;
	private JMenuItem mandatoryItm;
	private JCheckBoxMenuItem floatingItm;
	private JMenuItem weakenItm;
	private JMenuItem gridItm;
	private JMenuItem propertiesItm;
	
	private GraphicalElement gElem;
	
	public ContextMenu(Canvas canv, DiagramOperation op, Selection sel)
	{
		canvas = canv;
		operation = op;
		selection = sel;
		
		createMenu();
	}
	
	private void createMenu()
	{
		contextMenu = new JPopupMenu();
		
		removeItm = new JMenuItem("remove");
		contextMenu.add(removeItm);
		removeItm.addActionListener(this);
		
		mandatoryItm = new JMenuItem();
		contextMenu.add(mandatoryItm);
		mandatoryItm.addActionListener(this);
		
		floatingItm = new JCheckBoxMenuItem("Floating Activity");
		contextMenu.add(floatingItm);
		floatingItm.addActionListener(this);
	    
		weakenItm = new JMenuItem();
		contextMenu.add(weakenItm);
		weakenItm.addActionListener(this);
		
	    gridItm = new JMenuItem("Grid");
		contextMenu.add(gridItm);
	    
		propertiesItm = new JMenuItem("Properties");
		contextMenu.add(propertiesItm);
		propertiesItm.addActionListener(this);
	}
	
	public void mousePressed(MouseEvent evt)
	{
		if (evt.isMetaDown())	//right mouse click
		{ 
		    buildPopup(evt);
		    
		    operation.stopSelection();
			contextMenu.show(evt.getComponent(), evt.getX(), evt.getY());
		}
	}
	
	private void buildPopup(MouseEvent evt)
	{
		gElem = 
			operation.detectShape(evt.getX(), evt.getY(), evt.isControlDown());
		
		if (gElem != null)
		{
			removeItm.setVisible(true);
			gridItm.setVisible(false);
			mandatoryItm.setVisible(false);
			floatingItm.setVisible(false);
			weakenItm.setVisible(false);
		
			if (Settings.instance().isTemplateMode() && 
						selection.isSingleThingSelected() && 
						gElem instanceof Element)
			{
				Element elem = (Element) gElem;
			
				if (elem.isMandatory())
					mandatoryItm.setText("set Optional");
				else
					mandatoryItm.setText("set Mandatory");
				
				mandatoryItm.setVisible(true);
				
				floatingItm.setState(elem.isFloating());
				floatingItm.setVisible(true);
			}
			else if (Settings.instance().isTemplateMode() && 
					selection.isSingleThingSelected() && 
					gElem instanceof NormalFlow)
			{
				NormalFlow nf = (NormalFlow)gElem;
				
				if (nf.isWeaken())
					weakenItm.setText("Strenghten");
				else
					weakenItm.setText("weaken");
				
				weakenItm.setVisible(true);
			}
		}
		else
		{	
			removeItm.setVisible(false);
			mandatoryItm.setVisible(false);
			floatingItm.setVisible(false);
			weakenItm.setVisible(false);
			gridItm.setVisible(true);
			operation.clearSelection();
		}
	}

	public void actionPerformed(ActionEvent evt) 
	{
		if (evt.getSource() == removeItm)
			operation.removeSelectedShapes();
		else if (evt.getSource() == mandatoryItm)
		{
			if (selection.getNode() instanceof Element)
			{
				Element elem = (Element) selection.getNode();
				
				elem.setMandatory(!elem.isMandatory());
				canvas.repaint();
			}
		}
		else if (evt.getSource() == floatingItm)
		{
			if (selection.getNode() instanceof Element)
			{
				Element elem = (Element) selection.getNode();
				
				elem.setFloating(floatingItm.getState());
				canvas.repaint();
			}
		}
		else if (evt.getSource() == weakenItm)
		{
			if (selection.getFlows().size() == 1 && 
					selection.getFlows().get(0) instanceof NormalFlow)
			{
				NormalFlow nf = (NormalFlow)selection.getFlows().get(0);
				
				nf.setWeaken(!nf.isWeaken());
				canvas.repaint();
			}
		}
		else if (evt.getSource() == propertiesItm)
		{
			if (gElem != null)
			{
				new PropertiesPage(gElem, (JFrame)canvas.getTopLevelAncestor());
				
				operation.stopSelection();
			}
		}	
	}
}
