package com.foxxite.mapteleport.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import static com.foxxite.mapteleport.MapTeleport.executeMapTeleport;

public class MapRightClick implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEvent(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (event.getHand() == EquipmentSlot.HAND) {
                if (player.isSneaking()) {
                    if (event.getItem() != null && event.getItem().getType() != null) {
                        if (event.getItem().getType() == Material.FILLED_MAP) {
                            if (player.hasPermission("mapteleport.use.item")) {
                                executeMapTeleport(event.getPlayer(), event.getItem(), null);
                            }
                        }
                    }
                }
            }
        }
    }

}
