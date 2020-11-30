package test;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class KnockBack {

	private static Random random = new Random();

	public static void applyKnockback(LivingEntity livingEntity, Entity attacker, double resistance) {
		if (random.nextDouble() >= resistance) {
			double dist = (attacker instanceof Player && ((Player) attacker).isSprinting()) ? 1.5 : 1;
			dist += random.nextDouble() * 0.4 - 0.2; // adds or subtract 0.2 to the distance
			int knockBackLevel = getKnockBackLevel(attacker);
			dist += 3 * knockBackLevel;
			double mag = distanceToMagnitude(dist);
			Location location = getLocation(attacker);
			location.setPitch(location.getPitch() - 15);
			Vector velocity = setMag(location.getDirection(), mag);
			livingEntity.setVelocity(velocity);
		}
	}

	private static Location getLocation(Entity entity) {
		if (entity instanceof Projectile) {
			Projectile projectile = (Projectile) entity;
			Location location = entity.getLocation();
			location.setDirection(projectile.getVelocity());
			return location;
		} else {
			return entity.getLocation();
		}
	}

	private static double distanceToMagnitude(double distance) {
		return ((distance + 1.5) / 5d);
	}

	private static Vector setMag(Vector vector, double mag) {
		double x = vector.getX();
		double y = vector.getY();
		double z = vector.getZ();
		double denominator = Math.sqrt(x * x + y * y + z * z);
		if (denominator != 0) {
			return vector.multiply(mag / denominator);
		} else {
			return vector;
		}
	}

	private static int getKnockBackLevel(Entity entity){
        if(entity instanceof LivingEntity){
            ItemStack mainHand = ((LivingEntity)entity).getEquipment().getItemInHand();
            if(mainHand != null && mainHand.hasItemMeta() && mainHand.getItemMeta().hasEnchants() && mainHand.getItemMeta().hasEnchant(Enchantment.KNOCKBACK)){
                return mainHand.getItemMeta().getEnchantLevel(Enchantment.KNOCKBACK);
            }else{
                return 0;
            }
        }
        return 0;
    }

}