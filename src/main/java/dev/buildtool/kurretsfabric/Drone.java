package dev.buildtool.kurretsfabric;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;

public abstract class Drone extends Turret {
    static final TrackedData<Boolean> FOLLOWING_OWNER = DataTracker.registerData(Drone.class, TrackedDataHandlerRegistry.BOOLEAN);

    public Drone(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(FOLLOWING_OWNER, true);
    }

    public boolean isFollowingOwner() {
        return dataTracker.get(FOLLOWING_OWNER);
    }

    public void setFollowingOwner(boolean b) {
        dataTracker.set(FOLLOWING_OWNER, b);
    }
}
