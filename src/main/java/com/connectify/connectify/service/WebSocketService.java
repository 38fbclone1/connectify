package com.connectify.connectify.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendData(String destination, Object data) {
        messagingTemplate.convertAndSend(destination, data);
    };
}
