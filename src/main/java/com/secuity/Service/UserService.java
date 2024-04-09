package com.secuity.Service;

import java.util.Set;

import com.secuity.exception.GenericResponse;
import com.secuity.model.User;
import com.secuity.model.userRole;

public interface UserService {

	    public GenericResponse createUser(User user, Set<userRole> userRoles) throws Exception;
	    public User getUser(String uname);
	    public void deleteUser(Long userId);
		

}
