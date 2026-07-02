package net.playnimbus.nimbusutils.modules.nimnite;

import net.minecraft.network.chat.FontDescription;
import net.minecraft.resources.Identifier;

import java.util.Map;

// todo: somehow generate these (most likely going to have to make a separate tool that fetches mappings and gens these)
public interface ClientPackFonts {
    FontDescription ITEMS_GUN = new FontDescription.Resource(Identifier.fromNamespaceAndPath("nimnite", "fq"));

    FontDescription MUZZLE_FLASH = new FontDescription.Resource(Identifier.fromNamespaceAndPath("nimnite", "fr"));

    FontDescription OVERLAYS = new FontDescription.Resource(Identifier.fromNamespaceAndPath("nimnite", "fs"));

    FontDescription ITEMS_CONSUMABLE = new FontDescription.Resource(Identifier.fromNamespaceAndPath("nimnite", "ft"));

    FontDescription ITEM_NAME = new FontDescription.Resource(Identifier.fromNamespaceAndPath("nimnite", "fu"));

    FontDescription ITEMS_PICKAXE = new FontDescription.Resource(Identifier.fromNamespaceAndPath("nimnite", "fv"));

    FontDescription ITEMS_RARITY = new FontDescription.Resource(Identifier.fromNamespaceAndPath("nimnite", "fw"));

    FontDescription ICONS = new FontDescription.Resource(Identifier.fromNamespaceAndPath("nimnite", "fx"));

    FontDescription WORLD_MAP = new FontDescription.Resource(Identifier.fromNamespaceAndPath("nimnite", "fy"));

    Map<String, FontDescription> MAPPINGS = Map.ofEntries(Map.entry("items_gun", ITEMS_GUN), Map.entry("muzzle_flash", MUZZLE_FLASH), Map.entry("overlays", OVERLAYS), Map.entry("items_consumable", ITEMS_CONSUMABLE), Map.entry("item_name", ITEM_NAME), Map.entry("items_pickaxe", ITEMS_PICKAXE), Map.entry("items_rarity", ITEMS_RARITY), Map.entry("icons", ICONS), Map.entry("world_map", WORLD_MAP));
}

