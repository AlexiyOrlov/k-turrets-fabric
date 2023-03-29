package dev.buildtool.kurretsfabric.goals;

import dev.buildtool.kurretsfabric.Drone;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class FollowOwnerGoal extends Goal {
    private Drone drone;
    private World world;

    public FollowOwnerGoal(Drone drone) {
        this.drone = drone;
        world = drone.world;
    }

    @Override
    public boolean canStart() {
        return drone.isFollowingOwner() && drone.getOwner().isPresent();
    }

    @Override
    public void tick() {
        drone.getOwner().ifPresent(uuid -> {
            PlayerEntity player = world.getPlayerByUuid(uuid);
            if (player != null) {
                if (drone.squaredDistanceTo(player) > 25)
                    drone.getNavigation().startMovingTo(player.getX(), player.getY() + 2, player.getZ(), 1);
                else if (drone.getNavigation().isFollowingPath())
                    drone.getNavigation().stop();
            }
        });
    }
}
