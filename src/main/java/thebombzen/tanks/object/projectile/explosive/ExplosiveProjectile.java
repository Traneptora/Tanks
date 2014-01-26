package thebombzen.tanks.object.projectile.explosive;

import java.awt.Color;

import thebombzen.tanks.Vector;
import thebombzen.tanks.World;
import thebombzen.tanks.object.Explosion;
import thebombzen.tanks.object.projectile.Projectile;

public class ExplosiveProjectile extends Projectile {

	protected double radiusMultiplier;

	public ExplosiveProjectile(Vector position, Vector velocity, double mass) {
		this(position, velocity, mass, 32.5D);
	}

	protected ExplosiveProjectile(Vector position, Vector velocity,
			double mass, double radiusMultiplier) {
		super(position, velocity, mass);
		this.radiusMultiplier = radiusMultiplier;
	}

	@Override
	public void advance(double timestep) {

	}

	@Override
	public void destroy() {
		World.getWorld().addObject(
				new Explosion(getPosition(), getMass() * radiusMultiplier, 0.75D,
						new Color(0xFF8000)));
	}

	@Override
	public double getMagnetMultiplier() {
		return 1D;
	}

	@Override
	public void onEnterTerrain(Vector oldPosition, Vector newPosition) {
		destroyAndKill();
	}
	
	@Override
	public void onTickInTerrain(Vector oldPosition, Vector newPosition){
		destroyAndKill();
	}

	@Override
	public void onLeaveTerrain(Vector oldPosition, Vector newPosition) {

	}

	@Override
	public void onMoveOffScreen() {

	}

	@Override
	public String toString() {
		return "ExplosiveProjectile [position=" + getPosition() + ", velocity="
				+ getVelocity() + ", getMass()=" + getMass() + "]";
	}

}
