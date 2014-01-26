package thebombzen.tanks.object.projectile.explosive;

import thebombzen.tanks.Vector;

public class DeltaForcedProjectile extends ExplosiveProjectile {

	private boolean deltaForced = false;

	public DeltaForcedProjectile(Vector position, Vector velocity, double mass) {
		super(position, velocity, mass, 25D);
	}

	public void deltaForce() {
		if (deltaForced) {
			return;
		}
		deltaForced = true;
		setVelocity(getVelocity().multiply(3D));
	}

}
