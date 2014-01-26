package thebombzen.tanks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thebombzen.tanks.forceprovider.ForceProvider;
import thebombzen.tanks.object.Explosion;
import thebombzen.tanks.object.Portal;
import thebombzen.tanks.object.Tank;
import thebombzen.tanks.object.projectile.Projectile;
import thebombzen.tanks.object.property.Advanceable;
import thebombzen.tanks.object.property.Blocker;
import thebombzen.tanks.object.property.Moving;
import thebombzen.tanks.object.property.Positioned;
import thebombzen.tanks.object.property.Renderable;

public class World implements Runnable {

	private static final World world = new World();

	public static World getWorld() {
		return world;
	}

	private List<Explosion> explosions = new ArrayList<Explosion>();
	private List<Portal> portals = new ArrayList<Portal>();
	private List<Projectile> projectiles = new ArrayList<Projectile>();
	private List<Tank> tanks = new ArrayList<Tank>();
	private List<Advanceable> advanceables = new ArrayList<Advanceable>();
	private List<Positioned> positionedobjects = new ArrayList<Positioned>();
	private List<Moving> movingobjects = new ArrayList<Moving>();
	private List<Renderable> renderables = new ArrayList<Renderable>();
	private List<ForceProvider> forceProviders = new ArrayList<ForceProvider>();
	private List<Blocker> blockers = new ArrayList<Blocker>();
	private boolean prevBlocked = false;
	private boolean prevSecondBlocked = false;
	private Map<Moving, Boolean> prevInTerrains = new HashMap<Moving, Boolean>();

	private long tick = 0;

	public void addObject(Object o) {
		if (o instanceof Explosion) {
			explosions.add((Explosion) o);
			explode((Explosion) o);
		}
		if (o instanceof Portal) {
			portals.add((Portal) o);
		}
		if (o instanceof Projectile) {
			projectiles.add((Projectile) o);
		}
		if (o instanceof Tank) {
			tanks.add((Tank) o);
		}
		if (o instanceof Advanceable) {
			advanceables.add((Advanceable) o);
		}
		if (o instanceof Positioned) {
			positionedobjects.add((Positioned) o);
		}
		if (o instanceof Moving) {
			movingobjects.add((Moving) o);
			prevInTerrains.put((Moving) o, ((Moving) o).calculateIfInTerrain());
		}
		if (o instanceof Renderable) {
			renderables.add((Renderable) o);
		}
		if (o instanceof ForceProvider) {
			forceProviders.add((ForceProvider) o);
		}
		if (o instanceof Blocker) {
			blockers.add((Blocker) o);
		}
	}

	private void explode(Explosion explosion) {
		explosion.advance(Constants.TICK_TIME_STEP);
		/*
		 * Vector normal = Terrain.getTerrain().getSurfaceNormalVector(
		 * explosion.getPosition()); double minAngle; double maxAngle; if
		 * (normal.getX() == 0 && normal.getY() == 0) { minAngle = 0F; maxAngle
		 * = Constants.TWO_PI; } else { double ang = normal.getAngle(); minAngle
		 * = ang - Constants.PI_4; maxAngle = ang + Constants.PI_4; } for
		 * (double angle = minAngle; angle <= maxAngle; angle += (Tanks.random
		 * .nextDouble() + 1F) (maxAngle - minAngle) 6.5F /
		 * explosion.getRadius()) {
		 * 
		 * Vector pair = Vector.polarToCartesian(1.0, angle);
		 * 
		 * double variationX = (10D + 2.5D * (Tanks.random.nextDouble() +
		 * Tanks.random .nextDouble())); double variationY = (10D + 2.5D *
		 * (Tanks.random.nextDouble() + Tanks.random .nextDouble())); Vector
		 * position = new Vector(pair.getX() * variationX +
		 * explosion.getPosition().getX(), pair.getY() * variationY +
		 * explosion.getPosition().getY()); variationX = (3D + 0.75D *
		 * (Tanks.random.nextDouble() + Tanks.random .nextDouble())); variationY
		 * = (3D + 0.75D * (Tanks.random.nextDouble() + Tanks.random
		 * .nextDouble())); double radius = explosion.getRadius(); Vector
		 * velocity = new Vector(pair.getX() * radius * variationX, pair.getY()
		 * * radius * variationY); if (explosion instanceof NoFriendlyFire) {
		 * addObject(new SafeDebrisProjectile(position, velocity, 5.0D,
		 * ((NoFriendlyFire) explosion).getParentTank())); } else {
		 * addObject(new DebrisProjectile(position, velocity, 5.0D)); } }
		 */
	}

