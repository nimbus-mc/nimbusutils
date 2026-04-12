package net.playnimbus.nimbusutils.nimnite;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import net.playnimbus.NimbusUtils;
import net.playnimbus.nimbusutils.NimbusUtilsClient;
import net.playnimbus.nimbusutils.Keybind;
import net.playnimbus.nimbusutils.networking.KeybindPayload;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.atomic.AtomicBoolean;

public class NimniteKeybinds {
	private static final KeyBinding.Category CATEGORY = new KeyBinding.Category(Identifier.of(NimbusUtils.MOD_ID, "nimnite"));
	protected static final AtomicBoolean rightClickHeld = new AtomicBoolean(false);
	protected static final AtomicBoolean leftClickHeld = new AtomicBoolean(false);

	public static final Keybind GUN_RELOAD = new Keybind(new KeyBinding("key.nimnite.reload", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, CATEGORY), NimniteKeybinds::onKeybindPressed);

	public static final Keybind TILE_WALL = new Keybind(new KeyBinding(TileType.WALL.getKey(), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z, CATEGORY), NimniteKeybinds::onKeybindPressed);
	public static final Keybind TILE_FLOOR = new Keybind(new KeyBinding(TileType.FLOOR.getKey(), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X, CATEGORY), NimniteKeybinds::onKeybindPressed);
	public static final Keybind TILE_STAIR = new Keybind(new KeyBinding(TileType.STAIR.getKey(), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_C, CATEGORY), NimniteKeybinds::onKeybindPressed);
	public static final Keybind TILE_PYRAMID = new Keybind(new KeyBinding(TileType.PYRAMID.getKey(), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, CATEGORY), NimniteKeybinds::onKeybindPressed);
	public static final Keybind TILE_TRAP = new Keybind(new KeyBinding(TileType.TRAP.getKey(), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_B, CATEGORY), NimniteKeybinds::onKeybindPressed);

	private static boolean onKeybindPressed(Keybind k, MinecraftClient client) {
		if (!NimbusUtilsClient.STATE.isInAGame()) return false;

		var payload = new KeybindPayload(k.keybind().getId());
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
