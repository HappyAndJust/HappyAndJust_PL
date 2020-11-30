package test.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import test.enums.EnumArmorType;
import test.enums.EnumItemInfo;
import test.enums.EnumRarity;

public class ItemUtil {

	public static boolean isInGameItem(ItemStack item) {
		try {
			if (item.getItemMeta().hasLore()) {

				List<String> lore = item.getItemMeta().getLore();
				String rarity = (lore.get(lore.size() - 1)).split(" ")[0];
				for (String str : EnumRarity.RARITY_COLOR) {
					if (str.toString().equals(rarity)) {
						return true;
					}
				}
			}

		} catch (Exception e) {

		}
		if (item.getType() == Material.AIR) {
			return true;
		}
		return false;

	}

	public static String getColorBold(String rarity) {
		switch (rarity) {
		case "COMMON":
			return "§f§l";

		case "UNCOMMON":
			return "§a§l";

		case "RARE":
			return "§9§l";

		case "EPIC":
			return "§5§l";

		case "LEGENDARY":
			return "§6§l";

		default:
			return null;
		}
	}

	public static String getColor(String rarity) {
		switch (rarity) {
		case "COMMON":
			return "§f";

		case "UNCOMMON":
			return "§a";

		case "RARE":
			return "§9";

		case "EPIC":
			return "§5";

		case "LEGENDARY":
			return "§6";

		default:
			return null;
		}
	}

