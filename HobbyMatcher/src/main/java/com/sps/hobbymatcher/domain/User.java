package com.sps.hobbymatcher.domain;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;

@Entity(name="users")
public class User {
	
	@Id
	private Long id;
	private String name;
	private String username;
	private String password; 
	private Set<String> connections=new HashSet<>();
	private Set<Long> myHobbies=new HashSet<>();
	private Set<Post> myPosts=new HashSet<>();
	
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Set<String> getConnections() {
		return connections;
	}
	public void setConnections(Set<String> connections) {
		this.connections = connections;
	}
	public Set<Long> getMyHobbies() {
		return myHobbies;
	}
	public void setMyHobbies(Set<Long> myHobbies) {
		this.myHobbies = myHobbies;
	}
	public Set<Post> getMyPosts() {
		return myPosts;
	}
	public void setMyPosts(Set<Post> myPosts) {
		this.myPosts = myPosts;
	}
    
    @Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", username=" + username + ", password=" + password + ", connections=" + connections + ", myHobbies=" + myHobbies + ", myPosts="
				+ myPosts + "]";
	}
}
