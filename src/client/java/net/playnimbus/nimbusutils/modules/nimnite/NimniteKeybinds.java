package net.playnimbus.nimbusutils.modules.nimnite;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.playnimbus.nimbusutils.NimbusUtils;
import net.playnimbus.nimbusutils.NimbusUtilsClient;
import net.playnimbus.nimbusutils.networking.Keybind;
import net.playnimbus.nimbusutils.networking.KeybindPacket;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.atomic.AtomicBoolean;

public class NimniteKeybinds {
	private static final KeyMapping.Category CATEGORY = new KeyMapping.Category(Identifier.fromNamespaceAndPath(NimbusUtils.MOD_ID, "nimnite"));
	protected static final AtomicBoolean rightClickHeld = new AtomicBoolean(false);
	protected static final AtomicBoolean leftClickHeld = new AtomicBoolean(false);
	public static final AtomicBoolean adsActive = new AtomicBoolean(false);

	public static final Keybind GUN_RELOAD = new Keybind(new KeyMapping("key.nimnite.reload", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, CATEGORY), NimniteKeybinds::onKeybindPressed);

	public static final Keybind TILE_WALL = new Keybind(new KeyMapping(TileType.WALL.getKey(), InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z, CATEGORY), NimniteKeybinds::onKeybindPressed);
	public static final Keybind TILE_FLOOR = new Keybind(new KeyMapping(TileType.FLOOR.getKey(), InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_X, CATEGORY), NimniteKeybinds::onKeybindPressed);
	public static final Keybind TILE_STAIR = new Keybind(new KeyMapping(TileType.STAIR.getKey(), InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C, CATEGORY), NimniteKeybinds::onKeybindPressed);
	public static final Keybind TILE_PYRAMID = new Keybind(new KeyMapping(TileType.PYRAMID.getKey(), InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, CATEGORY), NimniteKeybinds::onKeybindPressed);
	public static final Keybind TILE_TRAP = new Keybind(new KeyMapping(TileType.TRAP.getKey(), InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_B, CATEGORY), NimniteKeybinds::onKeybindPressed);

	private static boolean onKeybindPressed(Keybind k, Minecraft client) {
		if (!NimbusUtilsClient.STATE.isConnected()) return false;

		var payload = new KeybindPacket(k.keybind().getName());
		ClientPlayNetworking.send(payload);

		return true;
	}

	public static boolean isRightClickHeld() {
		return rightClickHeld.get();
	}

	public static boolean isLeftClickHeld() {
		return leftClickHeld.get();
	}

	public static void registerAll() {
		TILE_WALL.register();
		TILE_FLOOR.register();
		TILE_STAIR.register();
		TILE_PYRAMID.register();
		TILE_TRAP.register();

		GUN_RELOAD.register();
	}
}
