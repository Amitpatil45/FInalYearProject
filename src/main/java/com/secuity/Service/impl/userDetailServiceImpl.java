package com.secuity.Service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.secuity.Repo.userRepository;
import com.secuity.model.User;
@Service
public class userDetailServiceImpl implements UserDetailsService{
	@Autowired
	private userRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		User user = this.userRepository.findByUsername(s);
		if (user == null) {
			System.out.println("User not found exception");
			throw new UsernameNotFoundException("No user found");
		}
		return user;
	}
}
