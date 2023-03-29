package dev.buildtool.kurretsfabric;

import com.google.common.collect.ImmutableSet;
import dev.buildtool.kurretsfabric.projectiles.Brick;
import dev.buildtool.kurretsfabric.projectiles.Bullet;
import dev.buildtool.kurretsfabric.projectiles.Cobblestone;
import dev.buildtool.kurretsfabric.projectiles.GaussBullet;
import dev.buildtool.kurretsfabric.screenhandlers.*;
import dev.buildtool.kurretsfabric.turrets.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.fabric.impl.object.builder.FabricEntityType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.OreBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
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

public class KTurrets implements ModInitializer {
    public static final String ID = "k_turrets";
    public static final dev.buildtool.kurretsfabric.Config CONFIGURATION = dev.buildtool.kurretsfabric.Config.createAndLoad();
    public static Item gaussBullet;
    public static Item explosivePowder;
    ItemGroup itemGroup = new ItemGroup(0, ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(gaussBullet);
        }
    };

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
    public static ScreenHandlerType<ArrowTurretScreenHandler> ARROW_TURRET_HANDLER;
    public static ScreenHandlerType<BrickTurretScreenHandler> BRICK_TURRET_HANDLER;
    public static ScreenHandlerType<BulletTurretScreenHandler> BULLET_TURRET_HANDLER;
    public static ScreenHandlerType<CobbleScreenHandler> COBBLE_TURRET_HANDLER;
    public static ScreenHandlerType<FireChargeTurretScreenHandler> FIRE_CHARGE_TURRET_HANDLER;
    public static ScreenHandlerType<GaussTurretHandler> GAUSS_TURRET_HANDLER;
    public static Identifier claim = new Identifier(ID, "claim");
    public static Identifier dismantle = new Identifier(ID, "dismantle");
    public static Identifier addPlayerException = new Identifier(ID, "add_exception");
    public static Identifier removePlayerException = new Identifier(ID, "remove_exception");
    public static Identifier toggleMobility = new Identifier(ID, "toggle_mobility");
    public static Identifier togglePlayerProtection = new Identifier(ID, "toggle_player_protection");
    public static Identifier toggleFollow = new Identifier(ID, "toggle_follow");
    public static Identifier targets = new Identifier(ID, "targets");
    public static SoundEvent BULLET_FIRE, GAUSS_BULLET_FIRE, COBBLE_FIRE, DRONE_PROPELLER;

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onInitialize() {
        Identifier ore1 = new Identifier(ID, "titanium_ore");
        Block titaniumOre = Registry.register(Registry.BLOCK, ore1, new OreBlock(AbstractBlock.Settings.of(Material.STONE).requiresTool().strength(3, 3)));
        Identifier ore2 = new Identifier(ID, "deepslate_titanium_ore");
        Block deepslateTitaniumOre = Registry.register(Registry.BLOCK, ore2, new OreBlock(AbstractBlock.Settings.copy(titaniumOre).sounds(BlockSoundGroup.DEEPSLATE)));
        Registry.register(Registry.ITEM, ore1, new BlockItem(titaniumOre, defaults()));
        Registry.register(Registry.ITEM, ore2, new BlockItem(deepslateTitaniumOre, defaults()));

        List<OreFeatureConfig.Target> blockTargets = List.of(OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, titaniumOre.getDefaultState()), OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, deepslateTitaniumOre.getDefaultState()));
        RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> configuredOres = ConfiguredFeatures.register("titanium_ore", Feature.ORE, new OreFeatureConfig(blockTargets, 11));
        PlacedFeatures.register("titanium_ore", configuredOres, List.of(CountPlacementModifier.of(35), HeightRangePlacementModifier.trapezoid(YOffset.getBottom(), YOffset.fixed(384))));

        RegistryKey<PlacedFeature> titaniumOreKey = RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier(ID, "titanium_ore"));
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, titaniumOreKey);

        registerEntities();

        Registry.register(Registry.ITEM, new Identifier(ID, "titanium_ingot"), new Item(defaults()));
        gaussBullet = Registry.register(Registry.ITEM, new Identifier(ID, "gauss_bullet"), new Item(defaults()));
        explosivePowder = Registry.register(Registry.ITEM, new Identifier(ID, "explosive_powder"), new Item(defaults()));

        registerPackets();

        BULLET_FIRE = Registry.register(Registry.SOUND_EVENT, new Identifier(ID, "bullet"), new SoundEvent(new Identifier(ID, "bullet_shoot")));
        GAUSS_BULLET_FIRE = Registry.register(Registry.SOUND_EVENT, new Identifier(ID, "gauss_bullet"), new SoundEvent(new Identifier(ID, "gauss_shoot")));
        COBBLE_FIRE = Registry.register(Registry.SOUND_EVENT, new Identifier(ID, "cobble_fire"), new SoundEvent(new Identifier(ID, "cobble_shoot")));
        DRONE_PROPELLER = Registry.register(Registry.SOUND_EVENT, new Identifier(ID, "drone_propeller"), new SoundEvent(new Identifier(ID, "drone_fly")));
    }

    private Item.Settings defaults() {
        return new Item.Settings().group(itemGroup);
    }

    private void registerEntities() {
        float droneWidth = 0.6f;
        Identifier arrowTurret = new Identifier(ID, "arrow_turret");
        ARROW_TURRET = Registry.register(Registry.ENTITY_TYPE, arrowTurret, new FabricEntityType<>((type, world) -> new ArrowTurret(world), SpawnGroup.MISC, true, true, false, false, ImmutableSet.of(), EntityDimensions.fixed(droneWidth, 0.8f), 5, 3, false));
        ARROW_TURRET_HANDLER = Registry.register(Registry.SCREEN_HANDLER, arrowTurret, new ExtendedScreenHandlerType<>(ArrowTurretScreenHandler::new));
        Registry.register(Registry.ITEM, new Identifier(ID, "arrow_turret_item"), new ContainerItem(ARROW_TURRET, 0x0, 0x0, defaults(), ContainerItem.Unit.TURRET));
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
        COBBLE_TURRET_HANDLER = Registry.register(Registry.SCREEN_HANDLER, cobbleTurret, new ExtendedScreenHandlerType<>(CobbleScreenHandler::new));
        COBBLESTONE = Registry.register(Registry.ENTITY_TYPE, new Identifier(ID, "cobblestone"), new FabricEntityType<>(Cobblestone::new, SpawnGroup.MISC, true, false, false, false, ImmutableSet.of(), EntityDimensions.fixed(0.25f, 0.25f), 5, 1, false));
        COBBLE_TURRET = Registry.register(Registry.ENTITY_TYPE, cobbleTurret, new FabricEntityType<>(CobbleTurret::new, SpawnGroup.MISC, true, true, false, false, ImmutableSet.of(), EntityDimensions.fixed(0.5f, 0.7f), 5, 3, false));
        FabricDefaultAttributeRegistry.register(COBBLE_TURRET, Turret.createDefaultAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, CONFIGURATION.cobbleTurretRange()).add(EntityAttributes.GENERIC_ARMOR, CONFIGURATION.cobbleTurretArmor()).add(EntityAttributes.GENERIC_MAX_HEALTH, CONFIGURATION.cobbleTurretHealth()));

        Identifier firechargeTurret = new Identifier(ID, "firecharge_turret");
        FIRE_CHARGE_TURRET = Registry.register(Registry.ENTITY_TYPE, firechargeTurret, new FabricEntityType<>(FirechargeTurret::new, SpawnGroup.MISC, true, true, true, false, ImmutableSet.of(), EntityDimensions.fixed(0.8f, 0.7f), 5, 3, false));
        FabricDefaultAttributeRegistry.register(FIRE_CHARGE_TURRET, Turret.createDefaultAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, CONFIGURATION.fireChargeTurretRange()).add(EntityAttributes.GENERIC_ARMOR, CONFIGURATION.fireChargeTurretArmor()).add(EntityAttributes.GENERIC_MAX_HEALTH, CONFIGURATION.fireChargeTurretHealth()));
        FIRE_CHARGE_TURRET_HANDLER = Registry.register(Registry.SCREEN_HANDLER, firechargeTurret, new ExtendedScreenHandlerType<>(FireChargeTurretScreenHandler::new));

        Identifier gaussTurret = new Identifier(ID, "gauss_turret");
        GAUSS_TURRET = Registry.register(Registry.ENTITY_TYPE, gaussTurret, new FabricEntityType<>(GaussTurret::new, SpawnGroup.MISC, true, true, false, false, ImmutableSet.of(), EntityDimensions.fixed(0.8f, 1), 5, 3, false));
        FabricDefaultAttributeRegistry.register(GAUSS_TURRET, Turret.createDefaultAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, CONFIGURATION.gaussTurretRange()).add(EntityAttributes.GENERIC_ARMOR, CONFIGURATION.gaussTurretArmor()).add(EntityAttributes.GENERIC_MAX_HEALTH, CONFIGURATION.gaussTurretHealth()));
        GAUSS_BULLET = Registry.register(Registry.ENTITY_TYPE, new Identifier(ID, "gauss_bullet"), new FabricEntityType<>(GaussBullet::new, SpawnGroup.MISC, true, false, false, false, ImmutableSet.of(), EntityDimensions.fixed(0.2f, 0.2f), 5, 1, false));
        GAUSS_TURRET_HANDLER = Registry.register(Registry.SCREEN_HANDLER, gaussTurret, new ExtendedScreenHandlerType<>(GaussTurretHandler::new));
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
                //TODO unit limits
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(toggleMobility, (server, player, handler, buf, responseSender) -> {
            Entity entity = player.world.getEntityById(buf.readInt());
            if (entity instanceof Turret turret) {
                turret.setPushable(buf.readBoolean());
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
}
