package com.pickup.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.pickup.model.KidEventXref;
import com.pickup.model.KidProfile;
import com.pickup.model.User;
import com.pickup.service.EventService;
import com.pickup.service.KidProfileService;
import com.pickup.service.UserService;

@Service
@Transactional
public class KidProfileServiceImpl implements KidProfileService {

	@Autowired
	private EventDao eventDao;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	
	/**
	  * Kids list by Parent Id
	  *
	  * @return a list of persons
	  */
	 public ArrayList<KidProfile> getKidProfileListByParentId( String userId) {
	  //logger.debug("Retrieving all persons");
	   
	  // Retrieve session from Hibernate
	  Session session = sessionFactory.getCurrentSession();
	   
	// Create a Hibernate query (HQL)
		  Query query = session.createQuery("Select k FROM  KidProfile  as k  left join k.authorizedUserSet as u where u.id = :id").setString("id", userId);
		  
	   
	  // Retrieve all
	 ArrayList<KidProfile> list =  (ArrayList<KidProfile>)query.list();
	  
	  
	  System.out.println("**** Kid list size " + list.size());
		
			for ( int i = 0 ; i < list.size() ; i++ ) {
				
				KidProfile kid = (KidProfile)list.get(i);
				System.out.println("Event desc " + kid.getUserid().getFirstName());
				System.out.println("Event desc " + kid.getUserid().getLastName());
			}
		
	  
	  return list;
	 }
	 
	 
	 public ArrayList<KidProfile> getKidProfileListByEventId( String eventId) {
		  //logger.debug("Retrieving all persons");
		   
		  // Retrieve session from Hibernate
		  Session session = sessionFactory.getCurrentSession();
		   
		// Create a Hibernate query (HQL)
			  Query query = session.createQuery("Select k FROM  KidProfile  as k  left join k.registeredEventSet as u where u.event.id = :id").setString("id", eventId);
			  
		   
		  // Retrieve all
		 ArrayList<KidProfile> list =  (ArrayList<KidProfile>)query.list();
		  
		  
		  System.out.println("**** Kid list size " + list.size());
			
				for ( int i = 0 ; i < list.size() ; i++ ) {
					
					KidProfile kid = (KidProfile)list.get(i);
					System.out.println("Event desc " + kid.getUserid().getFirstName());
					System.out.println("Event desc " + kid.getUserid().getLastName());
				}
			
		  
		  return list;
		 }
		 
	 
	 public ArrayList<KidProfile> getKidProfileListByEventIdAndPickupReady( String eventId) {
		  //logger.debug("Retrieving all persons");
		   
		  // Retrieve session from Hibernate
		  Session session = sessionFactory.getCurrentSession();
		  
		  SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			 
			 String todaysDate = format.format(new Date());
			 
			 System.out.println("Todays date " + todaysDate);
		   
		// Create a Hibernate query (HQL)
			  Query query = session.createQuery("Select k FROM  KidProfile  as k  left join k.registeredEventSet as u left join u.kidEventXrefLedgerSet as l " +
			  		"where u.event.id = :id and l.dateTime = :todaysDate and l.pickupReadyDateTime is not null and l.pickedUpDateTime is null").setString("id", eventId).setString("todaysDate" ,  todaysDate);
			  
		   
		  // Retrieve all
		 ArrayList<KidProfile> list =  (ArrayList<KidProfile>)query.list();
		  
		  
		  System.out.println("**** Kid list size " + list.size());
			
				for ( int i = 0 ; i < list.size() ; i++ ) {
					
					KidProfile kid = (KidProfile)list.get(i);
					System.out.println("Event desc " + kid.getUserid().getFirstName());
					System.out.println("Event desc " + kid.getUserid().getLastName());
				}
			
		  
		  return list;
		 }
		 
	 
	 
