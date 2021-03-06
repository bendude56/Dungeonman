package com.bendude56.dungeonman.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.bendude56.dungeonman.GameInstance;
import com.bendude56.dungeonman.entity.Entity;
import com.bendude56.dungeonman.entity.EntityDroppedItem;
import com.bendude56.dungeonman.item.ItemStack;
import com.bendude56.dungeonman.world.tile.Tile;
import com.bendude56.dungeonman.world.tile.TileMetadata;
import com.bendude56.dungeonman.world.tile.TileState;
import com.bendude56.dungeonman.world.tile.TileWall;

/**
 * @author Benjamin C. Thomas
 */

public class World {
    private GameInstance game;
    
    private Tile[][] tiles;
    private TileMetadata[][] tileMeta;
    private int width, height;
    private int entryX, entryY;
    private int exitX, exitY;
    private int dungeonLevel;
    private boolean[][] tileKnown;
    private boolean[][] tileVisible;
    
    private HashMap<Integer, Entity> entities;
    
    public World(int width, int height, int dungeonLevel) {
        this.tiles = new Tile[width][height];
        this.tileMeta = new TileMetadata[width][height];
        this.width = width;
        this.height = height;
        this.dungeonLevel = dungeonLevel;
        this.entities = new HashMap<Integer, Entity>();
        this.game = GameInstance.getActiveInstance();
        
        this.tileKnown = new boolean[width][height];
        this.tileVisible = new boolean[width][height];
        
        setRect(0, 0, width - 1, height - 1, Tile.wall);
    }
    
    /**
     * Gets the width of this world.
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Gets the height of this world.
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Gets the tile at the specified location.
     * 
     * @param l The location at which to get the tile.
     */
    public Tile getTile(WorldLocation l) {
        return getTile(l.x, l.y);
    }
    
