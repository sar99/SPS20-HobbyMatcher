package com.sps.hobbymatcher.domain;

import org.springframework.security.core.GrantedAuthority;

import org.springframework.data.annotation.Id;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;

@Entity
public class Authority implements GrantedAuthority{

	private static final long serialVersionUID = 1272548942962614584L;

    @Id
	private Long id;
	private String authority;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getAuthority() {
		return this.authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

}