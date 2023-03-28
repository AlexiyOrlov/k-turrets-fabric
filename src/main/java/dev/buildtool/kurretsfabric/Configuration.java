package dev.buildtool.kurretsfabric;

import eu.midnightdust.lib.config.MidnightConfig;

public class Configuration extends MidnightConfig {
    @Entry(min = 1, max = 100, name = "Arrow turret base damage")
    public static int arrowTurretDamage = 6;
    @Entry(min = 10, max = 60, name = "Arrow turret fire delay")
    public static int arrowTurretDelay = 20;
}