	 public ArrayList<KidProfile> getKidProfileListByEventIdAndCheckedIn( String eventId) {
		 
		   
		 String query = "Select k FROM  KidProfile  as k  left join k.registeredEventSet as u left join u.kidEventXrefLedgerSet as l " +
			  		"where u.event.id = :id and l.dateTime = :todaysDate and l.checkedInDateTime is not null and l.checkInVerifiedDateTime is null";
		 
		 return getKidProfileListByQuery(eventId, query);
		
		 }
		 
	 
	 public ArrayList<KidProfile> getKidProfileListByEventIdAndCheckedInAndPickedUp( String eventId) {
		
		 String query = "Select k FROM  KidProfile  as k  left join k.registeredEventSet as u left join u.kidEventXrefLedgerSet as l " +
			  		"where u.event.id = :id and l.dateTime = :todaysDate and l.checkedInDateTime is not null and l.pickedUpVerifiedDateTime is null";
		   
		return getKidProfileListByQuery(eventId, query);
		 }
		 
	 
	 public ArrayList<KidProfile> getKidProfileListByQuery ( String eventId , String queryString )  {
		 
		 Session session = sessionFactory.getCurrentSession();
		  
		  SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			 
			 String todaysDate = format.format(new Date());
			 
			 System.out.println("Todays date " + todaysDate);
			 
			 Query query = session.createQuery(queryString).setString("id", eventId).setString("todaysDate" ,  todaysDate);
				  
			   
			  // Retrieve all
			 ArrayList<KidProfile> list =  (ArrayList<KidProfile>)query.list();
			  
			  
			  System.out.println("**** Kid list size " + list.size());
				
					for ( int i = 0 ; i < list.size() ; i++ ) {
						
						KidProfile kid = (KidProfile)list.get(i);
						System.out.println("Event desc " + kid.getUserid().getFirstName());
						System.out.println("Event desc " + kid.getUserid().getLastName());
					}
				
			  
					
					
				//	sessionFactory.getCurrentSession().find(arg0, arg1, arg2)
			  return list;
	 }
	 
	 public ArrayList<KidProfile> getKidProfileListByEventIdAndPickedUp( String eventId) {
		  //logger.debug("Retrieving all persons");
		   
		  // Retrieve session from Hibernate
		 String query = "Select k FROM  KidProfile  as k  left join k.registeredEventSet as u left join u.kidEventXrefLedgerSet as l " +
			  		"where u.event.id = :id and l.dateTime = :todaysDate and l.pickupReady = 'Y' and l.pickedUpDateTime is not null";
		 
		 return getKidProfileListByQuery(eventId, query);
		   
		// Create a Hibernate query (HQL)
			 
		 }
		 
	 
	 
	 public ArrayList<KidProfile> getKidProfileListByEventIdAndWaiting( String eventId) {
		
		   
		 String query = "Select k FROM  KidProfile  as k  left join k.registeredEventSet as u left join u.kidEventXrefLedgerSet as l " +
			  		"where u.event.id = :id and l.dateTime = :todaysDate and l.pickupReady is null  and l.pickedUpDateTime is null";
		// Create a Hibernate query (HQL)
			 return getKidProfileListByQuery(eventId, query);
		 }
		 
	 
	 
