package thebombzen.tanks.object.projectile.magnet;

import java.awt.Shape;

import thebombzen.tanks.Vector;
import thebombzen.tanks.object.Terrain;
import thebombzen.tanks.object.projectile.Projectile;

public abstract class MagnetProjectile extends Projectile {
	protected MagnetProjectile(Vector position, Vector velocity, double mass) {
		super(position, velocity, mass);
	}

	@Override
	public void destroy() {

	}

	@Override
	public double getMagnetMultiplier() {
		return 2.0D;
	}

	@Override
	public void onEnterTerrain(Shape outsideShape, Shape insideShape, Vector outsidePosition, Vector insidePosition) {
		setPosition(Terrain.getTerrain().getInnerImpactLocation(outsideShape, insideShape, outsidePosition, insidePosition));
		setFrozen(true);
	}
	
	@Override
	protected void onImpactWithProjectile(Projectile projectile){
		
	}

	@Override
	public void onLeaveTerrain(Vector oldPosition, Vector newPosition) {
		setFrozen(false);
	}
	
	public void onTickInTerrain(Vector oldPosition, Vector newPosition){
		setFrozen(true);
	}

}
