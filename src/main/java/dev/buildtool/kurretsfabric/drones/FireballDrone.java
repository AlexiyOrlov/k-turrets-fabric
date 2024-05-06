package dev.buildtool.kurretsfabric.drones;

import dev.buildtool.kurretsfabric.Drone;
import dev.buildtool.kurretsfabric.KTurrets;
import dev.buildtool.kurretsfabric.projectiles.Fireball;
import dev.buildtool.kurretsfabric.screenhandlers.FireballDroneScreenHandler;
import dev.buildtool.satako.DefaultInventory;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.ProjectileAttackGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class FireballDrone extends Drone {
    public DefaultInventory ammo = new DefaultInventory(18) {
        @Override
        public boolean isValid(int slot, ItemStack stack) {
            return stack.isOf(KTurrets.explosivePowder);
        }
    };

    public FireballDrone(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean isArmed() {
        return !ammo.isEmpty();
    }

    @Override
    protected List<DefaultInventory> getContainedItems() {
        return Collections.singletonList(ammo);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeInt(getId());
    }

    @Override
    public void attack(LivingEntity target, float pullProgress) {
        if (target.isAlive()) {
            for (ItemStack item : ammo.getItems()) {
                if (!item.isEmpty()) {
                    double d0 = target.getX() - this.getX();
                    double d1 = target.getEyeY() - getEyeY();
                    double d2 = target.getZ() - this.getZ();
                    Fireball fireball = new Fireball(this, d0, d1, d2);
                    fireball.setPos(getX(), getEyeY(), getZ());
                    world.spawnEntity(fireball);
                    world.playSound(null, getBlockPos(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.NEUTRAL, 1, 1);
                    item.decrement(1);
                    break;
                }
            }
        }
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        PacketByteBuf byteBuf = new PacketByteBuf(Unpooled.buffer());
        byteBuf.writeInt(getId());
        return new FireballDroneScreenHandler(syncId, inv, byteBuf);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.put("Ammo", ammo.writeToTag());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        ammo.readFromTag(nbt.getCompound("Ammo"));
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        goalSelector.add(5, new ProjectileAttackGoal(this, 1, KTurrets.CONFIGURATION.fireChargeTurretDelay(), (float) getRange()));
        targetSelector.add(5, new ActiveTargetGoal<>(this, LivingEntity.class, 0, true, true, livingEntity -> {
            if (livingEntity instanceof PlayerEntity player) {
                if (isProtectingFromPlayers())
                    return alienPlayers.test(player);
                else return false;
            }
            else {
                return !livingEntity.isFireImmune() && decodeTargets(getTargets()).contains(livingEntity.getType());
            }
        }) {
            @Override
            public boolean canStart() {
                return ((!isFollowingOwner() && isGuardingArea()) || isFollowingOwner()) && isArmed() && super.canStart();
            }

            @Override
            public boolean shouldContinue() {
                return ((!isFollowingOwner() && isGuardingArea()) || isFollowingOwner()) && isArmed() && super.shouldContinue();
            }
        });
    }
}
