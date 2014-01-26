package thebombzen.tanks.object.projectile.teleport;

import thebombzen.tanks.Vector;
import thebombzen.tanks.object.Tank;
import thebombzen.tanks.object.Terrain;
import thebombzen.tanks.object.projectile.Projectile;

public class TeleporterProjectile extends Projectile {

	private Tank parentTank;

	public TeleporterProjectile(Vector position, Vector velocity, double mass,
			Tank parentTank) {
		super(position, velocity, mass);
		this.parentTank = parentTank;
	}

	@Override
	public void advance(double timestep) {

	}

	@Override
	public void destroy() {
		parentTank.setPosition(getPosition());
	}

	@Override
	public double getMagnetMultiplier() {
		return 0D;
	}

	public Tank getParentTank() {
		return parentTank;
	}

	@Override
	public void onEnterTerrain(Vector oldPosition, Vector newPosition) {
		parentTank.setPosition(Terrain.getTerrain().getOuterImpactLocation(
				newPosition, oldPosition));
		setDead();
	}

	@Override
	public void onLeaveTerrain(Vector oldPosition, Vector newPosition) {

	}

	@Override
	public void onMoveOffScreen() {

	}

	@Override
	public String toString() {
		return "TeleporterProjectile [getParentTank()=" + getParentTank()
				+ ", getPosition()=" + getPosition() + ", getVelocity()="
				+ getVelocity() + ", getMass()=" + getMass() + "]";
	}

}
