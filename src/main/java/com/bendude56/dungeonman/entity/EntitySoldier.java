package com.bendude56.dungeonman.entity;

import java.awt.Graphics;
import java.awt.Image;

import com.bendude56.dungeonman.gfx.ImageUtil;
import com.bendude56.dungeonman.item.Item;
import com.bendude56.dungeonman.item.ItemMetadata;
import com.bendude56.dungeonman.item.ItemStack;
import com.bendude56.dungeonman.world.WorldLocation;

/**
 * @author Benjamin C. Thomas
 */

public class EntitySoldier extends EntityEnemy {
    public static final Image sprite = ImageUtil.loadImage("/entity/enemy/soldier.png");
    
    public EntitySoldier(WorldLocation l) {
        super(l, new EntityStats(0, 10, 8, 5, 9, 3, 50), "Soldier", 3, 10);
    }
    
    @Override
    public void render(Graphics g, int x, int y) {
        g.drawImage(sprite, x, y, null);
    }
    
    @Override
    public void die() {
        super.die();
        
        dropItem(new ItemStack(Item.goldCoin, new ItemMetadata(), 5));
    }
    
}
