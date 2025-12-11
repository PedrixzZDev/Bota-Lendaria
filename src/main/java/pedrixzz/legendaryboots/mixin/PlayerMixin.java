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
import net.minecraft.entity.player.SkinTextures; // Import da 1.21.11
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

    // 1. Definição da variável sincronizada (DataTracker)
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

    // 2. Lógica da Skin (Movid para cá porque o método existe em PlayerEntity)
    @Inject(method = "getSkinTextures", at = @At("HEAD"), cancellable = true)
    private void stealSkin(CallbackInfoReturnable<SkinTextures> cir) {
        // A lógica de skin só deve rodar no CLIENTE
        if (this.getWorld().isClient()) {
            try {
                // Pega o nome do disfarce
                String disguiseName = this.getDataTracker().get(DISGUISE_NAME);

                if (disguiseName != null && !disguiseName.isEmpty()) {
                    var client = MinecraftClient.getInstance();
                    
                    // Verifica se o handler de rede existe
                    if (client.getNetworkHandler() != null) {
                        PlayerListEntry targetInfo = client.getNetworkHandler().getPlayerListEntry(disguiseName);
                        
                        // Se encontrou o player alvo, usa a skin dele
                        if (targetInfo != null) {
                            cir.setReturnValue(targetInfo.getSkinTextures());
                        }
                    }
                }
            } catch (Exception e) {
                // Silencia erros para evitar crash
            }
        }
    }
}
