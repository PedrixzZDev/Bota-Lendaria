package pedrixzz.legendaryboots.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.sound.SoundCategory;
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
        if (!player.getWorld().isClient) {
            Vec3d playerPos = player.getPos();
            Vec3d targetPos = entity.getPos();

            player.teleport(targetPos.x, targetPos.y, targetPos.z);
            entity.teleport(playerPos.x, playerPos.y, playerPos.z);

            player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);
            player.sendMessage(Text.literal("§bTroca realizada!"), true);
            stack.damage(1, player, LivingEntity.getSlotForHand(hand));
            player.getItemCooldownManager().set(this, 60);
            
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
}