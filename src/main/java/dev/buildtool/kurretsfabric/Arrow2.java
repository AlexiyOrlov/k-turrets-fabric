package dev.buildtool.kurretsfabric;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Arrow2 extends ArrowEntity {
    private Turret turret;

    public Arrow2(EntityType<? extends ArrowEntity> entityType, World world) {
        super(entityType, world);
    }

    public Arrow2(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public Arrow2(World world, PersistentProjectileEntity arrowEntity, Turret turret) {
        super(world, turret);
        copyPositionAndRotation(arrowEntity);
        setVelocity(arrowEntity.getVelocity());
        setPierceLevel(arrowEntity.getPierceLevel());
        if (arrowEntity instanceof SpectralArrowEntity) {
            addEffect(new StatusEffectInstance(StatusEffects.GLOWING, 200));
        } else if (arrowEntity instanceof ArrowEntity arrow) {
            potion = arrow.potion;
            arrow.effects.forEach(this::addEffect);
        }
        setOwner(arrowEntity.getOwner());
        this.turret = turret;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        discard();
    }

    @Override
    public void applyEnchantmentEffects(LivingEntity entity, float damageModifier) {
        int i = EnchantmentHelper.getEquipmentLevel(Enchantments.POWER, entity);
        int j = EnchantmentHelper.getEquipmentLevel(Enchantments.PUNCH, entity);
        if (i > 0) {
            this.setDamage(this.getDamage() + (double) i * 0.5 + 0.5);
        }
        if (j > 0) {
            this.setPunch(j);
        }
        if (EnchantmentHelper.getEquipmentLevel(Enchantments.FLAME, entity) > 0) {
            this.setOnFireFor(100);
        }
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
        Entity entity = entityHitResult.getEntity();
        double d = getDamage();
        if (this.isCritical()) {
            long l = this.random.nextInt((int) (d / 2 + 2));
            d = (int) Math.min(l + (long) d, Integer.MAX_VALUE);
        }
        DamageSource damageSource;
        Entity owner = getOwner();
        if (owner != null) {
            damageSource = new IndirectDamageSource("arrow", this, owner);
            if (owner instanceof LivingEntity livingEntity && entity instanceof LivingEntity entity1)
                entity1.setAttacker(livingEntity); //FIXME
        } else damageSource = new IndirectDamageSource("arrow", this, this);

        if (this.isOnFire()) {
            entity.setOnFireFor(5);
        }
        int j = getFireTicks();
        if (entity.damage(damageSource, (float) d)) {
            if (entity instanceof LivingEntity livingEntity) {
                if (!this.world.isClient && this.getPierceLevel() <= 0) {
                    livingEntity.setStuckArrowCount(livingEntity.getStuckArrowCount() + 1);
                }
                if (this.getPunch() > 0) {
                    Vec3d vec3d = this.getVelocity().multiply(1.0, 0.0, 1.0).normalize().multiply((double) this.getPunch() * 0.6);
                    if (vec3d.lengthSquared() > 0.0) {
                        livingEntity.addVelocity(vec3d.x, 0.1, vec3d.z);
                    }
                }
                if (!this.world.isClient && owner instanceof LivingEntity) {
                    EnchantmentHelper.onUserDamaged(livingEntity, owner);
                    EnchantmentHelper.onTargetDamaged((LivingEntity) owner, livingEntity);
                }
                this.onHit(livingEntity);
                if (livingEntity != owner && livingEntity instanceof PlayerEntity && owner instanceof ServerPlayerEntity && !this.isSilent()) {
                    ((ServerPlayerEntity) owner).networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.PROJECTILE_HIT_PLAYER, GameStateChangeS2CPacket.DEMO_OPEN_SCREEN));
                }
            }
            this.playSound(this.getSound(), 1.0f, 1.2f / (this.random.nextFloat() * 0.2f + 0.9f));
            if (this.getPierceLevel() <= 0) {
                this.discard();
            }
        } else {
            entity.setFireTicks(j);
            this.setVelocity(this.getVelocity().multiply(-0.1));
            this.setYaw(this.getYaw() + 180.0f);
            this.prevYaw += 180.0f;
            if (!this.world.isClient && this.getVelocity().lengthSquared() < 1.0E-7) {
                this.discard();
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getVelocity().length() < 1)
            discard();
    }

    @Override
    public void setVelocity(double x, double y, double z, float speed, float divergence) {
        Vec3d vec3d = new Vec3d(x, y, z);
        this.setVelocity(vec3d);
        double d = vec3d.horizontalLength();
        this.setYaw((float) (MathHelper.atan2(vec3d.x, vec3d.z) * 57.2957763671875));
        this.setPitch((float) (MathHelper.atan2(vec3d.y, d) * 57.2957763671875));
        this.prevYaw = this.getYaw();
        this.prevPitch = this.getPitch();
    }
}
