package dev.buildtool.kurretsfabric;

import dev.buildtool.kurretsfabric.goals.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Flutterer;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class Drone extends Turret {
    private static final TrackedData<Boolean> FOLLOWING_OWNER = DataTracker.registerData(Drone.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> GUARDING_AREA = DataTracker.registerData(Drone.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<BlockPos> GUARD_POSITION = DataTracker.registerData(Drone.class, TrackedDataHandlerRegistry.BLOCK_POS);
    public Drone(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
        setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1);
        setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, -1);
        setPathfindingPenalty(PathNodeType.DAMAGE_CACTUS, -1);
        moveControl = new DroneMovementControl(this, 20, true);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(FOLLOWING_OWNER, true);
        dataTracker.set(PUSHABLE, true);
        dataTracker.startTracking(GUARDING_AREA, false);
        dataTracker.startTracking(GUARD_POSITION, BlockPos.ORIGIN);
    }

    public boolean isGuardingArea() {
        return dataTracker.get(GUARDING_AREA);
    }

    public void setGuardingArea(boolean b) {
        dataTracker.set(GUARDING_AREA, b);
    }

    public BlockPos getGuardPosition() {
        return dataTracker.get(GUARD_POSITION);
    }

    public void setGuardPosition(BlockPos blockPos) {
        dataTracker.set(GUARD_POSITION, blockPos);
    }

    public boolean isFollowingOwner() {
        return dataTracker.get(FOLLOWING_OWNER);
    }

    public void setFollowingOwner(boolean b) {
        dataTracker.set(FOLLOWING_OWNER, b);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("Following", isFollowingOwner());
        nbt.putBoolean("Guarding area", isGuardingArea());
        nbt.putLong("Guard position", getGuardPosition().asLong());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        setFollowingOwner(nbt.getBoolean("Following"));
        setGuardingArea(nbt.getBoolean("Guarding area"));
        setGuardPosition(BlockPos.fromLong(nbt.getLong("Guard position")));
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        goalSelector.add(4, new FollowOwnerGoal(this));
        goalSelector.add(5, new MoveOutOfLava(this));
        goalSelector.add(6, new AvoidAggressors(this));
        goalSelector.add(7, new StrafeByTarget(this));
        goalSelector.add(8, new GuardArea(this));
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        BirdNavigation birdNavigation = new BirdNavigation(this, world);
        birdNavigation.setCanEnterOpenDoors(true);
        birdNavigation.setCanSwim(true);
        birdNavigation.setCanPathThroughDoors(false);
        return birdNavigation;
    }

    @Override
    public float getEyeHeight(EntityPose pose) {
        return getHeight() * 0.4f;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {

    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {

    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    public void travel(Vec3d movementInput) {
        float flySpeed = getMovementSpeed();
        if (isSubmergedInWater()) {
            this.updateVelocity(flySpeed / 2, movementInput);
            move(MovementType.SELF, getVelocity());
            setVelocity(getVelocity().multiply(0.8));
        } else if (isInLava()) {
            updateVelocity(flySpeed / 2, movementInput);
            move(MovementType.SELF, getVelocity());
            setVelocity(getVelocity().multiply(0.5));
        } else {
            BlockPos blockPos = new BlockPos(getX(), getY() - 1, getZ());
            float f = 0.91f;
            if (onGround) {
                f = world.getBlockState(blockPos).getBlock().getSlipperiness() * 0.91f;
            }
            float f1 = (float) (getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) / (f * f * f));
            updateVelocity(onGround ? 0.1f * f1 : flySpeed, movementInput);
            move(MovementType.SELF, getVelocity());
            setVelocity(getVelocity().multiply(f));

        }
        updateLimbs(this, this instanceof Flutterer);
    }

    @Override
    protected float restoreHealth() {
        return getMaxHealth() / 4;
    }

    @Override
    public boolean isClimbing() {
        return false;
    }
}
