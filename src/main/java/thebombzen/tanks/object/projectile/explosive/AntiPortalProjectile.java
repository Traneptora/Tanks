package thebombzen.tanks.object.projectile.explosive;

import thebombzen.tanks.Vector;
import thebombzen.tanks.World;
import thebombzen.tanks.object.Portal;

public class AntiPortalProjectile extends ExplosiveProjectile {

	public AntiPortalProjectile(Vector position, Vector velocity, double mass) {
		super(position, velocity, mass, 25D);
	}

	@Override
	protected boolean onImpactWithPortal(Portal portal) {
		World.getWorld().removeObject(portal);
		if (portal.getLinkedPortal() != null) {
			World.getWorld().removeObject(portal.getLinkedPortal());
		}
		destroyAndKill();
		return false;
	}

}
