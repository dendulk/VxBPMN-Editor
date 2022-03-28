package nl.rug.sasleg.editor.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.FocusAdapter;

import javax.swing.JPanel;

import nl.rug.sasleg.editor.model.Process;

public class Canvas extends JPanel
{	
	private static final long serialVersionUID = 1L;
	private CanvasPainter canvasPainter;
	
	public Canvas(Process model)
	{
		setPreferredSize(new Dimension(640, 480));
		setBackground(Color.WHITE);
		
		setFocusable(true);
		addFocusListener(new FocusAdapter() {});
		
		createUserInteraction(model);
	}
	
	private void createUserInteraction(Process model)
	{
		Selection selection = new Selection();		
		SelectionBox selectionBox = new SelectionBox(model, selection);
		
		DiagramOperation operation = 
				new DiagramOperation(this, model, selection, selectionBox);
		
		addKeyListener(new KeybHandler(operation));
		
		MouseHandler mh = new MouseHandler(this, operation, selection);
		addMouseListener(mh);
		addMouseMotionListener(mh);
		
	    addMouseListener(new ContextMenu(this, operation, selection));
	    
	    canvasPainter = new CanvasPainter(model, selection, selectionBox);
	}

	public void paintComponent(Graphics gfx)
	{
		super.paintComponent(gfx);
		
		canvasPainter.drawModel(gfx);
	}
}
