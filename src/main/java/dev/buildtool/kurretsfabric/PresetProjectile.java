package dev.buildtool.kurretsfabric;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public abstract class PresetProjectile extends ExplosiveProjectileEntity {
    protected Turret turret;
    protected static double movementMultiplier = KTurrets.CONFIGURATION.projectileSpeed();
    private static final TrackedData<Integer> DAMAGE = DataTracker.registerData(PresetProjectile.class, TrackedDataHandlerRegistry.INTEGER);

    protected PresetProjectile(EntityType<? extends ExplosiveProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public PresetProjectile(EntityType<? extends ExplosiveProjectileEntity> type, Turret owner, double directionX, double directionY, double directionZ, World world) {
        super(type, owner, directionX, directionY, directionZ, world);
        setPos(owner.getX(), owner.getEyeY(), owner.getZ());
        turret = owner;
        powerX *= movementMultiplier;
        powerY *= movementMultiplier;
        powerZ *= movementMultiplier;
    }

    @Override
    protected boolean isBurning() {
        return false;
    }

    @Override
    protected float getDrag() {
        return 1;
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return super.createSpawnPacket(); //TODO
    }

    @Override
    protected boolean canHit(Entity target) {
        Turret owner = (Turret) getOwner();
        if (owner != null) {
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

    protected abstract DamageSource getDamageSource();

    public void setDamage(int damage) {
        dataTracker.set(DAMAGE, damage);
    }

    public int getDamage() {
        return dataTracker.get(DAMAGE);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(DAMAGE, 0);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        entity.damage(getDamageSource(), getDamage());
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        discard();
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putInt("Damage", getDamage());
        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        setDamage(nbt.getInt("Damage"));
    }
}
