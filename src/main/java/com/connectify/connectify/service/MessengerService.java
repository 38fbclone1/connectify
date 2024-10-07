package com.connectify.connectify.service;

import com.connectify.connectify.DTO.request.EditMessengerRequest;
import com.connectify.connectify.DTO.request.SearchPersonalMessengerByMember;
import com.connectify.connectify.DTO.response.CommonResponse;
import com.connectify.connectify.DTO.response.MessengerResponse;
import com.connectify.connectify.entity.Account;
import com.connectify.connectify.entity.Messenger;
import com.connectify.connectify.enums.EError;
import com.connectify.connectify.enums.EMessengerType;
import com.connectify.connectify.exception.CustomException;
import com.connectify.connectify.repository.AccountRepository;
import com.connectify.connectify.repository.MessengerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MessengerService {

    @Autowired
    MessengerRepository messengerRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AuthService authService;

    @Autowired
    ModelMapper mapper;

    private String p_create(EditMessengerRequest request) {
        Account currentAccount = authService.getCurrentAccount();
        request.getMembers().add(currentAccount.getId());
        List<Account> accounts =  accountRepository.findAllById(request.getMembers());
        Set<Account> accountSet = new HashSet<>(accounts);
        if (accountSet.size() < 2 || request.getType() == null) throw new CustomException(EError.BAD_REQUEST);
        Messenger messenger = new Messenger();
        messenger.setCreatedAt(new Date());
        messenger.setCreatedBy(currentAccount);
        messenger.setMembers(accountSet);
        messenger.setName(request.getName());
        messenger.setType(request.getType());
        Messenger createdMessenger = messengerRepository.save(messenger);
        return createdMessenger.getId();
    }

    public ResponseEntity<?> create(EditMessengerRequest request) {
        String createdMessengerId = p_create(request);

        CommonResponse<?> response = new CommonResponse<>(200, createdMessengerId, "Create successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> get(String id) {
        Optional<Messenger> optionalMessenger = messengerRepository.findById(id);
        if (optionalMessenger.isEmpty()) throw new CustomException(EError.BAD_REQUEST);
        MessengerResponse messengerResponse = mapper.map(optionalMessenger.get(), MessengerResponse.class);
        CommonResponse<?> response = new CommonResponse<>(200, messengerResponse, "Get messenger successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> findPersonalByMember (SearchPersonalMessengerByMember searchRequest) {
        Account currentAccount = authService.getCurrentAccount();
        Optional<Messenger> optionalMessenger = messengerRepository.findPersonalByMembers(currentAccount.getId(), searchRequest.getTargetMember());
        if (optionalMessenger.isPresent()) {
            CommonResponse<String> response = new CommonResponse<>(200, optionalMessenger.get().getId(), "Messenger is existed!");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        EditMessengerRequest request = new EditMessengerRequest();
        List<String> members = new ArrayList<>();
        members.add(searchRequest.getTargetMember());
        request.setMembers(members);
        request.setType(EMessengerType.PERSONAL);
        System.out.println(request.getMembers().get(0));
        String createdMessengerId = p_create(request);
        CommonResponse<String> response = new CommonResponse<>(200, createdMessengerId, "Create messenger successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
