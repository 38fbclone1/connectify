package com.connectify.connectify.service;

import com.connectify.connectify.DTO.request.EditMessengerRequest;
import com.connectify.connectify.DTO.request.SearchMessengerOfAccountRequest;
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
import com.connectify.connectify.util.AESUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    AESUtils aesUtils;

    MessengerService () {
        this.aesUtils = new AESUtils();
    }

    private String p_create(EditMessengerRequest request) {
        if (request.getType().equals(EMessengerType.GROUP) && request.getMembers().size() < 2) throw new CustomException(EError.BAD_REQUEST);
        Account currentAccount = authService.getCurrentAccount();
        request.getMembers().add(currentAccount.getId());
        List<Account> accounts =  accountRepository.findAllById(request.getMembers());
        Set<Account> accountSet = new HashSet<>(accounts);

        if (accountSet.size() < 2 || request.getType() == null) throw new CustomException(EError.BAD_REQUEST);
        Messenger messenger = new Messenger();
        messenger.setCreatedAt(new Date());
        messenger.setCreatedBy(currentAccount);
        messenger.setMembers(accountSet);
        if (request.getName() != null && request.getName().isEmpty()) {
            messenger.setName("Cuộc trò chuyện mới");
        } else {
            messenger.setName(request.getName());
        }
        messenger.setType(request.getType());
        messenger.setName(request.getName());
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
        String createdMessengerId = p_create(request);
        CommonResponse<String> response = new CommonResponse<>(200, createdMessengerId, "Create messenger successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> searchMessengerOfAccount (SearchMessengerOfAccountRequest request) {
        List<Messenger> messengers = messengerRepository.findMessengersOfAccount(request.getAccountId());
        List<MessengerResponse> messengerResponses = messengers.stream().map(this::messengerToMessengerResponse).toList();
        CommonResponse<?> response = new CommonResponse<>(200, messengerResponses, "Search messenger successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> addMember (EditMessengerRequest request) {
        Optional<Messenger> optionalMessenger = messengerRepository.findById(request.getId());
        if (optionalMessenger.isEmpty()) throw new CustomException(EError.BAD_REQUEST);
        Messenger messenger = optionalMessenger.get();
        if (messenger.getType().equals(EMessengerType.PERSONAL)) throw new CustomException(EError.BAD_REQUEST);
        List<Account> members = accountRepository.findAllById(request.getMembers());
        messenger.getMembers().addAll(members);
        messengerRepository.save(messenger);
        CommonResponse<?> response = new CommonResponse<>(200, null, "Add members successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public MessengerResponse messengerToMessengerResponse (Messenger messenger) {
        return mapper.map(messenger, MessengerResponse.class);
    }

    //internal use
    public Messenger s_get (String id) {
        Optional<Messenger> optionalMessenger = messengerRepository.findById(id);
        if (optionalMessenger.isEmpty()) throw new CustomException(EError.BAD_REQUEST);
        return optionalMessenger.get();
    }

}
