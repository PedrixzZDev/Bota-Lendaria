package pedrixzz.legendaryboots.client.gui;

import pedrixzz.legendaryboots.network.ModPackets;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import java.util.Collection;

public class SoulMirrorScreen extends Screen {
    
    public SoulMirrorScreen() {
        super(Text.literal("Selecione uma Alma"));
    }

    @Override
    protected void init() {
        super.init();
        if (MinecraftClient.getInstance().getNetworkHandler() == null) return;
        
        Collection<PlayerListEntry> players = MinecraftClient.getInstance().getNetworkHandler().getPlayerList();
        
        int y = 40;
        int x = this.width / 2 - 100;
        
        this.addDrawableChild(ButtonWidget.builder(Text.literal("§cRemover Disfarce"), (btn) -> {
            ClientPlayNetworking.send(new ModPackets.DisguisePayload(""));
            this.close();
        }).dimensions(x, y - 25, 200, 20).build());

        for (PlayerListEntry info : players) {
            // CORREÇÃO: Verificação de segurança e uso de getName()
            if (info.getProfile() == null || info.getProfile().getName() == null) continue;
            
            String myName = MinecraftClient.getInstance().player.getGameProfile().getName();
            if (info.getProfile().getName().equals(myName)) continue;

            String playerName = info.getProfile().getName();
            
            this.addDrawableChild(ButtonWidget.builder(Text.literal(playerName), (btn) -> {
                ClientPlayNetworking.send(new ModPackets.DisguisePayload(playerName));
                this.close();
            })
            .dimensions(x, y, 200, 20)
            .build());
            
            y += 25;
            if (y > this.height - 20) break;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 10, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }
}