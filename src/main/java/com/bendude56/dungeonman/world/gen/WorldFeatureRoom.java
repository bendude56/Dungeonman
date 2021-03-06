package com.bendude56.dungeonman.world.gen;

import java.util.Random;

import com.bendude56.dungeonman.world.WorldLocation;
import com.bendude56.dungeonman.world.WorldLocation.Direction;
import com.bendude56.dungeonman.world.tile.Tile;

/**
 * @author Benjamin C. Thomas
 */

public class WorldFeatureRoom extends WorldFeature {
    public int width;
    public int height;
    
    public WorldFeatureRoom(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public boolean checkLocation(WorldLocation l, Direction orientation) {
        WorldLocation l1, l2;
        
        if (orientation == null) {
            l1 = l.adjustLocation(-((width / 2) + 1), (height / 2) + 1);
            l2 = l1.adjustLocation(width + 2, -(height + 2));
            
            return l.world.isAvailable(l1, l2);
        } else {
            l1 = l.adjustLocation(-((width / 2) + 1), height + 2, orientation);
            l2 = l1.adjustLocation(width + 2, -(height + 2), orientation);
            
            return l.world.isAvailable(l1, l2);
        }
    }
    
    @Override
    public WorldFeatureInfo generateAt(DoorType door, WorldLocation l, Direction orientation, Random random) {
        WorldFeatureInfo info = new WorldFeatureInfo();
        WorldLocation l1, l2;
        int numItems = random.nextInt((width * height) / 5);
        
        if (orientation == null) {
            l1 = l.adjustLocation(-(width / 2), height / 2);
            l2 = l1.adjustLocation(width, -height);
            
            l.world.setRect(l1, l2, Tile.stoneFloor);
            
            for (int x = l1.x; x <= l2.x; x++) {
                info.walls.add(new WallInfo(new WorldLocation(l.world, x, l1.y - 1), Direction.NORTH));
                info.walls.add(new WallInfo(new WorldLocation(l.world, x, l2.y + 1), Direction.SOUTH));
            }
            
            for (int y = l1.y; y <= l2.y; y++) {
                info.walls.add(new WallInfo(new WorldLocation(l.world, l1.x - 1, y), Direction.WEST));
                info.walls.add(new WallInfo(new WorldLocation(l.world, l2.x + 1, y), Direction.EAST));
            }
        } else {
            int x1, y1, x2, y2;
            
            setDoor(l, door);
            
            l1 = l.adjustLocation(-(width / 2), height + 1, orientation);
            l2 = l1.adjustLocation(width, -height, orientation);
            
            if (l1.x > l2.x) {
                x1 = l2.x;
                x2 = l1.x;
            } else {
                x1 = l1.x;
                x2 = l2.x;
            }
            
            if (l1.y > l2.y) {
                y1 = l2.y;
                y2 = l1.y;
            } else {
                y1 = l1.y;
                y2 = l2.y;
            }
            
            l.world.setRect(x1, y1, x2, y2, Tile.stoneFloor);
            
            for (int i = 0; i <= width; i++) {
                info.walls.add(new WallInfo(l1.adjustLocation(i, 1, orientation), orientation));
            }
            
            for (int i = 0; i <= height; i++) {
                info.walls.add(new WallInfo(l1.adjustLocation(-1, -i, orientation), orientation.rotateCounterClockwise()));
                info.walls.add(new WallInfo(l2.adjustLocation(1, i, orientation), orientation.rotateClockwise()));
            }
            
            info.possibleStairs.add(l1.adjustLocation(random.nextInt(width + 1), -random.nextInt(height + 1), orientation));
            info.possibleMonsters.add(l1.adjustLocation(random.nextInt(width + 1), -random.nextInt(height + 1), orientation));
            
            for (int i = 0; i < numItems; i++) {
                WorldLocation itemLocation = l1.adjustLocation(random.nextInt(width + 1), -random.nextInt(height + 1),
                        orientation);
                
                if (!info.possibleStairs.contains(itemLocation) && !info.possibleItems.contains(itemLocation)) {
                    info.possibleItems.add(itemLocation);
                }
            }
        }
        
        return info;
    }
    
}
