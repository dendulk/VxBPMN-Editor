package nl.rug.sasleg.editor.model;

import java.awt.Graphics2D;

public interface GraphicalElement
{
	String getId();
	String getName();
	void setId(String id);
	void setName(String name);
	void draw(Graphics2D gfx2D);
}
