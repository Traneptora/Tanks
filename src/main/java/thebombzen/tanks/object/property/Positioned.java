package thebombzen.tanks.object.property;

import thebombzen.tanks.Vector;

public interface Positioned extends Renderable {
	public Vector getPosition();

	public double getRadius();

	public boolean isDead();

	public void setDead();

	public void setPosition(Vector vector);
}
