package thebombzen.tanks.object.property;

import java.awt.Graphics2D;

public interface Renderable {
	public int getRenderLayer();

	public void render(Graphics2D g2);
}
