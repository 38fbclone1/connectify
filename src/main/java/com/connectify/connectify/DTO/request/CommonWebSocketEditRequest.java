package com.connectify.connectify.DTO.request;

import lombok.Data;

@Data
public class CommonWebSocketEditRequest<E, S> {
    private E editRequest;
    private S searchRequest;
}
