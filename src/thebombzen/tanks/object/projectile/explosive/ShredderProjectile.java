package thebombzen.tanks.object.projectile.explosive;

import java.awt.Shape;

import thebombzen.tanks.Vector;
import thebombzen.tanks.object.Terrain;

public class ShredderProjectile extends ExplosiveProjectile {

	public ShredderProjectile(Vector position, Vector velocity, double mass) {
		super(position, velocity, mass, 25F);
	}

	@Override
	public void onEnterTerrain(Shape outsideShape, Shape insideShape, Vector outsidePosition, Vector insidePosition) {
		Terrain.getTerrain().setTerrainAroundRadius(getPosition(), 2D * getRadius(), false);
	}

}
