package net.pietu1998.whosehorse;

import java.text.MessageFormat;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

public class WhoseHorse extends JavaPlugin {

	@Override
	public void onEnable() {
		// Get configuration
		FileConfiguration config = getConfig();
		// Set default configuration
		config.addDefault("timeout", 20);
		// Save defaults to the file
		config.options().copyDefaults(true);
		saveConfig();
		// Register listener for events
		getServer().getPluginManager().registerEvents(new PlayerActionListener(this), this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// Get configuration file
		FileConfiguration config = getConfig();
		if (command.getName().equalsIgnoreCase("whose")) {
			// Check that the sender is a player
			if (!(sender instanceof Player)) {
				sender.sendMessage(Messages.MUST_BE_PLAYER);
				return true;
			}
			// Check for valid format, i.e. no arguments
			if (args.length > 0) {
				sender.sendMessage(MessageFormat.format(Messages.NO_ARGS_NEEDED, label));
				return true;
			}
			final Player player = (Player) sender;
			// Current activation time
			final long activationTime = System.currentTimeMillis();
			// Set the player's status to active
			player.setMetadata("whosehorse.activated", new FixedMetadataValue(this, activationTime));
			// Send success message
			player.sendMessage(Messages.COMMAND_SUCCESS);
			// Create cancel task
			class CancelTask implements Runnable {
				@Override
				public void run() {
					// Get activation time
					long activated = (long) MetadataHelper.getMetadata(player, "whosehorse.activated", WhoseHorse.this,
							-1);
					if (activated == activationTime)
						cancelActivation(player, Messages.TIMED_OUT);
				}
			}
			getServer().getScheduler().scheduleSyncDelayedTask(this, new CancelTask(), config.getInt("timeout") * 20);
			return true;
		}
		return false;
	}

	/**
	 * Sets a player's status to inactive, and optionally sends them a message.
	 * The message is not sent if the status is not changed.
	 * 
	 * @param player
	 *            the player to set the status on
	 * @param message
	 *            the message to be sent, or null for no message
	 */
	void cancelActivation(Player player, String message) {
		// Get activation time
		long activated = (long) MetadataHelper.getMetadata(player, "whosehorse.activated", this, -1);
		// Send message iff the player is online and active
		if (player.isOnline() && activated != -1 && message != null)
			player.sendMessage(message);
		// Set the status to inactive
		player.setMetadata("whosehorse.activated", new FixedMetadataValue(this, -1));
	}

	/**
	 * Called when a player interacts with an entity in any way.
	 * 
	 * @param player
	 *            the player that interacted with an entity
	 * @param entity
	 *            the entity interacted with
	 * @return whether or not this plugin should cancel the event
	 */
	boolean interactWithEntity(Player player, Entity entity) {
		// Get activation time
		long activated = (long) MetadataHelper.getMetadata(player, "whosehorse.activated", this, -1);
		// The event should only be affected if the player is active
		if (activated != -1) {
			if (entity instanceof Tameable) {
				// Convert to Tameable
				Tameable tameable = (Tameable) entity;
				// Get properties
				String mobName = getMobName(tameable);
				String ownerName = getOwnerName(tameable, player);
				// Send results
				player.sendMessage(MessageFormat.format(Messages.OWNER_INFO, mobName, ownerName));
				// Silently set the player's status to inactive
				cancelActivation(player, null);
			} else {
				// Set the player's status to inactive and send an error message
				player.sendMessage(Messages.NOT_ANIMAL);
				cancelActivation(player, null);
			}
			return true;
		} else
			return false;
	}

	/**
	 * Gets the name of the owner of a Tameable.
	 * 
	 * @param entity
	 *            the Tameable
	 * @param player
	 *            the player to treat as "you"
	 * @return the name of the owner
	 */
	private String getOwnerName(Tameable entity, Player player) {
		// Check if tamed
		boolean tamed = entity.isTamed();
		if (tamed) {
			// Get owner information of entity
			AnimalTamer tamer = entity.getOwner();
			// Get UUID, check for self
			UUID ownerId = tamer.getUniqueId();
			if (ownerId != null && player != null && ownerId.equals(player.getUniqueId()))
				return Messages.YOU;
			else {
				// Get name via owner player
				Player ownerPlayer = getServer().getPlayer(ownerId);
				if (ownerPlayer != null)
					return ownerPlayer.getDisplayName();
				else {
					// Get name, check for self
					String ownerName = tamer.getName();
					if (player != null && ownerName.equalsIgnoreCase(player.getName()))
						ownerName = Messages.YOU;
					return ownerName;
				}
			}
		}
		// Not tamed
		return Messages.NO_ONE;
	}

	/**
	 * Gets the name of a mob.
	 * 
	 * @param entity
	 *            the mob
	 * @return the name of the mob
	 */
	private String getMobName(Tameable entity) {
		boolean tamed = entity.isTamed();
		if (entity instanceof Horse)
			return Messages.HORSE;
		if (entity instanceof Ocelot)
			return tamed ? Messages.CAT : Messages.OCELOT;
		if (entity instanceof Wolf)
			return tamed ? Messages.DOG : Messages.WOLF;
		return Messages.ANIMAL;
	}

}
