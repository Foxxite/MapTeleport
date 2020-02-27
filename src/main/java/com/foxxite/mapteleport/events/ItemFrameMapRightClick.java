package com.foxxite.mapteleport.events;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import static com.foxxite.mapteleport.MapTeleport.executeMapTeleport;

public class ItemFrameMapRightClick implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEvent(final PlayerInteractEntityEvent event) {

        final Player player = event.getPlayer();

        if (event.getRightClicked().getType() == EntityType.ITEM_FRAME) {
            final ItemFrame itemFrame = (ItemFrame) event.getRightClicked();
            if (itemFrame.getItem().getType() == Material.FILLED_MAP) {
                if (player.isSneaking()) {
                    if (player.hasPermission("mapteleport.use.frame")) {
                        event.setCancelled(true);
                        executeMapTeleport(player, itemFrame.getItem(), null);
                    }
                }
            }
        }

    }

}
