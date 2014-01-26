package thebombzen.tanks.object;

import java.awt.Color;
import java.awt.Graphics2D;

import thebombzen.tanks.Vector;
import thebombzen.tanks.object.property.Advanceable;
import thebombzen.tanks.object.property.Blocker;
import thebombzen.tanks.object.property.Positioned;
import thebombzen.tanks.object.property.Renderable;

public class Explosion implements Positioned, Renderable, Advanceable, Blocker {

	private Color color;
	private double elapsedTime = 0;
	private double maxRadius;
	private double totalTime;
	private Vector position;
	private boolean dead = false;

	public Explosion(Vector position, double maxRadius, double totalTime,
			Color color) {
		this.color = color;
		this.position = position;
		this.maxRadius = maxRadius;
		this.totalTime = totalTime;
	}

	@Override
	public void advance(double timestep) {
		elapsedTime += timestep;
		if (elapsedTime > totalTime) {
			setDead();
			Terrain.getTerrain().setTerrainAroundRadius(getPosition(),
					getRadius(), false);
		}
	}

	public Color getColor() {
		return color;
	}

	public double getMaxRadius() {
		return maxRadius;
	}

	@Override
	public Vector getPosition() {
		return position;
	}

	@Override
	public double getRadius() {
		return Math.pow(getTimeFraction(), 0.75D) * getMaxRadius();
	}

	@Override
	public int getRenderLayer() {
		return 1000;
	}

	public double getTimeFraction() {
		return elapsedTime / totalTime;
	}

	@Override
	public boolean isBlocking() {
		return true;
	}

	@Override
	public boolean isDead() {
		return dead;
	}

	@Override
	public void render(Graphics2D g2) {
		int radius = (int) getRadius();
		g2.setColor(getColor());
		g2.fillOval(position.getIntegerX() - radius, position.getIntegerY()
				- radius, 2 * radius, 2 * radius);
	}

	@Override
	public void setDead() {
		dead = true;

	}

	@Override
	public void setPosition(Vector position) {
		this.position = position;
	}

}
