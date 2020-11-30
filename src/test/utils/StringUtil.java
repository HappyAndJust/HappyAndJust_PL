package test.utils;

public class StringUtil {

	public static boolean isNumber(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static String numToLevel(int level) {
		return "§8[§7Lv" + level + "§8]";
	}

	public static String armorStandNameGenerator(int level, long health, long maxHealth, String name) {
		return numToLevel(level) + " §c" + name + ((health >= maxHealth / 2) ? "§a " : "§e ") + health + "§f/§a" + maxHealth
				+ "§c❤";
	}

}
