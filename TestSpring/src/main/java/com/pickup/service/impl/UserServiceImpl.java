package com.pickup.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.pickup.dao.EventDao;
import com.pickup.model.Event;
import com.pickup.model.User;
import com.pickup.service.EventService;
import com.pickup.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private EventDao eventDao;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	
	/**
	  * Retrieves all persons
	  *
	  * @return a list of persons
	  */
	 public User verifyUser( String username , String password) {
	  //logger.debug("Retrieving all persons");
	   
	  // Retrieve session from Hibernate
	  Session session = sessionFactory.getCurrentSession();
	   
	  // Create a Hibernate query (HQL)
	  Query query = session.createQuery("FROM  User as u where u.username = :username and u.password = :password" ).setString("username",username).setString("password" , password);
	  
	   
	  // Retrieve all
	  User user =   (User)query.uniqueResult();
	  if ( user != null ) {
	  
	  System.out.println("**** User found " + user.getFirstName());
	  System.out.println("**** User found " + user.getLastName());
	  
	  return user ;
	  }else {
		  System.out.println("*** No user found ");
		  return null;
	  }
		
	  
	//  return user;
	 }
	 
	 
	 
}
