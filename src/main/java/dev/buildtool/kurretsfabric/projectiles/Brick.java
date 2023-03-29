package dev.buildtool.kurretsfabric.projectiles;

import dev.buildtool.kurretsfabric.IndirectDamageSource;
import dev.buildtool.kurretsfabric.KTurrets;
import dev.buildtool.kurretsfabric.PresetProjectile;
import dev.buildtool.kurretsfabric.Turret;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class Brick extends PresetProjectile {
    public Brick(EntityType<? extends ExplosiveProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public Brick(Turret owner, double directionX, double directionY, double directionZ, World world) {
        super(KTurrets.BRICK, owner, directionX, directionY, directionZ, world);
    }

    @Override
    protected DamageSource getDamageSource() {
        return new IndirectDamageSource("k_turrets.brick", this, getOwner());
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        playSound(SoundEvents.BLOCK_GILDED_BLACKSTONE_BREAK, 1, 1);
    }
}
