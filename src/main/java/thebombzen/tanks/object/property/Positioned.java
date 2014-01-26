package thebombzen.tanks.object.property;

import java.awt.Shape;

import thebombzen.tanks.Vector;

public abstract class Positioned {
	
	protected Vector position;
	protected boolean dead;
	
	protected Positioned(Vector position){
		this.position = position;
	}
	
	public abstract Shape getBoundingShape();

	public Vector getPosition(){
		return position;
	}

	public boolean isDead(){
		return dead;
	}

	public void setDead(){
		dead = true;
	}
	
	public void setPosition(Vector position){
		this.position = position;
	}
	
}
