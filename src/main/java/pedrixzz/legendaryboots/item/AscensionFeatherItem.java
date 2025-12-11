package pedrixzz.legendaryboots.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.InteractionResultHolder;
import net.minecraft.world.World;

public class AscensionFeatherItem extends Item {
    public AscensionFeatherItem(Settings settings) {
        super(settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClient()) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 60, 4));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 140, 0));
            
            // CORREÇÃO: O cooldown agora exige um ItemStack, não um Item
            player.getItemCooldownManager().set(player.getStackInHand(hand), 300);
        }
        return InteractionResultHolder.success(player.getStackInHand(hand));
    }
}