	 /**
	  * Events for kid which they are registered for --- 
	  *
	  * @return a list of persons
	  */
	 public List<KidEventXref> getKidEventsById( String userId) {
	  //logger.debug("Retrieving all persons");
	   
	  // Retrieve session from Hibernate
	  Session session = sessionFactory.getCurrentSession();
	   
	  Date todaysDate = new Date();
	  System.out.println("Todays Date:" + todaysDate.toString());
	 // Calendar.get(Calendar.)
	  
	// Create a Hibernate query (HQL)
		  Query query = session.createQuery("Select k.registeredEventSet FROM  KidProfile  as k  where k.userid.id = :id ").setString("id", userId);
		  
	   
	  // Retrieve all
	 List<KidEventXref> list =  query.list();
	  List tempList = new ArrayList();
	 if ( list != null ) {
	  
	  System.out.println("**** Kid event list size " + list.size());
		
			for ( int i = 0 ; i < list.size() ; i++ ) {
				
				KidEventXref  kidXref = (KidEventXref)list.get(i);
				// here 
				
				if (todaysDate.after(kidXref.getEvent().getEndDate()) || kidXref.getActive().equals("0")) {
					
					// event has ended - does not need to be included 
				}else {
					tempList.add(kidXref);
				}
				System.out.println("Event desc " + kidXref.getEvent().getDescription());
				System.out.println("Event desc " + kidXref.getEvent().getName());
			}
		
	  
			// now we need to see which of these events have not started yet  
			System.out.println("Going for temp list " + tempList.size());
			for ( int i = 0 ; i < tempList.size(); i++) {
				System.out.print("**** in the temp list ");
				
				KidEventXref  kidXref = (KidEventXref)tempList.get(i);
				
				//System.out.println(" today time " + todaysDate.getTime());
				//System.out.println(" start time " + kidXref.getEvent().getStartDate().getTime());
				System.out.println(" start time " + kidXref.getEvent().getStartDateString());
				//System.out.println(" end time " + kidXref.getEvent().getEndDate().getTime());
				System.out.println(" end time " + kidXref.getEvent().getEndDateString());
				//System.out.println("diff curr and start " + ( todaysDate.getTime() - kidXref.getEvent().getStartDate().getTime()));
				//System.out.println("diff curr and end " + ( todaysDate.getTime() - kidXref.getEvent().getEndDate().getTime()));
				//System.out.print("**** in the temp list " + kidXref.getEvent().getStartDate());
				if ( todaysDate.before(kidXref.getEvent().getStartDate())) {
					
					
					
					
					kidXref.getEvent().setEventRegistrationState("0");
				}else {
					// on state -- 
					
					System.out.println("Event tYpe "+ kidXref.getEvent().getEventDuration());
					
					
					kidXref.getEvent().setEventRegistrationState("1");
					
					if ( kidXref.getEvent().getEventDuration().equals("D")) {
						
						
						// which means daily event 
						
						long todaysTime = todaysDate.getTime();
						
						long startTime = kidXref.getEvent().getStartDate().getTime();
						long endTime = kidXref.getEvent().getEndDate().getTime();
						
						System.out.println("Checking todays time greater than start time :" + this.isCurrentTimeBeforeEventTime(todaysDate, kidXref.getEvent().getStartDate()));
						System.out.println("Checking todays time greater than start time :" + this.isCurrentTimeBeforeEventTime(todaysDate, kidXref.getEvent().getEndDate()));
						
						if (  this.isCurrentTimeBeforeEventTime(todaysDate, kidXref.getEvent().getStartDate()) || this.isCurrentTimeBeforeEventTime( kidXref.getEvent().getEndDate() , todaysDate)) {
							System.out.println("Goint to set the flag to 0");
							kidXref.getEvent().setEventRegistrationState("0");
						}else {
							System.out.println("Goint to set the flag to 1");
						}
					}
				}
			}
			
	 }	else {
		 System.out.println("List came out null");
	 }
	  return tempList;
	 }
	 
	 
	 
	 private boolean isCurrentTimeBeforeEventTime ( Date todaysDate , Date compareDate ) {
		int todaysHour  = todaysDate.getHours();
		
		int todaysMin = todaysDate.getMinutes();
		
		int todaysSec = todaysDate.getSeconds();
		
		int compHour = compareDate.getHours();
		
		int compMin = compareDate.getMinutes();
		
		int compSec = compareDate.getSeconds();
		
		
		if ( todaysHour < compHour) {
			return true ;
			
		}else if ( todaysHour > compHour) {
			return false ;
		}else {
			
			
			// means hour are same so lets check the minutes 
			
			if ( todaysMin < compMin)  {
				return true ;
			}else if ( todaysMin > compMin) {
				return false ;
				
			}else {
				// even min are same so lets compare seconds 
				if ( todaysSec < compSec) {
					return true ;
					
				}else {
					return false ;
				}
			}
		}
		 
		 
		 
	 }
	 
	 public KidProfile getKidById( String userId) {
		  //logger.debug("Retrieving all persons");
		   
		  // Retrieve session from Hibernate
		  Session session = sessionFactory.getCurrentSession();
		   
		// Create a Hibernate query (HQL)
			  Query query = session.createQuery("Select k FROM  KidProfile  as k  where k.userid.id = :id").setString("id", userId);
			  
		   
		  // Retrieve all
		 List list =  query.list();
		  
		  
		  System.out.println("**** Kid event list size " + list.size());
			
				
			if ( list.size() > 0) {
				return (KidProfile)list.get(0);
			}
		  
				
				
				
		  return null;
		 }
}
 