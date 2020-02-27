package com.foxxite.mapteleport.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.List;

import static com.foxxite.mapteleport.MapTeleport.executeMapTeleport;

public class CampfireTeleport implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    void onEvent(final PlayerInteractEvent event) {

        final Player player = event.getPlayer();
        final Block block = event.getClickedBlock();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getHand() == EquipmentSlot.HAND) {
                if (block.getType() == Material.CAMPFIRE) {
                    if (event.getItem() != null && event.getItem().getType() != null) {
                        if (event.getItem().getType() == Material.FILLED_MAP) {
                            if (player.hasPermission("mapteleport.use.campfire")) {
                                final List<Entity> nearbyEntites = (List<Entity>) block.getLocation().getWorld().getNearbyEntities(block.getLocation(), 5, 5, 5);

                                for (final Entity entity : nearbyEntites) {

                                    try {
                                        final LivingEntity livingEntity = (LivingEntity) entity;
                                        executeMapTeleport(livingEntity, event.getItem(), block.getLocation());
                                    } catch (final Exception e) {
                                        continue;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }

}
