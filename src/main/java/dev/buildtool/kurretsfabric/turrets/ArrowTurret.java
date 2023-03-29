package dev.buildtool.kurretsfabric.turrets;

import dev.buildtool.kurretsfabric.Arrow2;
import dev.buildtool.kurretsfabric.KTurrets;
import dev.buildtool.kurretsfabric.Turret;
import dev.buildtool.kurretsfabric.goals.AttackTask;
import dev.buildtool.kurretsfabric.screenhandlers.ArrowTurretScreenHandler;
import dev.buildtool.satako.DefaultInventory;
import io.netty.buffer.Unpooled;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ProjectileAttackGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ArrowTurret extends Turret {
    public final DefaultInventory ammo = new DefaultInventory(27) {
        @Override
        public boolean isValid(int slot, ItemStack stack) {
            return stack.getItem() instanceof ArrowItem;
        }
    }, weapon = new DefaultInventory(1) {
        @Override
        public boolean isValid(int slot, ItemStack stack) {
            return stack.getItem() instanceof BowItem || stack.getItem() instanceof CrossbowItem;
        }
    };

    public ArrowTurret(World world) {
        super(KTurrets.ARROW_TURRET, world);
    }

    @Override
    public boolean isArmed() {
        return !ammo.isEmpty() && !weapon.isEmpty();
    }

    @Override
    protected List<DefaultInventory> getContainedItems() {
        return List.of(ammo, weapon);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeInt(getId());
    }

    @Override
    public void attack(LivingEntity target, float pullProgress) {
        if (target.isAlive()) {
            ItemStack weapon = this.weapon.getStack(0);
            if (!weapon.isEmpty()) {
                for (int i = 0; i < ammo.size(); i++) {
                    ItemStack ammo = this.ammo.getStack(i);
                    if (!ammo.isEmpty()) {
                        PersistentProjectileEntity projectile = ProjectileUtil.createArrowProjectile(this, ammo, pullProgress);
                        double d0 = target.getX() - this.getX();
                        double d1 = target.getEyeY() - getEyeY();
                        double d2 = target.getZ() - this.getZ();
                        Arrow2 arrow2 = new Arrow2(world, projectile, this);
                        if (weapon.getItem() instanceof BowItem)
                            arrow2.setDamage(KTurrets.CONFIGURATION.arrowTurretDamage());
                        else if (weapon.getItem() instanceof CrossbowItem) {
                            arrow2.setDamage(KTurrets.CONFIGURATION.arrowTurretDamage() * 1.2);
                            arrow2.setShotFromCrossbow(true);
                            int p = EnchantmentHelper.getLevel(Enchantments.PIERCING, weapon);
                            if (p > 0)
                                arrow2.setPierceLevel((byte) p);
                        }
                        arrow2.applyEnchantmentEffects(this, pullProgress);
                        arrow2.setNoGravity(true);
                        arrow2.setVelocity(d0, d1, d2, 1.8f, 0);
                        playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.random.nextFloat() * 0.4F + 0.8F));
                        world.spawnEntity(arrow2);
                        ammo.decrement(1);
                        if (EnchantmentHelper.getLevel(Enchantments.INFINITY, weapon) == 0 && EnchantmentHelper.getLevel(Enchantments.MULTISHOT, weapon) == 0)
                            weapon.damage(1, this, arrowTurret -> arrowTurret.sendToolBreakStatus(Hand.MAIN_HAND));
                        break;
                    }
                }
            }
        }
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
        packetByteBuf.writeInt(getId());
        return new ArrowTurretScreenHandler(syncId, inv, packetByteBuf);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.put("Ammo", ammo.writeToTag());
        nbt.put("Weapon", weapon.writeToTag());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        ammo.readFromTag(nbt.getCompound("Ammo"));
        weapon.readFromTag(nbt.getCompound("Weapon"));
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        goalSelector.add(5, new ProjectileAttackGoal(this, 0, KTurrets.CONFIGURATION.arrowTurretDelay(), (float) getRange()));
        targetSelector.add(5, new AttackTask(this));
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        return weapon.getStack(0);
    }
}
