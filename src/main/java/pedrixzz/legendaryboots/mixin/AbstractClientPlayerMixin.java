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

    // CORREÇÃO: Removida a assinatura complexa. Usar apenas o nome do método.
    // O Fabric Loom vai gerar o mapa de referência (refmap) automaticamente para encontrar o método certo.
    @Inject(method = "getSkinTextures", at = @At("HEAD"), cancellable = true)
    private void stealSkin(CallbackInfoReturnable<SkinTextures> cir) {
        if (this instanceof IDisguiseAccessor accessor) {
            // Verifica se o DataTracker está inicializado para evitar crash no construtor
            if (this.getDataTracker() == null) return;

            // Tenta pegar o ID. Se falhar, ignora.
            try {
                String disguiseName = this.getDataTracker().get(accessor.getDisguiseDataId());

                if (disguiseName != null && !disguiseName.isEmpty()) {
                    if (MinecraftClient.getInstance().getNetworkHandler() != null) {
                        PlayerListEntry targetInfo = MinecraftClient.getInstance().getNetworkHandler().getPlayerListEntry(disguiseName);
                        if (targetInfo != null) {
                            cir.setReturnValue(targetInfo.getSkinTextures());
                        }
                    }
                }
            } catch (Exception e) {
                // Silencia erros durante a renderização para não crashar o jogo por causa de skin
            }
        }
    }
}
