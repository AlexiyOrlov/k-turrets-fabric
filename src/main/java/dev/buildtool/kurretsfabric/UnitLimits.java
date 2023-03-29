package dev.buildtool.kurretsfabric;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.nbt.NbtCompound;

public class UnitLimits implements Component {
    private int turretCount, droneCount;

    @Override
    public void readFromNbt(NbtCompound tag) {
        turretCount = tag.getInt("Turret count");
        droneCount = tag.getInt("Drone count");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("Turret count", turretCount);
        tag.putInt("Drone count", droneCount);
    }

    public int getDroneCount() {
        return droneCount;
    }

    public int getTurretCount() {
        return turretCount;
    }

    public void increaseDroneCount() {
        droneCount++;
    }

    public void decreaseDroneCount() {
        if (droneCount > 0)
            droneCount--;
    }

    public void increaseTurretCount() {
        turretCount++;
    }

    public void decreaseTurretCount() {

        if (turretCount > 0)
            turretCount--;
    }
}
