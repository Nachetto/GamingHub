package dev.nacho.ghub.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.nacho.ghub.domain.model.Mensaje;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    // Guardas sesiones activas
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // Aquí puedes asociar el usuario a la sesión (por ejemplo con una query param)
        String userId = getUserIdFromSession(session);
        sessions.put(userId, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Mensaje JSON enviado por el cliente
        String payload = message.getPayload();
        ObjectMapper mapper = new ObjectMapper();
        Mensaje chatMessage = mapper.readValue(payload, Mensaje.class);

        switch (chatMessage.getType()) {
            case "subscribe":
                // registrar subscripción a un chatId
                break;
            case "message":
                // reenviar mensaje a usuarios subscritos
                sendToChat(chatMessage);
                break;
            // puedes tener más tipos: seen, typing, etc.
        }
    }

    private void sendTo564Chat(Mensaje msg) throws IOException {
        for (WebSocketSession session : sessions.values()) {
            // enviar solo a los usuarios del chat correspondiente
            session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(msg)));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // limpiar sesiones
        sessions.values().removeIf(s -> s.getId().equals(session.getId()));
    }

    private String getUserIdFromSession(WebSocketSession session) {
        // puedes pasarlo como query param o header, o usar cookies/token JWT
        return "user1";
    }
}
