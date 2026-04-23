package net.playnimbus.nimbusutils.networking;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public record Keybind(KeyBinding keybind, BiFunction<Keybind, MinecraftClient, Boolean> onPressed) {
	public static final Map<String, Keybind> REGISTRY = new HashMap<>();

	public void register() {
		KeyBindingHelper.registerKeyBinding(keybind());
		REGISTRY.put(keybind.getId(), this);
	}

	/**
	 * this gets ran at the end of the client's tick
	 */
	public void tick(MinecraftClient client) {
		while (keybind != null && keybind.wasPressed()) {
			onPressed().apply(this, client);
		}
	}

	public static void tickAll(MinecraftClient client) {
		REGISTRY.forEach((i, k) -> k.tick(client));
	}
}
