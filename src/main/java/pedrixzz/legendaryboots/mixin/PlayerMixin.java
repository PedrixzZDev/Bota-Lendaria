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

    // O método getSkinTextures não existe em PlayerEntity (lado do servidor) na 1.21+.
    // A lógica de skin é tratada em AbstractClientPlayerMixin.java.
    // O código abaixo foi removido para evitar erros de mapeamento.
    /*
    @Inject(method = "getSkinTextures", at = @At("HEAD"), cancellable = true)
    private void stealSkin(CallbackInfoReturnable<SkinTextures> cir) {
        // ... (código removido)
    }
    */
}
