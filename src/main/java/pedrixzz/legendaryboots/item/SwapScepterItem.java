package pedrixzz.legendaryboots.item;

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
import net.minecraft.util.math.Vec3d;

public class SwapScepterItem extends Item {
    public SwapScepterItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand) {
        // CORREÇÃO: Usando world (campo) ou getEntityWorld se getWorld falhar
        if (!player.getEntityWorld().isClient()) {
            Vec3d playerPos = player.getPos(); // Se der erro, tente .getX(), .getY()...
            Vec3d targetPos = entity.getPos();

            if (player instanceof ServerPlayerEntity serverPlayer) {
                serverPlayer.requestTeleport(targetPos.x, targetPos.y, targetPos.z);
            } else {
                player.teleport(targetPos.x, targetPos.y, targetPos.z, false);
            }

            entity.teleport(playerPos.x, playerPos.y, playerPos.z, false);

            player.getEntityWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);
            player.sendMessage(Text.literal("§bTroca realizada!"), true);
            
            stack.damage(1, player, LivingEntity.getSlotForHand(hand));
            player.getItemCooldownManager().set(this, 60);
            
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
}