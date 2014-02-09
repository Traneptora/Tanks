package thebombzen.tanks.object.projectile;

import thebombzen.tanks.Vector;
import thebombzen.tanks.object.Tank;
import thebombzen.tanks.object.property.NoFriendlyFire;

public class SafeDebrisProjectile extends DebrisProjectile implements
		NoFriendlyFire {

	private Tank parentTank;

	public SafeDebrisProjectile(Vector position, Vector velocity, double mass,
			Tank parentTank) {
		super(position, velocity, mass);
		this.parentTank = parentTank;
	}

	@Override
	public Tank getParentTank() {
		return parentTank;
	}

}
