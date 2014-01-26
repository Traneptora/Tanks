package thebombzen.tanks.object.projectile.terrain;

import java.awt.Shape;

import thebombzen.tanks.Vector;
import thebombzen.tanks.object.Terrain;
import thebombzen.tanks.object.projectile.Projectile;

public class CraterProjectile extends Projectile {

	public CraterProjectile(Vector position, Vector velocity, double mass) {
		super(position, velocity, mass);
	}

	@Override
	public void destroy() {
		Terrain.getTerrain().setTerrainAroundRadius(getPosition(), getMass() * 80D, false);
	}

	@Override
	public double getMagnetMultiplier() {
		return 0;
	}

	@Override
	public void onEnterTerrain(Shape outsideShape, Shape insideShape,
			Vector outsidePosition, Vector insidePosition) {
		destroyAndKill();
	}

}
