package com.connectify.connectify.controller;

import com.connectify.connectify.DTO.request.EditMessengerRequest;
import com.connectify.connectify.DTO.request.SearchPersonalMessengerByMember;
import com.connectify.connectify.service.MessengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/messengers")
public class MessengerController {
    @Autowired
    MessengerService messengerService;

    @GetMapping("{id}")
    public ResponseEntity<?> get (@PathVariable String id) {
        return messengerService.get(id);
    }

    @PostMapping()
    public ResponseEntity<?> create (@RequestBody EditMessengerRequest request) {
        return messengerService.create(request);
    }

    @PostMapping("find-personal-by-members")
    public ResponseEntity<?> findByMembers (@RequestBody SearchPersonalMessengerByMember request) {
        return messengerService.findPersonalByMember(request);
    }
}
