package dev.buildtool.kurretsfabric.goals;

import dev.buildtool.kurretsfabric.Drone;
import net.minecraft.entity.ai.goal.Goal;

public class StrafeByTarget extends Goal {
    private final Drone drone;
    private int timer;

    public StrafeByTarget(Drone drone) {
        this.drone = drone;
    }

    @Override
    public boolean canStart() {
        return drone.getTarget() != null && drone.getTarget().isAlive();
    }

    @Override
    public void tick() {
        if (timer == 0) {
            timer = drone.world.random.nextBoolean() ? 60 : -60;
        } else if (timer < 0)
            timer++;
        else
            timer--;
        drone.getMoveControl().strafeTo(0, Math.signum(timer) * 0.1f);
    }

    @Override
    public void stop() {
        timer = 0;
    }
}
