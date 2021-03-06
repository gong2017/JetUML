/*******************************************************************************
 * JetUML - A desktop application for fast UML diagramming.
 *
 * Copyright (C) 2018 by the contributors of the JetUML project.
 *     
 * See: https://github.com/prmr/JetUML
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package ca.mcgill.cs.jetuml.views.nodes;

import ca.mcgill.cs.jetuml.diagram.nodes.FieldNode;
import ca.mcgill.cs.jetuml.diagram.nodes.ObjectNode;
import ca.mcgill.cs.jetuml.geom.Direction;
import ca.mcgill.cs.jetuml.geom.Point;
import ca.mcgill.cs.jetuml.geom.Rectangle;
import ca.mcgill.cs.jetuml.views.StringViewer;
import javafx.scene.canvas.GraphicsContext;

/**
 * An object to render a FieldNode.
 */
public final class FieldNodeView extends AbstractNodeView
{
	private static final String EQUALS = " = ";
	private static final int DEFAULT_WIDTH = 60;
	private static final int DEFAULT_HEIGHT = 20;
	private static final int XGAP = 5;
	private static final StringViewer VALUE_VIEWER = new StringViewer(StringViewer.Align.LEFT, false, false);
	private static final StringViewer NAME_VIEWER = new StringViewer(StringViewer.Align.LEFT, false, false);
	private static final StringViewer EQUALS_VIEWER = new StringViewer(StringViewer.Align.LEFT, false, false);
	private static final int MID_OFFSET = EQUALS_VIEWER.getDimension(EQUALS).getWidth() / 2;
	
	/**
	 * @param pNode The node to wrap.
	 */
	public FieldNodeView(FieldNode pNode)
	{
		super(pNode);
	}
	
	private String name()
	{
		return ((FieldNode)node()).getName();
	}
	
	private String value()
	{
		return ((FieldNode)node()).getValue();
	}
	
	@Override
	public void draw(GraphicsContext pGraphics)
	{
		final Rectangle bounds = getBounds();
		int split = getSplitPosition();
		NAME_VIEWER.draw(name(), pGraphics, new Rectangle(split - leftWidth(), bounds.getY(), leftWidth(), bounds.getHeight()));
		EQUALS_VIEWER.draw(EQUALS, pGraphics, new Rectangle(split - MID_OFFSET, bounds.getY(), MID_OFFSET * 2, bounds.getHeight()));
		VALUE_VIEWER.draw(value(), pGraphics, new Rectangle(split + MID_OFFSET, bounds.getY(), rightWidth(), bounds.getHeight()));
	}
	
	private int getSplitPosition()
	{
		ObjectNode parent = (ObjectNode)((FieldNode)node()).getParent();
		if( parent != null )
		{
			return ((ObjectNodeView)parent.view()).getSplitPosition();
		}
		else
		{
			return DEFAULT_WIDTH / 2;
		}
	}
	
	@Override
	public Rectangle getBounds()
	{
		ObjectNode parent = (ObjectNode)((FieldNode)node()).getParent();
		if( parent != null )
		{
			int yPosition = ((ObjectNodeView)parent.view()).getYPosition((FieldNode)node());
			Rectangle parentBounds = ((ObjectNode)((FieldNode)node()).getParent()).view().getBounds();
			return new Rectangle(parentBounds.getX() + XGAP, yPosition, parentBounds.getWidth() - 2*XGAP, getHeight());
		}
		return new Rectangle(DEFAULT_WIDTH / 2 - leftWidth(), 0, leftWidth() + rightWidth(), getHeight());
	}
	
	/**
	 * @return The width of the left (name) part of the node.
	 */
	public int leftWidth()
	{
		return NAME_VIEWER.getDimension(name()).getWidth() + MID_OFFSET;
	}
	
	/**
	 * @return The width of the right (value) part of the node.
	 */
	public int rightWidth()
	{
		int rightWidth = VALUE_VIEWER.getDimension(value()).getWidth();
		if(rightWidth == 0)
		{
			rightWidth = DEFAULT_WIDTH / 2;
		}
		return rightWidth + MID_OFFSET;
	}
	
	/**
	 * @return The height of this node.
	 */
	public int getHeight()
	{
		return Math.max(DEFAULT_HEIGHT, Math.max(NAME_VIEWER.getDimension(name()).getHeight(), 
				Math.max(VALUE_VIEWER.getDimension(value()).getHeight(), EQUALS_VIEWER.getDimension(EQUALS).getHeight())));
	}
	
	@Override
	public Point getConnectionPoint(Direction pDirection)
	{
		Rectangle bounds = getBounds();
		return new Point(bounds.getMaxX() - XGAP, bounds.getCenter().getY());
	}
}
