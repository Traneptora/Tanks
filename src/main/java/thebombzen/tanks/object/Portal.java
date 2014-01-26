package thebombzen.tanks.object;

import java.awt.Color;
import java.awt.Graphics2D;

import thebombzen.tanks.RenderHelper;
import thebombzen.tanks.Vector;
import thebombzen.tanks.object.property.Positioned;
import thebombzen.tanks.object.property.Renderable;

public class Portal implements Positioned, Renderable {

	private Color color;
	private Portal linkedPortal = null;
	private int turnsAlive = 0;
	private boolean dead = false;

	private Vector position;

	public Portal(Portal link, Vector position) {
		this.color = link.getColor();
		this.position = position;
		this.linkedPortal = link;
		link.linkedPortal = this;
	}

	public Portal(Vector position) {
		this.color = RenderHelper.generateRandomColor(1.0F, 0.5F);
		this.position = position;
	}

	public Color getColor() {
		return color;
	}

	public Portal getLinkedPortal() {
		return linkedPortal;
	}

	@Override
	public Vector getPosition() {
		return position;
	}

	@Override
	public double getRadius() {
		return 15D;
	}

	@Override
	public int getRenderLayer() {
		return -500;
	}

	public int getTurnsAlive() {
		return turnsAlive;
	}

	public void incrementTurnsAlive() {
		turnsAlive++;
		if (turnsAlive > 5) {
			setDead();
		}
	}

	@Override
	public boolean isDead() {
		return dead;
	}

	@Override
	public void render(Graphics2D g2) {
		double radius = getRadius();
		RenderHelper.drawCircle(g2, getPosition(), (int) radius, getColor());
		RenderHelper.drawCircle(g2, getPosition(), (int) ((2D / 3D) * radius),
				Color.LIGHT_GRAY);
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
