package dev.buildtool.kurretsfabric.projectiles;

import dev.buildtool.kurretsfabric.IndirectDamageSource;
import dev.buildtool.kurretsfabric.KTurrets;
import dev.buildtool.kurretsfabric.PresetProjectile;
import dev.buildtool.kurretsfabric.Turret;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class Cobblestone extends PresetProjectile {
    public Cobblestone(EntityType<? extends ExplosiveProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public Cobblestone(Turret owner, double directionX, double directionY, double directionZ, World world) {
        super(KTurrets.COBBLESTONE, owner, directionX, directionY, directionZ, world);
    }

    @Override
    protected DamageSource getDamageSource() {
        return new IndirectDamageSource("k_turrets.cobblestone", this, getOwner());
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        world.playSound(null, getBlockPos(), SoundEvents.BLOCK_NETHER_BRICKS_BREAK, SoundCategory.NEUTRAL, 1, 1);
    }
}
