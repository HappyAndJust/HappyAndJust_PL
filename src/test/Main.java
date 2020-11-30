package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.EntityZombie;
import net.minecraft.server.v1_8_R1.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R1.PlayerConnection;
import test.enums.EnumArmorName;
import test.enums.EnumArmorType;
import test.enums.EnumEntityName;
import test.enums.EnumItemInfo;
import test.enums.EnumItemName;
import test.enums.EnumRarity;
import test.managers.EventsManager;
import test.managers.NPCManager;
import test.utils.CustomEntityUtil;
import test.utils.ItemUtil;
import test.utils.PlayerUtil;

public class Main extends JavaPlugin {
	public static Main instance;
	public static Player me = null;
	public static HashMap<ArmorStand, Entity> health = new HashMap<>();
	public static HashMap<Entity, Long> mobHealthGetter = new HashMap<>();
	public static HashMap<Entity, Integer> mobLevelGetter = new HashMap<>();
	public static HashMap<Entity, Long> mobMaxHealthGetter = new HashMap<>();
	public static HashMap<Entity, String> mobNameGetter = new HashMap<>();
	public static ArrayList<ArmorStand> damageArmorStand = new ArrayList<>();
	public static HashMap<String, PlayerUtil> testPlayerGetter = new HashMap<>();
	public static HashMap<LivingEntity, CustomEntityUtil> customEntityGetter = new HashMap<>();
	NPCManager npcManager = new NPCManager();
	int currentCommandNum = 0;
	int repeatNum = 0;
	public static String texture = null;
	public static String signature = null;

	public static Main getInstance() {
		return instance;
	}

	private void setInstance(Main instance) {
		Main.instance = instance;
	}

	@Override
	public void onEnable() {
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		Bukkit.getPluginManager().registerEvents(new EventsManager(this), this);
		if (Bukkit.getPlayer("WhyAreYouToxic") != null) {
			me = Bukkit.getPlayer("WhyAreYouToxic");
		}
		getJoinedPlayer();
		setInstance(this);
		texture = this.getConfig().getString("Texture");
		signature = this.getConfig().getString("Signature");
		for (Player p : Bukkit.getOnlinePlayers()) {
			testPlayerGetter.put(p.getName(), new PlayerUtil(p.getName()));
			PlayerUtil testPlayer = testPlayerGetter.get(p.getName());
			testPlayer.setCombatLevel(0);
			showScoreboard(p);
		}

		MobInformation.putInformation();

	}

