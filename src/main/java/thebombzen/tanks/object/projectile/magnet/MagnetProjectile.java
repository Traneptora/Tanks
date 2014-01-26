package thebombzen.tanks.object.projectile.magnet;

import thebombzen.tanks.Vector;
import thebombzen.tanks.object.Terrain;
import thebombzen.tanks.object.projectile.Projectile;

public abstract class MagnetProjectile extends Projectile {
	protected MagnetProjectile(Vector position, Vector velocity, double mass) {
		super(position, velocity, mass);
	}

	@Override
	public void advance(double timestep) {

	}

	@Override
	public void destroy() {

	}

	@Override
	public double getMagnetMultiplier() {
		return 2.0D;
	}

	@Override
	public void onEnterTerrain(Vector oldPosition, Vector newPosition) {
		setPosition(Terrain.getTerrain().getInnerImpactLocation(newPosition,
				oldPosition));
		setFrozen(true);
	}
	
	@Override
	protected void onImpactWithProjectile(Projectile projectile){
		
	}

	@Override
	public void onLeaveTerrain(Vector oldPosition, Vector newPosition) {
		setFrozen(false);
	}

	@Override
	public void onMoveOffScreen() {

	}
	
	public void onTickInTerrain(Vector oldPosition, Vector newPosition){
		setFrozen(true);
	}

}
