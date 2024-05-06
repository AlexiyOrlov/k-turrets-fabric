package dev.buildtool.kurretsfabric.projectiles;

import dev.buildtool.kurretsfabric.KTurrets;
import dev.buildtool.kurretsfabric.Turret;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.util.hit.EntityHitResult;

public class Fireball extends SmallFireballEntity {
    private static final double movementMultiplier = KTurrets.CONFIGURATION.projectileSpeed();

    public Fireball(Turret owner, double velocityX, double velocityY, double velocityZ) {
        super(owner.world, owner, velocityX, velocityY, velocityZ);
        powerX *= movementMultiplier;
        powerY *= movementMultiplier;
        powerZ *= movementMultiplier;
    }

    @Override
    protected boolean canHit(Entity target) {
        Entity entity = getOwner();
        if (entity instanceof Turret owner) {
            if (target instanceof PlayerEntity player) {
                if (owner.getOwner().isPresent() && player.getUuid().equals(owner.getOwner().get()))
                    return false;
                return !target.isTeammate(owner);
            }
            if (target instanceof Turret turret) {
                if (owner.getOwner().isPresent()) {
                    if (turret.getOwner().isPresent()) {
                        return !owner.getOwner().get().equals(turret.getOwner().get());
                    } else
                        return true;
                }
                return true;
            }
            if (target.getType().getSpawnGroup().isPeaceful()) {
                return target == owner.getTarget();
            } else {
                return Turret.decodeTargets(owner.getTargets()).contains(target.getType());
            }
        }
        return false;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (!world.isClient) {
            Entity entity = entityHitResult.getEntity();
            if (!entity.isFireImmune()) {
                Entity owner = getOwner();
                int t = entity.getFireTicks();
                entity.setOnFireFor(5);
                boolean d = entity.damage(DamageSource.fireball(this, owner), KTurrets.CONFIGURATION.fireChargeTurretDamage());
                if (!d)
                    entity.setFireTicks(t);
                else if (owner instanceof LivingEntity livingEntity)
                    this.applyDamageEffects(livingEntity, entity);
            }
        }
    }
}
