package thebombzen.tanks.object.projectile.explosive;

import thebombzen.tanks.Vector;
import thebombzen.tanks.object.Terrain;

public class TimedExplosiveProjectile extends ExplosiveProjectile {

	private double time = 0;

	public TimedExplosiveProjectile(Vector position, Vector velocity,
			double mass) {
		super(position, velocity, mass);
		if (Terrain.getTerrain().doesTerrainExistAt(position)) {
			setFrozen(true);
		}
	}

	public TimedExplosiveProjectile(Vector position, Vector velocity,
			double mass, double radiusMultiplier) {
		super(position, velocity, mass, radiusMultiplier);
	}

	@Override
	public void advance(double timestep) {
		time += timestep;
		if (time >= 5D) {
			destroyAndKill();
		}
	}

	@Override
	public void onEnterTerrain(Vector oldPosition, Vector newPosition) {
		setPosition(Terrain.getTerrain().getOuterImpactLocation(newPosition,
				oldPosition));
		Vector normal = Terrain.getTerrain().getSurfaceNormalVector(
				getPosition());
		Vector normalComponent = getVelocity().getComponent(normal);
		setVelocity(getVelocity().subtract(normalComponent.multiply(2.0D))
				.multiply(0.5D));
	}

}
