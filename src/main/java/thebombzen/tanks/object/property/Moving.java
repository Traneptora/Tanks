package thebombzen.tanks.object.property;

import thebombzen.tanks.Vector;
import thebombzen.tanks.object.Portal;

public interface Moving extends Positioned {

	/**
	 * Calculate and store so subsequent calls to wasInsideTerrain() return the
	 * same thing.
	 * 
	 * @return
	 */
	public boolean calculateIfInTerrain();

	public void collide(Positioned object);

	public void destroyAndKill();

	public double getMagnetMultiplier();

	public double getMass();

	public Portal getMostRecentTraveledPortal();

	public Vector getVelocity();

	@Override
	public boolean isDead();

	public boolean isFrozen();

	public void onEnterTerrain(Vector oldPosition, Vector newPosition);

	public void onLeaveTerrain(Vector oldPosition, Vector newPosition);

	public void onMoveOffScreen();

	/**
	 * Used to clean up the moving if it went off the screen
	 */
	@Override
	public void setDead();

	public void setFrozen(boolean frozen);

	public void setMass(double mass);

	public void setMostRecentTraveledPortal(Portal portal);

	public void setVelocity(Vector velocity);

}
