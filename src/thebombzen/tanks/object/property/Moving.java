package thebombzen.tanks.object.property;

import java.awt.Shape;

import thebombzen.tanks.Vector;
import thebombzen.tanks.object.Explosion;
import thebombzen.tanks.object.Portal;
import thebombzen.tanks.object.Tank;
import thebombzen.tanks.object.projectile.Projectile;

public abstract class Moving extends Positioned {

	protected Vector velocity;
	protected boolean frozen = false;
	protected Portal mostRecentlyTraveledPortal = null;
	protected double mass;
	
	protected Moving(Vector position, Vector velocity, double mass){
		super(position);
		this.velocity = velocity;
		this.mass = mass;
	}
	
	public void collide(Positioned object){
		if (object instanceof Projectile){
			onImpactWithProjectile((Projectile)object);
		}
		if (object instanceof Tank){
			onImpactWithTank((Tank)object);
		}
		if (object instanceof Explosion){
			onImpactWithExplosion((Explosion)object);
		}
		if (object instanceof Portal){
			onImpactWithPortal((Portal)object);
		}
	}
	protected abstract void destroy();
	public void destroyAndKill(){
		destroy();
		setDead();
	}
	public abstract double getMagnetMultiplier();
	public double getMass(){
		return mass;
	}
	
	public Portal getMostRecentlyTraveledPortal(){
		return mostRecentlyTraveledPortal;
	}

	public Vector getVelocity(){
		if (frozen){
			return Vector.ZERO;
		}
		return velocity;
	}

	public boolean isFrozen(){
		return frozen;
	}

	public abstract void onEnterTerrain(Shape outsideShape, Shape insideShape, Vector outsidePosition, Vector insidePosition);

	protected abstract void onImpactWithExplosion(Explosion explosion);

	protected abstract void onImpactWithPortal(Portal portal);

	protected abstract void onImpactWithProjectile(Projectile projectile);

	protected abstract void onImpactWithTank(Tank tank);
	
	public void onLeaveTerrain(Vector oldPosition, Vector newPosition){
		
	}

	public abstract void onMoveOffScreen();

	public void onTickInTerrain(Shape outsideShape, Shape insideShape, Vector outsidePosition, Vector insidePosition){
		onEnterTerrain(outsideShape, insideShape, outsidePosition, insidePosition);
	}

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

	public void setFrozen(boolean frozen){
		setVelocity(Vector.ZERO);
		this.frozen = frozen;
	}

	public void setMass(double mass){
		this.mass = mass;
	}

	public void setMostRecentlyTraveledPortal(Portal portal){
		mostRecentlyTraveledPortal = portal;
	}
	
	public void setVelocity(Vector velocity){
		this.velocity = velocity;
	}

}
