package pedrixzz.legendaryboots;

import pedrixzz.legendaryboots.item.*;
import pedrixzz.legendaryboots.network.ModPackets;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.function.Function;

public class LegendaryBoots implements ModInitializer {
    public static final String MOD_ID = "legendaryboots";

    // --- REGISTRO DE ITENS ---
    // Agora usamos 'registerItem' passando o construtor (Ex: SoulMirrorItem::new) em vez do objeto já criado

    public static final Item SOUL_MIRROR = registerItem("soul_mirror", SoulMirrorItem::new, 
            new Item.Settings().rarity(Rarity.EPIC).maxCount(1));

    public static final Item VOID_CLOAK = registerItem("void_cloak", VoidCloakItem::new, 
            new Item.Settings().rarity(Rarity.EPIC).maxCount(1));

    public static final Item ASCENSION_FEATHER = registerItem("ascension_feather", AscensionFeatherItem::new, 
            new Item.Settings().rarity(Rarity.UNCOMMON).maxCount(1));

    public static final Item THUNDER_HAMMER = registerItem("thunder_hammer", ThunderHammerItem::new, 
            new Item.Settings().rarity(Rarity.RARE).maxCount(1));

    public static final Item SWAP_SCEPTER = registerItem("swap_scepter", SwapScepterItem::new, 
            new Item.Settings().rarity(Rarity.RARE).maxDamage(50));

    // --- MÉTODO AUXILIAR CORRIGIDO PARA 1.21.11 ---
    private static Item registerItem(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        // 1. Cria o ID e a Chave de Registro
        Identifier id = Identifier.of(MOD_ID, name);
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);

        // 2. OBRIGATÓRIO: Define a chave nas configurações ANTES de criar o item
        settings.registryKey(key);

        // 3. Cria o item usando a fábrica (o construtor)
        Item item = itemFactory.apply(settings);

        // 4. Registra no jogo
        return Registry.register(Registries.ITEM, key, item);
    }

    @Override
    public void onInitialize() {
        // Inicializa os pacotes de rede
        ModPackets.registerC2SPackets();
    }
}