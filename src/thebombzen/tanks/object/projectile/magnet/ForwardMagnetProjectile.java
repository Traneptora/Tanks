package thebombzen.tanks.object.projectile.magnet;

import thebombzen.tanks.Vector;
import thebombzen.tanks.forceprovider.ForceProvider;
import thebombzen.tanks.object.property.Moving;

public class ForwardMagnetProjectile extends MagnetProjectile implements
		ForceProvider {

	private boolean attract;

	public ForwardMagnetProjectile(Vector position, Vector velocity,
			double mass, boolean attract) {
		super(position, velocity, mass);
		this.attract = attract;
	}

	@Override
	public Vector getForce(Moving moving) {
		if (moving == this) {
			return Vector.ZERO;
		}
		Vector displacement = moving.getPosition().subtract(getPosition());
		Vector force = displacement.multiply(1E6D
				* moving.getMagnetMultiplier() * getMass()
				/ displacement.getNormCubed());
		if (attract) {
			force = force.negate();
		}
		return force;
	}

}
