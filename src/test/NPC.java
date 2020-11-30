package test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import net.minecraft.server.v1_8_R1.EntityCreeper;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.EntityInsentient;
import net.minecraft.server.v1_8_R1.EntityTypes;
import net.minecraft.server.v1_8_R1.EntityZombie;
import net.minecraft.server.v1_8_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R1.PathfinderGoalMoveThroughVillage;
import net.minecraft.server.v1_8_R1.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_8_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_8_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R1.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R1.World;

public class NPC extends EntityZombie {

	@SuppressWarnings("rawtypes")
	public NPC(World world) {
		super(world);

		List goalB = (List) getPrivateField("b", PathfinderGoalSelector.class, goalSelector);
		goalB.clear();
		List goalC = (List) getPrivateField("c", PathfinderGoalSelector.class, goalSelector);
		goalC.clear();
		List targetB = (List) getPrivateField("b", PathfinderGoalSelector.class, targetSelector);
		targetB.clear();
		List targetC = (List) getPrivateField("c", PathfinderGoalSelector.class, targetSelector);
		targetC.clear();

		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, EntityCreeper.class, 1.0D, true));
		this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
		this.goalSelector.a(6, new PathfinderGoalMoveThroughVillage(this, 1.0D, false));
		this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
		this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
		this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
		this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityCreeper.class, true));
	}

	@SuppressWarnings("rawtypes")
	public static Object getPrivateField(String fieldName, Class clazz, Object object) {
		Field field;
		Object o = null;

		try {
			field = clazz.getDeclaredField(fieldName);

			field.setAccessible(true);

			o = field.get(object);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return o;
	}

	public void registerEntity(String name, int id, Class<? extends EntityInsentient> nmsClass,
			Class<? extends EntityInsentient> customClass) {
		try {

			List<Map<?, ?>> dataMap = new ArrayList<Map<?, ?>>();
			for (Field f : EntityTypes.class.getDeclaredFields()) {
				if (f.getType().getSimpleName().equals(Map.class.getSimpleName())) {
					f.setAccessible(true);
					dataMap.add((Map<?, ?>) f.get(null));
				}
			}

			if (dataMap.get(2).containsKey(id)) {
				dataMap.get(0).remove(name);
				dataMap.get(2).remove(id);
			}

			Method method = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, int.class);
			method.setAccessible(true);
			method.invoke(null, customClass, name, id);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Zombie spawn(Location loc) {
		net.minecraft.server.v1_8_R1.World mcWorld = ((CraftWorld) loc.getWorld()).getHandle();
		final NPC customEnt = new NPC(mcWorld);
		customEnt.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		((CraftLivingEntity) customEnt.getBukkitEntity()).setRemoveWhenFarAway(false); // Do we want to remove it when
																						// the NPC is far away? I won
		mcWorld.addEntity(customEnt, SpawnReason.CUSTOM);
		return (Zombie) customEnt.getBukkitEntity();
	}

}
