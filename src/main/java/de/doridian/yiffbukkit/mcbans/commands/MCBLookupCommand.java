package de.doridian.yiffbukkit.mcbans.commands;

import de.doridian.yiffbukkit.main.YiffBukkitCommandException;
import de.doridian.yiffbukkit.main.commands.system.ICommand;
import de.doridian.yiffbukkit.main.commands.system.ICommand.Help;
import de.doridian.yiffbukkit.main.commands.system.ICommand.Names;
import de.doridian.yiffbukkit.main.commands.system.ICommand.Permission;
import de.doridian.yiffbukkit.main.commands.system.ICommand.Usage;
import de.doridian.yiffbukkit.mcbans.MCBansUtil;
import de.doridian.yiffbukkitsplit.util.PlayerHelper;
import org.bukkit.command.CommandSender;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@Names("mcblookup")
@Help("Does an MCBans lookup on target player")
@Usage("<name>")
@Permission("yiffbukkit.users.lookup")
public class MCBLookupCommand extends ICommand {
	@Override
	public void run(final CommandSender commandSender, String[] args, String argStr) throws YiffBukkitCommandException {
		if (args.length < 1)
			throw new YiffBukkitCommandException("Argument expected.");

		final String otherName = playerHelper.completePlayerName(args[0], true);

		new Thread() {
			public void run() {
				JSONObject lookupret = MCBansUtil.apiQuery("exec=playerLookup&player="+MCBansUtil.URLEncode(otherName));
				/*
				 * {"total":2,"reputation":0,
				 * "local":[],
				 * "global":["mcbans.com .:. permabanned", "minecraft.digiex.net .:. Griefer (ThePanasonicGriefers)"]
				 * }
				 */

				PlayerHelper.sendDirectedMessage(commandSender, "Player \u00a73" + otherName + "\u00a7f has \u00a74" + lookupret.get("total").toString() + " ban(s)\u00a7f and \u00a79"+lookupret.get("reputation").toString()+" REP\u00a7f.");

				final JSONArray banReasonsGlobal = (JSONArray)lookupret.get("global");
				if (!banReasonsGlobal.isEmpty()) {
					PlayerHelper.sendDirectedMessage(commandSender, "\u00a74Global bans");
					for(Object obj : banReasonsGlobal) {
						PlayerHelper.sendDirectedMessage(commandSender, (String)obj);
					}
				}

				final JSONArray banReasonsLocal = (JSONArray)lookupret.get("local");
				if (!banReasonsLocal.isEmpty()) {
					PlayerHelper.sendDirectedMessage(commandSender, "\u00a76Local bans");
					for(Object obj : banReasonsLocal) {
						PlayerHelper.sendDirectedMessage(commandSender, (String)obj);
					}
				}
			}
		}.start();
	}
}