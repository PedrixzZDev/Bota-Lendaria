package pedrixzz.legendaryboots;

import pedrixzz.legendaryboots.item.*;
import pedrixzz.legendaryboots.network.ModPackets;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class LegendaryBoots implements ModInitializer {
    public static final String MOD_ID = "legendaryboots";

    // Registrar Itens
    public static final Item SOUL_MIRROR = registerItem("soul_mirror", new SoulMirrorItem(new Item.Settings().rarity(Rarity.EPIC).maxCount(1)));
    public static final Item VOID_CLOAK = registerItem("void_cloak", new VoidCloakItem(new Item.Settings().rarity(Rarity.EPIC).maxCount(1)));
    public static final Item ASCENSION_FEATHER = registerItem("ascension_feather", new AscensionFeatherItem(new Item.Settings().rarity(Rarity.UNCOMMON).maxCount(1)));
    public static final Item THUNDER_HAMMER = registerItem("thunder_hammer", new ThunderHammerItem(new Item.Settings().rarity(Rarity.RARE).maxCount(1)));
    public static final Item SWAP_SCEPTER = registerItem("swap_scepter", new SwapScepterItem(new Item.Settings().rarity(Rarity.RARE).maxDamage(50)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(MOD_ID, name), item);
    }

    @Override
    public void onInitialize() {
        ModPackets.registerC2SPackets();
    }
}