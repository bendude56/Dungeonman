package com.bendude56.dungeonman.entity;

import com.bendude56.dungeonman.world.World;
import com.bendude56.dungeonman.world.WorldLocation;

public abstract class Entity {
	private int x, y;
	private World world;
	private boolean dead = false;
	
	public Entity(WorldLocation l) {
		setLocation(l);
	}
	
	public final World getWorld() {
		return world;
	}
	
	public final WorldLocation getLocation() {
		return new WorldLocation(world, x, y);
	}
	
	public void setLocation(WorldLocation l) {
		x = l.x;
		y = l.y;
		world = l.world;
	}
	
	public abstract void doTurn();
	
	public boolean isVisible() {
		return world.isTileVisible(getLocation());
	}
	
	public final boolean isDead() {
		return dead;
	}
	
	public void die() {
		dead = true;
	}
}
