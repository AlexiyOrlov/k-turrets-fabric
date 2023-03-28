package dev.buildtool.kurretsfabric;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.OreBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
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
    ItemGroup itemGroup = new ItemGroup(0, ID) {
        @Override
        public ItemStack createIcon() {
            return null;
        }
    };

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

    }

    private Item.Settings defaults() {
        return new Item.Settings().group(itemGroup);
    }
}
