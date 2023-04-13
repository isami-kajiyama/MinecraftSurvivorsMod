//package jp.nora.minecraftsurvivors_mod.entity;
//
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.Mob;
//import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
//import net.minecraft.world.entity.ai.attributes.Attributes;
//import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
//import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
//import net.minecraft.world.entity.ai.goal.WanderAroundGoal;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.level.Level;
//
//public class GoldChickenEntity extends Mob {
//
//    public GoldChickenEntity(EntityType<? extends Mob> entityType, Level level) {
//        super(entityType, level);
//    }
//
//    public static AttributeSupplier.Builder createAttributes() {
//        return Mob.createMobAttributes()
//                .add(Attributes.MAX_HEALTH, 4.0D)
//                .add(Attributes.MOVEMENT_SPEED, 0.25D);
//    }
//
//    @Override
//    protected void registerGoals() {
//        this.goalSelector.addGoal(0, new AvoidEntityGoal<>(this, Player.class, 6.0F, 1.0D, 1.2D));
//        this.goalSelector.addGoal(1, new WanderAroundGoal(this, 1.0D));
//        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 6.0F));
//    }
//}