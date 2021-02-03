package com.kobbyopoku.springbootwebsockets.controller;

import com.kobbyopoku.springbootwebsockets.model.ChatMessage;
import com.kobbyopoku.springbootwebsockets.model.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


@Component
public class WSEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(WSEventListener.class);

    @Autowired
    private SimpMessageSendingOperations so;

    @EventListener
    public void handleWSConnectListener(final SessionConnectedEvent sce){

    }

    @EventListener
    public void handleWSDisconnectListener(final SessionDisconnectEvent sce){
        final StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sce.getMessage());
        final String username = (String) headerAccessor.getSessionAttributes().get("username");
        final ChatMessage chatMessage = ChatMessage.builder()
                .messageType(MessageType.DISCONNECT)
                .sender(username)
                .build();

        so.convertAndSend("/topic/public", chatMessage);
    }
}
