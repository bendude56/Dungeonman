package com.bendude56.dungeonman.item;

/**
 * Benjamin C. Thomas
 * Computer Science 30
 * 2012/13 Semester 2
 * Centennial High School
 *
 * @author Benjamin C. Thomas
 */

public class ItemMetadataKey extends ItemMetadata {
	private int keyId;
	
	public ItemMetadataKey(int keyId) {
		this.keyId = keyId;
	}
	
	public int getKeyId() {
		return keyId;
	}

	@Override
	public ItemMetadata clone() {
		return new ItemMetadataKey(keyId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + keyId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemMetadataKey other = (ItemMetadataKey) obj;
		if (keyId != other.keyId)
			return false;
		return true;
	}
}
