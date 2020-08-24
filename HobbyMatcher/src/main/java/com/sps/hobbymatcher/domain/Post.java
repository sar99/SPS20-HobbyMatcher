package com.sps.hobbymatcher.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Unindexed;
import org.springframework.data.annotation.Id;

@Entity
public class Post {

    @Id
    private Long id;

    @Unindexed
	private String text;
	private long votes= 0L;
    private Long userId;
    private Date createdDate;
	private Set<Long> usersVoted=new HashSet<>();
	
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
	public long getVotes() {
		return votes;
	}
	public void setVotes(long votes) {
		this.votes = votes;
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
	public Set<Long> getUsersVoted() {
		return usersVoted;
	}
	public void setUsersVoted(Set<Long> usersVoted) {
		this.usersVoted = usersVoted;
	}

	@Override
	public String toString() {
		return "Post [id=" + id +", text=" + text
				+ ", votes=" + votes + ", userId=" + userId + ", createdDate=" + createdDate 
                +", usersVoted=" + usersVoted + "]";
	}
}