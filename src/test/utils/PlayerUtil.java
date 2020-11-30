package test.utils;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import test.Main;
import test.enums.EnumItemInfo;

public class PlayerUtil {

	int baseCritChance = 30;
	private double strength = 0;
	private double critDamage = 0;
	private int combatLevel = 0;
	private Player player;
	private int critChance = 0;
	double baseCritDamage = 50;
	public boolean isCrit = false;
	private int baseHealth = 100;
	private int health = 0;
	private int effectiveHealth = 0;
	private int defense = 0;
	private int damage = 0;
	private int armorHealth = 0;
	private int armorDefense = 0;
	private int sbMaxHealth = 0;

	public PlayerUtil(String name) {
		try {
			setPlayer(Bukkit.getPlayer(name));
			itemBonusChecker();
			health = sbMaxHealth;
			healthRegeneration();
		} catch (Exception e) {

		}
	}

	public int getsbMaxHealth() {
		return sbMaxHealth;
	}

	public int getEffectiveHealth() {
		return effectiveHealth;
	}

	public int getHealth() {
		return health;
	}

	public int getDefense() {
		return defense;
	}

	public double getStrength() {
		return strength;
	}

	public double getCritDamage() {
		return critDamage;
	}

	public int getCombatLevel() {
		return combatLevel;
	}

	public void setCombatLevel(int combatLevel) {
		this.combatLevel = combatLevel;
	}

	public Player getPlayer() {
		return player;
	}

	private void setPlayer(Player player) {
		this.player = player;
	}

	public int getCritChance() {
		return critChance;
	}

	public void setHealth(int health) {
		this.baseHealth = health;
	}

	public void damageToPlayer(int damage) {
		this.damage = damage;
	}

	public double getDamage(int weaponDamage) {
		Random random = new Random();
		int a = random.nextInt(99) + 1;
		double baseDamage = (weaponDamage + (strength / 5)) * (1 + (strength / 100));
		double damageMultiplier = 1 + (combatLevel * 0.04);
		double finalDamage = 0;
		double critDamageMultiplier = (critDamage / 100) + 1;
		if (a <= critChance) {
			finalDamage = (baseDamage * damageMultiplier + 5) * critDamageMultiplier;
			isCrit = true;
		} else {
			finalDamage = baseDamage * damageMultiplier + 5;
			isCrit = false;
		}
		return finalDamage;
	}

	private void itemBonusChecker() {
		new BukkitRunnable() {

			@Override
			public void run() {
				try {
					HashMap<EnumItemInfo, Integer> itemInfo = ItemUtil.getItemInfo(player.getItemInHand());
					HashMap<EnumItemInfo, Integer> armorInfo = ItemUtil.getArmorInfo(player);
					armorHealth = armorInfo.get(EnumItemInfo.HEALTH);
					armorDefense = armorInfo.get(EnumItemInfo.DEFENSE);

					player.setFoodLevel(20);
					strength = itemInfo.get(EnumItemInfo.STRENGTH) + armorInfo.get(EnumItemInfo.STRENGTH);
					critChance = itemInfo.get(EnumItemInfo.CRIT_CHANCE) + armorInfo.get(EnumItemInfo.CRIT_CHANCE)
							+ baseCritChance + ((int) combatLevel / 2);
					critDamage = itemInfo.get(EnumItemInfo.CRIT_DAMAGE) + armorInfo.get(EnumItemInfo.CRIT_DAMAGE)
							+ baseCritDamage;
					sbMaxHealth = baseHealth + armorHealth;
					if (sbMaxHealth < health) {
						health = sbMaxHealth;
					}
					if (damage > 0) {
						effectiveHealth = (health * ((defense / 100) + 1)) - damage;
						damage = 0;
					} else {
						effectiveHealth = health * ((defense / 100) + 1);
					}

					health = effectiveHealth / ((defense / 100) + 1);
					int maxHealth = 20 + (((sbMaxHealth - 100) / 100) * 2);

					double actualMaxHealth;
					if (maxHealth <= 40) {
						actualMaxHealth = maxHealth;
					} else {
						actualMaxHealth = 40;
					}
					player.setMaxHealth(actualMaxHealth);
					double divide = ((double) sbMaxHealth) / ((double) actualMaxHealth);
					int currentHealth = (int) (health / divide);
					if (currentHealth <= 0) {
						player.sendMessage("§cYou died!");
						player.setHealth(actualMaxHealth);
						health = sbMaxHealth;
					} else if (currentHealth <= 40) {
						player.setHealth(currentHealth);
					} else {
						player.setHealth(40);
					}
					if (critChance >= 100) {
						critChance = 100;
					}
					defense = armorDefense;
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}.runTaskTimer(Main.getInstance(), 5, 0);
	}

	private void healthRegeneration() {
		new BukkitRunnable() {

			@Override
			public void run() {
				if (health < sbMaxHealth) {
					health += sbMaxHealth / 63;
				}
			}
		}.runTaskTimer(Main.getInstance(), 10, 20);
	}

}
