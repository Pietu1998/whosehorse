package net.pietu1998.whosehorse;

import org.bukkit.ChatColor;

public class Messages {

	public static final String MUST_BE_PLAYER = "Only players can use this command.";
	public static final String NO_ARGS_NEEDED = ChatColor.DARK_RED + "Usage: /{0}";
	public static final String COMMAND_SUCCESS = ChatColor.YELLOW + "Right click on an animal to see its owner.";
	public static final String NOT_ANIMAL = ChatColor.YELLOW + "That's not a tameable animal.";
	public static final String TIMED_OUT = ChatColor.YELLOW
			+ "No longer checking an animal's owner. Use /whose to re-enable.";
	public static final String OWNER_INFO = ChatColor.YELLOW + "That " + ChatColor.WHITE + "{0}" + ChatColor.YELLOW
			+ " is owned by " + ChatColor.WHITE + "{1}" + ChatColor.YELLOW + ".";

	public static final String YOU = "you";
	public static final String NO_ONE = "no one";
	public static final String ANIMAL = "animal";
	public static final String HORSE = "horse";
	public static final String OCELOT = "ocelot";
	public static final String WOLF = "wolf";
	public static final String CAT = "cat";
	public static final String DOG = "dog";

	private Messages() {}

}
