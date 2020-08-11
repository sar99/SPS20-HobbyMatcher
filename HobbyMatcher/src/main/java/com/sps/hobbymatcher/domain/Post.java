package com.sps.hobbymatcher.domain;

import java.awt.Image;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.data.annotation.Id;

@Entity
public class Post {

	private String text;
	private String video;
	private Image image;
	private Long votes;
	private Set<User> usersVoted=new HashSet<>();
	
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
	public Set<User> getUsersVoted() {
		return usersVoted;
	}
	public void setUsersVoted(Set<User> usersVoted) {
		this.usersVoted = usersVoted;
	}

	@Override
	public String toString() {
		return "Post [text=" + text + ", video=" + video + ", image=" + image
				+ ", votes=" + votes + ", usersVoted=" + usersVoted + "]";
	}
}