    /**
     * Gets the tile at the specified location.
     * 
     * @param x The x coordinate at which to get the tile.
     * @param y The y coordinate at which to get the tile.
     */
    public Tile getTile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException("Location is out of bounds!");
        }
        
        return tiles[x][y];
    }
    
    /**
     * Sets the tile at the specified location.
     * 
     * @param l The location at which to set the tile.
     */
    public void setTile(WorldLocation l, Tile tile) {
        setTile(l.x, l.y, tile);
    }
    
    /**
     * Sets the tile at the specified location.
     * 
     * @param x The x coordinate at which to set the tile.
     * @param y The y coordinate at which to set the tile.
     */
    public void setTile(int x, int y, Tile tile) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException("Location is out of bounds!");
        }
        
        tiles[x][y] = tile;
        tileMeta[x][y] = new TileMetadata();
    }
    
    /**
     * Sets the tile and metadata at the specified location.
     * 
     * @param l The location at which to set the tile and metadata.
     */
    public void setTileAndMetadata(WorldLocation l, Tile tile, TileMetadata m) {
        setTileAndMetadata(l.x, l.y, tile, m);
    }
    
    /**
     * Sets the tile and metadata at the specified location.
     * 
     * @param x The x coordinate at which to set the tile and metadata.
     * @param y The y coordinate at which to set the tile and metadata.
     */
    public void setTileAndMetadata(int x, int y, Tile tile, TileMetadata m) {
        setTile(x, y, tile);
        setMetadata(x, y, m);
    }
    
    /**
     * Gets the metadata present at the given location.
     * 
     * @param l The location to get the metadata from.
     */
    public TileMetadata getMetadata(WorldLocation l) {
        return getMetadata(l.x, l.y);
    }
    
    /**
     * Gets the metadata present at the given location.
     * 
     * @param x The x coordinate to get the metadata from.
     * @param y The y coordinate to get the metadata from.
     */
    public TileMetadata getMetadata(int x, int y) {
        return tileMeta[x][y];
    }
    
    /**
     * Sets the metadata at the given location.
     * 
     * @param l The location at which to set the metadata.
     */
    public void setMetadata(WorldLocation l, TileMetadata m) {
        setMetadata(l.x, l.y, m);
    }
    
    /**
     * Sets the metadata at the given location.
     * 
     * @param x The x coordinate at which to set the metadata.
     * @param y The y coordinate at which to set the metadata.
     */
    public void setMetadata(int x, int y, TileMetadata m) {
        tileMeta[x][y] = m;
    }
    
    /**
     * Gets a snapshot of the tile state at the specified location.
     * 
     * @param x The x coordinate to get the state from.
     * @param y The y coordinate to get the state from.
     */
    public TileState getTileState(int x, int y) {
        return getTileState(new WorldLocation(this, x, y));
    }
    
    /**
     * Gets a snapshot of the tile state at the specified location.
     * 
     * @param l The location to get the state from
     */
    public TileState getTileState(WorldLocation l) {
        return new TileState(l);
    }
    
    /**
     * Sets a rectangular area to a specific tile. This will also clear any
     * metadata present within the given rectangle.
     */
    public void setRect(WorldLocation l1, WorldLocation l2, Tile tile) {
        setRect(l1.x, l1.y, l2.x, l2.y, tile);
    }
    
    /**
     * Sets a rectangular area to a specific tile. This will also clear any
     * metadata present within the given rectangle.
     */
    public void setRect(int x1, int y1, int x2, int y2, Tile tile) {
        int temp;
        
        // Make sure x1 < x2 and y1 < y2
        if (x1 > x2) {
            temp = x1;
            x1 = x2;
            x2 = temp;
        }
        if (y1 > y2) {
            temp = y1;
            y1 = y2;
            y2 = temp;
        }
        
        // If we're going out of bounds, throw an exception
        if (y1 < 0 || y2 >= height || x1 < 0 || x2 >= width) {
            throw new IllegalArgumentException("Cannot set tiles outside of the world bounds");
        }
        
        // Set all tiles within the defined bounds
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                tiles[x][y] = tile;
                tileMeta[x][y] = new TileMetadata();
            }
        }
    }
    
    /**
     * Checks whether a rectangular area is available for world generation. A
     * location is defined as being available if and only if it is composed
     * solely of wall tiles.
     */
    public boolean isAvailable(WorldLocation l1, WorldLocation l2) {
        return isAvailable(l1.x, l1.y, l2.x, l2.y);
    }
    
    /**
     * Checks whether a rectangular area is available for world generation. A
     * location is defined as being available if and only if it is composed
     * solely of wall tiles.
     */
    public boolean isAvailable(int x1, int y1, int x2, int y2) {
        int temp;
        
        // Make sure x1 < x2 and y1 < y2
        if (x1 > x2) {
            temp = x1;
            x1 = x2;
            x2 = temp;
        }
        if (y1 > y2) {
            temp = y1;
            y1 = y2;
            y2 = temp;
        }
        
        // No space is available outside of world bounds
        if (y1 < 0 || y2 >= height || x1 < 0 || x2 >= width) {
            return false;
        }
        
        // If any tile within the bounds isn't a wall, the area is unavailable
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                if (!(tiles[x][y] instanceof TileWall))
                    return false;
            }
        }
        
        return true;
    }
    
    /**
     * Gets the location of the entry stairway into this world.
     */
    public WorldLocation getEntryLocation() {
        return new WorldLocation(this, entryX, entryY);
    }
    
    /**
     * Gets the location of the exit stairway from this world.
     * 
     * @return
     */
    public WorldLocation getExitLocation() {
        return new WorldLocation(this, exitX, exitY);
    }
    
    public void setEntryLocation(WorldLocation l) {
        entryX = l.x;
        entryY = l.y;
    }
    
    public void setExitLocation(WorldLocation l) {
        exitX = l.x;
        exitY = l.y;
    }
    
    public boolean isTileKnown(WorldLocation l) {
        return isTileKnown(l.x, l.y);
    }
    
    public boolean isTileKnown(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException("Location is out of bounds!");
        }
        
        return tileKnown[x][y];
    }
    
    public boolean isTileVisible(WorldLocation l) {
        return isTileVisible(l.x, l.y);
    }
    
    public boolean isTileVisible(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException("Location is out of bounds!");
        }
        
        return tileVisible[x][y];
    }
    
    public void setTileVisible(WorldLocation l, boolean visible) {
        setTileVisible(l.x, l.y, visible);
    }
    
    public void setTileVisible(int x, int y, boolean visible) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException("Location is out of bounds!");
        }
        
        tileVisible[x][y] = visible;
        tileKnown[x][y] = true;
    }
    
    public void clearTileVisibility() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tileVisible[x][y] = false;
            }
        }
    }
    
    public int getFloor() {
        return dungeonLevel;
    }
    
    public void dropItemStack(ItemStack stack, WorldLocation l) {
        dropItemStack(stack, l.x, l.y);
    }
    
    public void dropItemStack(ItemStack stack, int x, int y) {
        List<Entity> entities = getEntities(x, y);
        
        for (Entity e : entities) {
            if (e instanceof EntityDroppedItem && ((EntityDroppedItem) e).getItemStack().getItem() == stack.getItem()
                    && ((EntityDroppedItem) e).getItemStack().getMetadata().equals(stack.getMetadata())) {
                ((EntityDroppedItem) e).getItemStack().setAmount(
                        ((EntityDroppedItem) e).getItemStack().getAmount() + stack.getAmount());
                return;
            }
        }
        
        addEntity(new EntityDroppedItem(new WorldLocation(this, x, y), stack));
    }
    
    public void addEntity(Entity e) {
        entities.put(e.getEntityId(), e);
    }
    
    public void removeEntity(Entity e) {
        entities.remove(e.getEntityId());
    }
    
    public Entity getEntity(int id) {
        return entities.get(id);
    }
    
    public List<Entity> getEntities(WorldLocation l) {
        return getEntities(l.x, l.y);
    }
    
    public List<Entity> getEntities(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException("Location is out of bounds!");
        }
        
        ArrayList<Entity> presentEntities = new ArrayList<Entity>();
        
        for (Entry<Integer, Entity> e : entities.entrySet()) {
            if (e.getValue().getLocation().equals(new WorldLocation(this, x, y))) {
                presentEntities.add(e.getValue());
            }
        }
        
        Collections.sort(presentEntities);
        return presentEntities;
    }
    
    public List<Entity> getEntities(WorldLocation l1, WorldLocation l2) {
        return getEntities(l1.x, l1.y, l2.x, l2.y);
    }
    
    public List<Entity> getEntities(int x1, int y1, int x2, int y2) {
        int temp;
        
        // Make sure x1 < x2 and y1 < y2
        if (x1 > x2) {
            temp = x1;
            x1 = x2;
            x2 = temp;
        }
        if (y1 > y2) {
            temp = y1;
            y1 = y2;
            y2 = temp;
        }
        
        ArrayList<Entity> presentEntities = new ArrayList<Entity>();
        
        for (Entry<Integer, Entity> e : entities.entrySet()) {
            WorldLocation l = e.getValue().getLocation();
            if (l.x >= x1 && l.x <= x2 && l.y >= y1 && l.y <= y2) {
                presentEntities.add(e.getValue());
            }
        }
        
        Collections.sort(presentEntities);
        return presentEntities;
    }
    
    public void clearEntities() {
        entities.clear();
    }
    
    public GameInstance getGameInstance() {
        return game;
    }
    
    public void doTurn() {
        ArrayList<Integer> deadEntities = new ArrayList<Integer>();
        
        for (Entry<Integer, Entity> e : entities.entrySet()) {
            if (!e.getValue().isDead())
                e.getValue().doTurn();
            if (e.getValue().isDead())
                deadEntities.add(e.getKey());
        }
        
        for (Integer dead : deadEntities) {
            entities.remove(dead);
        }
    }
}
