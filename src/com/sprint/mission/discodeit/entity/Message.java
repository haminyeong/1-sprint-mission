package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {

    private final UUID id;
    private final Long createdAt;
    private Long updateAt;

    private String content;
    private final UUID authorId;
    private final UUID channelId;

    public Message(String content,UUID authorId, UUID channelId) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updateAt = this.createdAt;

        this.content = content;
        this.authorId = authorId;
        this.channelId = channelId;
    }

    public UUID getId() {
        return id;
    }
    public Long getCreatedAt() {
        return createdAt;
    }
    public Long getUpdateAt() {
        return updateAt;
    }
    public String  getContent() {
        return content;
    }
    public UUID getAuthorId() {
        return authorId;
    }
    public UUID getChannelId() {
        return channelId;
    }

    public void update(String content) {
        this.content = content;
        this.updateAt = System.currentTimeMillis();
    }
}
