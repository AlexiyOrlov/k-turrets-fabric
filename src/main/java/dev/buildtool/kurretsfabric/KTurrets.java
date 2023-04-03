package dev.buildtool.kurretsfabric;

import com.google.common.collect.ImmutableSet;
import dev.buildtool.kurretsfabric.drones.*;
import dev.buildtool.kurretsfabric.projectiles.Brick;
import dev.buildtool.kurretsfabric.projectiles.Bullet;
import dev.buildtool.kurretsfabric.projectiles.Cobblestone;
import dev.buildtool.kurretsfabric.projectiles.GaussBullet;
import dev.buildtool.kurretsfabric.screenhandlers.*;
import dev.buildtool.kurretsfabric.turrets.*;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.fabric.impl.object.builder.FabricEntityType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.OreBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;

import java.util.List;

public class KTurrets implements ModInitializer, EntityComponentInitializer {
    public static final String ID = "k_turrets";
    public static final dev.buildtool.kurretsfabric.Config CONFIGURATION = dev.buildtool.kurretsfabric.Config.createAndLoad();
    public static Item gaussBullet;
    public static Item explosivePowder;

    static Item rawTitanium, titaniumIngot, arrowTurret, brickTurret, bulletTurret,
            cobbleTurret, fireballTurret, gaussTurret, arrowDrone, brickDrone, bulletDrone,
            cobbleDrone, fireballDrone, gaussDrone;
    static Block titaniumOre;
    static Block deepslateTitaniumOre;

    static {
        FabricItemGroupBuilder.create(new Identifier(ID, "everything")).appendItems(itemStacks -> {
            itemStacks.add(new ItemStack(rawTitanium));
            itemStacks.add(new ItemStack(titaniumIngot));
            itemStacks.add(new ItemStack(titaniumOre));
            itemStacks.add(new ItemStack(deepslateTitaniumOre));
            itemStacks.add(new ItemStack(cobbleTurret));
            itemStacks.add(new ItemStack(arrowTurret));
            itemStacks.add(new ItemStack(fireballTurret));
            itemStacks.add(new ItemStack(bulletTurret));
            itemStacks.add(new ItemStack(brickTurret));
            itemStacks.add(new ItemStack(gaussTurret));
            itemStacks.add(new ItemStack(cobbleDrone));
            itemStacks.add(new ItemStack(arrowDrone));
            itemStacks.add(new ItemStack(fireballDrone));
            itemStacks.add(new ItemStack(bulletDrone));
            itemStacks.add(new ItemStack(brickDrone));
            itemStacks.add(new ItemStack(gaussDrone));
            itemStacks.add(new ItemStack(gaussBullet));
            itemStacks.add(new ItemStack(explosivePowder));
        }).icon(() -> new ItemStack(gaussBullet)).build();
    }


    public static Identifier titaniumIngots = new Identifier("c", "titanium_ingots");

