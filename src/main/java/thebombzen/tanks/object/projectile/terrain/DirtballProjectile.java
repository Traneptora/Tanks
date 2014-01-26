package thebombzen.tanks.object.projectile.terrain;

import thebombzen.tanks.Vector;
import thebombzen.tanks.object.Terrain;
import thebombzen.tanks.object.projectile.Projectile;

public class DirtballProjectile extends Projectile {

	public DirtballProjectile(Vector position, Vector velocity, double mass) {
		super(position, velocity, mass);
	}

	@Override
	public void advance(double timestep) {

	}

	@Override
	public void destroy() {
		Terrain.getTerrain().setTerrainAroundRadius(getPosition(),
				(int) (getMass() * 80D), true);
	}

	@Override
	public double getMagnetMultiplier() {
		return 0;
	}
	
	public void onTickInTerrain(Vector oldPosition, Vector newPosition) {
		destroyAndKill();
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
		return "DirtballProjectile [getPosition()=" + getPosition()
				+ ", getVelocity()=" + getVelocity() + ", getMass()="
				+ getMass() + "]";
	}

}
