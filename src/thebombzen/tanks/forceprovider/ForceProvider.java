package thebombzen.tanks.forceprovider;

import thebombzen.tanks.Vector;
import thebombzen.tanks.object.property.Moving;

public interface ForceProvider {

	public Vector getForce(Moving moving);

}
