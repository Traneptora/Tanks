package thebombzen.tanks.object;

import java.awt.Color;
import java.awt.Graphics2D;

import thebombzen.tanks.RenderHelper;
import thebombzen.tanks.Vector;
import thebombzen.tanks.forceprovider.ForceProvider;
import thebombzen.tanks.object.property.Advanceable;
import thebombzen.tanks.object.property.Blocker;
import thebombzen.tanks.object.property.Moving;
import thebombzen.tanks.object.property.Positioned;
import thebombzen.tanks.object.property.Renderable;

public class Explosion implements Positioned, Renderable, Advanceable, Blocker, ForceProvider {

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
		return getAdjustedTimeFraction() * getMaxRadius();
	}
	
	public double getAdjustedTimeFraction(){
		return Math.pow(getTimeFraction(), 0.75D);
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
		RenderHelper.drawCircle(g2, getPosition(), (int)getRadius(), getColor());
		g2.setColor(getColor());
		double sRadius = 10D * Math.pow(getTimeFraction(), 1.5D) * getMaxRadius();
		g2.setColor(new Color(0, 0, 0, (int)(255D * (1D - getAdjustedTimeFraction()))));
		g2.drawOval((int)(getPosition().getX() - sRadius), (int)(getPosition().getY() - sRadius), (int)(sRadius * 2D), (int)(sRadius * 2D));
	}

	@Override
	public void setDead() {
		dead = true;

	}

	@Override
	public void setPosition(Vector position) {
		this.position = position;
	}
	
	@Override
	public Vector getForce(Moving moving){
		double shockwaveRadius = Math.pow(getTimeFraction(), 1.5D) * getMaxRadius() * 10D;
		Vector displacement = moving.getPosition().subtract(getPosition());
		double normSquared = displacement.getNormSquared();
		if (displacement.getNormSquared() >= shockwaveRadius * shockwaveRadius){
			return Vector.ZERO;
		}
		double distanceToWave = Math.max(10D, Math.abs(Math.sqrt(normSquared) - shockwaveRadius));
		double relativeStrength = 1E6D / (distanceToWave);
		Vector force = displacement.setLength(relativeStrength * relativeStrength / shockwaveRadius);
		return force;
	}

}
