package com.bendude56.dungeonman.entity;

import com.bendude56.dungeonman.world.WorldLocation;

public abstract class EntityAlive extends Entity {
	protected int viewDistance = 0;
	private int maxHp = 1;
	private int hp = 1;
	
	public EntityAlive(WorldLocation l, int maxHp) {
		super(l);
		this.maxHp = maxHp;
		this.hp = maxHp;
	}
	
	public final boolean canSee(Entity e) {
		// TODO: Implement line of sight
		return true;
	}
	
	public int getViewDistance() {
		return viewDistance;
	}
	
	public int getHp() {
		return hp;
	}
	
	public int getMaxHp() {
		return maxHp;
	}
	
	@Override
	public void die() {
		hp = 0;
		super.die();
	}
	
	public void doDamage(int damage) {
		hp -= damage;
		
		if (hp > maxHp) {
			hp = maxHp;
		} else if (hp <= 0) {
			die();
		}
	}
}
