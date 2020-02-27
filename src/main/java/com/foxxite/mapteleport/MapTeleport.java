package com.foxxite.mapteleport;

import com.foxxite.mapteleport.events.CampfireTeleport;
import com.foxxite.mapteleport.events.ItemFrameMapRightClick;
import com.foxxite.mapteleport.events.MapRightClick;
import com.foxxite.mapteleport.events.PlayerLogin;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MapTeleport extends JavaPlugin {

    private PlayerLogin playerLogin;
    private CampfireTeleport campfireTeleport;
    private MapRightClick mapRightClick;
    private ItemFrameMapRightClick itemFrameMapRightClick;

    public static void executeMapTeleport(final LivingEntity entity, final ItemStack mapItem, final Location relativeLoc) {
        final MapMeta mapMeta = (MapMeta) mapItem.getItemMeta();
        final MapView map = mapMeta.getMapView();

        int mapX = 0;
        int mapY = 0;
        int mapZ = 0;

        int OriginalMapX = 0;
        int OriginalMapZ = 0;

        final World mapWorld = map.getWorld();

        boolean createDestinationCampfire = false;

        final Location entityLoc = entity.getLocation();
        entityLoc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, entityLoc, 10);
        entityLoc.getWorld().playSound(entityLoc, Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1, 1);

        if (relativeLoc == null) {
            mapX = map.getCenterX();
            mapZ = map.getCenterZ();
        } else {

            OriginalMapX = map.getCenterX();
            OriginalMapZ = map.getCenterZ();

            final double relavitveX = relativeLoc.getBlockX() - entityLoc.getBlockX();
            final double relavitveZ = relativeLoc.getBlockZ() - entityLoc.getBlockZ();

            mapX = map.getCenterX() - (int) Math.round(relavitveX);
            mapZ = map.getCenterZ() - (int) Math.round(relavitveZ);

            createDestinationCampfire = true;
        }

        int i = 255;
        boolean safeBlockFound = false;

        while (!safeBlockFound) {
            final Material checkBlock = mapWorld.getBlockAt(mapX, i, mapZ).getType();
            if (checkBlock != Material.AIR && checkBlock != Material.CAVE_AIR && (checkBlock.isSolid() || (checkBlock == Material.WATER || checkBlock == Material.LAVA))) {
                safeBlockFound = true;
                mapY = i + 1;

                if (checkBlock == Material.WATER || checkBlock == Material.LAVA) {
                    mapWorld.getBlockAt(mapX, i, mapZ).setType(Material.DIRT);
                }

            } else if (i > 0) {
                i--;
            } else {
                entity.sendMessage(Common.colorize("&8( &6&l!&8 ) &cNo safe teleport location found."));
                return;
            }
        }

        if (createDestinationCampfire) {

            boolean isCampfire = false;

            int y = 255;
            while (y > 0) {
                if (mapWorld.getBlockAt(OriginalMapX, y, OriginalMapZ).getType() == Material.CAMPFIRE) {
                    isCampfire = true;
                    break;
                } else if (y > 0) {
                    y = y - 1;
                } else {
                    break;
                }
            }
            if (!isCampfire) {
                mapWorld.getBlockAt(OriginalMapX, i + 1, OriginalMapZ).setType(Material.CAMPFIRE);
            }
        }

        final Location teleportLocation = new Location(mapWorld, mapX + 0.5, mapY, mapZ + 0.5);
        teleportLocation.setPitch(entity.getLocation().getPitch());
        teleportLocation.setYaw(entity.getLocation().getYaw());

        double hungerCost = 0;
        if (entity.getWorld() == mapWorld) {
            hungerCost = (entity.getLocation().distance(teleportLocation) * 0.05);
        } else {
            hungerCost = 20;
        }

        entity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 1, true), true);
        entity.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20, 1, true), true);

        entity.teleport(teleportLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
        entity.damage(0.1);

        if (entity.getType() == EntityType.PLAYER) {
            final Player player = (Player) entity;

            player.playSound(entityLoc, Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1000, 1);
            final int newLevel = player.getFoodLevel() - (int) hungerCost;
            if (newLevel < 2) {
                player.setFoodLevel(2);
            } else {
                player.setFoodLevel(newLevel);
            }
        }

        if (mapItem.getItemMeta().getDisplayName().equalsIgnoreCase("Map") || mapItem.getItemMeta().getDisplayName().isEmpty()) {
            entity.sendMessage(Common.colorize("&8( &6&l!&8 ) &7You have been teleported to: " + mapX + ", " + mapY + ", " + mapZ + ", " + mapWorld.getName()));
        } else {
            entity.sendMessage(Common.colorize("&8( &6&l!&8 ) &7You have been teleported to: " + mapItem.getItemMeta().getDisplayName()));
        }

    }

    @Override
    public void onEnable() {
        System.out.println("Foxxite's Map Teleport plugin enabled");
        this.mapRightClick = new MapRightClick();
        this.itemFrameMapRightClick = new ItemFrameMapRightClick();
        this.campfireTeleport = new CampfireTeleport();
        this.playerLogin = new PlayerLogin();

        this.getServer().getPluginManager().registerEvents(this.mapRightClick, this);
        this.getServer().getPluginManager().registerEvents(this.itemFrameMapRightClick, this);
        this.getServer().getPluginManager().registerEvents(this.campfireTeleport, this);
        this.getServer().getPluginManager().registerEvents(this.playerLogin, this);
    }

    @Override
    public void onDisable() {
        System.out.println("Foxxite's Map Teleport plugin disabled");
        this.mapRightClick = null;
        this.itemFrameMapRightClick = null;
        this.campfireTeleport = null;
        this.playerLogin = null;
    }

}
