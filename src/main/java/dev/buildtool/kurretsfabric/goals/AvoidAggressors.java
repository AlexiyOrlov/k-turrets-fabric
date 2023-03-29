package dev.buildtool.kurretsfabric.goals;

import dev.buildtool.kurretsfabric.Drone;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;

import java.util.List;
import java.util.Optional;

public class AvoidAggressors extends Goal {
    private final Drone drone;

    public AvoidAggressors(Drone drone) {
        this.drone = drone;
    }

    @Override
    public boolean canStart() {
        List<MobEntity> aggressors = drone.world.getEntitiesByClass(MobEntity.class, drone.getBoundingBox().expand(5), mob -> mob.getTarget() == drone);
        Optional<MobEntity> aggressor = aggressors.stream().filter(mob -> mob.distanceTo(drone) < 3).findAny();
        return aggressor.isPresent();
    }

    @Override
    public void tick() {
        drone.getMoveControl().strafeTo(5, 0);
    }
}
