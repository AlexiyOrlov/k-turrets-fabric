package dev.buildtool.kurretsfabric.projectiles;

import dev.buildtool.kurretsfabric.IndirectDamageSource;
import dev.buildtool.kurretsfabric.KTurrets;
import dev.buildtool.kurretsfabric.PresetProjectile;
import dev.buildtool.kurretsfabric.Turret;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.world.World;

public class GaussBullet extends PresetProjectile {

    static {
        movementMultiplier = movementMultiplier * 3;
    }

    public GaussBullet(EntityType<? extends ExplosiveProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public GaussBullet(Turret owner, double directionX, double directionY, double directionZ, World world) {
        super(KTurrets.GAUSS_BULLET, owner, directionX, directionY, directionZ, world);
    }

    @Override
    protected DamageSource getDamageSource() {
        return new IndirectDamageSource("k_turrets.gauss_bullet", this, getOwner());
    }
}
