package com.pickup.controller;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



import com.pickup.convertors.ListWrapper;
import com.pickup.model.Event;
import com.pickup.model.ExtResult;
import com.pickup.model.KidEventXref;
import com.pickup.model.KidProfile;
import com.pickup.model.User;
import com.pickup.service.EventService;
import com.pickup.service.KidProfileService;
import com.pickup.service.UserService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class EventController {
	
	private static final Logger logger = LoggerFactory.getLogger(EventController.class);
	
	
@Autowired
private KidProfileService kidProfileService;

	
	
	

	

	
}