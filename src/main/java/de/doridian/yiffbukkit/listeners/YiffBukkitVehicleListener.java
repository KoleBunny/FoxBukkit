package de.doridian.yiffbukkit.listeners;

import de.doridian.yiffbukkit.YiffBukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleUpdateEvent;
import org.bukkit.util.Vector;

public class YiffBukkitVehicleListener implements Listener {
	private YiffBukkit plugin;

	public YiffBukkitVehicleListener(YiffBukkit instance) {
		plugin = instance;

		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onVehicleEntityCollision(VehicleEntityCollisionEvent event) {
		if (event.isCancelled())
			return;
		
		final Vehicle vehicle = event.getVehicle();

		if (!(vehicle instanceof Minecart))
			return;

		Entity entity = event.getEntity();

		if (!(entity instanceof Minecart)) {
			if (entity instanceof LivingEntity)
				event.setCancelled(checkLiving(vehicle, (LivingEntity)entity));
			return;
		}

		final boolean vehicleFull = vehicle.getPassenger() instanceof Player;
		final boolean entityFull = entity.getPassenger() instanceof Player;

		if (entityFull == vehicleFull)
			return;

		final Entity emptyMinecart = entityFull ? vehicle : entity;

		emptyMinecart.remove();
		event.setCancelled(true);
	}

	private boolean checkLiving(Vehicle vehicle, LivingEntity entity) {
		if (entity instanceof Player)
			return false;

		if (vehicle.getPassenger() == null)
			return false;

		entity.damage(entity.getHealth());
		return true;
	}
}