    public static EntityType<ArrowTurret> ARROW_TURRET;
    public static EntityType<Brick> BRICK;
    public static EntityType<BrickTurret> BRICK_TURRET;
    public static EntityType<BulletTurret> BULLET_TURRET;
    public static EntityType<Bullet> BULLET;
    public static EntityType<Cobblestone> COBBLESTONE;
    public static EntityType<CobbleTurret> COBBLE_TURRET;
    public static EntityType<FirechargeTurret> FIRE_CHARGE_TURRET;
    public static EntityType<GaussBullet> GAUSS_BULLET;
    public static EntityType<GaussTurret> GAUSS_TURRET;
    public static EntityType<ArrowDrone> ARROW_DRONE;
    public static EntityType<BrickDrone> BRICK_DRONE;
    public static EntityType<BulletDrone> BULLET_DRONE;
    public static EntityType<CobbleDrone> COBBLE_DRONE;
    public static EntityType<FireballDrone> FIREBALL_DRONE;
    public static EntityType<GaussDrone> GAUSS_DRONE;
    public static ScreenHandlerType<ArrowTurretScreenHandler> ARROW_TURRET_HANDLER;
    public static ScreenHandlerType<BrickTurretScreenHandler> BRICK_TURRET_HANDLER;
    public static ScreenHandlerType<BulletDroneScreenHandler> BULLET_DRONE_HANDLER;
    public static ScreenHandlerType<CobbleDroneScreenHandler> COBBLE_DRONE_HANDLER;
    public static ScreenHandlerType<BulletTurretScreenHandler> BULLET_TURRET_HANDLER;
    public static ScreenHandlerType<FireballDroneScreenHandler> FIREBALL_DRONE_HANDLER;
    public static ScreenHandlerType<CobbleTurretScreenHandler> COBBLE_TURRET_HANDLER;
    public static ScreenHandlerType<FireChargeTurretScreenHandler> FIRE_CHARGE_TURRET_HANDLER;
    public static ScreenHandlerType<GaussTurretScreenHandler> GAUSS_TURRET_HANDLER;
    public static ScreenHandlerType<ArrowDroneScreenHandler> ARROW_DRONE_HANDLER;
    public static ScreenHandlerType<BrickDroneScreenHandler> BRICK_DRONE_HANDLER;
    public static ScreenHandlerType<GaussDroneScreenHandler> GAUSS_DRONE_HANDLER;
    public static Identifier claim = new Identifier(ID, "claim");
    public static Identifier dismantle = new Identifier(ID, "dismantle");
    public static Identifier addPlayerException = new Identifier(ID, "add_exception");
    public static Identifier removePlayerException = new Identifier(ID, "remove_exception");
    public static Identifier toggleMobility = new Identifier(ID, "toggle_mobility");
    public static Identifier togglePlayerProtection = new Identifier(ID, "toggle_player_protection");
    public static Identifier toggleFollow = new Identifier(ID, "toggle_follow");
    public static Identifier targets = new Identifier(ID, "targets");
    public static SoundEvent BULLET_FIRE, GAUSS_BULLET_FIRE, COBBLE_FIRE, DRONE_PROPELLER;

    public static final ComponentKey<UnitLimits> UNIT_LIMITS = ComponentRegistry.getOrCreate(new Identifier(ID, "unit_limits"), UnitLimits.class);

