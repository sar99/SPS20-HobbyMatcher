package com.sps.hobbymatcher.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.sps.hobbymatcher.domain.User;
import com.sps.hobbymatcher.repository.UserRepository;
import com.sps.hobbymatcher.security.CustomSecurityUser;

import java.util.*;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		List<User> user=userRepo.findByUsername(username);

		if(user.size()==0) {
				throw new UsernameNotFoundException("Invalid Username and Password");
        }
        
		return new CustomSecurityUser(user.get(0));
	}
}