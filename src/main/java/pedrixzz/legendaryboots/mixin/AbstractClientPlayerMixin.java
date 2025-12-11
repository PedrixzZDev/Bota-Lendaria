package pedrixzz.legendaryboots.mixin;

import pedrixzz.legendaryboots.util.IDisguiseAccessor;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.SkinTextures; 
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerMixin extends PlayerEntity {

    public AbstractClientPlayerMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, gameProfile);
    }

    // CORREÇÃO: Adicionei a assinatura completa ()Lnet/minecraft/entity/player/SkinTextures;
    // Isso ajuda o remapper a encontrar o método exato mesmo se houver confusão de nomes.
    @Inject(method = "getSkinTextures()Lnet/minecraft/entity/player/SkinTextures;", at = @At("HEAD"), cancellable = true)
    private void stealSkin(CallbackInfoReturnable<SkinTextures> cir) {
        if (this instanceof IDisguiseAccessor accessor) {
            String disguiseName = this.getDataTracker().get(accessor.getDisguiseDataId());

            if (disguiseName != null && !disguiseName.isEmpty()) {
                if (MinecraftClient.getInstance().getNetworkHandler() != null) {
                    PlayerListEntry targetInfo = MinecraftClient.getInstance().getNetworkHandler().getPlayerListEntry(disguiseName);
                    if (targetInfo != null) {
                        cir.setReturnValue(targetInfo.getSkinTextures());
                    }
                }
            }
        }
    }
}