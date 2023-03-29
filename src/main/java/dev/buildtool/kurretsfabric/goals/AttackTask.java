package dev.buildtool.kurretsfabric.goals;

import dev.buildtool.kurretsfabric.Turret;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;

public class AttackTask extends ActiveTargetGoal<LivingEntity> {
    private final Turret turret;

    public AttackTask(Turret turret) {
        super(turret, LivingEntity.class, 0, true, true, livingEntity -> {
            if (turret.isProtectingFromPlayers() && livingEntity instanceof PlayerEntity)
                return turret.alienPlayers.test(livingEntity);
            return Turret.decodeTargets(turret.getTargets()).contains(livingEntity.getType());
        });
        this.turret = turret;
    }

    @Override
    public boolean canStart() {
        return turret.isArmed() && super.canStart();
    }

    @Override
    public boolean shouldContinue() {
        return turret.isArmed() && super.shouldContinue();
    }

    @Override
    protected Box getSearchBox(double distance) {
        return turret.getBoundingBox().expand(distance, distance, distance);
    }
}
