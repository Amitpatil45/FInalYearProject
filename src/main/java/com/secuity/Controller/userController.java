package com.secuity.Controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.secuity.Repo.userRepository;
import com.secuity.Service.UserService;
import com.secuity.exception.GenericResponse;
import com.secuity.model.Role;
import com.secuity.model.SMSdto;
import com.secuity.model.User;
import com.secuity.model.userRole;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class userController {

	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserService userService;

	@Autowired
	private userRepository userRepository;

	@PostMapping("/")
	public ResponseEntity<GenericResponse>  createUser(@RequestBody User user) throws Exception {

		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

		Role role = new Role();
		role.setRoleName("NORMAL");
		Set<userRole> userRoleSet = new HashSet<>();
		userRole userRole = new userRole();
		userRole.setRole(role);
		userRole.setUser(user);
		userRoleSet.add(userRole);
		GenericResponse response = userService.createUser(user, userRoleSet);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/{userName}")
	public User getUser(@PathVariable("userName") String uname) {
		System.out.println(uname);

		return userService.getUser(uname);
	}

	@DeleteMapping("/{userId}")
	public void deleteUser(@PathVariable("userId") Long uid) {
		System.out.println(uid);
		userService.deleteUser(uid);
	}

//--------------------------------------------------------------------------------------------	
	@PostMapping("/otpsend")
	public String sendOTP( @RequestBody SMSdto smSdto  ) {
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		return "OTP SEND SUCESFULLY";
		
	}
	
	
	
}