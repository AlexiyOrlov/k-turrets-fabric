package dev.buildtool.kurretsfabric;

import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;

public class DroneMovementControl extends FlightMoveControl {

    public DroneMovementControl(MobEntity entity, int maxPitchChange, boolean noGravity) {
        super(entity, maxPitchChange, noGravity);
    }

    @Override
    public void tick() {
        if (this.state == State.STRAFE) {
            float f = (float) this.entity.getAttributeValue(EntityAttributes.GENERIC_FLYING_SPEED);
            entity.setMovementSpeed(f);
            entity.setForwardSpeed(this.forwardMovement);
            entity.setSidewaysSpeed(this.sidewaysMovement);
            state = State.WAIT;
        } else
            super.tick();
    }
}
