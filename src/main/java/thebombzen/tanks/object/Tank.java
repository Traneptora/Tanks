package thebombzen.tanks.object;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;

import thebombzen.tanks.Constants;
import thebombzen.tanks.ControlPanel;
import thebombzen.tanks.RenderHelper;
import thebombzen.tanks.Vector;
import thebombzen.tanks.World;
import thebombzen.tanks.object.projectile.Projectile;
import thebombzen.tanks.object.property.Advanceable;
import thebombzen.tanks.object.property.Moving;
import thebombzen.tanks.object.property.Positioned;
import thebombzen.tanks.object.property.Renderable;

public class Tank implements Moving, Positioned, Renderable, Advanceable {

	private int playerNumber;
	private Vector position;
	private Vector velocity;

	private Color color;
	private double health = 100D;

	private double fireAngle;
	private int firePower = 70;
	private int selectedAmmo = 0;
	private int selectedMass = 1;
	private boolean dead = false;
	private Portal mostRecentTraveledPortal = null;
	private boolean frozen = false;

	public Tank(int playerNumber, Vector position) {
		this.position = position;
		this.velocity = Vector.ZERO;
		this.playerNumber = playerNumber;
		if (position.getX() < Constants.WIDTH / 2) {
			fireAngle = -Math.PI / 6D;
		} else {
			fireAngle = -Math.PI * (5D / 6D);
		}
		if (playerNumber == 0) {
			color = Color.BLUE;
		} else {
			color = Color.RED;
		}
	}

	@Override
	public void advance(double timestep) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				ControlPanel.getControlPanel().getTankHealthBars()[getPlayerNumber()]
						.setValue((int) getHealth());
			}
		});
	}

	@Override
	public boolean calculateIfInTerrain() {
		return Terrain.getTerrain().doesTerrainExistAt(getPosition());
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

	public void damageTank(double damage) {
		if (isDead()) {
			return;
		}
		health -= damage;
		if (health <= 0) {
			health = 0;
			destroyAndKill();
		}
	}

	protected void destroy() {
		World.getWorld().addObject(
				new Explosion(getPosition(), 150D, 1D, RenderHelper
						.getInverse(getColor())));
	}

	@Override
	public void destroyAndKill() {
		destroy();
		setDead();
	}

	public Color getColor() {
		return color;
	}

	public double getFireAngle() {
		return fireAngle;
	}

	public int getFirePower() {
		return firePower;
	}

	public double getHealth() {
		return health;
	}

	@Override
	public double getMagnetMultiplier() {
		return 0D;
	}

	@Override
	public double getMass() {
		return 1E5D;
	}

	@Override
	public Portal getMostRecentTraveledPortal() {
		return mostRecentTraveledPortal;
	}

	public int getPlayerNumber() {
		return playerNumber;
	}

	@Override
	public Vector getPosition() {
		return position;
	}

	@Override
	public double getRadius() {
		return 10D;
	}

	@Override
	public int getRenderLayer() {
		return 0;
	}

	public int getSelectedAmmo() {
		return selectedAmmo;
	}

	public int getSelectedMass() {
		return selectedMass;
	}

	@Override
	public Vector getVelocity() {
		return velocity;
	}

	public boolean isBlocking() {
		return false;
	}

	@Override
	public boolean isDead() {
		return dead;
	}

	@Override
	public boolean isFrozen() {
		return frozen;
	}

	private void move(int deltaX) { // either -1 or 1
		double y = getPosition().getY();
		while (Terrain.getTerrain().doesTerrainExistAt(
				new Vector(getPosition().getX() + deltaX, y - 1D))) {
			y--;
		}
		double slope = (y - getPosition().getY()) / deltaX;
		if (slope > 2D || slope < -2D) {
			return;
		}
		setPosition(new Vector(getPosition().getX() + deltaX, y));
	}

	public void moveLeft() {
		if (getPosition().getX() <= 0) {
			return;
		}
		move(-1);
	}

	public void moveRight() {
		if (getPosition().getX() >= Constants.WIDTH - 1) {
			return;
		}
		move(1);
	}

	@Override
	public void onEnterTerrain(Vector oldPosition, Vector newPosition) {
		setVelocity(Vector.ZERO);
		setFrozen(true);
		// setPosition(Terrain.getTerrain().getInnerImpactLocation(newPosition,
		// oldPosition));
	}

	protected void onImpactWithExplosion(Explosion ex) {
		damageTank(Constants.TICK_TIME_STEP * 20D);
	}

	protected void onImpactWithPortal(Portal portal) {

	}

	protected void onImpactWithProjectile(Projectile projectile) {

	}

	protected void onImpactWithTank(Tank tank) {

	}

	@Override
	public void onLeaveTerrain(Vector oldPosition, Vector newPosition) {
		setFrozen(false);
	}

	@Override
	public void onMoveOffScreen() {
		destroy();
	}

	@Override
	public void render(Graphics2D g2) {
		double radius = getRadius();
		RenderHelper.drawCircle(g2, getPosition(), (int) radius, getColor());
		Vector barrelVector = Vector.polarToCartesian(radius * 2.5D,
				getFireAngle());
		Vector barrelEnd = getPosition().add(barrelVector);
		g2.setColor(RenderHelper.getInverse(getColor()));
		g2.drawLine(getPosition().getIntegerX(), getPosition().getIntegerY(),
				barrelEnd.getIntegerX(), barrelEnd.getIntegerY());
	}

	@Override
	public void setDead() {
		dead = true;
		ControlPanel.getControlPanel().playerWon(1 - getPlayerNumber());
	}

	public void setFireAngle(double angle) {
		fireAngle = angle;
	}

	public void setFirePower(int firePower) {
		this.firePower = firePower;
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

	}

	@Override
	public void setMostRecentTraveledPortal(Portal portal) {
		mostRecentTraveledPortal = portal;
	}

	@Override
	public void setPosition(Vector position) {
		this.position = position;
	}

	public void setSelectedAmmo(int selectedAmmo) {
		this.selectedAmmo = selectedAmmo;
	}

	public void setSelectedMass(int selectedMass) {
		this.selectedMass = selectedMass;
	}

	@Override
	public void setVelocity(Vector velocity) {
		if (frozen) {
			return;
		}
		this.velocity = velocity;
	}

	@Override
	public String toString() {
		return "Tank [getHealth()=" + getHealth() + ", getPlayerNumber()="
				+ getPlayerNumber() + ", getFireAngle()=" + getFireAngle()
				+ ", getPosition()=" + getPosition() + ", getVelocity()="
				+ getVelocity() + "]";
	}

}
