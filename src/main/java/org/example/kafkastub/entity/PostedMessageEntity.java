package org.example.kafkastub.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "posted_messages")
public class PostedMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "msg_id", nullable = false)
    private String msgId;

    @Column(name = "message_timestamp", nullable = false)
    private String timestamp;

    @Column(name = "method", nullable = false)
    private String method;

    @Column(name = "uri", nullable = false)
    private String uri;

    @Column(name = "kafka_partition")
    private Integer kafkaPartition;

    @Column(name = "kafka_offset")
    private Long kafkaOffset;

    public PostedMessageEntity() {
    }

    public PostedMessageEntity(String msgId, String timestamp, String method, String uri,
                                Integer kafkaPartition, Long kafkaOffset) {
        this.msgId = msgId;
        this.timestamp = timestamp;
        this.method = method;
        this.uri = uri;
        this.kafkaPartition = kafkaPartition;
        this.kafkaOffset = kafkaOffset;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getKafkaPartition() {
        return kafkaPartition;
    }

    public void setKafkaPartition(Integer kafkaPartition) {
        this.kafkaPartition = kafkaPartition;
    }

    public Long getKafkaOffset() {
        return kafkaOffset;
    }

    public void setKafkaOffset(Long kafkaOffset) {
        this.kafkaOffset = kafkaOffset;
    }
}