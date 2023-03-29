package dev.buildtool.kurretsfabric;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.RangeConstraint;

@SuppressWarnings("unused")
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
    @RangeConstraint(min = 10, max = 500)
    public int brickTurretHealth = 60;
    @RangeConstraint(min = 0, max = 100)
    public int brickTurretArmor = 3;
    @RangeConstraint(min = 8, max = 100)
    public int brickTurretRange = 32;
    @RangeConstraint(min = 1, max = 100)
    public int netherBrickDamage = 10;
    @RangeConstraint(min = 1, max = 100)
    public int goldBulletDamage = 7;
    @RangeConstraint(min = 1, max = 100)
    public int ironBulletDamage = 8;
    @RangeConstraint(min = 10, max = 60)
    public int bulletTurretDelay = 20;
    @RangeConstraint(min = 10, max = 500)
    public int bulletTurretHealth = 60;
    @RangeConstraint(min = 0, max = 100)
    public int bulletTurretArmor = 3;
    @RangeConstraint(min = 8, max = 100)
    public int bulletTurretRange = 32;
    @RangeConstraint(min = 10, max = 60)
    public int cobbleTurretDelay = 20;
    @RangeConstraint(min = 10, max = 500)
    public int cobbleTurretHealth = 60;
    @RangeConstraint(min = 0, max = 100)
    public int cobbleTurretArmor = 3;
    @RangeConstraint(min = 8, max = 100)
    public int cobbleTurretRange = 32;
    @RangeConstraint(min = 1, max = 100)
    public int cobbleTurretDamage = 3;
    @RangeConstraint(min = 10, max = 60)
    public int fireChargeTurretDelay = 20;
    @RangeConstraint(min = 10, max = 500)
    public int fireChargeTurretHealth = 60;
    @RangeConstraint(min = 0, max = 100)
    public int fireChargeTurretArmor = 3;
    @RangeConstraint(min = 8, max = 100)
    public int fireChargeTurretRange = 32;
    @RangeConstraint(min = 1, max = 100)
    public int fireChargeTurretDamage = 6;
}
