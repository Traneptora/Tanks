package thebombzen.tanks.object.projectile.teleport;

import java.awt.Shape;

import thebombzen.tanks.Vector;
import thebombzen.tanks.object.Tank;
import thebombzen.tanks.object.Terrain;
import thebombzen.tanks.object.projectile.Projectile;

public class TeleporterProjectile extends Projectile {

	private Tank parentTank;

	public TeleporterProjectile(Vector position, Vector velocity, double mass,
			Tank parentTank) {
		super(position, velocity, mass);
		this.parentTank = parentTank;
	}

	@Override
	public void destroy() {
		parentTank.setPosition(getPosition());
	}

	@Override
	public double getMagnetMultiplier() {
		return 0D;
	}

	public Tank getParentTank() {
		return parentTank;
	}

	@Override
	public void onEnterTerrain(Shape outsideShape, Shape insideShape, Vector outsidePosition, Vector insidePosition){
		parentTank.setPosition(Terrain.getTerrain().getOuterImpactLocation(outsideShape, insideShape, outsidePosition, insidePosition));
		setDead();
	}
}
