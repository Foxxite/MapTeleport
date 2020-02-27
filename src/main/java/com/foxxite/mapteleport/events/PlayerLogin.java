package com.foxxite.mapteleport.events;

import com.foxxite.mapteleport.Common;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLogin implements Listener {

    void onPlayerLogin(final PlayerLoginEvent event) {
        final Player player = event.getPlayer();

        if (player.getLocale().contains("zh")) {
            player.sendMessage(Common.colorize("&6&l~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"));
            player.sendMessage(Common.colorize("&0"));
            player.sendMessage(Common.colorize("&3 MapTeleport"));
            player.sendMessage(Common.colorize("&0"));
            player.sendMessage(Common.colorize("&e該地圖瞬移的版本可能不會從原來的源下載。"));
            player.sendMessage(Common.colorize("&b如果是這種情況，請從以下位置下載插件："));
            player.sendMessage(Common.colorize("&9https://www.spigotmc.org/resources/74472/"));
            player.sendMessage(Common.colorize("&0"));
            player.sendMessage(Common.colorize("&6&l~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"));
        }
    }

}
