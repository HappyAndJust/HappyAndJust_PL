package test;

import java.util.HashMap;

import test.enums.EnumEntityInfo;
import test.enums.EnumEntityName;

public class MobInformation {

	public static HashMap<EnumEntityName, HashMap<EnumEntityInfo, String>> mobInformation = new HashMap<>();
	static HashMap<EnumEntityInfo, String> zealot = new HashMap<>();
	static HashMap<EnumEntityInfo, String> zombie = new HashMap<>();
	static HashMap<EnumEntityInfo, String> zombieVillager = new HashMap<>();
	static HashMap<EnumEntityInfo, String> cryptGhoul = new HashMap<>();
	static HashMap<EnumEntityInfo, String> goldenGhoul = new HashMap<>();

	public static void putInformation() {

		information(zealot, "13000", "55", "1250", "ENDERMAN", "Zealot");
		information(zombie, "100", "1", "20", "ZOMBIE", "Zombie");
		information(cryptGhoul, "2000", "30", "350", "ZOMBIE", "Crypt Ghoul");
		information(goldenGhoul, "45000", "60", "800", "ZOMBIE", "Golden Ghoul");
		information(zombieVillager, "120", "1", "24", "ZOMBIE", "Zombie Villager");
		
		mobInformation.put(EnumEntityName.ZEALOT, zealot);
		mobInformation.put(EnumEntityName.ZOMBIE, zombie);
		mobInformation.put(EnumEntityName.CRYPT_GHOUL, cryptGhoul);
		mobInformation.put(EnumEntityName.GOLDEN_GHOUL, goldenGhoul);
		mobInformation.put(EnumEntityName.ZOMBIE_VILLAGER, zombieVillager);

	}

	private static void information(HashMap<EnumEntityInfo, String> entity, String health, String level, String damage,
			String entityType, String name) {
		entity.put(EnumEntityInfo.HEALTH, health);
		entity.put(EnumEntityInfo.LEVEL, level);
		entity.put(EnumEntityInfo.DAMAGE, damage);
		entity.put(EnumEntityInfo.ENTITY_TYPE, entityType);
		entity.put(EnumEntityInfo.NAME, name);
	}

}
