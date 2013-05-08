package com.pickup.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



import com.pickup.model.Event;
import com.pickup.model.ExtResult;
import com.pickup.model.User;
import com.pickup.service.EventService;
import com.pickup.service.UserService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class LoginController {
	
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EventService eventService;
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! the client locale is "+ locale.toString());
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	
	

	@RequestMapping(method=RequestMethod.GET, value="/loginUser", headers="Accept=application/json")
	public @ResponseBody ExtResult loginUser(@RequestParam("username")  String username , @RequestParam("password") String password ) {
		//Employee e = employeeDS.get(Long.parseLong(id));
		System.out.println("***Login User");
		System.out.println("***Login User" + username);
		System.out.println("***Login User" + password);
		User user  = userService.verifyUser(username, password);
		
//		if ( user != null) {
//			
//			//get all events for this user 
//			List list = eventService.getEventListByUser(user.getId());
//			
//			System.out.println("Events for user " + list);
//		}
		ExtResult result = new ExtResult();
		
		
		if ( user == null ) {
			// then its an error condition so need the app be informed 
			result.setMsg("User not found");
			result.setSuccess("false");
		}else {
			System.out.println("User info " + user);
			result.setData(user);
		}
		
		
		
		
		return result;
	}
	
	
	
	@RequestMapping(method=RequestMethod.GET, value="/getUserEvents", headers="Accept=application/json")
	public @ResponseBody ExtResult getUserEvents(@RequestParam("userId")  String userId ) {
		//Employee e = employeeDS.get(Long.parseLong(id));
		System.out.println("***Get Events By  UserId" + userId);
		//System.out.println("***Login User" + username);
		//System.out.println("***Login User" + password);
		//User user  = userService.verifyUser(username, password);
		
//		if ( user != null) {
//			
//			//get all events for this user 
			List list = eventService.getEventListByUser(userId);
			
			System.out.println("Events for user " + list.size());
		//}
		
		//System.out.println("User info " + user);
		
		ExtResult result = new ExtResult();
		result.setData(list);
		return result;
	}
	
}
