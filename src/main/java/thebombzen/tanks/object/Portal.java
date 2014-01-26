package thebombzen.tanks.object;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import thebombzen.tanks.RenderHelper;
import thebombzen.tanks.Vector;
import thebombzen.tanks.object.property.Positioned;
import thebombzen.tanks.object.property.Renderable;

public class Portal extends Positioned implements Renderable {

	private Color color;
	private Portal linkedPortal = null;
	private int turnsAlive = 0;

	public Portal(Portal link, Vector position) {
		super(position);
		this.color = link.getColor();
		this.position = position;
		setLinkedPortal(link);
		link.setLinkedPortal(this);
	}

	public Portal(Vector position) {
		super(position);
		this.color = RenderHelper.generateRandomColor(1.0F, 0.5F);
		this.position = position;
	}

	@Override
	public Shape getBoundingShape() {
		double radius = getRadius();
		return new Ellipse2D.Double(position.getX() - radius, position.getY() - radius, radius * 2D, radius * 2D);
	}

	public Color getColor() {
		return color;
	}
	
	public Portal getLinkedPortal() {
		return linkedPortal;
	}

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
		if (turnsAlive > 10) {
			setDead();
		}
	}

	@Override
	public void render(Graphics2D g2) {
		double radius = getRadius();
		RenderHelper.drawCircle(g2, getPosition(), radius, getColor());
		RenderHelper.drawCircle(g2, getPosition(), (2D / 3D) * radius, Color.LIGHT_GRAY);
	}

	public void setLinkedPortal(Portal portal){
		linkedPortal = portal;
	}

}
