package com.sps.hobbymatcher.domain;

import java.awt.Image;
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
	private String video;
	private Image image;
	private Long votes;
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
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	public Long getVotes() {
		return votes;
	}
	public void setVotes(Long votes) {
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
		return "Post [id=" + id +", text=" + text + ", video=" + video + ", image=" + image
				+ ", votes=" + votes + ", userId=" + userId + ", createdDate=" + createdDate 
                +", usersVoted=" + usersVoted + "]";
	}
}
