package thebombzen.tanks.forceprovider;

import thebombzen.tanks.Constants;
import thebombzen.tanks.Vector;
import thebombzen.tanks.object.property.Moving;

public class GravitationalForceProvider implements ForceProvider {

	public GravitationalForceProvider() {

	}

	@Override
	public Vector getForce(Moving moving) {
		return new Vector(0, Constants.G * moving.getMass());
	}

}