    @Override
    public void onInitialize() {
        Identifier ore1 = new Identifier(ID, "titanium_ore");
        titaniumOre = Registry.register(Registry.BLOCK, ore1, new OreBlock(AbstractBlock.Settings.of(Material.STONE).requiresTool().strength(3, 3)));
        Identifier ore2 = new Identifier(ID, "deepslate_titanium_ore");
        deepslateTitaniumOre = Registry.register(Registry.BLOCK, ore2, new OreBlock(AbstractBlock.Settings.copy(titaniumOre).sounds(BlockSoundGroup.DEEPSLATE).luminance(value -> FabricLoader.getInstance().isDevelopmentEnvironment() ? 15 : 0)));
        Registry.register(Registry.ITEM, ore1, new BlockItem(titaniumOre, defaults()));
        Registry.register(Registry.ITEM, ore2, new BlockItem(deepslateTitaniumOre, defaults()));

        List<OreFeatureConfig.Target> blockTargets = List.of(OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, titaniumOre.getDefaultState()), OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, deepslateTitaniumOre.getDefaultState()));
        RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> configuredOres = ConfiguredFeatures.register("titanium_ore", Feature.ORE, new OreFeatureConfig(blockTargets, 11));
        PlacedFeatures.register("titanium_ore", configuredOres, List.of(CountPlacementModifier.of(29), HeightRangePlacementModifier.trapezoid(YOffset.getBottom(), YOffset.fixed(384))));

        RegistryKey<PlacedFeature> titaniumOreKey = RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier(ID, "titanium_ore"));
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, titaniumOreKey);

        registerEntities();

        registerItems();

        registerPackets();

        BULLET_FIRE = Registry.register(Registry.SOUND_EVENT, new Identifier(ID, "bullet"), new SoundEvent(new Identifier(ID, "bullet_shoot")));
        GAUSS_BULLET_FIRE = Registry.register(Registry.SOUND_EVENT, new Identifier(ID, "gauss_bullet"), new SoundEvent(new Identifier(ID, "gauss_shoot")));
        COBBLE_FIRE = Registry.register(Registry.SOUND_EVENT, new Identifier(ID, "cobble_fire"), new SoundEvent(new Identifier(ID, "cobble_shoot")));
        DRONE_PROPELLER = Registry.register(Registry.SOUND_EVENT, new Identifier(ID, "drone_propeller"), new SoundEvent(new Identifier(ID, "drone_fly")));
    }

    private void registerItems() {
        rawTitanium = Registry.register(Registry.ITEM, new Identifier(ID, "raw_titanium"), new Item(defaults()));
        titaniumIngot = Registry.register(Registry.ITEM, new Identifier(ID, "titanium_ingot"), new Item(defaults()));
        gaussBullet = Registry.register(Registry.ITEM, new Identifier(ID, "gauss_bullet"), new Item(defaults()));
        explosivePowder = Registry.register(Registry.ITEM, new Identifier(ID, "explosive_powder"), new Item(defaults()));

        arrowTurret = Registry.register(Registry.ITEM, new Identifier(ID, "arrow_turret_item"), new ContainerItem(ARROW_TURRET, 0, 0, defaults(), ContainerItem.Unit.TURRET));
        arrowDrone = Registry.register(Registry.ITEM, new Identifier(ID, "arrow_drone_item"), new ContainerItem(ARROW_DRONE, 0, 0, defaults(), ContainerItem.Unit.DRONE));
        brickTurret = Registry.register(Registry.ITEM, new Identifier(ID, "brick_turret_item"), new ContainerItem(BRICK_TURRET, 0, 0, defaults(), ContainerItem.Unit.TURRET));
        brickDrone = Registry.register(Registry.ITEM, new Identifier(ID, "brick_drone_item"), new ContainerItem(BRICK_DRONE, 0, 0, defaults(), ContainerItem.Unit.DRONE));
        bulletTurret = Registry.register(Registry.ITEM, new Identifier(ID, "bullet_turret_item"), new ContainerItem(BULLET_TURRET, 0, 0, defaults(), ContainerItem.Unit.TURRET));
        bulletDrone = Registry.register(Registry.ITEM, new Identifier(ID, "bullet_drone_item"), new ContainerItem(BULLET_DRONE, 0, 0, defaults(), ContainerItem.Unit.DRONE));
        cobbleTurret = Registry.register(Registry.ITEM, new Identifier(ID, "cobble_turret_item"), new ContainerItem(COBBLE_TURRET, 0, 0, defaults(), ContainerItem.Unit.TURRET));
        cobbleDrone = Registry.register(Registry.ITEM, new Identifier(ID, "cobble_drone_item"), new ContainerItem(COBBLE_DRONE, 0, 0, defaults(), ContainerItem.Unit.DRONE));
        fireballTurret = Registry.register(Registry.ITEM, new Identifier(ID, "firecharge_turret_item"), new ContainerItem(FIRE_CHARGE_TURRET, 0, 0, defaults(), ContainerItem.Unit.TURRET));
        fireballDrone = Registry.register(Registry.ITEM, new Identifier(ID, "firecharge_drone_item"), new ContainerItem(FIREBALL_DRONE, 0, 0, defaults(), ContainerItem.Unit.DRONE));
        gaussTurret = Registry.register(Registry.ITEM, new Identifier(ID, "gauss_turret_item"), new ContainerItem(GAUSS_TURRET, 0, 0, defaults(), ContainerItem.Unit.TURRET));
        gaussDrone = Registry.register(Registry.ITEM, new Identifier(ID, "gauss_drone_item"), new ContainerItem(GAUSS_DRONE, 0, 0, defaults(), ContainerItem.Unit.DRONE));
    }

    private Item.Settings defaults() {
        return new FabricItemSettings();
    }

    @SuppressWarnings({"SuspiciousNameCombination", "UnstableApiUsage"})
    private void registerEntities() {
        float droneWidth = 0.6f;
        Identifier arrowTurret = new Identifier(ID, "arrow_turret");
        ARROW_TURRET = Registry.register(Registry.ENTITY_TYPE, arrowTurret, new FabricEntityType<>((type, world) -> new ArrowTurret(world), SpawnGroup.MISC, true, true, false, false, ImmutableSet.of(), EntityDimensions.fixed(droneWidth, 0.8f), 5, 3, false));
        ARROW_TURRET_HANDLER = Registry.register(Registry.SCREEN_HANDLER, arrowTurret, new ExtendedScreenHandlerType<>(ArrowTurretScreenHandler::new));
        FabricDefaultAttributeRegistry.register(ARROW_TURRET, Turret.createDefaultAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, CONFIGURATION.arrowTurretRange()).add(EntityAttributes.GENERIC_ARMOR, CONFIGURATION.arrowTurretArmor()).add(EntityAttributes.GENERIC_MAX_HEALTH, CONFIGURATION.arrowTurretHealth()));

        Identifier brickTurret = new Identifier(ID, "brick_turret");
        BRICK_TURRET = Registry.register(Registry.ENTITY_TYPE, brickTurret, new FabricEntityType<>(BrickTurret::new, SpawnGroup.MISC, true, true, false, false, ImmutableSet.of(), EntityDimensions.fixed(0.7f, 0.7f), 5, 3, false));
        BRICK_TURRET_HANDLER = Registry.register(Registry.SCREEN_HANDLER, brickTurret, new ExtendedScreenHandlerType<>(BrickTurretScreenHandler::new));
        BRICK = Registry.register(Registry.ENTITY_TYPE, new Identifier(ID, "brick"), new FabricEntityType<>(Brick::new, SpawnGroup.MISC, true, false, false, false, ImmutableSet.of(), EntityDimensions.fixed(0.4f, 0.4f), 5, 1, false));
        FabricDefaultAttributeRegistry.register(BRICK_TURRET, Turret.createDefaultAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, CONFIGURATION.bulletTurretRange()).add(EntityAttributes.GENERIC_MAX_HEALTH, CONFIGURATION.bulletTurretHealth()).add(EntityAttributes.GENERIC_ARMOR, CONFIGURATION.brickTurretArmor()));

        Identifier bulletTurret = new Identifier(ID, "bullet_turret");
        BULLET_TURRET = Registry.register(Registry.ENTITY_TYPE, bulletTurret, new FabricEntityType<>((type, world) -> new BulletTurret(world), SpawnGroup.MISC, true, true, false, false, ImmutableSet.of(), EntityDimensions.fixed(0.8f, 0.8f), 5, 3, false));
        BULLET = Registry.register(Registry.ENTITY_TYPE, new Identifier(ID, "bullet"), new FabricEntityType<>((type, world) -> new Bullet(world), SpawnGroup.MISC, true, false, false, false, ImmutableSet.of(), EntityDimensions.fixed(0.2f, 0.2f), 5, 1, false));
        BULLET_TURRET_HANDLER = Registry.register(Registry.SCREEN_HANDLER, bulletTurret, new ExtendedScreenHandlerType<>(BulletTurretScreenHandler::new));
        FabricDefaultAttributeRegistry.register(BULLET_TURRET, Turret.createDefaultAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, CONFIGURATION.bulletTurretRange()).add(EntityAttributes.GENERIC_MAX_HEALTH, CONFIGURATION.bulletTurretHealth()).add(EntityAttributes.GENERIC_ARMOR, CONFIGURATION.bulletTurretArmor()));

        Identifier cobbleTurret = new Identifier(ID, "cobble_turret");
        COBBLE_TURRET_HANDLER = Registry.register(Registry.SCREEN_HANDLER, cobbleTurret, new ExtendedScreenHandlerType<>(CobbleTurretScreenHandler::new));
        COBBLESTONE = Registry.register(Registry.ENTITY_TYPE, new Identifier(ID, "cobblestone"), new FabricEntityType<>(Cobblestone::new, SpawnGroup.MISC, true, false, false, false, ImmutableSet.of(), EntityDimensions.fixed(0.25f, 0.25f), 5, 1, false));
        COBBLE_TURRET = Registry.register(Registry.ENTITY_TYPE, cobbleTurret, new FabricEntityType<>(CobbleTurret::new, SpawnGroup.MISC, true, true, false, false, ImmutableSet.of(), EntityDimensions.fixed(0.5f, 0.7f), 5, 3, false));
        FabricDefaultAttributeRegistry.register(COBBLE_TURRET, Turret.createDefaultAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, CONFIGURATION.cobbleTurretRange()).add(EntityAttributes.GENERIC_ARMOR, CONFIGURATION.cobbleTurretArmor()).add(EntityAttributes.GENERIC_MAX_HEALTH, CONFIGURATION.cobbleTurretHealth()));

        Identifier firechargeTurret = new Identifier(ID, "fireball_turret");
        FIRE_CHARGE_TURRET = Registry.register(Registry.ENTITY_TYPE, firechargeTurret, new FabricEntityType<>(FirechargeTurret::new, SpawnGroup.MISC, true, true, true, false, ImmutableSet.of(), EntityDimensions.fixed(0.8f, 0.7f), 5, 3, false));
        FabricDefaultAttributeRegistry.register(FIRE_CHARGE_TURRET, Turret.createDefaultAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, CONFIGURATION.fireChargeTurretRange()).add(EntityAttributes.GENERIC_ARMOR, CONFIGURATION.fireChargeTurretArmor()).add(EntityAttributes.GENERIC_MAX_HEALTH, CONFIGURATION.fireChargeTurretHealth()));
        FIRE_CHARGE_TURRET_HANDLER = Registry.register(Registry.SCREEN_HANDLER, firechargeTurret, new ExtendedScreenHandlerType<>(FireChargeTurretScreenHandler::new));

        Identifier gaussTurret = new Identifier(ID, "gauss_turret");
        GAUSS_TURRET = Registry.register(Registry.ENTITY_TYPE, gaussTurret, new FabricEntityType<>(GaussTurret::new, SpawnGroup.MISC, true, true, false, false, ImmutableSet.of(), EntityDimensions.fixed(0.8f, 1), 5, 3, false));
        FabricDefaultAttributeRegistry.register(GAUSS_TURRET, Turret.createDefaultAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, CONFIGURATION.gaussTurretRange()).add(EntityAttributes.GENERIC_ARMOR, CONFIGURATION.gaussTurretArmor()).add(EntityAttributes.GENERIC_MAX_HEALTH, CONFIGURATION.gaussTurretHealth()));
        GAUSS_BULLET = Registry.register(Registry.ENTITY_TYPE, new Identifier(ID, "gauss_bullet"), new FabricEntityType<>(GaussBullet::new, SpawnGroup.MISC, true, false, false, false, ImmutableSet.of(), EntityDimensions.fixed(0.2f, 0.2f), 5, 1, false));
        GAUSS_TURRET_HANDLER = Registry.register(Registry.SCREEN_HANDLER, gaussTurret, new ExtendedScreenHandlerType<>(GaussTurretScreenHandler::new));

        Identifier arrowDrone = new Identifier(ID, "arrow_drone");
        ARROW_DRONE = Registry.register(Registry.ENTITY_TYPE, arrowDrone, new FabricEntityType<>(ArrowDrone::new, SpawnGroup.MISC, true, true, false, false, ImmutableSet.of(), EntityDimensions.fixed(droneWidth, droneWidth), 5, 3, false));
        ARROW_DRONE_HANDLER = Registry.register(Registry.SCREEN_HANDLER, arrowDrone, new ExtendedScreenHandlerType<>(ArrowDroneScreenHandler::new));
        FabricDefaultAttributeRegistry.register(ARROW_DRONE, Turret.createDefaultAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, CONFIGURATION.arrowTurretRange() - 5).add(EntityAttributes.GENERIC_ARMOR, Math.max(CONFIGURATION.arrowTurretArmor() - 2, 0)).add(EntityAttributes.GENERIC_MAX_HEALTH, Math.max(10, CONFIGURATION.arrowTurretHealth() - 15)));

        Identifier brickDrone = new Identifier(ID, "brick_drone");
        BRICK_DRONE = Registry.register(Registry.ENTITY_TYPE, brickDrone, new FabricEntityType<>(BrickDrone::new, SpawnGroup.MISC, true, true, false, false, ImmutableSet.of(), EntityDimensions.fixed(droneWidth, droneWidth), 5, 3, false));
        BRICK_DRONE_HANDLER = Registry.register(Registry.SCREEN_HANDLER, brickDrone, new ExtendedScreenHandlerType<>(BrickDroneScreenHandler::new));
        FabricDefaultAttributeRegistry.register(BRICK_DRONE, Turret.createDefaultAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, CONFIGURATION.bulletTurretRange() - 5).add(EntityAttributes.GENERIC_MAX_HEALTH, Math.max(10, CONFIGURATION.brickTurretHealth() - 15)).add(EntityAttributes.GENERIC_ARMOR, Math.max(0, CONFIGURATION.brickTurretArmor() - 2)));

        Identifier bulletDrone = new Identifier(ID, "bullet_drone");
        BULLET_DRONE = Registry.register(Registry.ENTITY_TYPE, bulletDrone, new FabricEntityType<>(BulletDrone::new, SpawnGroup.MISC, true, true, false, false, ImmutableSet.of(), EntityDimensions.fixed(droneWidth, droneWidth), 5, 3, false));
        BULLET_DRONE_HANDLER = Registry.register(Registry.SCREEN_HANDLER, bulletDrone, new ExtendedScreenHandlerType<>(BulletDroneScreenHandler::new));
        FabricDefaultAttributeRegistry.register(BULLET_DRONE, Turret.createDefaultAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, CONFIGURATION.bulletTurretRange() - 5).add(EntityAttributes.GENERIC_MAX_HEALTH, Math.max(10, CONFIGURATION.bulletTurretHealth() - 15)).add(EntityAttributes.GENERIC_ARMOR, Math.max(0, CONFIGURATION.bulletTurretArmor() - 2)));

        Identifier cobbleDrone = new Identifier(ID, "cobble_drone");
        COBBLE_DRONE = Registry.register(Registry.ENTITY_TYPE, cobbleDrone, new FabricEntityType<>(CobbleDrone::new, SpawnGroup.MISC, true, true, false, false, ImmutableSet.of(), EntityDimensions.fixed(droneWidth, droneWidth), 5, 3, false));
        COBBLE_DRONE_HANDLER = Registry.register(Registry.SCREEN_HANDLER, cobbleDrone, new ExtendedScreenHandlerType<>(CobbleDroneScreenHandler::new));
        FabricDefaultAttributeRegistry.register(COBBLE_DRONE, Turret.createDefaultAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, CONFIGURATION.cobbleTurretRange() - 5).add(EntityAttributes.GENERIC_ARMOR, Math.max(0, CONFIGURATION.cobbleTurretArmor() - 2)).add(EntityAttributes.GENERIC_MAX_HEALTH, Math.max(10, CONFIGURATION.cobbleTurretHealth() - 15)));

        Identifier fireballDrone = new Identifier(ID, "fireball_drone");
        FIREBALL_DRONE = Registry.register(Registry.ENTITY_TYPE, fireballDrone, new FabricEntityType<>(FireballDrone::new, SpawnGroup.MISC, true, true, true, false, ImmutableSet.of(), EntityDimensions.fixed(droneWidth, droneWidth), 5, 3, false));
        FIREBALL_DRONE_HANDLER = Registry.register(Registry.SCREEN_HANDLER, fireballDrone, new ExtendedScreenHandlerType<>(FireballDroneScreenHandler::new));
        FabricDefaultAttributeRegistry.register(FIREBALL_DRONE, Turret.createDefaultAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, CONFIGURATION.fireChargeTurretRange() - 5).add(EntityAttributes.GENERIC_MAX_HEALTH, Math.max(10, CONFIGURATION.fireChargeTurretHealth() - 15)).add(EntityAttributes.GENERIC_ARMOR, Math.max(0, CONFIGURATION.fireChargeTurretArmor() - 2)));

        Identifier gaussDrone = new Identifier(ID, "gauss_drone");
        GAUSS_DRONE = Registry.register(Registry.ENTITY_TYPE, gaussDrone, new FabricEntityType<>(GaussDrone::new, SpawnGroup.MISC, true, true, false, false, ImmutableSet.of(), EntityDimensions.fixed(droneWidth, droneWidth), 5, 3, false));
        GAUSS_DRONE_HANDLER = Registry.register(Registry.SCREEN_HANDLER, gaussDrone, new ExtendedScreenHandlerType<>(GaussDroneScreenHandler::new));
        FabricDefaultAttributeRegistry.register(GAUSS_DRONE, Turret.createDefaultAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, CONFIGURATION.gaussTurretRange() - 5).add(EntityAttributes.GENERIC_ARMOR, Math.max(0, CONFIGURATION.gaussTurretArmor() - 2)).add(EntityAttributes.GENERIC_MAX_HEALTH, Math.max(10, CONFIGURATION.gaussTurretHealth() - 15)));
    }

    private void registerPackets() {
        ServerPlayNetworking.registerGlobalReceiver(addPlayerException, (server, player, handler, buf, responseSender) -> {
            Entity entity = player.world.getEntityById(buf.readInt());
            if (entity instanceof Turret turret) {
                turret.addPlayerToExceptions(buf.readString());
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(removePlayerException, (server, player, handler, buf, responseSender) -> {
            Entity entity = player.world.getEntityById(buf.readInt());
            if (entity instanceof Turret turret) {
                turret.removePlayerFromExceptions(buf.readString());
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(dismantle, (server, player, handler, buf, responseSender) -> {
            Entity entity = player.world.getEntityById(buf.readInt());
            if (entity instanceof Turret turret) {
                turret.getContainedItems().forEach(defaultInventory -> ItemScatterer.spawn(player.world, turret.getBlockPos(), defaultInventory));
                turret.discard();
                ItemStack itemStack = new ItemStack(turret.getSpawnItem());
                itemStack.getOrCreateNbt().put("Contained", turret.writeNbt(new NbtCompound()));
                itemStack.getNbt().putUuid("UUID", turret.getUuid());
                player.world.spawnEntity(new ItemEntity(player.world, turret.getX(), turret.getY(), turret.getZ(), itemStack));
                UnitLimits unitLimits = UNIT_LIMITS.get(player);
                if (turret instanceof Drone)
                    unitLimits.decreaseDroneCount();
                else unitLimits.decreaseTurretCount();
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(toggleMobility, (server, player, handler, buf, responseSender) -> {
            Entity entity = player.world.getEntityById(buf.readInt());
            if (entity instanceof Turret turret) {
                turret.setMobile(buf.readBoolean());
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(togglePlayerProtection, (server, player, handler, buf, responseSender) -> {
            Entity entity = player.world.getEntityById(buf.readInt());
            if (entity instanceof Turret turret) {
                turret.setProtectingFromPlayers(buf.readBoolean());
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(claim, (server, player, handler, buf, responseSender) -> {
            Entity entity = player.world.getEntityById(buf.readInt());
            if (entity instanceof Turret turret) {
                turret.setOwner(buf.readUuid());
                turret.setOwnerName(player.getName().getString());
                if (turret instanceof Drone)
                    player.sendMessage(Text.translatable("k_turrets.drone_claimed"), true);
                else
                    player.sendMessage(Text.translatable("k_turrets.turret_claimed"), true);
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(toggleFollow, (server, player, handler, buf, responseSender) -> {
            Entity entity = player.world.getEntityById(buf.readInt());
            if (entity instanceof Drone drone) {
                drone.setFollowingOwner(buf.readBoolean());
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(targets, (server, player, handler, buf, responseSender) -> {
            Entity entity = player.world.getEntityById(buf.readInt());
            if (entity instanceof Turret turret) {
                turret.setTargets(buf.readNbt());
            }
        });
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(PlayerEntity.class, UNIT_LIMITS, playerEntity -> new UnitLimits());
    }
}
