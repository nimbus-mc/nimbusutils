package net.playnimbus.keybinds;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import net.playnimbus.NimbusUtils;
import net.playnimbus.NimbusUtilsClient;
import net.playnimbus.networking.KeybindPayload;
import net.playnimbus.nimnite.TileType;
import org.lwjgl.glfw.GLFW;

public class NimniteKeybinds {
	private static final KeyBinding.Category CATEGORY = new KeyBinding.Category(Identifier.of(NimbusUtils.MOD_ID, "nimnite"));

	public static final Keybind TILE_WALL = new Keybind(new KeyBinding(TileType.WALL.getKey(), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, CATEGORY), NimniteKeybinds::onTileKeybindPressed);
	public static final Keybind TILE_FLOOR = new Keybind(new KeyBinding(TileType.FLOOR.getKey(), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z, CATEGORY), NimniteKeybinds::onTileKeybindPressed);
	public static final Keybind TILE_STAIR = new Keybind(new KeyBinding(TileType.STAIR.getKey(), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_B, CATEGORY), NimniteKeybinds::onTileKeybindPressed);
	public static final Keybind TILE_PYRAMID = new Keybind(new KeyBinding(TileType.PYRAMID.getKey(), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, CATEGORY), NimniteKeybinds::onTileKeybindPressed);
	public static final Keybind TILE_TRAP = new Keybind(new KeyBinding(TileType.TRAP.getKey(), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_H, CATEGORY), NimniteKeybinds::onTileKeybindPressed);

	private static boolean onTileKeybindPressed(Keybind k, MinecraftClient client) {
		if (!NimbusUtilsClient.ENABLED) return false;

		var payload = new KeybindPayload(k.keybind().getId());
		ClientPlayNetworking.send(payload);

		return true;
	}

	public static void registerAll() {
		TILE_WALL.register();
		TILE_FLOOR.register();
		TILE_STAIR.register();
		TILE_PYRAMID.register();
		TILE_TRAP.register();
	}
}
