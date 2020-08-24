package com.sps.hobbymatcher.domain;

import java.util.*;

import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Unindexed;
import org.springframework.data.annotation.Id;

@Entity
public class Comment {

    @Id
    private Long id;

    @Unindexed
	private String text;
    private Long userId;
    private Date createdDate;
	private List<Comment> replies = new ArrayList<>();

    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
    public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
    public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public List<Comment> getReplies() {
		return replies;
	}
	public void setReplies(List<Comment> replies) {
		this.replies = replies;
	}

	@Override
	public String toString() {
		return "Comments [ id=" + id + "text=" + text + ", userId=" + userId + ", createdDate=" + createdDate 
                    +", replies=" + replies + "]";
	}
}