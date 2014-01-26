package thebombzen.tanks.object.projectile.teleport;

import thebombzen.tanks.ControlPanel;
import thebombzen.tanks.Vector;
import thebombzen.tanks.World;
import thebombzen.tanks.object.Portal;
import thebombzen.tanks.object.projectile.Projectile;

public class PortalProjectile extends Projectile {

	public PortalProjectile(Vector position, Vector velocity, double mass) {
		super(position, velocity, mass);
	}

	@Override
	public void advance(double timestep) {

	}

	@Override
	public void destroy() {
		Portal portal = new Portal(getPosition());
		World.getWorld().addObject(portal);
		ControlPanel.getControlPanel().setMostRecentlyFiredPortal(portal);
	}

	@Override
	public double getMagnetMultiplier() {
		return 0;
	}

	@Override
	public void onEnterTerrain(Vector oldPosition, Vector newPosition) {
		destroyAndKill();
	}
	
	public void onTickInTerrain(Vector oldPosition, Vector newPosition){
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
		return "PortalProjectile [getPosition()=" + getPosition()
				+ ", getVelocity()=" + getVelocity() + ", getMass()="
				+ getMass() + "]";
	}

}
