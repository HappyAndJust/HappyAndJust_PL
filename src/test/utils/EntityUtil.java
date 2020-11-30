package test.utils;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import test.Main;

public class EntityUtil {

	public static boolean isInGameEntity(Entity checkEntity) {
		for (ArmorStand en : Main.health.keySet()) {
			LivingEntity entity = (LivingEntity) Main.health.get(en);
			if (checkEntity.equals(entity) || checkEntity.equals(en)) {
				return true;
			}
		}
		return false;
	}

}
