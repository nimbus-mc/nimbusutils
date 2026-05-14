package net.playnimbus.nimbusutils.networking;

import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public record Keybind(KeyMapping keybind, BiFunction<Keybind, Minecraft, Boolean> onPressed) {
	public static final Map<String, Keybind> REGISTRY = new HashMap<>();

	public void register() {
		KeyMappingHelper.registerKeyMapping(keybind());
		REGISTRY.put(keybind.getName(), this);
	}

	/**
	 * this gets ran at the end of the client's tick
	 */
	public void tick(Minecraft client) {
		while (keybind != null && keybind.consumeClick()) {
			onPressed().apply(this, client);
		}
	}

	public static void tickAll(Minecraft client) {
		REGISTRY.forEach((i, k) -> k.tick(client));
	}
}
