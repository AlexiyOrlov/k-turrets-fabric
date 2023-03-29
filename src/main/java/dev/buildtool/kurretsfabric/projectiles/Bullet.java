package dev.buildtool.kurretsfabric.projectiles;

import dev.buildtool.kurretsfabric.IndirectDamageSource;
import dev.buildtool.kurretsfabric.KTurrets;
import dev.buildtool.kurretsfabric.PresetProjectile;
import dev.buildtool.kurretsfabric.Turret;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;

public class Bullet extends PresetProjectile {
    public Bullet(World world) {
        super(KTurrets.BULLET, world);
    }

    public Bullet(Turret owner, double directionX, double directionY, double directionZ, World world) {
        super(KTurrets.BULLET, owner, directionX, directionY, directionZ, world);
    }

    @Override
    protected DamageSource getDamageSource() {
        return new IndirectDamageSource("k_turrets.bullet", this, getOwner());
    }
}