	private void getJoinedPlayer() {
		new BukkitRunnable() {

			@Override
			public void run() {
				for (String name : EventsManager.joinedPlayer.keySet()) {
					if (testPlayerGetter.get(name) == null) {
						testPlayerGetter.put(name, new PlayerUtil(name));
						showScoreboard(testPlayerGetter.get(name).getPlayer());
					}
				}
			}
		}.runTaskTimer(this, 5, 0);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		Player p = null;
		if (sender instanceof Player) {
			p = (Player) sender;
		}

		if (cmd.getName().equalsIgnoreCase("raritylist")) {
			for (String rarity : EnumRarity.RARITY) {
				p.sendMessage(rarity);
			}
		}

		if (cmd.getName().equalsIgnoreCase("createnpc")) {
			if (args.length == 1) {
				EntityPlayer npc = npcManager.createNPC(p, args[0]);
				for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
					PlayerConnection connection = ((CraftPlayer) onlinePlayer).getHandle().playerConnection;
					connection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, npc));
					connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
					new BukkitRunnable() {

						@Override
						public void run() {
							connection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, npc));
						}
					}.runTaskTimer(this, 20, 0);

				}
			}
		}

		if (cmd.getName().equalsIgnoreCase("check")) {
			NPC nms = new NPC(((CraftWorld) p.getWorld()).getHandle());
			nms.registerEntity("Zombie", 54, EntityZombie.class, NPC.class);
			NPC.spawn(p.getLocation());
		}

		if (cmd.getName().equalsIgnoreCase("checkitem")) {
			HashMap<EnumItemInfo, Integer> armorInfo = ItemUtil.getArmorInfo(p);
			sender.sendMessage("Strength : " + armorInfo.get(EnumItemInfo.STRENGTH));
			sender.sendMessage("Crit Chance : " + armorInfo.get(EnumItemInfo.CRIT_CHANCE));
			sender.sendMessage("Crit Damage : " + armorInfo.get(EnumItemInfo.CRIT_DAMAGE));
		}

		if (cmd.getName().equalsIgnoreCase("setplayer")) {
			// 0 : 컴뱃 레벨
			try {
				if (args.length == 1) {
					if (Integer.parseInt(args[0]) >= 0 && Integer.parseInt(args[0]) <= 50) {
						try {
							String name = p.getName();
							PlayerUtil testPlayer = testPlayerGetter.get(name);
							testPlayer.setCombatLevel(Integer.parseInt(args[0]));
						} catch (Exception e) {

						}
					} else {
						sender.sendMessage("Combat Level must be between 0 and 50");
					}
				}

			} catch (Exception e) {

			}

		}

		if (cmd.getName().equalsIgnoreCase("testitem")) {
			// 0: 아이템 타입, 1: 등급, 2: 데미지, 3: 힘, 4: 크릿 찬스, 5 : 크릿 데미지 6: 인챈 여부, 7: 아이템 이름

			if (args.length >= 8) {
				try {
					StringBuilder builder = new StringBuilder();
					for (int i = 7; i < args.length; i++) {
						builder.append(args[i] + " ");
					}
					String name = builder.toString();

					Material itemType = Material.valueOf(args[0].toUpperCase());
					String rarity = args[1].toUpperCase();
					int damage = Integer.parseInt(args[2]);
					int strength = Integer.parseInt(args[3]);
					int critChance = Integer.parseInt(args[4]);
					int critDamage = Integer.parseInt(args[5]);
					boolean isEnchanted = Boolean.parseBoolean(args[6]);
					p.getInventory().addItem(ItemUtil.createItem(itemType, rarity, damage, strength, critChance,
							critDamage, isEnchanted, name));
				} catch (Exception e) {
				}
			}
			if (args.length == 1) {
				try {
					switch (EnumItemName.valueOf(args[0])) {

					case HAPPYANDJUST_SWORD:
						p.getInventory().addItem(ItemUtil.createItem(Material.DIAMOND_SWORD, "LEGENDARY", 550, 300, 50,
								250, true, "HappyAndJust's Sword"));
						break;

					}
				} catch (Exception e) {

				}
			}
		}

		if (cmd.getName().equalsIgnoreCase("testarmor")) {
			// 0: 아이템 타입, 1 : 등급, 2: 힘, 3: 크릿 찬스, 4: 크릿 데미지, 5 : 체력, 6 : 디펜스 7: 인챈여부, 8 :
			// 아이템이름
			if (args.length >= 9) {
				try {
					StringBuilder builder = new StringBuilder();
					for (int i = 8; i < args.length; i++) {
						builder.append(args[i] + " ");
					}
					String name = builder.toString();

					Material itemType = Material.valueOf(args[0].toUpperCase());
					String rarity = args[1].toUpperCase();
					int strength = Integer.parseInt(args[2]);
					int critChance = Integer.parseInt(args[3]);
					int critDamage = Integer.parseInt(args[4]);
					int health = Integer.parseInt(args[5]);
					int defense = Integer.parseInt(args[6]);
					boolean isEnchanted = Boolean.parseBoolean(args[7]);
					p.getInventory().addItem(ItemUtil.createItem(itemType, rarity, strength, critChance, critDamage,
							health, defense, isEnchanted, name));
				} catch (Exception e) {
				}
			}
			if (args.length == 1) {
				try {

					switch (EnumArmorName.valueOf(args[0])) {

					case HAPPYANDJUST_HELMET:
						p.getInventory().addItem(ItemUtil.createItem(Material.DIAMOND_HELMET, "LEGENDARY", 50, 18, 32,
								400, 300, true, "HappyAndJust's Helmet"));
						break;
					case HAPPYANDJUST_CHESTPLATE:
						p.getInventory().addItem(ItemUtil.createItem(Material.DIAMOND_CHESTPLATE, "LEGENDARY", 50, 18,
								32, 400, 300, true, "HappyAndJust's Chestplate"));
						break;
					case HAPPYANDJUST_LEGGINGS:
						p.getInventory().addItem(ItemUtil.createItem(Material.DIAMOND_LEGGINGS, "LEGENDARY", 50, 18, 32,
								400, 300, true, "HappyAndJust's Leggings"));
						break;
					case HAPPYANDJUST_BOOTS:
						p.getInventory().addItem(ItemUtil.createItem(Material.DIAMOND_BOOTS, "LEGENDARY", 50, 18, 32,
								400, 300, true, "HappyAndJust's Boots"));
						break;

					}

				} catch (Exception e) {

				}
			}
		}
		if (cmd.getName().equalsIgnoreCase("spawncustommob")) {

			if (args.length == 1) {
				try {
					switch (EnumEntityName.valueOf(args[0])) {
					case ZEALOT:
						new CustomEntityUtil(EnumEntityName.ZEALOT, p);
						break;
					case ZOMBIE:
						CustomEntityUtil zombieUtil = new CustomEntityUtil(EnumEntityName.ZOMBIE, p);
						Zombie zombie = (Zombie) zombieUtil.getEntity();
						zombie.setBaby(false);
						zombie.setVillager(false);
						break;
					case ZOMBIE_VILLAGER:
						CustomEntityUtil zombieVillagerUtil = new CustomEntityUtil(EnumEntityName.ZOMBIE_VILLAGER, p);
						Zombie zombie_villager = (Zombie) zombieVillagerUtil.getEntity();
						zombie_villager.setBaby(false);
						zombie_villager.setVillager(true);
						zombie_villager.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
						zombie_villager.getEquipment().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
						zombie_villager.getEquipment().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
						zombie_villager.getEquipment().setBoots(new ItemStack(Material.LEATHER_BOOTS));
						break;
					case CRYPT_GHOUL:
						CustomEntityUtil cryptGhoulUtil = new CustomEntityUtil(EnumEntityName.CRYPT_GHOUL, p);
						Zombie cryptGhoul = (Zombie) cryptGhoulUtil.getEntity();
						cryptGhoul.setVillager(false);
						cryptGhoul.setBaby(false);
						cryptGhoul.getEquipment().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
						cryptGhoul.getEquipment().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
						cryptGhoul.getEquipment().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
						cryptGhoul.getEquipment().setItemInHand(new ItemStack(Material.IRON_SWORD));
						break;
					case GOLDEN_GHOUL:
						CustomEntityUtil goldenGhoulUtil = new CustomEntityUtil(EnumEntityName.GOLDEN_GHOUL, p);
						Zombie goldenGhoul = (Zombie) goldenGhoulUtil.getEntity();
						goldenGhoul.setBaby(false);
						goldenGhoul.setVillager(false);
						goldenGhoul.getEquipment().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE));
						goldenGhoul.getEquipment().setLeggings(new ItemStack(Material.GOLD_LEGGINGS));
						goldenGhoul.getEquipment().setBoots(new ItemStack(Material.GOLD_BOOTS));
						goldenGhoul.getEquipment().setItemInHand(new ItemStack(Material.GOLD_SWORD));
					}
				} catch (Exception e) {

				}
			}
		}
		if (cmd.getName().equalsIgnoreCase("loop")) {
			currentCommandNum = 0;
			repeatNum = 0;
			if (args.length >= 3) {
				try {
					int repeatNum = Integer.parseInt(args[0]);
					this.repeatNum = repeatNum;
					int interval = Integer.parseInt(args[1]);
					StringBuilder command = new StringBuilder();
					for (String str : args) {
						if (!str.equals(args[0]) && !str.equals(args[1])) {
							command.append(str + " ");
						}
					}

					countdown(repeatNum, interval, p, command);
				} catch (Exception e) {

				}
			}

		}

		if (cmd.getName().equalsIgnoreCase("progress")) {
			p.sendMessage("§a" + currentCommandNum + "/" + repeatNum);
		}

		if (cmd.getName().equalsIgnoreCase("rmv")) {
			for (Entity en : p.getWorld().getEntities()) {
				if (!(en instanceof Player)) {
					en.remove();
				}
			}
		}

		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {

		if (cmd.getName().equalsIgnoreCase("spawncustommob")) {
			List<String> entityList = new ArrayList<>();
			List<String> tabList = new ArrayList<>();
			if (args.length == 1) {

				for (EnumEntityName name : EnumEntityName.values()) {
					entityList.add(name.name());
				}

			}

			for (String str : entityList) {
				if (str.startsWith(args[0].toUpperCase())) {
					tabList.add(str);
				}
			}
			return tabList;
		}
		if (cmd.getName().equalsIgnoreCase("testitem")) {
			List<String> itemList = new ArrayList<>();
			List<String> tabList = new ArrayList<>();
			if (args.length == 1) {
				for (Material mat : Material.values()) {
					int num = 0;
					for (Material enumArmorType : EnumArmorType.ITEM_ARMOR) {
						if (enumArmorType == mat) {
							num++;
						}
					}
					if (num == 0) {
						itemList.add(mat.name());
					}
				}

				for (EnumItemName str : EnumItemName.values()) {
					itemList.add(str.name());
				}
			}

			for (String str : itemList) {
				if (str.startsWith(args[0].toUpperCase())) {
					tabList.add(str);
				}
			}

			return tabList;
		}
		if (cmd.getName().equalsIgnoreCase("testarmor")) {
			List<String> armorList = new ArrayList<>();
			List<String> tabList = new ArrayList<>();
			if (args.length == 1) {
				for (Material mat : Material.values()) {
					int num = 0;
					for (Material enumArmorType : EnumArmorType.ITEM_ARMOR) {
						if (enumArmorType == mat) {
							num++;
						}
					}
					if (num == 1) {
						armorList.add(mat.name());
					}
				}

				for (EnumArmorName str : EnumArmorName.values()) {
					armorList.add(str.name());
				}
			}

			for (String str : armorList) {
				if (str.startsWith(args[0].toUpperCase())) {
					tabList.add(str);
				}
			}

			return tabList;
		}
		return null;

	}

	public static void showScoreboard(Player p) {
		PlayerUtil testPlayer = testPlayerGetter.get(p.getName());
		ScoreboardManager scoreboardManager = Main.getInstance().getServer().getScoreboardManager();
		Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
		Objective objective = scoreboard.registerNewObjective("§b" + p.getName() + "", "Dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		Team strength = scoreboard.registerNewTeam("strength");
		Team critChance = scoreboard.registerNewTeam("critChance");
		Team critDamage = scoreboard.registerNewTeam("critDamage");
		Team combatLevel = scoreboard.registerNewTeam("combatLevel");
		Team health = scoreboard.registerNewTeam("health");
		Team defense = scoreboard.registerNewTeam("defense");
		defense.addEntry("§a❈");
		health.addEntry("§c❤");
		strength.addEntry("§c❁");
		critChance.addEntry("§9☣");
		critDamage.addEntry("§9☠");
		combatLevel.addEntry("§3");
		objective.getScore("§c❁").setScore(2);
		objective.getScore("§9☣").setScore(1);
		objective.getScore("§9☠").setScore(0);
		objective.getScore("§3").setScore(3);
		objective.getScore("§c❤").setScore(5);
		objective.getScore("§a❈").setScore(4);
		new BukkitRunnable() {

			@Override
			public void run() {
				strength.setPrefix("§fStrength: ");
				critChance.setPrefix("§fCrit Chance: ");
				critDamage.setPrefix("§fCrit Damage: ");
				combatLevel.setPrefix("§fCombat Level: ");
				health.setPrefix("§fHealth: ");
				defense.setPrefix("§fDefense: ");

				strength.setSuffix(Integer.toString((int) testPlayer.getStrength()));
				critChance.setSuffix(Integer.toString(testPlayer.getCritChance()));
				critDamage.setSuffix(Integer.toString((int) testPlayer.getCritDamage()));
				combatLevel.setSuffix(Integer.toString(testPlayer.getCombatLevel()));
				health.setSuffix(testPlayer.getHealth() + "/" + testPlayer.getsbMaxHealth());
				defense.setSuffix(Integer.toString(testPlayer.getDefense()));
			}
		}.runTaskTimer(Main.getInstance(), 0, 0);

		p.setScoreboard(scoreboard);
	}

	public String integerToRoman(int num) {

		int[] values = { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1 };
		String[] romanLiterals = { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I" };

		StringBuilder roman = new StringBuilder();

		for (int i = 0; i < values.length; i++) {
			while (num >= values[i]) {
				num -= values[i];
				roman.append(romanLiterals[i]);
			}
		}

		return roman.toString();

	}

	public static void armorStandTeleport(ArmorStand armorStand, Entity en) {
		new BukkitRunnable() {

			@Override
			public void run() {

				if (en.isDead()) {
					Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
						int num = 0;

						@Override
						public void run() {
							if (num == 2) {
								armorStand.remove();
								return;
							}
							num++;
						}
					}, 0L, 13L);
					health.remove(armorStand);
				}
				double entityHeight = ((LivingEntity) en).getEyeHeight();
				double armorStandHeight = armorStand.getEyeHeight();
				double gap = armorStandHeight - entityHeight;
				Location loc = en.getLocation().subtract(0, gap - 0.0375, 0);
				armorStand.teleport(loc);

			}

		}.runTaskTimer(Main.getInstance(), 2, 0);

	}

	public static void changeArmorStandName(ArmorStand armorStand, String name) {
		armorStand.setCustomName(name);
		armorStand.setCustomNameVisible(true);
	}

	private void countdown(int repeatNum, int period, Player p, StringBuilder command) {
		Plugin plugin = this;
		new BukkitRunnable() {
			int num = 0;

			@Override
			public void run() {
				try {
					p.performCommand(command.toString());
				} catch (Exception e) {

				}
				num++;
				currentCommandNum++;
				if (num == repeatNum) {
					cancel();
					return;
				}
			}
		}.runTaskTimer(plugin, 0, 5 * period);
	}

}
