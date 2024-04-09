package com.secuity.Service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.secuity.Repo.roleRepository;
import com.secuity.Repo.userRepository;
import com.secuity.Service.UserService;
import com.secuity.exception.GenericResponse;
import com.secuity.model.User;
import com.secuity.model.userRole;
@Service
public class userServiceImpl implements UserService {

	@Autowired
	private userRepository userRepository;

	@Autowired
	private roleRepository roleRepository;

	@Override
	public GenericResponse createUser(User user, Set<userRole> userRoles) throws Exception {

		User local = userRepository.findByUsername(user.getUsername());

		if (local != null) {
			System.out.println("User already present");
			throw new Exception("User already present");
		} else {
			for (userRole ur : userRoles) {
				roleRepository.save(ur.getRole());
			}
			user.getUserRoles().addAll(userRoles);
			local = userRepository.save(user);
		}
		return new GenericResponse(201, "Created Succesfully");
	}

	@Override
	public User getUser(String uname) {
		return userRepository.findByUsername(uname);
	}

	@Override
	public void deleteUser(Long userId) {
		userRepository.deleteById(userId);
	}

	

}
