package dev.buildtool.kurretsfabric.goals;

import dev.buildtool.kurretsfabric.Drone;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;

public class GuardArea extends Goal {
    private Drone drone;

    public GuardArea(Drone drone) {
        this.drone = drone;
    }

    @Override
    public boolean canStart() {
        return drone.isGuardingArea();
    }

    @Override
    public void tick() {
        if (drone.getGuardPosition().equals(BlockPos.ORIGIN))
            drone.setGuardPosition(drone.getBlockPos());
        else {
            if (drone.getTarget() == null) {
                BlockPos guardPos = drone.getGuardPosition();
                if (drone.squaredDistanceTo(guardPos.getX() + 02.5, guardPos.getY(), guardPos.getZ() + 0.5) > 16)
                    drone.getNavigation().startMovingTo(guardPos.getX() + 0.5, guardPos.getY(), guardPos.getZ() + 0.5, 1);
            }
        }
    }
}
