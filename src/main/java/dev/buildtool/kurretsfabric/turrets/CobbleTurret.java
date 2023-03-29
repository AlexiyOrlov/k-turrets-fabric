package dev.buildtool.kurretsfabric.turrets;

import dev.buildtool.kurretsfabric.KTurrets;
import dev.buildtool.kurretsfabric.Turret;
import dev.buildtool.kurretsfabric.goals.AttackGoal;
import dev.buildtool.kurretsfabric.projectiles.Cobblestone;
import dev.buildtool.kurretsfabric.screenhandlers.CobbleScreenHandler;
import dev.buildtool.satako.DefaultInventory;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
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
import net.minecraft.tag.ItemTags;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class CobbleTurret extends Turret {
    public DefaultInventory ammo = new DefaultInventory(27) {
        @Override
        public boolean isValid(int slot, ItemStack stack) {
            return stack.streamTags().anyMatch(itemTagKey -> ItemTags.STONE_TOOL_MATERIALS.id().equals(itemTagKey.id()));
        }
    };

    public CobbleTurret(EntityType<? extends MobEntity> entityType, World world) {
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
            for (ItemStack stack : ammo.getItems()) {
                if (!stack.isEmpty()) {
                    double xa = target.getX() - getX();
                    double ya = target.getEyeY() - getEyeY();
                    double za = target.getZ() - getZ();
                    Cobblestone cobblestone = new Cobblestone(this, xa, ya, za, world);
                    cobblestone.setDamage(KTurrets.CONFIGURATION.cobbleTurretDamage());
                    world.spawnEntity(cobblestone);
                    world.playSound(null, getBlockPos(), KTurrets.COBBLE_FIRE, SoundCategory.NEUTRAL, 1, 1);
                    stack.decrement(1);
                    break;
                }
            }
        }
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(getId());
        return new CobbleScreenHandler(syncId, inv, buf);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        goalSelector.add(5, new ProjectileAttackGoal((RangedAttackMob) this, 0, KTurrets.CONFIGURATION.cobbleTurretDelay(), (float) getRange()));
        targetSelector.add(5, new AttackGoal(this));
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
}
