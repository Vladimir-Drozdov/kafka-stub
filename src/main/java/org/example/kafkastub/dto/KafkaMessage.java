package org.example.kafkastub.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KafkaMessage {

    @JsonProperty("msg_id")
    private String msgId;

    private String timestamp;

    private String method;

    private String uri;

    public KafkaMessage() {
    }

    public KafkaMessage(String msgId, String timestamp, String method, String uri) {
        this.msgId = msgId;
        this.timestamp = timestamp;
        this.method = method;
        this.uri = uri;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
