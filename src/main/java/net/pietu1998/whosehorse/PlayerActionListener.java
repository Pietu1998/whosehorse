package net.pietu1998.whosehorse;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerActionListener implements Listener {

	private WhoseHorse plugin;

	public PlayerActionListener(WhoseHorse plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void playerInteract(PlayerInteractEvent event) {
		switch (event.getAction()) {
		case RIGHT_CLICK_AIR:
		case RIGHT_CLICK_BLOCK:
			plugin.cancelActivation(event.getPlayer(), Messages.NOT_ANIMAL);
			break;
		default:
			break;
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void playerInteractWithEntity(PlayerInteractEntityEvent event) {
		if (plugin.interactWithEntity(event.getPlayer(), event.getRightClicked()))
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void playerLeave(PlayerQuitEvent event) {
		plugin.cancelActivation(event.getPlayer(), null);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void playerChangeWorld(PlayerChangedWorldEvent event) {
		plugin.cancelActivation(event.getPlayer(), Messages.TIMED_OUT);
	}

}
