package thebombzen.tanks.object.projectile;

import java.awt.Color;
import java.awt.Graphics2D;

import thebombzen.tanks.RenderHelper;
import thebombzen.tanks.Vector;
import thebombzen.tanks.object.Explosion;
import thebombzen.tanks.object.Portal;
import thebombzen.tanks.object.Tank;
import thebombzen.tanks.object.Terrain;
import thebombzen.tanks.object.property.Advanceable;
import thebombzen.tanks.object.property.Blocker;
import thebombzen.tanks.object.property.Moving;
import thebombzen.tanks.object.property.Positioned;
import thebombzen.tanks.object.property.Renderable;

public abstract class Projectile implements Renderable, Positioned, Moving,
		Advanceable, Blocker {

	private boolean dead = false;
	private boolean frozen = false;
	private Portal mostRecentTraveledPortal = null;

	private Vector position;
	private Vector velocity;
	private double mass;
	private boolean wasInTerrain;

	protected Projectile(Vector position, Vector velocity, double mass) {
		this.position = position;
		this.velocity = velocity;
		this.mass = mass;
		calculateIfInTerrain();
	}

	@Override
	public boolean calculateIfInTerrain() {
		wasInTerrain = calculateIfInTerrain0();
		return wasInTerrain;
	}

	protected boolean calculateIfInTerrain0() {
		return Terrain.getTerrain().doesTerrainExistAt(getPosition());
	}

	/**
	 * Includes Portals, Explosions, Tanks, and other Projectiles
	 */
	@Override
	public void collide(Positioned positioned) {
		if (positioned instanceof Explosion) {
			onImpactWithExplosion((Explosion) positioned);
		} else if (positioned instanceof Portal) {
			onImpactWithPortal((Portal) positioned);
		} else if (positioned instanceof Tank) {
			onImpactWithTank((Tank) positioned);
		} else if (positioned instanceof Projectile) {
			onImpactWithProjectile((Projectile) positioned);
		}
	}

	/**
	 * This is called when the Projectile will disappear actively. Do not call
	 * on out-of-bounds or other passive disappearances. This will not make the
	 * Projectile die, it will only destroy it.
	 */
	public abstract void destroy();

	@Override
	public void destroyAndKill() {
		destroy();
		setDead();
	}

	public Color getColor() {
		return new Color(0x00A000);
	}

	@Override
	public abstract double getMagnetMultiplier();

	@Override
	public double getMass() {
		return mass;
	}

	@Override
	public Portal getMostRecentTraveledPortal() {
		return this.mostRecentTraveledPortal;
	}

	@Override
	public Vector getPosition() {
		return position;
	}

	@Override
	public double getRadius() {
		return 5D * getMass();
	}

	@Override
	public int getRenderLayer() {
		return 500;
	}

	@Override
	public Vector getVelocity() {
		if (frozen) {
			return Vector.ZERO;
		}
		return velocity;
	}

	@Override
	public boolean isBlocking() {
		return !isFrozen();
	}

	/**
	 * 
	 * @return Whether this Projectile is marked for cleanup.
	 */
	@Override
	public boolean isDead() {
		return dead;
	}

	@Override
	public boolean isFrozen() {
		return frozen;
	}

	@Override
	public abstract void onEnterTerrain(Vector oldPosition, Vector newPosition);

	protected void onImpactWithExplosion(Explosion explosion) {
		destroyAndKill();
	}

	/**
	 * Do not travel through the portal here. That will be handled by the
	 * central updating mechanism.
	 * 
	 * @param portal
	 * @return Whether to travel through the portal.
	 */
	protected boolean onImpactWithPortal(Portal portal) {
		return true;
	}

	protected void onImpactWithProjectile(Projectile projectile) {
		destroyAndKill();
	}

	protected void onImpactWithTank(Tank tank) {
		destroyAndKill();
	}

	@Override
	public abstract void onLeaveTerrain(Vector oldPosition, Vector newPosition);

	protected void performElastiCollision(Positioned positioned) {
		Vector collisionDirection = getPosition().subtract(
				positioned.getPosition());
		Vector inNormalVelocity = getVelocity()
				.getComponent(collisionDirection);
		if (positioned instanceof Moving) {
			Vector keptVelocity = getVelocity().subtract(inNormalVelocity);
			Moving object = (Moving) positioned;
			Vector otherNormalVelocity = object.getVelocity().getComponent(
					collisionDirection);
			Vector outNormalVelocity = inNormalVelocity
					.multiply(getMass() - object.getMass())
					.add(otherNormalVelocity.multiply(2.0D * object.getMass()))
					.divide(getMass() + object.getMass());
			setVelocity(outNormalVelocity.add(keptVelocity));
		} else {
			setVelocity(getVelocity().subtract(
					collisionDirection.multiply(2.0D)));
		}
	}

	@Override
	public void render(Graphics2D g2) {
		RenderHelper.drawCircle(g2, getPosition(), (int) getRadius(),
				getColor());
	}

	/**
	 * Mark this Projectile for cleanup.
	 */
	@Override
	public void setDead() {
		dead = true;
	}

	@Override
	public void setFrozen(boolean frozen) {
		if (frozen) {
			setVelocity(Vector.ZERO);
		}
		this.frozen = frozen;
	}

	@Override
	public void setMass(double mass) {
		this.mass = mass;
	}

	@Override
	public void setMostRecentTraveledPortal(Portal portal) {
		this.mostRecentTraveledPortal = portal;
	}

	@Override
	public void setPosition(Vector position) {
		this.position = position;
	}

	@Override
	public void setVelocity(Vector velocity) {
		if (frozen) {
			return;
		}
		this.velocity = velocity;
	}

	@Override
	public abstract String toString();

	public boolean wasInsideTerrain() {
		return wasInTerrain;
	}

}
