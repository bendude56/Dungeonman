package com.bendude56.dungeonman.world.tile;

/**
 * @author Benjamin C. Thomas
 */

public class TileMetadataStairs extends TileMetadata {
    private final boolean isGoingUp;
    
    public TileMetadataStairs(boolean isGoingUp) {
        this.isGoingUp = isGoingUp;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (isGoingUp ? 1231 : 1237);
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (getClass() != obj.getClass())
            return false;
        TileMetadataStairs other = (TileMetadataStairs) obj;
        if (isGoingUp != other.isGoingUp)
            return false;
        return true;
    }
    
    /**
     * Gets a boolean value representing whether the stairs on the specified
     * tile are going up or down.
     * 
     * @return True if the stairs are going up, false if they are going down.
     */
    public boolean isGoingUp() {
        return isGoingUp;
    }
    
    @Override
    public TileMetadata clone() {
        return new TileMetadataStairs(isGoingUp);
    }
}
