package pedrixzz.legendaryboots.item;

import pedrixzz.legendaryboots.client.gui.SoulMirrorScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult; // Importar a Interface
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class SoulMirrorItem extends Item {
    public SoulMirrorItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity player, Hand hand) {
        if (world.isClient) {
            MinecraftClient.getInstance().setScreen(new SoulMirrorScreen());
        }
        // Não precisa mais retornar o ItemStack, apenas o resultado
        return ActionResult.SUCCESS;
    }
}