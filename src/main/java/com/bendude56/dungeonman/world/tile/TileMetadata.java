package com.bendude56.dungeonman.world.tile;

/**
 * Benjamin C. Thomas
 * Computer Science 30
 * 2012/13 Semester 2
 * Centennial High School
 *
 * @author Benjamin C. Thomas
 */

public class TileMetadata {
	public TileMetadata clone() {
		return new TileMetadata();
	}

	@Override
	public boolean equals(Object obj) {
		return obj.getClass().equals(TileMetadata.class);
	}
}
