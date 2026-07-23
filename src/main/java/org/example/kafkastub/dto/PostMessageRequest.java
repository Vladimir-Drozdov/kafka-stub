package org.example.kafkastub.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class PostMessageRequest {

    @NotBlank(message = "msg_id не должен быть пустым")
    @JsonProperty("msg_id")
    private String msgId;

    public PostMessageRequest() {
    }

    public PostMessageRequest(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}