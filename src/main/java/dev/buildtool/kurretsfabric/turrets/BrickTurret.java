package dev.buildtool.kurretsfabric.turrets;

import dev.buildtool.kurretsfabric.KTurrets;
import dev.buildtool.kurretsfabric.Turret;
import dev.buildtool.kurretsfabric.projectiles.Brick;
import dev.buildtool.kurretsfabric.screenhandlers.BrickTurretScreenHandler;
import dev.buildtool.satako.DefaultInventory;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ProjectileAttackGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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

public class BrickTurret extends Turret {
    public DefaultInventory bricks = new DefaultInventory(27) {
        @Override
        public boolean isValid(int slot, ItemStack stack) {
            return stack.getItem() == Items.BRICK || stack.getItem() == Items.NETHER_BRICK;
        }
    };

    public BrickTurret(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean isArmed() {
        return !bricks.isEmpty();
    }

    @Override
    protected List<DefaultInventory> getContainedItems() {
        return Collections.singletonList(bricks);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeInt(getId());
    }

    @Override
    public void attack(LivingEntity target, float pullProgress) {
        if (target.isAlive()) {
            for (ItemStack item : bricks.getItems()) {
                if (item.getItem() == Items.BRICK || item.getItem() == Items.NETHER_BRICK) {
                    double xa = target.getX() - getX();
                    double ya = target.getEyeY() - getEyeY();
                    double za = target.getZ() - getZ();
                    Brick brick = new Brick(this, xa, ya, za, world);
                    brick.setDamage(item.getItem() == Items.BRICK ? KTurrets.CONFIG.brickDamage() : KTurrets.CONFIG.netherBrickDamage());
                    world.spawnEntity(brick);
                    world.playSound(null, getBlockPos(), SoundEvents.ENTITY_WITCH_THROW, SoundCategory.NEUTRAL, 1, 0.5f);
                    item.decrement(1);
                    break;
                }
            }
        }
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
        packetByteBuf.writeInt(getId());
        return new BrickTurretScreenHandler(syncId, inv, packetByteBuf);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        goalSelector.add(5, new ProjectileAttackGoal(this, 0, KTurrets.CONFIG.brickTurretDelay(), (float) getRange()));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.put("Bricks", bricks.writeToTag());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        bricks.readFromTag(nbt.getCompound("Bricks"));
    }
}
