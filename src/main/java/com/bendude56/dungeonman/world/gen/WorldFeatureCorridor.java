package com.bendude56.dungeonman.world.gen;

import java.util.Random;

import com.bendude56.dungeonman.world.WorldLocation;
import com.bendude56.dungeonman.world.WorldLocation.Direction;
import com.bendude56.dungeonman.world.tile.Tile;

/**
 * @author Benjamin C. Thomas
 */

public class WorldFeatureCorridor extends WorldFeature {
    public int corridorLength;
    
    public WorldFeatureCorridor(int corridorLength) {
        this.corridorLength = corridorLength;
    }
    
    @Override
    public boolean checkLocation(WorldLocation l, Direction orientation) {
        return l.world.isAvailable(l.adjustLocation(-1, corridorLength, orientation), l.adjustLocation(1, 0, orientation));
    }
    
    @Override
    public WorldFeatureInfo generateAt(DoorType door, WorldLocation l, Direction orientation, Random random) {
        WorldFeatureInfo info = new WorldFeatureInfo();
        
        for (int i = 0; i < corridorLength; i++) {
            if (i == 0)
                setDoor(l, door);
            else
                l.setTile(Tile.stoneFloor);
            
            info.walls.add(new WallInfo(l.adjustLocation(1, 0, orientation), orientation.rotateClockwise()));
            info.walls.add(new WallInfo(l.adjustLocation(-1, 0, orientation), orientation.rotateCounterClockwise()));
            
            l = l.adjustLocation(0, 1, orientation);
        }
        
        info.walls.add(new WallInfo(l, orientation));
        
        return info;
    }
    
}
