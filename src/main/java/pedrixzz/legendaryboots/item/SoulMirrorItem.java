package pedrixzz.legendaryboots.item;

import pedrixzz.legendaryboots.client.gui.SoulMirrorScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult; // Garante que está importado
import net.minecraft.world.World;

public class SoulMirrorItem extends Item {
    public SoulMirrorItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        // CORREÇÃO: Uso de parenteses .isClient()
        if (world.isClient()) {
            MinecraftClient.getInstance().setScreen(new SoulMirrorScreen());
        }
        return TypedActionResult.success(player.getStackInHand(hand));
    }
}