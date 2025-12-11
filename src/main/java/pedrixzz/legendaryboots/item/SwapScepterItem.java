package pedrixzz.legendaryboots.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class SwapScepterItem extends Item {
    public SwapScepterItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand) {
        // CORREÇÃO: Adicionado ()
        if (!player.getEntityWorld().isClient()) {
            double pX = player.getX();
            double pY = player.getY();
            double pZ = player.getZ();

            double tX = entity.getX();
            double tY = entity.getY();
            double tZ = entity.getZ();

            if (player instanceof ServerPlayerEntity serverPlayer) {
                serverPlayer.requestTeleport(tX, tY, tZ);
            } else {
                player.teleport(tX, tY, tZ, false);
            }

            entity.teleport(pX, pY, pZ, false);

            player.getEntityWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);
            player.sendMessage(Text.literal("§bTroca realizada!"), true);
            
            EquipmentSlot slot = (hand == Hand.MAIN_HAND) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
            stack.damage(1, player, slot);
            
            // CORREÇÃO: Passando o ItemStack 'stack'
            player.getItemCooldownManager().set(stack, 60);
            
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
}