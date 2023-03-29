package dev.buildtool.kurretsfabric;

import dev.buildtool.kurretsfabric.goals.AvoidAggressors;
import dev.buildtool.kurretsfabric.goals.FollowOwnerGoal;
import dev.buildtool.kurretsfabric.goals.MoveOutOfLava;
import dev.buildtool.kurretsfabric.goals.StrafeByTarget;
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
    static final TrackedData<Boolean> FOLLOWING_OWNER = DataTracker.registerData(Drone.class, TrackedDataHandlerRegistry.BOOLEAN);

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
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        setFollowingOwner(nbt.getBoolean("Following"));
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        goalSelector.add(4, new FollowOwnerGoal(this));
        goalSelector.add(5, new MoveOutOfLava(this));
        goalSelector.add(6, new AvoidAggressors(this));
        goalSelector.add(7, new StrafeByTarget(this));
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
