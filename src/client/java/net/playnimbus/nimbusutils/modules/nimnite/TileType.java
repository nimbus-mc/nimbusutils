package net.playnimbus.nimbusutils.modules.nimnite;

public enum TileType {
	WALL("key.nimnite.tile_wall"),
	FLOOR("key.nimnite.tile_floor"),
	STAIR("key.nimnite.tile_stair"),
	PYRAMID("key.nimnite.tile_pyramid"),
	TRAP("key.nimnite.tile_trap");

	private final String key;

	TileType(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}
}
