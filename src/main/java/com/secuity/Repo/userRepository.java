package com.secuity.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.secuity.model.User;


public interface userRepository extends JpaRepository<User, Long> {

	User findByUsername(String Username);

	static boolean existsByPhone(String phone) {
		// TODO Auto-generated method stub
		return false;
	}

	User findByPhone(String phone);

	User getUserByPhone(String mobileNumber);
}
