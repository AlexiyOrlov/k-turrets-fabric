package dev.buildtool.kurretsfabric;

import com.google.common.collect.ImmutableSet;
import dev.buildtool.kurretsfabric.screenhandlers.ArrowTurretScreenHandler;
import dev.buildtool.kurretsfabric.turrets.ArrowTurret;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.fabric.impl.object.builder.FabricEntityType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.OreBlock;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;

import java.util.List;

public class KTurrets implements ModInitializer {
    public static String ID = "k_turrets";
    Item gaussBullet;
    ItemGroup itemGroup = new ItemGroup(0, ID) {
        @Override
        public ItemStack createIcon() {
            return null;
        }
    };

    public static Identifier titaniumIngots = new Identifier("c", "titanium_ingots");

    public static EntityType<ArrowTurret> ARROW_TURRET;

    public static ScreenHandlerType<ArrowTurretScreenHandler> ARROW_TURRET_HANDLER;

    @Override
    public void onInitialize() {
        MidnightConfig.init(ID, Configuration.class);
        Identifier ore1 = new Identifier(ID, "titanium_ore");
        Block titaniumOre = Registry.register(Registry.BLOCK, ore1, new OreBlock(AbstractBlock.Settings.of(Material.STONE).requiresTool().strength(3, 3)));
        Identifier ore2 = new Identifier(ID, "deepslate_titanium_ore");
        Block deepslateTitaniumOre = Registry.register(Registry.BLOCK, ore2, new OreBlock(AbstractBlock.Settings.copy(titaniumOre).sounds(BlockSoundGroup.DEEPSLATE)));
        Registry.register(Registry.ITEM, ore1, new BlockItem(titaniumOre, defaults()));
        Registry.register(Registry.ITEM, ore2, new BlockItem(deepslateTitaniumOre, defaults()));

        List<OreFeatureConfig.Target> blockTargets = List.of(OreFeatureConfig.createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, titaniumOre.getDefaultState()), OreFeatureConfig.createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, deepslateTitaniumOre.getDefaultState()));
        RegistryEntry<ConfiguredFeature<OreFeatureConfig, ?>> configuredOres = ConfiguredFeatures.register("titanium_ore", Feature.ORE, new OreFeatureConfig(blockTargets, 11));
        PlacedFeatures.register("titanium_ore", configuredOres, List.of(CountPlacementModifier.of(35), HeightRangePlacementModifier.trapezoid(YOffset.getBottom(), YOffset.fixed(384))));

        float droneWidth = 0.6f;
        Identifier arrowTurret = new Identifier(ID, "arrow_turret");
        ARROW_TURRET = Registry.register(Registry.ENTITY_TYPE, arrowTurret, new FabricEntityType<>((type, world) -> new ArrowTurret(world), SpawnGroup.MISC, true, true, false, false, ImmutableSet.of(), EntityDimensions.fixed(droneWidth, 0.8f), 5, 3, false));
        ARROW_TURRET_HANDLER = Registry.register(Registry.SCREEN_HANDLER, arrowTurret, new ExtendedScreenHandlerType<>(ArrowTurretScreenHandler::new));
        Registry.register(Registry.ITEM, new Identifier(ID, "arrow_turret_item"), new ContainerItem(ARROW_TURRET, 0x0, 0x0, defaults(), ContainerItem.Unit.TURRET));

        Registry.register(Registry.ITEM, new Identifier(ID, "titanium_ingot"), new Item(defaults()));
        gaussBullet = Registry.register(Registry.ITEM, new Identifier(ID, "gauss_bullet"), new Item(defaults()));

    }

    private Item.Settings defaults() {
        return new Item.Settings().group(itemGroup);
    }
}
