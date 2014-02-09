package thebombzen.tanks.object.projectile;

import java.awt.Color;
import java.awt.Shape;

import thebombzen.tanks.Vector;
import thebombzen.tanks.object.Tank;
import thebombzen.tanks.object.property.NoFriendlyFire;

public class DebrisProjectile extends Projectile {

	public DebrisProjectile(Vector position, Vector velocity, double mass) {
		super(position, velocity, mass);
	}

	@Override
	public void destroy() {

	}

	@Override
	public Color getColor() {
		return Color.BLACK;
	}

	@Override
	public double getMagnetMultiplier() {
		return 0;
	}

	@Override
	public double getRadius() {
		return 2D;
	}

	@Override
	public void onEnterTerrain(Shape outsideShape, Shape insideShape, Vector outsidePosition, Vector insidePosition) {
		setDead();
	}

	@Override
	protected void onImpactWithTank(Tank tank) {
		if (this instanceof NoFriendlyFire
				&& ((NoFriendlyFire) this).getParentTank() == tank) {
			return;
		}
		tank.damageTank(2D);
		setDead();
	}

}
