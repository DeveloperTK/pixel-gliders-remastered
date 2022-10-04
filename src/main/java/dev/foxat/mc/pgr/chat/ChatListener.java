package dev.foxat.mc.pgr.chat;

import dev.foxat.mc.pgr.Lobby;
import net.minestom.server.event.player.PlayerChatEvent;

import java.util.UUID;
import java.util.function.Consumer;

public class ChatListener implements Consumer<PlayerChatEvent> {

    @Override
    public void accept(PlayerChatEvent playerChatEvent) {
        UUID instanceId = playerChatEvent.getPlayer().getInstance().getUniqueId();

    }

    private boolean isLobby(UUID instanceId) {
        return Lobby.INSTANCE.getUniqueId().equals(instanceId);
    }
}
