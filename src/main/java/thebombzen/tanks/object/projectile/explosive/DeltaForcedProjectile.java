package thebombzen.tanks.object.projectile.explosive;

import thebombzen.tanks.Vector;
import thebombzen.tanks.object.projectile.DeltaForced;

public class DeltaForcedProjectile extends ExplosiveProjectile implements DeltaForced {

	private boolean deltaForced = false;

	public DeltaForcedProjectile(Vector position, Vector velocity, double mass) {
		super(position, velocity, mass, 20D);
	}

	public void deltaForce() {
		if (deltaForced) {
			return;
		}
		deltaForced = true;
		setVelocity(getVelocity().multiply(3D));
	}

}
