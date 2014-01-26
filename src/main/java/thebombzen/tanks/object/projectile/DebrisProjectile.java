package thebombzen.tanks.object.projectile;

import java.awt.Color;

import thebombzen.tanks.Vector;
import thebombzen.tanks.object.Tank;
import thebombzen.tanks.object.property.NoFriendlyFire;

public class DebrisProjectile extends Projectile {

	public DebrisProjectile(Vector position, Vector velocity, double mass) {
		super(position, velocity, mass);
	}

	@Override
	public void advance(double timestep) {

	}

	@Override
	public void destroy() {

	}

	@Override
	public Color getColor() {
		return Color.BLACK;
	}

	@Override
	public double getMagnetMultiplier() {
		return 0;
	}

	@Override
	public double getRadius() {
		return 2D;
	}

	@Override
	public void onEnterTerrain(Vector oldPosition, Vector newPosition) {
		setDead();
	}

	@Override
	protected void onImpactWithTank(Tank tank) {
		if (this instanceof NoFriendlyFire
				&& ((NoFriendlyFire) this).getParentTank() == tank) {
			return;
		}
		tank.damageTank(5D);
	}

	@Override
	public void onLeaveTerrain(Vector oldPosition, Vector newPosition) {

	}

	@Override
	public void onMoveOffScreen() {

	}

	@Override
	public String toString() {
		return "DebrisProjectile [getPosition()=" + getPosition()
				+ ", getVelocity()=" + getVelocity() + ", getMass()="
				+ getMass() + "]";
	}
	
	public void onTickInTerrain(Vector oldPosition, Vector newPosition) {
		destroyAndKill();
	}

}
