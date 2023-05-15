package com.spotify.game.service.impl;

import com.spotify.game.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketServiceImpl implements WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void sendMessage(String topicSuffix) {
        messagingTemplate.convertAndSend("/topic/" + topicSuffix, "Default message from WebSocketService");
    }
}
