package com.bendude56.dungeonman.world.tile;

import java.awt.Color;
import java.awt.Graphics;

import com.bendude56.dungeonman.entity.Entity;
import com.bendude56.dungeonman.entity.EntityPlayer;

/**
 * @author Benjamin C. Thomas
 */

public class TileWall extends Tile {
    
    public TileWall() {
        super(0);
    }
    
    @Override
    public Color getColor(TileState state) {
        return Color.gray;
    }
    
    @Override
    public boolean onPlayerMove(TileState state, EntityPlayer player) {
        return false;
    }
    
    @Override
    public boolean onEntityMove(TileState state, Entity e) {
        return false;
    }
    
    @Override
    public void render(Graphics g, int x, int y, TileState state) {
        g.setColor(getColor(state));
        g.fillRect(x, y, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
    }
    
    @Override
    public boolean canItemAlgorithmPass() {
        return false;
    }
    
    @Override
    public boolean isTransparent() {
        return false;
    }
    
    @Override
    public boolean onPlayerClimb(TileState state, EntityPlayer player) {
        return false;
    }
    
}
