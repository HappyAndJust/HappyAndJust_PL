package test.managers;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import test.KnockBack;
import test.Main;
import test.enums.EnumItemInfo;
import test.utils.EntityUtil;
import test.utils.ItemUtil;
import test.utils.PlayerUtil;
import test.utils.StringUtil;

public class EventsManager implements Listener {
	// ✧
	public static Main plugin;
	public static HashMap<String, Player> joinedPlayer = new HashMap<>();

	public EventsManager(Main main) {
		EventsManager.plugin = main;
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {

		if (EntityUtil.isInGameEntity(e.getDamager()) && e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			e.setDamage(0);
			int damage = Main.customEntityGetter.get(e.getDamager()).getDamage();
			PlayerUtil testPlayer = Main.testPlayerGetter.get(p.getName());
			testPlayer.damageToPlayer(damage);
			double gap = p.getEyeHeight() / 2;
			Location loc = p.getLocation().subtract(0, gap, 0);
			ArmorStand damageDisplay = (ArmorStand) p.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
			damageDisplay.setVisible(false);
			String damageText = "§7" + ((damage / ((testPlayer.getDefense() / 100) + 1)) + 1);
			damageDisplay.setCustomName(damageText);
			damageDisplay.setCustomNameVisible(true);
			damageDisplay.setGravity(false);
			Main.damageArmorStand.add(damageDisplay);
			removeDamageDisplay(damageDisplay);
		}

		for (ArmorStand en : Main.health.keySet()) {
			LivingEntity entity = (LivingEntity) Main.health.get(en);
			if (e.getEntity().equals(entity) || e.getEntity().equals(en)) {
				e.setDamage(0);
				if (e.getEntity().equals(en)) {
					KnockBack.applyKnockback((LivingEntity) entity, e.getDamager(), 0);
					((LivingEntity) entity).damage(0);
				}
				if (e.getDamager() instanceof Player) {
					Player p = (Player) e.getDamager();
					if (ItemUtil.isInGameItem(p.getItemInHand()) && Main.testPlayerGetter.get(p.getName()) != null) {
						try {
							PlayerUtil testPlayer = Main.testPlayerGetter.get(p.getName());
							long currentHealth = Main.mobHealthGetter.get(entity);
							HashMap<EnumItemInfo, Integer> itemInfo = ItemUtil.getItemInfo(p.getItemInHand());
							long damage = (long) testPlayer.getDamage(itemInfo.get(EnumItemInfo.DAMAGE));
							if (damage < 0) {
								damage = Long.MAX_VALUE;
							}

							long finalHealth = currentHealth - damage;
							Main.mobHealthGetter.remove(entity);
							double gap = entity.getEyeHeight() / 2;
							Location loc = en.getLocation().subtract(0, gap, 0);
							ArmorStand damageDisplay = (ArmorStand) p.getWorld().spawnEntity(loc,
									EntityType.ARMOR_STAND);
							damageDisplay.setVisible(false);
							String damageText = null;
							if (testPlayer.isCrit) {
								String damage_str = "✧" + Long.toString(damage) + "✧";

								StringBuilder temp = new StringBuilder();
								for (int i = 0; i < damage_str.length(); i++) {
									char msg = damage_str.charAt(i);
									switch (i % 6) {
									case 0: // white
										temp.append("§f" + msg);
										break;

									case 1: // white
										temp.append("§f" + msg);
										break;

									case 2: // yellow
										temp.append("§e" + msg);
										break;

									case 3: // gold
										temp.append("§6" + msg);
										break;

									case 4: // red
										temp.append("§c" + msg);
										break;

									case 5: // red
										temp.append("§c" + msg);
										break;
									}
									damageText = temp.toString();
								}
							} else {
								damageText = "§7" + damage;
							}

							damageDisplay.setCustomName(damageText);
							damageDisplay.setCustomNameVisible(true);
							damageDisplay.setGravity(false);
							Main.damageArmorStand.add(damageDisplay);
							if (finalHealth > 0) {
								Main.mobHealthGetter.put(entity, (long) finalHealth);
								Main.changeArmorStandName(en,
										StringUtil.armorStandNameGenerator(Main.mobLevelGetter.get(entity),
												Main.mobHealthGetter.get(entity), Main.mobMaxHealthGetter.get(entity),
												Main.mobNameGetter.get(entity)));
							} else {
								Main.mobHealthGetter.put(entity, 0L);
								Main.changeArmorStandName(en,
										StringUtil.armorStandNameGenerator(Main.mobLevelGetter.get(entity),
												Main.mobHealthGetter.get(entity), Main.mobMaxHealthGetter.get(entity),
												Main.mobNameGetter.get(entity)));
								((LivingEntity) entity).setHealth(0);
							}
							removeDamageDisplay(damageDisplay);

						} catch (Exception e1) {

						}
					}
				}
			}
		}

	}

	private void removeDamageDisplay(ArmorStand armorStand) {
		new BukkitRunnable() {
			int num = 0;

			@Override
			public void run() {
				if (num == 2) {
					Main.damageArmorStand.remove(armorStand);
					armorStand.remove();
					cancel();
					return;
				}
				num++;
			}
		}.runTaskTimer(plugin, 0, 13);
	}

	@EventHandler
	public void onDamageNotByPlayer(EntityDamageEvent e) {
		if (EntityUtil.isInGameEntity(e.getEntity())) {
			e.setDamage(0);

		}
		if (e.getEntity() instanceof Player) {
			e.setDamage(0);
		}

	}

	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		if (EntityUtil.isInGameEntity(e.getEntity())) {
			e.getDrops().clear();
			e.setDroppedExp(0);
		}

	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		joinedPlayer.put(e.getPlayer().getName(), e.getPlayer());
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		joinedPlayer.remove(e.getPlayer().getName());
		Main.testPlayerGetter.remove(e.getPlayer().getName());
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if (ItemUtil.isInGameItem(e.getPlayer().getItemInHand())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onEndermanTeleport(EntityTeleportEvent e) {
		if (e.getEntityType() == EntityType.ENDERMAN) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onEntityBurn(EntityCombustEvent e) {
		if (EntityUtil.isInGameEntity(e.getEntity())) {
			e.setCancelled(true);
		}
	}

}
