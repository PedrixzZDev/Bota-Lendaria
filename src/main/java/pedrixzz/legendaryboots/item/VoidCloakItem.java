package pedrixzz.legendaryboots.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class VoidCloakItem extends Item {
    public VoidCloakItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity player, Hand hand) {
        // CORREÇÃO: Adicionado ()
        if (!world.isClient() && player instanceof ServerPlayerEntity serverPlayer) {
            boolean isHidden = player.getCommandTags().contains("reliquias.hidden");
            
            if (!isHidden) {
                player.addCommandTag("reliquias.hidden");
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 999999, 0, false, false));
                player.setInvisible(true);
                
                var removePacket = new PlayerListS2CPacket(PlayerListS2CPacket.Action.UPDATE_LISTED, serverPlayer);
                serverPlayer.networkHandler.sendPacket(removePacket);
                
                player.sendMessage(Text.literal("§8[Vazio] Você desapareceu."), true);
            } else {
                player.removeCommandTag("reliquias.hidden");
                player.removeStatusEffect(StatusEffects.INVISIBILITY);
                player.setInvisible(false);
                
                player.sendMessage(Text.literal("§f[Vazio] Você retornou."), true);
            }
        }
        return ActionResult.SUCCESS;
    }
}