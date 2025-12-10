package pedrixzz.legendaryboots.network;

import pedrixzz.legendaryboots.LegendaryBoots;
import pedrixzz.legendaryboots.util.IDisguiseAccessor;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModPackets {
    public static final CustomPayload.Id<DisguisePayload> DISGUISE_ID = 
        new CustomPayload.Id<>(Identifier.of(LegendaryBoots.MOD_ID, "disguise"));

    public static void registerC2SPackets() {
        PayloadTypeRegistry.playC2S().register(DISGUISE_ID, DisguisePayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(DISGUISE_ID, (payload, context) -> {
            context.server().execute(() -> {
                ServerPlayerEntity player = context.player();
                String targetName = payload.targetName();

                if (player instanceof IDisguiseAccessor accessor) {
                     player.getDataTracker().set(accessor.getDisguiseDataId(), targetName);
                }
                
                if (targetName.isEmpty()) {
                    player.setCustomName(null);
                    player.sendMessage(Text.literal("§e[Reliquias] §fDisfarce removido."), true);
                } else {
                    player.setCustomName(Text.literal(targetName));
                    player.sendMessage(Text.literal("§e[Reliquias] §aVocê agora parece: " + targetName), true);
                }
                player.setCustomNameVisible(true);
            });
        });
    }

    public record DisguisePayload(String targetName) implements CustomPayload {
        public static final PacketCodec<RegistryByteBuf, DisguisePayload> CODEC = CustomPayload.codecOf(
            (payload, buf) -> buf.writeString(payload.targetName),
            (buf) -> new DisguisePayload(buf.readString())
        );

        @Override
        public Id<? extends CustomPayload> getId() {
            return DISGUISE_ID;
        }
    }
}