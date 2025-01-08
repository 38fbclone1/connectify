package com.connectify.connectify.service;

import com.connectify.connectify.DTO.WebRTCDto;
import com.connectify.connectify.DTO.response.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RoomService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public ResponseEntity<?> sendWebRTCData(WebRTCDto request) {
        String destination = "/topic/rooms/" + request.getRoomId() + "/video-call";
        messagingTemplate.convertAndSend(destination, request);
        CommonResponse<?> response = new CommonResponse<>(200, null, "Start call successfully!");
        return ResponseEntity.ok(response);
    }
}