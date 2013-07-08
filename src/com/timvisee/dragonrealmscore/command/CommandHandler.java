package com.timvisee.dragonrealmscore.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler {
	
	private final String TEXTUREPACK_URL = "http://dl.dropbox.com/u/8495623/Dragon%20Realms/Server%20Texturepack/Dragon%20Realms.zip";
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if(commandLabel.equalsIgnoreCase("profile")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				printMessageBlock(sender, new String[]{
					    "                 " + ChatColor.DARK_GREEN + "Click on the link below see your profile:",
						"  " + ChatColor.BLUE + "http://mcdragonrealms.com/player.php?player=" + p.getName()
						});
			} else
				sender.sendMessage(ChatColor.DARK_RED + "This command can only be executed in-game!");
			return true;
		}

		if(commandLabel.equalsIgnoreCase("register") || commandLabel.equalsIgnoreCase("signup")) {
			printMessageBlock(sender, new String[]{
				    "                   " + ChatColor.DARK_GREEN + "Click on the link below to sign up:",
					"             " + ChatColor.BLUE + "http://mcdragonrealms.com/register.php"
					});
			return true;
		}
		
		if(commandLabel.equalsIgnoreCase("texturepack") || commandLabel.equalsIgnoreCase("txp")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				
				if(args.length == 0)
					printMessageBlock(sender, new String[]{
							ChatColor.DARK_GREEN + "What would you like to do?",
							ChatColor.DARK_GREEN + "/" + commandLabel + " install" + ChatColor.DARK_GRAY + " : Install our Texturepack"
							});
				else if(args.length == 1) {
					
					if(args[0].equalsIgnoreCase("install")) {
						printMessageBlock(sender, new String[]{
								"               " + ChatColor.DARK_GREEN + "Our texurepack should install now!",
								"",
								"                        " + ChatColor.DARK_GREEN + "Make sure you enable",
								"           " + ChatColor.DARK_GRAY + "Options > Video Settings > Server Textures",
								"                           " + ChatColor.DARK_GREEN + "if it wont install!"});
						p.setTexturePack(TEXTUREPACK_URL);
					} else {
						sender.sendMessage(ChatColor.DARK_RED + "Unknown command!");
						sender.sendMessage(ChatColor.DARK_RED + "Try: " + ChatColor.GOLD + "/" + commandLabel);
					}
					
				} else {
					sender.sendMessage(ChatColor.DARK_RED + "Unknown command!");
					sender.sendMessage(ChatColor.DARK_RED + "Try: " + ChatColor.GOLD + "/" + commandLabel);
				}
				return true;
				
			} else {
				sender.sendMessage(ChatColor.DARK_RED + "This command can only be executed in-game!");
				return true;
			}
		}
		
		if(commandLabel.equalsIgnoreCase("votes") || commandLabel.equalsIgnoreCase("vote") || commandLabel.equalsIgnoreCase("voting")) {
			printMessageBlock(sender, new String[]{
					"         " + ChatColor.DARK_GREEN + "Click on the link below to visit our voting page:",
					"               " + ChatColor.BLUE + "http://mcdragonrealms.com/votes.php"
					});
			return true;
		}
		
		if(commandLabel.equalsIgnoreCase("website") || commandLabel.equalsIgnoreCase("site")) {
			printMessageBlock(sender, new String[]{
					"           " + ChatColor.DARK_GREEN + "Click on the link below to visit our website:",
					"                    " + ChatColor.BLUE + "http://mcdragonrealms.com/"
					});
			return true;
		}
		
		return false;
	}
	
	public void printMessageBlock(CommandSender sender, String[] messages) {
		printMessageBlock(sender, Arrays.asList(messages));
	}
	
	public void printMessageBlock(CommandSender sender, List<String> message) {
		printMessageBlock(sender, message, true, true);
	}
	
	public void printMessageBlock(CommandSender sender, List<String> messages, boolean topLine, boolean bottomLine) {
		List<String> out = new ArrayList<String>();
		
		// Generate the message line
		String messageLine = "";
		for(int i = 0; i < 26; i++)
			messageLine += ChatColor.DARK_GREEN + "-" + ChatColor.DARK_GRAY + "-";
		messageLine += ChatColor.DARK_GREEN + "-";
		
		// Add a mesage line to the top if needed
		if(topLine)
			out.add(messageLine);
		
		// Add all the message
		out.addAll(messages);
		
		// Add a message line to the bottom if neede
		if(bottomLine)
			out.add(messageLine);
		
		for(String msg : out)
			sender.sendMessage(msg);
	}
}
