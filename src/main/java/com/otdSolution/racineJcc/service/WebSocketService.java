package com.otdSolution.racineJcc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketService(final SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessage( @DestinationVariable String topicSuffix, String payload) {

        messagingTemplate.convertAndSend("/topic/" + topicSuffix ,payload );
    }


}
