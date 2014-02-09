package thebombzen.tanks.object.projectile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import thebombzen.tanks.Vector;
import thebombzen.tanks.object.Explosion;
import thebombzen.tanks.object.Portal;
import thebombzen.tanks.object.Tank;
import thebombzen.tanks.object.property.Advanceable;
import thebombzen.tanks.object.property.Blocker;
import thebombzen.tanks.object.property.Moving;
import thebombzen.tanks.object.property.Renderable;

public abstract class Projectile extends Moving implements Renderable,
		Advanceable, Blocker {

	protected Projectile(Vector position, Vector velocity, double mass) {
		super(position, velocity, mass);
	}
	
	@Override
	public void advance(double timestep){
		
	}

	@Override
	public Shape getBoundingShape(){
		double radius = getRadius();
		return new Ellipse2D.Double(position.getX() - radius, position.getY() - radius, radius * 2D, radius * 2D);
	}
	
	public Color getColor() {
		return new Color(0x00A000);
	}

	public double getRadius() {
		return 5D * getMass();
	}

	@Override
	public int getRenderLayer() {
		return 500;
	}

	@Override
	public boolean isBlocking() {
		return !isFrozen();
	}

	@Override
	protected void onImpactWithExplosion(Explosion explosion) {
		destroyAndKill();
	}

	/**
	 * Do not travel through the portal here. That will be handled by the
	 * central updating mechanism.
	 * 
	 * @param portal
	 * @return Whether to travel through the portal.
	 */
	@Override
	protected void onImpactWithPortal(Portal portal) {
		
	}

	@Override
	protected void onImpactWithProjectile(Projectile projectile) {
		destroyAndKill();
	}

	@Override
	protected void onImpactWithTank(Tank tank) {
		destroyAndKill();
	}
	
	@Override
	public void onMoveOffScreen(){
		
	}
	
	@Override
	public void render(Graphics2D g2) {
		g2.setColor(getColor());
		g2.fill(getBoundingShape());
	}
	
}
