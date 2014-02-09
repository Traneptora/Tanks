package thebombzen.tanks.forceprovider;

import thebombzen.tanks.Tanks;
import thebombzen.tanks.Vector;
import thebombzen.tanks.object.property.Moving;

public class WindForceProvider implements ForceProvider {

	@Override
	public Vector getForce(Moving moving) {
		return new Vector(Tanks.getTanks().getWindStrength(), 0);
	}

}
