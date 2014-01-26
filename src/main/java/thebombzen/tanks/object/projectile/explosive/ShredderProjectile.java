package thebombzen.tanks.object.projectile.explosive;

import thebombzen.tanks.Vector;
import thebombzen.tanks.object.Terrain;

public class ShredderProjectile extends ExplosiveProjectile {

	public ShredderProjectile(Vector position, Vector velocity, double mass) {
		super(position, velocity, mass, 25F);
	}

	@Override
	public void onEnterTerrain(Vector oldPosition, Vector newPosition) {
		Terrain.getTerrain().setTerrainAroundRadius(getPosition(),
				(int) (2D * getRadius()), false);
	}

}
