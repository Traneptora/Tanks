package thebombzen.tanks.object.projectile.terrain;

import thebombzen.tanks.Vector;
import thebombzen.tanks.object.Terrain;
import thebombzen.tanks.object.projectile.Projectile;

public class CraterProjectile extends Projectile {

	public CraterProjectile(Vector position, Vector velocity, double mass) {
		super(position, velocity, mass);
	}

	@Override
	public void advance(double timestep) {

	}

	@Override
	public void destroy() {
		Terrain.getTerrain().setTerrainAroundRadius(getPosition(),
				(int) (getMass() * 80D), false);
	}

	@Override
	public double getMagnetMultiplier() {
		return 0;
	}

	@Override
	public void onEnterTerrain(Vector oldPosition, Vector newPosition) {
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
		return "CraterProjectile [getPosition()=" + getPosition()
				+ ", getVelocity()=" + getVelocity() + ", getMass()="
				+ getMass() + "]";
	}

}
