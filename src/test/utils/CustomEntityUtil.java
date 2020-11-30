package test.utils;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import test.Main;
import test.MobInformation;
import test.enums.EnumEntityInfo;
import test.enums.EnumEntityName;

public class CustomEntityUtil {

	private LivingEntity entity;
	private String name;
	private int health;
	private int level;
	private int damage;

	public CustomEntityUtil(EnumEntityName customEntity, Player p) {
		try {
			String name = MobInformation.mobInformation.get(customEntity).get(EnumEntityInfo.NAME);
			String en_type = MobInformation.mobInformation.get(customEntity).get(EnumEntityInfo.ENTITY_TYPE);
			int health = Integer.parseInt(MobInformation.mobInformation.get(customEntity).get(EnumEntityInfo.HEALTH));
			int level = Integer.parseInt(MobInformation.mobInformation.get(customEntity).get(EnumEntityInfo.LEVEL));
			int damage = Integer.parseInt(MobInformation.mobInformation.get(customEntity).get(EnumEntityInfo.DAMAGE));
			EntityType en = EntityType.valueOf(en_type.toUpperCase());

			Location loc = p.getLocation();
			if (en == EntityType.FIREBALL || en == EntityType.SMALL_FIREBALL) {
				loc = loc.subtract(0, 1, 0);
			}

			LivingEntity entity = (LivingEntity) p.getWorld().spawnEntity(loc, en);
			if (name.equals(en_type)) {
				name = entity.getName();
			}
			ArmorStand armorStand = (ArmorStand) p.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
			armorStand.setVisible(false);
			armorStand.setGravity(false);
			Main.health.put(armorStand, entity);
			int mobHealth = health;
			Main.mobHealthGetter.put(entity, (long) mobHealth);
			int mobLevel = level;
			Main.mobLevelGetter.put(entity, mobLevel);
			int maxHealth = health;
			Main.mobMaxHealthGetter.put(entity, (long) maxHealth);
			Main.mobNameGetter.put(entity, name);
			armorStand.setCustomName(StringUtil.armorStandNameGenerator(mobLevel, mobHealth, maxHealth, name));
			armorStand.setCustomNameVisible(true);
			Main.armorStandTeleport(armorStand, entity);
			this.entity = entity;
			this.damage = damage;
			Main.customEntityGetter.put(entity, this);
		} catch (Exception e) {
			throw new NullPointerException();
		}
	}

	public int getDamage() {
		return damage;
	}

	public String getName() {
		return name;
	}

	public int getHealth() {
		return health;
	}

	public int getLevel() {
		return level;
	}

	public LivingEntity getEntity() {
		return entity;
	}

	public void setEntity(LivingEntity entity) {
		this.entity = entity;
	}

}
