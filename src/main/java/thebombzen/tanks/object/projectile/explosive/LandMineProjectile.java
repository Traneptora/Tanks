package thebombzen.tanks.object.projectile.explosive;

import thebombzen.tanks.Vector;
import thebombzen.tanks.object.Terrain;

public class LandMineProjectile extends ExplosiveProjectile {

	public LandMineProjectile(Vector position, Vector velocity, double mass) {
		super(position, velocity, mass);
	}

	@Override
	public void onEnterTerrain(Vector oldPosition, Vector newPosition) {
		setPosition(Terrain.getTerrain().getInnerImpactLocation(newPosition,
				oldPosition));
		setFrozen(true);
	}
	
	@Override
	public void onTickInTerrain(Vector oldPosition, Vector newPosition) {
		setPosition(Terrain.getTerrain().getInnerImpactLocation(newPosition,
				oldPosition));
		setFrozen(true);
	}

	@Override
	public void onLeaveTerrain(Vector oldPosition, Vector newPosition) {
		destroyAndKill();
	}

}
