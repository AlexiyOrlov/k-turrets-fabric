package dev.buildtool.kurretsfabric.goals;

import dev.buildtool.kurretsfabric.Drone;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.apache.commons.lang3.ArrayUtils;

public class MoveOutOfLava extends Goal {
    private final Drone drone;

    public MoveOutOfLava(Drone drone) {
        this.drone = drone;
    }

    @Override
    public boolean canStart() {
        return drone.isInLava();
    }

    @Override
    public void start() {
        BlockPos blockPos = drone.getSteppingPos();
        int counter = 1;
        loop:
        do {

            for (Direction direction : ArrayUtils.removeElement(Direction.values(), Direction.DOWN)) {
                BlockPos nextCheck = blockPos.offset(direction, counter);
                if (drone.world.getBlockState(nextCheck).isAir()) {
                    drone.refreshPositionAndAngles(nextCheck.offset(direction), 1, 1);
                    break loop;
                }
            }
            counter++;
        }
        while (counter < 60);
    }
}
