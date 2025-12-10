package pedrixzz.legendaryboots.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import java.util.List;

public class VoidCloakItem extends Item {
    public VoidCloakItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClient && player instanceof ServerPlayerEntity serverPlayer) {
            // Lógica simplificada de toggle usando tag customizada no player
            boolean isHidden = player.getCommandTags().contains("reliquias.hidden");
            
            if (!isHidden) {
                // ESCONDER
                player.addCommandTag("reliquias.hidden");
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 999999, 0, false, false));
                player.setInvisible(true);
                
                // Hack para remover do TAB
                var removePacket = new PlayerListS2CPacket(PlayerListS2CPacket.Action.UPDATE_LISTED, serverPlayer);
                // Nota: Manipular o TAB perfeitamente no Fabric requer mixins complexos no PlayerList.
                // Vou usar invisibilidade total aqui.
                
                player.sendMessage(Text.literal("§8[Vazio] Você desapareceu."), true);
            } else {
                // REAPARECER
                player.removeCommandTag("reliquias.hidden");
                player.removeStatusEffect(StatusEffects.INVISIBILITY);
                player.setInvisible(false);
                
                player.sendMessage(Text.literal("§f[Vazio] Você retornou."), true);
            }
        }
        return TypedActionResult.success(player.getStackInHand(hand));
    }
}