	public static HashMap<EnumItemInfo, Integer> getItemInfo(ItemStack item) {

		if (!ItemUtil.isInGameItem(item) || item.getType() == Material.AIR) {
			HashMap<EnumItemInfo, Integer> itemInfo = new HashMap<>();
			itemInfo.put(EnumItemInfo.DAMAGE, 0);
			itemInfo.put(EnumItemInfo.STRENGTH, 0);
			itemInfo.put(EnumItemInfo.CRIT_CHANCE, 0);
			itemInfo.put(EnumItemInfo.CRIT_DAMAGE, 0);
			return itemInfo;
		}
		if (item.getItemMeta().hasLore()) {
			try {
				HashMap<EnumItemInfo, Integer> itemInfo = new HashMap<>();
				int damage = 0;
				int strength = 0;
				int critChance = 0;
				int critDamage = 0;
				ArrayList<String> lores = new ArrayList<>();
				for (int i = 0; i <= item.getItemMeta().getLore().size() - 1; i++) {
					lores.add(item.getItemMeta().getLore().get(i));
				}
				String damage_str = lores.get(0);
				String strength_str = null;
				String critChance_str = null;
				String critDamage_str = null;
				for (String str : lores) {
					if (str.contains("Strength")) {
						strength_str = str.trim();
					} else if (str.contains("Crit Chance")) {
						critChance_str = str.trim();
					} else if (str.contains("Crit Damage")) {
						critDamage_str = str.trim();
					}
				}
				damage = Integer.parseInt(damage_str.split(" ")[1].substring(3, damage_str.split(" ")[1].length()));

				if (strength_str != null) {
					strength = Integer
							.parseInt(strength_str.split(" ")[1].substring(3, strength_str.split(" ")[1].length()));

				} else {
					strength = 0;
				}
				if (critChance_str != null) {
					critChance = Integer.parseInt(
							critChance_str.split(" ")[2].substring(3, critChance_str.split(" ")[2].length() - 1)); // §c+100%
				} else {
					critChance = 0;
				}
				if (critDamage_str != null) {
					critDamage = Integer.parseInt(
							critDamage_str.split(" ")[2].substring(3, critDamage_str.split(" ")[2].length() - 1));
				} else {
					critDamage = 0;
				}
				itemInfo.put(EnumItemInfo.DAMAGE, damage);
				itemInfo.put(EnumItemInfo.STRENGTH, strength);
				itemInfo.put(EnumItemInfo.CRIT_CHANCE, critChance);
				itemInfo.put(EnumItemInfo.CRIT_DAMAGE, critDamage);
				return itemInfo;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;

	}

	public static HashMap<EnumItemInfo, Integer> getArmorInfo(Player p) {
		HashMap<EnumItemInfo, Integer> armorInfo = new HashMap<>();
		HashMap<EnumItemInfo, Integer> helmetInfo = new HashMap<>();
		HashMap<EnumItemInfo, Integer> chestplateInfo = new HashMap<>();
		HashMap<EnumItemInfo, Integer> leggingsInfo = new HashMap<>();
		HashMap<EnumItemInfo, Integer> bootsInfo = new HashMap<>();
		ItemStack helmet = p.getInventory().getHelmet();
		ItemStack chestplate = p.getInventory().getChestplate();
		ItemStack leggings = p.getInventory().getLeggings();
		ItemStack boots = p.getInventory().getBoots();
		boolean isHelmetValid = true;
		boolean isChestplateValid = true;
		boolean isLeggingsValid = true;
		boolean isBootsValid = true;
		if (helmet == null) {
			isHelmetValid = false;
		}
		if (chestplate == null) {
			isChestplateValid = false;
		}
		if (leggings == null) {
			isLeggingsValid = false;
		}
		if (boots == null) {
			isBootsValid = false;

		}

		helmetInfo.put(EnumItemInfo.STRENGTH, 0);
		helmetInfo.put(EnumItemInfo.CRIT_CHANCE, 0);
		helmetInfo.put(EnumItemInfo.CRIT_DAMAGE, 0);
		helmetInfo.put(EnumItemInfo.HEALTH, 0);
		helmetInfo.put(EnumItemInfo.DEFENSE, 0);

		chestplateInfo.put(EnumItemInfo.STRENGTH, 0);
		chestplateInfo.put(EnumItemInfo.CRIT_CHANCE, 0);
		chestplateInfo.put(EnumItemInfo.CRIT_DAMAGE, 0);
		chestplateInfo.put(EnumItemInfo.HEALTH, 0);
		chestplateInfo.put(EnumItemInfo.DEFENSE, 0);

		leggingsInfo.put(EnumItemInfo.STRENGTH, 0);
		leggingsInfo.put(EnumItemInfo.CRIT_CHANCE, 0);
		leggingsInfo.put(EnumItemInfo.CRIT_DAMAGE, 0);
		leggingsInfo.put(EnumItemInfo.HEALTH, 0);
		leggingsInfo.put(EnumItemInfo.DEFENSE, 0);

		bootsInfo.put(EnumItemInfo.STRENGTH, 0);
		bootsInfo.put(EnumItemInfo.CRIT_CHANCE, 0);
		bootsInfo.put(EnumItemInfo.CRIT_DAMAGE, 0);
		bootsInfo.put(EnumItemInfo.HEALTH, 0);
		bootsInfo.put(EnumItemInfo.DEFENSE, 0);

		if (isHelmetValid) {
			if (helmet.hasItemMeta()) {
				if (helmet.getItemMeta().hasLore()) {
					try {
						int strength = 0;
						int critChance = 0;
						int critDamage = 0;
						int health = 0;
						int defense = 0;
						ArrayList<String> lores = new ArrayList<>();
						for (int i = 0; i <= helmet.getItemMeta().getLore().size() - 1; i++) {
							lores.add(helmet.getItemMeta().getLore().get(i));
						}
						String strength_str = null;
						String critChance_str = null;
						String critDamage_str = null;
						String health_str = null;
						String defense_str = null;
						for (String str : lores) {
							if (str.contains("Strength")) {
								strength_str = str.trim();
							} else if (str.contains("Crit Chance")) {
								critChance_str = str.trim();
							} else if (str.contains("Crit Damage")) {
								critDamage_str = str.trim();
							} else if (str.contains("Health")) {
								health_str = str;
							} else if (str.contains("Defense")) {
								defense_str = str;
							}
						}

						if (strength_str != null) {
							strength = Integer.parseInt(
									strength_str.split(" ")[1].substring(3, strength_str.split(" ")[1].length()));

						}
						if (critChance_str != null) {
							critChance = Integer.parseInt(critChance_str.split(" ")[2].substring(3,
									critChance_str.split(" ")[2].length() - 1)); // §c+100%
						}
						if (critDamage_str != null) {
							critDamage = Integer.parseInt(critDamage_str.split(" ")[2].substring(3,
									critDamage_str.split(" ")[2].length() - 1));
						}
						if (health_str != null) {
							health = Integer
									.parseInt(health_str.split(" ")[1].substring(3, health_str.split(" ")[1].length()));
						}
						if (defense_str != null) {
							defense = Integer.parseInt(
									defense_str.split(" ")[1].substring(3, defense_str.split(" ")[1].length()));
						}
						helmetInfo.put(EnumItemInfo.STRENGTH, strength);
						helmetInfo.put(EnumItemInfo.CRIT_CHANCE, critChance);
						helmetInfo.put(EnumItemInfo.CRIT_DAMAGE, critDamage);
						helmetInfo.put(EnumItemInfo.HEALTH, health);
						helmetInfo.put(EnumItemInfo.DEFENSE, defense);
					} catch (Exception e) {

					}
				}
			}
		}
		if (isChestplateValid) {

			if (chestplate.hasItemMeta()) {
				if (chestplate.getItemMeta().hasLore()) {
					try {
						int strength = 0;
						int critChance = 0;
						int critDamage = 0;
						int health = 0;
						int defense = 0;
						ArrayList<String> lores = new ArrayList<>();
						for (int i = 0; i <= chestplate.getItemMeta().getLore().size() - 1; i++) {
							lores.add(chestplate.getItemMeta().getLore().get(i));
						}
						String strength_str = null;
						String critChance_str = null;
						String critDamage_str = null;
						String health_str = null;
						String defense_str = null;
						for (String str : lores) {
							if (str.contains("Strength")) {
								strength_str = str.trim();
							} else if (str.contains("Crit Chance")) {
								critChance_str = str.trim();
							} else if (str.contains("Crit Damage")) {
								critDamage_str = str.trim();
							} else if (str.contains("Health")) {
								health_str = str;
							} else if (str.contains("Defense")) {
								defense_str = str;
							}
						}

						if (strength_str != null) {
							strength = Integer.parseInt(
									strength_str.split(" ")[1].substring(3, strength_str.split(" ")[1].length()));

						}
						if (critChance_str != null) {
							critChance = Integer.parseInt(critChance_str.split(" ")[2].substring(3,
									critChance_str.split(" ")[2].length() - 1)); // §c+100%
						}
						if (critDamage_str != null) {
							critDamage = Integer.parseInt(critDamage_str.split(" ")[2].substring(3,
									critDamage_str.split(" ")[2].length() - 1));
						}
						if (health_str != null) {
							health = Integer
									.parseInt(health_str.split(" ")[1].substring(3, health_str.split(" ")[1].length()));
						}
						if (defense_str != null) {
							defense = Integer.parseInt(
									defense_str.split(" ")[1].substring(3, defense_str.split(" ")[1].length()));
						}
						chestplateInfo.put(EnumItemInfo.STRENGTH, strength);
						chestplateInfo.put(EnumItemInfo.CRIT_CHANCE, critChance);
						chestplateInfo.put(EnumItemInfo.CRIT_DAMAGE, critDamage);
						chestplateInfo.put(EnumItemInfo.HEALTH, health);
						chestplateInfo.put(EnumItemInfo.DEFENSE, defense);
					} catch (Exception e) {

					}
				}
			}
		}
		if (isLeggingsValid) {
			if (leggings.hasItemMeta()) {

				if (leggings.getItemMeta().hasLore()) {
					try {
						int strength = 0;
						int critChance = 0;
						int critDamage = 0;
						int health = 0;
						int defense = 0;
						ArrayList<String> lores = new ArrayList<>();
						for (int i = 0; i <= leggings.getItemMeta().getLore().size() - 1; i++) {
							lores.add(leggings.getItemMeta().getLore().get(i));
						}
						String strength_str = null;
						String critChance_str = null;
						String critDamage_str = null;
						String health_str = null;
						String defense_str = null;
						for (String str : lores) {
							if (str.contains("Strength")) {
								strength_str = str.trim();
							} else if (str.contains("Crit Chance")) {
								critChance_str = str.trim();
							} else if (str.contains("Crit Damage")) {
								critDamage_str = str.trim();
							} else if (str.contains("Health")) {
								health_str = str;
							} else if (str.contains("Defense")) {
								defense_str = str;
							}
						}

						if (strength_str != null) {
							strength = Integer.parseInt(
									strength_str.split(" ")[1].substring(3, strength_str.split(" ")[1].length()));

						}
						if (critChance_str != null) {
							critChance = Integer.parseInt(critChance_str.split(" ")[2].substring(3,
									critChance_str.split(" ")[2].length() - 1)); // §c+100%
						}
						if (critDamage_str != null) {
							critDamage = Integer.parseInt(critDamage_str.split(" ")[2].substring(3,
									critDamage_str.split(" ")[2].length() - 1));
						}
						if (health_str != null) {
							health = Integer
									.parseInt(health_str.split(" ")[1].substring(3, health_str.split(" ")[1].length()));
						}
						if (defense_str != null) {
							defense = Integer.parseInt(
									defense_str.split(" ")[1].substring(3, defense_str.split(" ")[1].length()));
						}
						leggingsInfo.put(EnumItemInfo.STRENGTH, strength);
						leggingsInfo.put(EnumItemInfo.CRIT_CHANCE, critChance);
						leggingsInfo.put(EnumItemInfo.CRIT_DAMAGE, critDamage);
						leggingsInfo.put(EnumItemInfo.HEALTH, health);
						leggingsInfo.put(EnumItemInfo.DEFENSE, defense);
					} catch (Exception e) {

					}
				}
			}
		}
		if (isBootsValid) {
			if (boots.hasItemMeta()) {
				if (boots.getItemMeta().hasLore()) {
					try {
						int strength = 0;
						int critChance = 0;
						int critDamage = 0;
						int health = 0;
						int defense = 0;
						ArrayList<String> lores = new ArrayList<>();
						for (int i = 0; i <= boots.getItemMeta().getLore().size() - 1; i++) {
							lores.add(boots.getItemMeta().getLore().get(i));
						}
						String strength_str = null;
						String critChance_str = null;
						String critDamage_str = null;
						String health_str = null;
						String defense_str = null;
						for (String str : lores) {
							if (str.contains("Strength")) {
								strength_str = str.trim();
							} else if (str.contains("Crit Chance")) {
								critChance_str = str.trim();
							} else if (str.contains("Crit Damage")) {
								critDamage_str = str.trim();
							} else if (str.contains("Health")) {
								health_str = str;
							} else if (str.contains("Defense")) {
								defense_str = str;
							}
						}

						if (strength_str != null) {
							strength = Integer.parseInt(
									strength_str.split(" ")[1].substring(3, strength_str.split(" ")[1].length()));

						}
						if (critChance_str != null) {
							critChance = Integer.parseInt(critChance_str.split(" ")[2].substring(3,
									critChance_str.split(" ")[2].length() - 1)); // §c+100%
						}
						if (critDamage_str != null) {
							critDamage = Integer.parseInt(critDamage_str.split(" ")[2].substring(3,
									critDamage_str.split(" ")[2].length() - 1));
						}
						if (health_str != null) {
							health = Integer
									.parseInt(health_str.split(" ")[1].substring(3, health_str.split(" ")[1].length()));
						}
						if (defense_str != null) {
							defense = Integer.parseInt(
									defense_str.split(" ")[1].substring(3, defense_str.split(" ")[1].length()));
						}
						bootsInfo.put(EnumItemInfo.STRENGTH, strength);
						bootsInfo.put(EnumItemInfo.CRIT_CHANCE, critChance);
						bootsInfo.put(EnumItemInfo.CRIT_DAMAGE, critDamage);
						bootsInfo.put(EnumItemInfo.HEALTH, health);
						bootsInfo.put(EnumItemInfo.DEFENSE, defense);
					} catch (Exception e) {

					}
				}
			}
		}
		armorInfo.put(EnumItemInfo.STRENGTH,
				helmetInfo.get(EnumItemInfo.STRENGTH) + chestplateInfo.get(EnumItemInfo.STRENGTH)
						+ leggingsInfo.get(EnumItemInfo.STRENGTH) + bootsInfo.get(EnumItemInfo.STRENGTH));
		armorInfo.put(EnumItemInfo.CRIT_CHANCE,
				helmetInfo.get(EnumItemInfo.CRIT_CHANCE) + chestplateInfo.get(EnumItemInfo.CRIT_CHANCE)
						+ leggingsInfo.get(EnumItemInfo.CRIT_CHANCE) + bootsInfo.get(EnumItemInfo.CRIT_CHANCE));
		armorInfo.put(EnumItemInfo.CRIT_DAMAGE,
				helmetInfo.get(EnumItemInfo.CRIT_DAMAGE) + chestplateInfo.get(EnumItemInfo.CRIT_DAMAGE)
						+ leggingsInfo.get(EnumItemInfo.CRIT_DAMAGE) + bootsInfo.get(EnumItemInfo.CRIT_DAMAGE));
		armorInfo.put(EnumItemInfo.HEALTH, helmetInfo.get(EnumItemInfo.HEALTH) + chestplateInfo.get(EnumItemInfo.HEALTH)
				+ leggingsInfo.get(EnumItemInfo.HEALTH) + bootsInfo.get(EnumItemInfo.HEALTH));
		armorInfo.put(EnumItemInfo.DEFENSE,
				helmetInfo.get(EnumItemInfo.DEFENSE) + chestplateInfo.get(EnumItemInfo.DEFENSE)
						+ leggingsInfo.get(EnumItemInfo.DEFENSE) + bootsInfo.get(EnumItemInfo.DEFENSE));

		return armorInfo;
	}

	public static ItemStack createItem(Material itemType, String rarity, int damage, int strength, int critChance,
			int critDamage, boolean isEnchanted, String name) {

		for (Material mat : EnumArmorType.ITEM_ARMOR) {
			if (mat == itemType) {
				return null;
			}
		}
		int num = 0;
		try {
			for (String str : EnumRarity.RARITY) {
				if (rarity.equals(str)) {
					num++;
				}
			}
			if (num == 0) {
				return null;
			}
			ItemStack item = new ItemStack(itemType);
			ItemUtil.setUnbreakable(item);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ItemUtil.getColor(rarity) + name);
			for (ItemFlag itemFlag : ItemFlag.values()) {
				meta.addItemFlags(itemFlag);
			}
			if (isEnchanted) {
				meta.addEnchant(Enchantment.DURABILITY, 1, true);
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			}
			List<String> lore = new ArrayList<>();
			if (damage >= 0) {
				lore.add("§7Damage§8: §c+" + damage);
			}
			if (strength > 0) {
				lore.add("§7Strength§8: §c+" + strength);
			}
			if (critChance > 0) {
				lore.add("§7Crit Chance§8: §c+" + critChance + "%");
			}
			if (critDamage > 0) {
				lore.add("§7Crit Damage§8: §c+" + critDamage + "%");
			}
			lore.add(" ");
			lore.add("§8This item can be reforged!");
			lore.add(ItemUtil.getColorBold(rarity) + rarity);
			meta.setLore(lore);
			item.setItemMeta(meta);
			return item;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ItemStack createItem(Material itemType, String rarity, int strength, int critChance, int critDamage,
			int health, int defense, boolean isEnchanted, String name) {
		int num = 0;
		for (Material mat : EnumArmorType.ITEM_ARMOR) {
			if (itemType == mat) {
				num++;
			}
		}
		if (num == 0) {
			return null;
		}

		num = 0;
		try {
			for (String str : EnumRarity.RARITY) {
				if (rarity.equals(str)) {
					num++;
				}
			}
			if (num == 0) {
				return null;
			}
			ItemStack item = new ItemStack(itemType);
			ItemUtil.setUnbreakable(item);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ItemUtil.getColor(rarity) + name);
			for (ItemFlag itemFlag : ItemFlag.values()) {
				meta.addItemFlags(itemFlag);
			}

			if (isEnchanted) {
				meta.addEnchant(Enchantment.DURABILITY, 1, true);
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			}
			List<String> lore = new ArrayList<>();
			if (strength > 0) {
				lore.add("§7Strength: §c+" + strength);
			}
			if (critChance > 0) {
				lore.add("§7Crit Chance: §c+" + critChance + "%");
			}
			if (critDamage > 0) {
				lore.add("§7Crit Damage: §c+" + critDamage + "%");
			}
			lore.add(" ");
			if (health > 0) {
				lore.add("§7Health: §a+" + health + " HP");
			}
			if (defense > 0) {
				lore.add("§7Defense: §a+" + defense);
			}
			lore.add(" ");
			lore.add("§8This item can be reforged!");
			lore.add(ItemUtil.getColorBold(rarity) + rarity);
			meta.setLore(lore);
			item.setItemMeta(meta);
			return item;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void setUnbreakable(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		meta.spigot().setUnbreakable(true);
		item.setItemMeta(meta);
	}

}
