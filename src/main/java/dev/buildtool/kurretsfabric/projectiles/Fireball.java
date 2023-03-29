package dev.buildtool.kurretsfabric.projectiles;

import dev.buildtool.kurretsfabric.KTurrets;
import dev.buildtool.kurretsfabric.Turret;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.util.hit.EntityHitResult;

public class Fireball extends SmallFireballEntity {
    private Turret turret;

    public Fireball(Turret owner, double velocityX, double velocityY, double velocityZ) {
        super(owner.world, owner, velocityX, velocityY, velocityZ);
    }

    @Override
    protected boolean canHit(Entity entity) {
        Entity owner = getOwner();
        if (turret != null && entity.getType().getSpawnGroup().isPeaceful() && Turret.decodeTargets(turret.getTargets()).contains(entity.getType()))
            return super.canHit(entity);
        else if (owner == null || !owner.isTeammate(entity) && !entity.getType().getSpawnGroup().isPeaceful()) {
            return super.canHit(entity);
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