	public Advanceable[] getAllAdvanceables() {
		return advanceables.toArray(new Advanceable[0]);
	}

	public Blocker[] getAllBlockers() {
		return blockers.toArray(new Blocker[0]);
	}

	public Explosion[] getAllExplosions() {
		return explosions.toArray(new Explosion[0]);
	}

	public ForceProvider[] getAllForceProviders() {
		return forceProviders.toArray(new ForceProvider[0]);
	}

	public Moving[] getAllMovingObjects() {
		return movingobjects.toArray(new Moving[0]);
	}

	public Portal[] getAllPortals() {
		return portals.toArray(new Portal[0]);
	}

	public Positioned[] getAllPositionedObjects() {
		return positionedobjects.toArray(new Positioned[0]);
	}

	public Projectile[] getAllProjectiles() {
		return projectiles.toArray(new Projectile[0]);
	}

	public Renderable[] getAllRenderables() {
		return renderables.toArray(new Renderable[0]);
	}

	public Tank[] getAllTanks() {
		return tanks.toArray(new Tank[0]);
	}

	private Vector getNetForce(Moving moving) {
		ForceProvider[] forceProviders = getAllForceProviders();
		Vector force = Vector.ZERO;
		for (ForceProvider provider : forceProviders) {
			force = force.add(provider.getForce(moving));
		}
		return force;
	}

	public void markBlocked() {
		prevBlocked = true;
	}

	public void removeObject(Object o) {
		if (o instanceof Explosion) {
			explosions.remove(o);
		}
		if (o instanceof Portal) {
			portals.remove(o);
		}
		if (o instanceof Projectile) {
			projectiles.remove(o);
		}
		if (o instanceof Tank) {
			tanks.remove(o);
		}
		if (o instanceof Advanceable) {
			advanceables.remove(o);
		}
		if (o instanceof Positioned) {
			positionedobjects.remove(o);
		}
		if (o instanceof Moving) {
			movingobjects.remove(o);
			prevInTerrains.remove(o);
		}
		if (o instanceof Renderable) {
			renderables.remove(o);
		}
		if (o instanceof ForceProvider) {
			forceProviders.remove(o);
		}
		if (o instanceof Blocker) {
			blockers.remove(o);
		}
	}

	public void reset() {
		projectiles.clear();
		explosions.clear();
		portals.clear();
		tanks.clear();
		advanceables.clear();
		positionedobjects.clear();
		movingobjects.clear();
		renderables.clear();
		forceProviders.clear();
		blockers.clear();
		prevInTerrains.clear();
		prevBlocked = false;
		prevSecondBlocked = false;
		tick = 0;
	}

	@Override
	public void run() {
		try {
			tick(Constants.TICK_TIME_STEP);
			GamePanel.getGamePanel().render();
		} catch (Throwable t) {
			t.printStackTrace();
			throw new RuntimeException(t);
		}
	}

	private void tick(double timestep) {

		Advanceable[] advanceables = getAllAdvanceables();
		Positioned[] positioneds = getAllPositionedObjects();
		Moving[] movings = getAllMovingObjects();

		Vector[] previousPositions = new Vector[movings.length];
		for (int i = 0; i < movings.length; i++) {
			previousPositions[i] = movings[i].getPosition();
		}

		for (Advanceable adv : advanceables) {
			adv.advance(timestep);
		}

		boolean right = ControlPanel.getControlPanel().isRightPressed();
		boolean left = ControlPanel.getControlPanel().isLeftPressed();
		if (right && !left && tick % 2 == 0) {
			Tanks.getTanks().getCurrentTank().moveRight();
		} else if (left && !right && tick % 2 == 0) {
			Tanks.getTanks().getCurrentTank().moveLeft();
		}

		for (Positioned positioned : positioneds) {
			for (Moving moving : movings) {
				if (positioned == moving) {
					continue;
				}
				double totalRadius = positioned.getRadius()
						+ moving.getRadius();
				if (positioned.getPosition().subtract(moving.getPosition())
						.getNormSquared() <= totalRadius * totalRadius) {
					moving.collide(positioned);
				}
			}
		}

		Portal[] portals = getAllPortals();

		for (Moving moving : movings) {
			for (Portal portal : portals) {
				if (portal.getLinkedPortal() != null
						&& portal != moving.getMostRecentTraveledPortal()) {
					if (portal.getPosition().subtract(moving.getPosition())
							.getNormSquared() <= portal.getRadius()
							* portal.getRadius()) {
						moving.setPosition(portal.getLinkedPortal()
								.getPosition());
						moving.setMostRecentTraveledPortal(portal
								.getLinkedPortal());
					}
				}
			}
		}

		updateKinematics(movings, timestep);

		for (int i = 0; i < movings.length; i++) {
			boolean wasInTerrain = prevInTerrains.get(movings[i]);
			boolean isInTerrain = movings[i].calculateIfInTerrain();
			if (isInTerrain) {
				movings[i].onEnterTerrain(previousPositions[i],
						movings[i].getPosition());
			} else if (!isInTerrain && wasInTerrain) {
				movings[i].onLeaveTerrain(previousPositions[i],
						movings[i].getPosition());
			}
			prevInTerrains.put(movings[i], isInTerrain);
		}

		for (Moving moving : movings) {
			if (moving.getPosition().getX() < -moving.getRadius()
					|| moving.getPosition().getX() >= Constants.WIDTH
							+ moving.getRadius()
					|| moving.getPosition().getY() >= Constants.HEIGHT
							+ moving.getRadius()) {
				moving.onMoveOffScreen();
				moving.setDead();
			}
		}

		for (Positioned positioned : positioneds) {
			if (positioned.isDead()) {
				removeObject(positioned);
			}
		}

		boolean blocked = false;
		Blocker[] blockers = getAllBlockers();
		for (Blocker blocker : blockers) {
			if (blocker.isBlocking()) {
				blocked = true;
				break;
			}
		}

		if (!blocked && !prevBlocked && prevSecondBlocked) {
			if (tanks.size() >= 2) {
				ControlPanel.getControlPanel().postFire();
			}
		}

		prevSecondBlocked = prevBlocked;
		prevBlocked = blocked;

		tick++;

	}

