package thebombzen.tanks.object.projectile.explosive;

import java.awt.Shape;

import thebombzen.tanks.Vector;
import thebombzen.tanks.object.Terrain;

public class LandMineProjectile extends ExplosiveProjectile {

	public LandMineProjectile(Vector position, Vector velocity, double mass) {
		super(position, velocity, mass);
	}

	@Override
	public void onEnterTerrain(Shape outsideShape, Shape insideShape, Vector outsidePosition, Vector insidePosition) {
		setPosition(Terrain.getTerrain().getInnerImpactLocation(outsideShape, insideShape, outsidePosition, insidePosition));
		setFrozen(true);
	}

	@Override
	public void onLeaveTerrain(Vector oldPosition, Vector newPosition) {
		destroyAndKill();
	}
	
	@Override
	public double getRadiusMultiplier(){
		return radiusMultiplier;
	}

}
