package thebombzen.tanks.object.projectile.magnet;

import thebombzen.tanks.Vector;
import thebombzen.tanks.forceprovider.ForceProvider;
import thebombzen.tanks.object.property.Moving;

public class SpiralMagnetProjectile extends MagnetProjectile implements
		ForceProvider {

	private boolean left;

	public SpiralMagnetProjectile(Vector position, Vector velocity,
			double mass, boolean left) {
		super(position, velocity, mass);
		this.left = left;
	}

	@Override
	public Vector getForce(Moving moving) {
		if (moving == this) {
			return Vector.ZERO;
		}
		Vector displacement = moving.getPosition().subtract(getPosition());
		Vector relativeVelocity = moving.getVelocity().subtract(getVelocity());
		double weightedMagneticField = 50D * moving.getMagnetMultiplier()
				* getMass() * relativeVelocity.negate().cross(displacement)
				/ displacement.getNormCubed();
		Vector force = relativeVelocity.cross(weightedMagneticField);
		if (left) {
			force = force.negate();
		}
		return force;
	}

	@Override
	public String toString() {
		return "SpiralMagnetProjectile [right=" + left + ", getPosition()="
				+ getPosition() + ", getVelocity()=" + getVelocity()
				+ ", isFrozen()=" + isFrozen() + ", getMass()=" + getMass()
				+ "]";
	}

}
