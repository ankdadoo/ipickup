package com.pickup.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pickup.model.User;

@Service
public interface UserService {

	 public User verifyUser( String username , String password) ;
}
