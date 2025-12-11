package pedrixzz.legendaryboots.mixin;

import pedrixzz.legendaryboots.util.IDisguiseAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.SkinTextures;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin extends LivingEntity implements IDisguiseAccessor {

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    private static final TrackedData<String> DISGUISE_NAME = 
        DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.STRING);

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void initDisguiseData(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(DISGUISE_NAME, "");
    }

    @Override
    public TrackedData<String> getDisguiseDataId() {
        return DISGUISE_NAME;
    }

    @Inject(method = "getSkinTextures", at = @At("HEAD"), cancellable = true)
    private void stealSkin(CallbackInfoReturnable<SkinTextures> cir) {
        // CORREÇÃO: Usando getEntityWorld() em vez de getWorld()
        // E acessando .isClient (campo) que é o padrão Yarn 1.21+
        if (this.getEntityWorld().isClient) {
            try {
                // Segurança: verifica se o tracker existe
                if (this.getDataTracker() == null) return;

                String disguiseName = this.getDataTracker().get(DISGUISE_NAME);

                if (disguiseName != null && !disguiseName.isEmpty()) {
                    var client = MinecraftClient.getInstance();
                    
                    if (client.getNetworkHandler() != null) {
                        PlayerListEntry targetInfo = client.getNetworkHandler().getPlayerListEntry(disguiseName);
                        
                        if (targetInfo != null) {
                            cir.setReturnValue(targetInfo.getSkinTextures());
                        }
                    }
                }
            } catch (Exception e) {
                // Silencia erros
            }
        }
    }
}
