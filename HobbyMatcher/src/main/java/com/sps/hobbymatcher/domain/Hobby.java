package com.sps.hobbymatcher.domain;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;

@Entity(name = "hobbies")
public class Hobby {
	
	@Id
	private Long id;
	private String name;
    private String about;
	private Set<Long> users=new HashSet<>();
	private Set<Long> posts=new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public Set<Long> getUsers() {
		return users;
	}

	public void setUsers(Set<Long> users) {
		this.users = users;
	}

	public Set<Long> getPosts() {
		return posts;
	}

	public void setPosts(Set<Long> posts) {
		this.posts = posts;
	}

    @Override
	public String toString() {
		return "Hobby [id=" + id + ", name=" + name + ", about=" + about + ", users=" + users + ", posts=" + posts + "]";
	}
}