	private void updateKinematics(Moving[] movings, double timestep) {
		Vector[] positions = new Vector[movings.length];
		Vector[][] velocities = new Vector[movings.length][4];
		Vector[][] accelerations = new Vector[movings.length][4];

		// set starting conditions and k1: y0 is position and velocity and k1 is
		// velocity and acceleration
		for (int i = 0; i < movings.length; i++) {
			positions[i] = movings[i].getPosition();
			velocities[i][0] = movings[i].getVelocity();
			accelerations[i][0] = getNetForce(movings[i]).divide(
					movings[i].getMass());
		}

		// get k2
		for (int i = 0; i < movings.length; i++) {
			Vector velocity = velocities[i][0];
			Vector acceleration = accelerations[i][0];

			movings[i].setPosition(positions[i].add(velocity
					.multiply(timestep * 0.5D)));
			movings[i].setVelocity(velocities[i][0].add(acceleration
					.multiply(timestep * 0.5D)));
		}
		for (int i = 0; i < movings.length; i++) {
			velocities[i][1] = movings[i].getVelocity();
			accelerations[i][1] = getNetForce(movings[i]).divide(
					movings[i].getMass());
		}

		// get k3
		for (int i = 0; i < movings.length; i++) {
			Vector velocity = velocities[i][1];
			Vector acceleration = accelerations[i][1];

			movings[i].setPosition(positions[i].add(velocity
					.multiply(timestep * 0.5D)));
			movings[i].setVelocity(velocities[i][0].add(acceleration
					.multiply(timestep * 0.5D)));
		}
		for (int i = 0; i < movings.length; i++) {
			velocities[i][2] = movings[i].getVelocity();
			accelerations[i][2] = getNetForce(movings[i]).divide(
					movings[i].getMass());
		}

		// get k4
		for (int i = 0; i < movings.length; i++) {
			Vector velocity = velocities[i][2];
			Vector acceleration = accelerations[i][2];

			movings[i]
					.setPosition(positions[i].add(velocity.multiply(timestep)));
			movings[i].setVelocity(velocities[i][0].add(acceleration
					.multiply(timestep)));
		}
		for (int i = 0; i < movings.length; i++) {
			velocities[i][3] = movings[i].getVelocity();
			accelerations[i][3] = getNetForce(movings[i]).divide(
					movings[i].getMass());
		}

		// finalize
		for (int i = 0; i < movings.length; i++) {
			Vector position = positions[i].add(velocities[i][0]
					.add(velocities[i][1].add(velocities[i][2]).multiply(2D))
					.add(velocities[i][3]).multiply(timestep / 6D));
			Vector velocity = velocities[i][0].add(accelerations[i][0]
					.add(accelerations[i][1].add(accelerations[i][2]).multiply(
							2D)).add(accelerations[i][3])
					.multiply(timestep / 6D));
			movings[i].setPosition(position);
			movings[i].setVelocity(velocity);
		}
	}

}
