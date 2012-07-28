package de.doridian.yiffbukkit.main.commands;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.command.CommandSender;

import de.doridian.yiffbukkit.main.YiffBukkitCommandException;
import de.doridian.yiffbukkit.main.commands.ICommand.*;
import de.doridian.yiffbukkit.main.util.PersistentScheduler;
import de.doridian.yiffbukkit.main.util.RunString;

@Names("at")
@Help("Runs a command after the given amount of seconds.")
@Usage("<seconds> <command>[;<command>[;<command> ...]")
@Permission("yiffbukkit.at")
public class AtCommand extends ICommand {
	final Pattern argumentPattern = Pattern.compile("^([^ ]+) (.*)$");
	@Override
	public void run(CommandSender commandSender, String[] args, String argStr) throws YiffBukkitCommandException {
		Matcher matcher = argumentPattern.matcher(argStr);
		if (!matcher.matches())
			throw new YiffBukkitCommandException("Syntax error");

		long t = (long) (Double.parseDouble(matcher.group(1))*1000);
		String commandString = matcher.group(2);

		final RunString parsedCommands = new RunString(commandString , Collections.singleton(""));
		PersistentScheduler.schedule(t, commandSender, parsedCommands);
	}
}
