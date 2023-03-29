package dev.buildtool.kurretsfabric;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.RangeConstraint;

@Config(name = KTurrets.ID, wrapperName = "Config")
public class Configuration {
    @RangeConstraint(min = 1, max = 100)
    public int arrowTurretDamage = 6;
    @RangeConstraint(min = 10, max = 60)
    public int arrowTurretDelay = 20;
    @RangeConstraint(min = 8, max = 100)
    public double arrowTurretRange = 32;
    @RangeConstraint(min = 0, max = 100)
    public int arrowTurretArmor = 3;
    @RangeConstraint(min = 10, max = 500)
    public int arrowTurretHealth = 60;
    @RangeConstraint(min = 10, max = 60)
    public int brickTurretDelay = 10;
    @RangeConstraint(min = 1, max = 100)
    public int brickDamage = 9;
    @RangeConstraint(min = 1, max = 100)
    public int netherBrickDamage = 10;
}
