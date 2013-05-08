package com.pickup.service.impl;

import java.text.SimpleDateFormat;
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
import com.pickup.model.KidEventXrefLedger;
import com.pickup.model.KidProfile;
import com.pickup.model.User;
import com.pickup.service.EventService;
import com.pickup.service.KidProfileService;

@Service
@Transactional
public class EventServiceImpl implements EventService {

	@Autowired
	private EventDao eventDao;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private KidProfileService kidProfileService;
	
	/**
	  * Retrieves all persons
	  *
	  * @return a list of persons
	  */
	 public List<Event> getEventList() {
	  //logger.debug("Retrieving all persons");
	   
	  // Retrieve session from Hibernate
	  Session session = sessionFactory.getCurrentSession();
	   
	  // Create a Hibernate query (HQL)
	  Query query = session.createQuery("FROM  Event");
	  
	   
	  // Retrieve all
	  List list =   query.list();
	  
	  
	  System.out.println("**** Event list size " + list.size());
		
		for ( int i = 0 ; i < list.size() ; i++ ) {
			
			Event event = (Event)list.get(i);
			System.out.println("Event desc " + event.getDescription());
			System.out.println("Event desc " + event.getEventType().getDescription());
			System.out.println("Event desc " + event.getEventType().getEventActionsSet().size());
		}
		
		return list;
	 }
	 
	 
	 public KidEventXref registerEvent ( String parentUserId , String eventId , String kidUserId) {
		 
		 // later on i need to make sure that there are not an entry existing for these three guys and if yes then just update the time stamp or dont do anything.
		 
		 System.out.println(" Going to register event for kid " + kidUserId);
		 Session session = sessionFactory.getCurrentSession();
		 
		 KidEventXref xref = null;
		 // first chec if the registration already exist or not - 
		 
		 Query query = session.createQuery("Select k FROM  KidEventXref as k where k.kidProfile.id = :kidUserId and k.event.id = :eventId and k.active = '1'").setString("kidUserId", kidUserId).setString("eventId" ,  eventId);
		  
		  List list = query.list();
		  
		  if ( list == null || list.size() == 0 ) {
				  xref = new KidEventXref();
		
				 Event event = (Event)session.get(Event.class, eventId);
				 KidProfile profile = this.kidProfileService.getKidById(kidUserId);
				 
				 User user = (User)session.get(User.class, parentUserId);
				 
				 xref.setId(java.util.UUID.randomUUID().toString());
				 xref.setEvent(event);
				 xref.setKidProfile(profile);
				 xref.setRegisterUserId(user);
				 xref.setRegisterDate(new Date());
				 xref.setActive("1");
				 System.out.println("Going to save the xref");
				 session.save(xref);
		  }else {
			  xref = (KidEventXref)list.get(0);
			  // already exist - so dont register again 
			  System.out.println("Registration  already exist for this event and kiddo " + xref.toString());
			  System.out.println("Registration  already exist for this event and kiddo " + xref.getId());
			  
			  // we can add the logic to ensure that the parent user which is registering the kid is part of the registered user  list 
			  xref = (KidEventXref)list.get(0);
			  
		  }
		  
		  return xref;
		 
	 }
	 
	 
	 public KidEventXrefLedger readyForPickup ( String xrefId ) {
		 
		 System.out.println(" Going to unregister event for kid " + xrefId);
		 Session session = sessionFactory.getCurrentSession();
		 
		 
		 KidEventXrefLedger ledger = null;
		 
		 //Date todaysDate = new Date();
		 
		 SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		 
		 String todaysDate = format.format(new Date());
		 
		 System.out.println("Todays date " + todaysDate);
		 // find a xref ledger record for xref id and todays date and if not found , create one and add the data 
		 // if one exist then just add the data  --- 
		 
		 KidEventXref xref = (KidEventXref)session.get(KidEventXref.class, xrefId);
		 
		 if ( xref != null ) {
			 // move to get the next step 
		 
		  Query query = session.createQuery("Select k FROM  KidEventXrefLedger as k where k.kidEventXref.id = :xrefId and k.dateTime= :dateTime").setString("xrefId", xrefId).setString("dateTime", todaysDate);
		  
		  List list = query.list();
		  
		  if ( list == null || list.size() == 0 ) {
			  // no record exist yet so create one 
			  ledger = new KidEventXrefLedger();
			  ledger.setId(java.util.UUID.randomUUID().toString());
			  ledger.setKidEventXref(xref);
			  ledger.setPickupReadyDateTime(new Date());
			 // ledger.setPickupReady("Y");
			  ledger.setDateTime(todaysDate);
			  
			  
			  session.saveOrUpdate(ledger);
		  }else {
			  ledger = (KidEventXrefLedger)list.get(0);
			  
			  if ( ledger != null ) {
				  
				  System.out.println("*** Ledger already exist for today " + ledger);
				  if ( ledger.getPickupReadyDateTime() != null) {
					  System.out.println(" Cant take any action , as the event has been recorded already ");
					  // not sure if i want to add the flag to show the warning or not .... will decide later on 
					  
				  }else {
					  ledger.setPickupReadyDateTime(new Date());
					//  ledger.setPickupReady("Y");
					  ledger.setWontPickUpDateTime(null);
					  ledger.setCancelPickupDateTime(null);
					  ledger.setPickedUpDateTime(null);
					  session.saveOrUpdate(ledger);
				  }
			  }
		  }
		// KidEventXref xref = (KidEventXref)session.get(KidEventXref.class, xrefId);

		 }else {
			 
		
			 System.out.println("*** no xref could be located ");
		 }
		 
		 
		 
		 return ledger;
		 
	 }
	 
	 
	 
 public KidEventXrefLedger checkIn ( String xrefId ) {
		 
		 System.out.println(" Going to unregister event for kid " + xrefId);
		 Session session = sessionFactory.getCurrentSession();
		 
		 
		 KidEventXrefLedger ledger = null;
		 
		 //Date todaysDate = new Date();
		 
		 SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		 
		 String todaysDate = format.format(new Date());
		 
		 System.out.println("Todays date " + todaysDate);
		 // find a xref ledger record for xref id and todays date and if not found , create one and add the data 
		 // if one exist then just add the data  --- 
		 
		 KidEventXref xref = (KidEventXref)session.get(KidEventXref.class, xrefId);
		 
		 if ( xref != null ) {
			 // move to get the next step 
		 
		  Query query = session.createQuery("Select k FROM  KidEventXrefLedger as k where k.kidEventXref.id = :xrefId and k.dateTime= :dateTime").setString("xrefId", xrefId).setString("dateTime", todaysDate);
		  
		  List list = query.list();
		  
		  if ( list == null || list.size() == 0 ) {
			  // no record exist yet so create one 
			  ledger = new KidEventXrefLedger();
			  ledger.setId(java.util.UUID.randomUUID().toString());
			  ledger.setKidEventXref(xref);
			  ledger.setCheckedInDateTime(new Date());
			 // ledger.setPickupReady("Y");
			  ledger.setDateTime(todaysDate);
			  
			  
			  session.saveOrUpdate(ledger);
		  }else {
			  ledger = (KidEventXrefLedger)list.get(0);
			  
			  if ( ledger != null ) {
				  
				  System.out.println("*** Ledger already exist for today " + ledger);
				  if ( ledger.getCheckedInDateTime() != null) {
					  System.out.println(" Cant take any action , as the event has been recorded already ");
					  // not sure if i want to add the flag to show the warning or not .... will decide later on 
					  
				  }else {
					  ledger.setCheckedInDateTime(new Date());
					//  ledger.setPickupReady("Y");
					 // ledger.setWontPickUpDateTime(null);
					 // ledger.setCancelPickupDateTime(null);
					 // ledger.setPickedUpDateTime(null);
					  session.saveOrUpdate(ledger);
				  }
			  }
		  }
		// KidEventXref xref = (KidEventXref)session.get(KidEventXref.class, xrefId);

		 }else {
			 
		
			 System.out.println("*** no xref could be located ");
		 }
		 
		 
		 
		 return ledger;
		 
	 }
	 
	 
 
 public KidEventXrefLedger checkInVerified ( String xrefId ) {
	 
	 System.out.println(" Going to unregister event for kid " + xrefId);
	 Session session = sessionFactory.getCurrentSession();
	 
	 
	 KidEventXrefLedger ledger = null;
	 
	 //Date todaysDate = new Date();
	 
	 SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
	 
	 String todaysDate = format.format(new Date());
	 
	 System.out.println("Todays date " + todaysDate);
	 // find a xref ledger record for xref id and todays date and if not found , create one and add the data 
	 // if one exist then just add the data  --- 
	 
	 KidEventXref xref = (KidEventXref)session.get(KidEventXref.class, xrefId);
	 
	 if ( xref != null ) {
		 // move to get the next step 
	 
	  Query query = session.createQuery("Select k FROM  KidEventXrefLedger as k where k.kidEventXref.id = :xrefId and k.dateTime= :dateTime").setString("xrefId", xrefId).setString("dateTime", todaysDate);
	  
	  List list = query.list();
	  
	  if ( list == null || list.size() == 0 ) {
		  // no record exist yet so create one 
		  ledger = new KidEventXrefLedger();
		  ledger.setId(java.util.UUID.randomUUID().toString());
		  ledger.setKidEventXref(xref);
		  ledger.setCheckInVerifiedDateTime(new Date());
		 // ledger.setPickupReady("Y");
		  ledger.setDateTime(todaysDate);
		  
		  
		  session.saveOrUpdate(ledger);
	  }else {
		  ledger = (KidEventXrefLedger)list.get(0);
		  
		  if ( ledger != null ) {
			  
			  System.out.println("*** Ledger already exist for today " + ledger);
			  if ( ledger.getCheckInVerifiedDateTime() != null) {
				  System.out.println(" Cant take any action , as the event has been recorded already ");
				  // not sure if i want to add the flag to show the warning or not .... will decide later on 
				  
			  }else {
				  ledger.setCheckInVerifiedDateTime(new Date());
				//  ledger.setPickupReady("Y");
				 // ledger.setWontPickUpDateTime(null);
				 // ledger.setCancelPickupDateTime(null);
				 // ledger.setPickedUpDateTime(null);
				  session.saveOrUpdate(ledger);
			  }
		  }
	  }
	// KidEventXref xref = (KidEventXref)session.get(KidEventXref.class, xrefId);

	 }else {
		 
	
		 System.out.println("*** no xref could be located ");
	 }
	 
	 
	 
	 return ledger;
	 
 }
 
 public KidEventXrefLedger walkInDropOff ( String xrefId ) {
	 
	 System.out.println(" Going to unregister event for kid " + xrefId);
	 Session session = sessionFactory.getCurrentSession();
	 
	 
	 KidEventXrefLedger ledger = null;
	 
	 //Date todaysDate = new Date();
	 
	 SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
	 
	 String todaysDate = format.format(new Date());
	 
	 System.out.println("Todays date " + todaysDate);
	 // find a xref ledger record for xref id and todays date and if not found , create one and add the data 
	 // if one exist then just add the data  --- 
	 
	 KidEventXref xref = (KidEventXref)session.get(KidEventXref.class, xrefId);
	 
	 if ( xref != null ) {
		 // move to get the next step 
	 
	  Query query = session.createQuery("Select k FROM  KidEventXrefLedger as k where k.kidEventXref.id = :xrefId and k.dateTime= :dateTime").setString("xrefId", xrefId).setString("dateTime", todaysDate);
	  
	  List list = query.list();
	  
	  if ( list == null || list.size() == 0 ) {
		  // no record exist yet so create one 
		  ledger = new KidEventXrefLedger();
		  ledger.setId(java.util.UUID.randomUUID().toString());
		  ledger.setKidEventXref(xref);
		 ledger.setCheckedInDateTime(new Date());
		  ledger.setCheckInVerifiedDateTime(new Date());
		 // ledger.setPickupReady("Y");
		  ledger.setDateTime(todaysDate);
		  
		  
		  session.saveOrUpdate(ledger);
	  }else {
		  ledger = (KidEventXrefLedger)list.get(0);
		  
		  if ( ledger != null ) {
			  
			  System.out.println("*** Ledger already exist for today " + ledger);
			  if ( ledger.getCheckInVerifiedDateTime() != null) {
				  System.out.println(" Cant take any action , as the event has been recorded already ");
				  // not sure if i want to add the flag to show the warning or not .... will decide later on 
				  
			  }else {
				  ledger.setCheckedInDateTime(new Date());
				  ledger.setCheckInVerifiedDateTime(new Date());
				//  ledger.setPickupReady("Y");
				 // ledger.setWontPickUpDateTime(null);
				 // ledger.setCancelPickupDateTime(null);
				 // ledger.setPickedUpDateTime(null);
				  session.saveOrUpdate(ledger);
			  }
		  }
	  }
	// KidEventXref xref = (KidEventXref)session.get(KidEventXref.class, xrefId);

	 }else {
		 
	
		 System.out.println("*** no xref could be located ");
	 }
	 
	 
	 
	 return ledger;
	 
 }
 
 
public KidEventXrefLedger pickupVerified ( String xrefId ) {
	 
	 System.out.println(" Going to unregister event for kid " + xrefId);
	 Session session = sessionFactory.getCurrentSession();
	 
	 
	 KidEventXrefLedger ledger = null;
	 
	 //Date todaysDate = new Date();
	 
	 SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
	 
	 String todaysDate = format.format(new Date());
	 
	 System.out.println("Todays date " + todaysDate);
	 // find a xref ledger record for xref id and todays date and if not found , create one and add the data 
	 // if one exist then just add the data  --- 
	 
	 KidEventXref xref = (KidEventXref)session.get(KidEventXref.class, xrefId);
	 
	 if ( xref != null ) {
		 // move to get the next step 
	 
	  Query query = session.createQuery("Select k FROM  KidEventXrefLedger as k where k.kidEventXref.id = :xrefId and k.dateTime= :dateTime").setString("xrefId", xrefId).setString("dateTime", todaysDate);
	  
	  List list = query.list();
	  
	  if ( list == null || list.size() == 0 ) {
		  // no record exist yet so create one 
		  ledger = new KidEventXrefLedger();
		  ledger.setId(java.util.UUID.randomUUID().toString());
		  ledger.setKidEventXref(xref);
		 ledger.setPickedUpVerifiedDateTime(new Date());
		//  ledger.setCheckInVerifiedDateTime(new Date());
		 // ledger.setPickupReady("Y");
		  ledger.setDateTime(todaysDate);
		  
		  
		  session.saveOrUpdate(ledger);
	  }else {
		  ledger = (KidEventXrefLedger)list.get(0);
		  
		  if ( ledger != null ) {
			  
			  System.out.println("*** Ledger already exist for today " + ledger);
			  if ( ledger.getPickedUpVerifiedDateTime() != null) {
				  System.out.println(" Cant take any action , as the event has been recorded already ");
				  // not sure if i want to add the flag to show the warning or not .... will decide later on 
				  
			  }else {
				  ledger.setPickedUpVerifiedDateTime(new Date());
				//  ledger.setCheckInVerifiedDateTime(new Date());
				//  ledger.setPickupReady("Y");
				 // ledger.setWontPickUpDateTime(null);
				 // ledger.setCancelPickupDateTime(null);
				 // ledger.setPickedUpDateTime(null);
				  session.saveOrUpdate(ledger);
			  }
		  }
	  }
	// KidEventXref xref = (KidEventXref)session.get(KidEventXref.class, xrefId);

	 }else {
		 
	
		 System.out.println("*** no xref could be located ");
	 }
	 
	 
	 
	 return ledger;
	 
 }
 
public KidEventXrefLedger checkOut ( String xrefId ) {
	 
	 System.out.println(" Going to unregister event for kid " + xrefId);
	 Session session = sessionFactory.getCurrentSession();
	 
	 
	 KidEventXrefLedger ledger = null;
	 
	 //Date todaysDate = new Date();
	 
	 SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
	 
	 String todaysDate = format.format(new Date());
	 
	 System.out.println("Todays date " + todaysDate);
	 // find a xref ledger record for xref id and todays date and if not found , create one and add the data 
	 // if one exist then just add the data  --- 
	 
	 KidEventXref xref = (KidEventXref)session.get(KidEventXref.class, xrefId);
	 
	 if ( xref != null ) {
		 // move to get the next step 
	 
	  Query query = session.createQuery("Select k FROM  KidEventXrefLedger as k where k.kidEventXref.id = :xrefId and k.dateTime= :dateTime").setString("xrefId", xrefId).setString("dateTime", todaysDate);
	  
	  List list = query.list();
	  
	  if ( list == null || list.size() == 0 ) {
		  // no record exist yet so create one 
		  ledger = new KidEventXrefLedger();
		  ledger.setId(java.util.UUID.randomUUID().toString());
		  ledger.setKidEventXref(xref);
		// ledger.setCheckedInDateTime(new Date());
		  ledger.setCheckedOutDateTime(new Date());
		 // ledger.setPickupReady("Y");
		  ledger.setDateTime(todaysDate);
		  
		  
		  session.saveOrUpdate(ledger);
	  }else {
		  ledger = (KidEventXrefLedger)list.get(0);
		  
		  if ( ledger != null ) {
			  
			  System.out.println("*** Ledger already exist for today " + ledger);
			  if ( ledger.getCheckedOutDateTime() != null) {
				  System.out.println(" Cant take any action , as the event has been recorded already ");
				  // not sure if i want to add the flag to show the warning or not .... will decide later on 
				  
			  }else {
				  ledger.setCheckedOutDateTime(new Date());
				//  ledger.setCheckInVerifiedDateTime(new Date());
				//  ledger.setPickupReady("Y");
				 // ledger.setWontPickUpDateTime(null);
				 // ledger.setCancelPickupDateTime(null);
				 // ledger.setPickedUpDateTime(null);
				  session.saveOrUpdate(ledger);
			  }
		  }
	  }
	// KidEventXref xref = (KidEventXref)session.get(KidEventXref.class, xrefId);

	 }else {
		 
	
		 System.out.println("*** no xref could be located ");
	 }
	 
	 
	 
	 return ledger;
	 
 }
 public KidEventXrefLedger walkInPickup ( String xrefId ) {
		 
		 System.out.println(" Going to unregister event for kid " + xrefId);
		 Session session = sessionFactory.getCurrentSession();
		 
		 
		 KidEventXrefLedger ledger = null;
		 
		 //Date todaysDate = new Date();
		 
		 SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		 
		 String todaysDate = format.format(new Date());
		 
		 System.out.println("Todays date " + todaysDate);
		 // find a xref ledger record for xref id and todays date and if not found , create one and add the data 
		 // if one exist then just add the data  --- 
		 
		 KidEventXref xref = (KidEventXref)session.get(KidEventXref.class, xrefId);
		 
		 if ( xref != null ) {
			 // move to get the next step 
		 
		  Query query = session.createQuery("Select k FROM  KidEventXrefLedger as k where k.kidEventXref.id = :xrefId and k.dateTime= :dateTime").setString("xrefId", xrefId).setString("dateTime", todaysDate);
		  
		  List list = query.list();
		  
		  if ( list == null || list.size() == 0 ) {
			  // no record exist yet so create one 
			  ledger = new KidEventXrefLedger();
			  ledger.setId(java.util.UUID.randomUUID().toString());
			  ledger.setKidEventXref(xref);
			  ledger.setPickupReadyDateTime(new Date());
			  ledger.setPickedUpDateTime(new Date());
			 // ledger.setPickupReady("Y");
			  ledger.setDateTime(todaysDate);
			  
			  
			  session.saveOrUpdate(ledger);
		  }else {
			  ledger = (KidEventXrefLedger)list.get(0);
			  
			  if ( ledger != null ) {
				  
				  System.out.println("*** Ledger already exist for today " + ledger);
				  if ( ledger.getPickedUpDateTime() != null) {
					  System.out.println(" Cant take any action , as the event has been recorded already ");
					  // not sure if i want to add the flag to show the warning or not .... will decide later on 
					  
				  }else {
					  ledger.setPickupReadyDateTime(new Date());
					  ledger.setPickedUpDateTime(new Date());
					//  ledger.setPickupReady("Y");
					  ledger.setWontPickUpDateTime(null);
					  ledger.setCancelPickupDateTime(null);
					 // ledger.setPickedUpDateTime(null);
					  session.saveOrUpdate(ledger);
				  }
			  }
		  }
		// KidEventXref xref = (KidEventXref)session.get(KidEventXref.class, xrefId);

		 }else {
			 
		
			 System.out.println("*** no xref could be located ");
		 }
		 
		 
		 
		 return ledger;
		 
	 }
 
 
 
 
 public KidEventXrefLedger walkInPickupAC ( String xrefId ) {
	 
	 System.out.println(" Going to unregister event for kid " + xrefId);
	 Session session = sessionFactory.getCurrentSession();
	 
	 
	 KidEventXrefLedger ledger = null;
	 
	 //Date todaysDate = new Date();
	 
	 SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
	 
	 String todaysDate = format.format(new Date());
	 
	 System.out.println("Todays date " + todaysDate);
	 // find a xref ledger record for xref id and todays date and if not found , create one and add the data 
	 // if one exist then just add the data  --- 
	 
	 KidEventXref xref = (KidEventXref)session.get(KidEventXref.class, xrefId);
	 
	 if ( xref != null ) {
		 // move to get the next step 
	 
	  Query query = session.createQuery("Select k FROM  KidEventXrefLedger as k where k.kidEventXref.id = :xrefId and k.dateTime= :dateTime").setString("xrefId", xrefId).setString("dateTime", todaysDate);
	  
	  List list = query.list();
	  
	  if ( list == null || list.size() == 0 ) {
		  // no record exist yet so create one 
		  ledger = new KidEventXrefLedger();
		  ledger.setId(java.util.UUID.randomUUID().toString());
		  ledger.setKidEventXref(xref);
		  ledger.setPickedUpVerifiedDateTime(new Date());
		  ledger.setPickedUpDateTime(new Date());
		 // ledger.setPickupReady("Y");
		  ledger.setDateTime(todaysDate);
		  
		  
		  session.saveOrUpdate(ledger);
	  }else {
		  ledger = (KidEventXrefLedger)list.get(0);
		  
		  if ( ledger != null ) {
			  
			  System.out.println("*** Ledger already exist for today " + ledger);
			  if ( ledger.getPickedUpVerifiedDateTime() != null) {
				  System.out.println(" Cant take any action , as the event has been recorded already ");
				  // not sure if i want to add the flag to show the warning or not .... will decide later on 
				  
			  }else {
				  ledger.setPickedUpVerifiedDateTime(new Date());
				  ledger.setPickedUpDateTime(new Date());
				//  ledger.setPickupReady("Y");
				  ledger.setWontPickUpDateTime(null);
				  ledger.setCancelPickupDateTime(null);
				 // ledger.setPickedUpDateTime(null);
				  session.saveOrUpdate(ledger);
			  }
		  }
	  }
	// KidEventXref xref = (KidEventXref)session.get(KidEventXref.class, xrefId);

	 }else {
		 
	
		 System.out.println("*** no xref could be located ");
	 }
	 
	 
	 
	 return ledger;
	 
 }
	 
 public KidEventXrefLedger wontPickup ( String xrefId ) {
		 
		 System.out.println(" Going to wont pickup event for kid " + xrefId);
		 Session session = sessionFactory.getCurrentSession();
		 
		 
		 KidEventXrefLedger ledger = null;
		 
		 //Date todaysDate = new Date();
		 
		 SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		 
		 String todaysDate = format.format(new Date());
		 
		 System.out.println("Todays date " + todaysDate);
		 // find a xref ledger record for xref id and todays date and if not found , create one and add the data 
		 // if one exist then just add the data  --- 
		 
		 KidEventXref xref = (KidEventXref)session.get(KidEventXref.class, xrefId);
		 
		 if ( xref != null ) {
			 // move to get the next step 
		 
		  Query query = session.createQuery("Select k FROM  KidEventXrefLedger as k where k.kidEventXref.id = :xrefId and k.dateTime= :dateTime").setString("xrefId", xrefId).setString("dateTime", todaysDate);
		  
		  List list = query.list();
		  
		  if ( list == null || list.size() == 0 ) {
			  // no record exist yet so create one 
			  ledger = new KidEventXrefLedger();
			  ledger.setId(java.util.UUID.randomUUID().toString());
			  ledger.setKidEventXref(xref);
			//  ledger.setPickupReadyDateTime(new Date());
			 // ledger.setWontPickupFlag("Y");
			  ledger.setWontPickUpDateTime(new Date());
			  ledger.setDateTime(todaysDate);
			  
			  session.saveOrUpdate(ledger);
		  }else {
			  ledger = (KidEventXrefLedger)list.get(0);
			  
			  if ( ledger != null ) {
				  
				  System.out.println("*** Ledger already exist for today " + ledger);
				  if ( ledger.getWontPickUpDateTime() != null) {
					  System.out.println(" Cant take any action , as the event has been recorded already ");
					  // not sure if i want to add the flag to show the warning or not .... will decide later on 
					  
				  }else {
					  ledger.setPickupReadyDateTime(null);
					  ledger.setCancelPickupDateTime(null);
					  ledger.setPickedUpDateTime(null);
					  ledger.setWontPickUpDateTime(new Date());
					//  ledger.setWontPickupFlag("Y");
					  session.saveOrUpdate(ledger);
				  }
			  }
		  }
		// KidEventXref xref = (KidEventXref)session.get(KidEventXref.class, xrefId);

		 }else {
			 
		
			 System.out.println("*** no xref could be located ");
		 }
		 
		 
		 
		 return ledger;
		 
	 }
public KidEventXrefLedger cancelPickup ( String xrefId ) {
		 
		 System.out.println(" Going to cancel event for kid " + xrefId);
		 Session session = sessionFactory.getCurrentSession();
		 
		 
		 KidEventXrefLedger ledger = null;
		 
		 //Date todaysDate = new Date();
		 
		 SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		 
		 String todaysDate = format.format(new Date());
		 
		 System.out.println("Todays date " + todaysDate);
		 // find a xref ledger record for xref id and todays date and if not found , create one and add the data 
		 // if one exist then just add the data  --- 
		 
		 KidEventXref xref = (KidEventXref)session.get(KidEventXref.class, xrefId);
		 
		 if ( xref != null ) {
			 // move to get the next step 
		 
		  Query query = session.createQuery("Select k FROM  KidEventXrefLedger as k where k.kidEventXref.id = :xrefId and k.dateTime= :dateTime").setString("xrefId", xrefId).setString("dateTime", todaysDate);
		  
		  List list = query.list();
		  
		  if ( list == null || list.size() == 0 ) {
			  // no record exist yet so create one 
			//  ledger = new KidEventXrefLedger();
			//  ledger.setId(java.util.UUID.randomUUID().toString());
			//  ledger.setKidEventXref(xref);
			//  ledger.setPickupReadyDateTime(new Date());
			//  ledger.setPickupReady("Y");
			//  ledger.setDateTime(todaysDate);
			  
			//  session.saveOrUpdate(ledger);
			  
			  System.out.println(" Error condition - should not have happened");
		  }else {
			  ledger = (KidEventXrefLedger)list.get(0);
			  
			  if ( ledger != null ) {
				  
				  System.out.println("*** Ledger already exist for today " + ledger);
				 // if ( ledger.getPickupReadyDateTime() != null) {
					 // System.out.println(" Cant take any action , as the event has been recorded already ");
					  // not sure if i want to add the flag to show the warning or not .... will decide later on 
					  
				 // }else {
					  ledger.setPickupReadyDateTime(null);
					//  ledger.setPickupReady(null);
					  ledger.setWontPickUpDateTime(null);
					//  ledger.setWontPickupFlag(null);
					  ledger.setCancelPickupDateTime(new Date());
					  session.saveOrUpdate(ledger);
				  //}
			  }
		  }
		// KidEventXref xref = (KidEventXref)session.get(KidEventXref.class, xrefId);

		 }else {
			 
		
			 System.out.println("*** no xref could be located ");
		 }
		 
		 
		 
		 return ledger;
		 
	 }
	 

public KidEventXrefLedger pickedUp ( String xrefId ) {
	 
	 System.out.println(" Going to unregister event for kid " + xrefId);
	 Session session = sessionFactory.getCurrentSession();
	 
	 
	 KidEventXrefLedger ledger = null;
	 
	 //Date todaysDate = new Date();
	 
	 SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
	 
	 String todaysDate = format.format(new Date());
	 
	 System.out.println("Todays date " + todaysDate);
	 // find a xref ledger record for xref id and todays date and if not found , create one and add the data 
	 // if one exist then just add the data  --- 
	 
	 KidEventXref xref = (KidEventXref)session.get(KidEventXref.class, xrefId);
	 
	 if ( xref != null ) {
		 // move to get the next step 
	 
	  Query query = session.createQuery("Select k FROM  KidEventXrefLedger as k where k.kidEventXref.id = :xrefId and k.dateTime= :dateTime").setString("xrefId", xrefId).setString("dateTime", todaysDate);
	  
	  List list = query.list();
	  
	  if ( list == null || list.size() == 0 ) {
		  // no record exist yet so create one 
		  ledger = new KidEventXrefLedger();
		  ledger.setId(java.util.UUID.randomUUID().toString());
		  ledger.setKidEventXref(xref);
		 ledger.setPickedUpDateTime(new Date());
		//  ledger.setCheckInVerifiedDateTime(new Date());
		 // ledger.setPickupReady("Y");
		  ledger.setDateTime(todaysDate);
		  
		  
		  session.saveOrUpdate(ledger);
	  }else {
		  ledger = (KidEventXrefLedger)list.get(0);
		  
		  if ( ledger != null ) {
			  
			  System.out.println("*** Ledger already exist for today " + ledger);
			  if ( ledger.getPickedUpDateTime() != null) {
				  System.out.println(" Cant take any action , as the event has been recorded already ");
				  // not sure if i want to add the flag to show the warning or not .... will decide later on 
				  
			  }else {
				  ledger.setPickedUpDateTime(new Date());
				//  ledger.setCheckInVerifiedDateTime(new Date());
				//  ledger.setPickupReady("Y");
				 // ledger.setWontPickUpDateTime(null);
				 // ledger.setCancelPickupDateTime(null);
				 // ledger.setPickedUpDateTime(null);
				  session.saveOrUpdate(ledger);
			  }
		  }
	  }
	// KidEventXref xref = (KidEventXref)session.get(KidEventXref.class, xrefId);

	 }else {
		 
	
		 System.out.println("*** no xref could be located ");
	 }
	 
	 
	 
	 return ledger;
	 
}





	 
 public KidEventXrefLedger pickedUp ( String eventId , String kidId ) {
	 System.out.println(" Going to set Picked UP  event for kid ");
	 Session session = sessionFactory.getCurrentSession();
	 
	 
	 KidEventXrefLedger ledger = null;
	 
	 //Date todaysDate = new Date();
	 
	 SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
	 
	 String todaysDate = format.format(new Date());
	 
	 System.out.println("Todays date " + todaysDate);
	 
	 KidEventXref xref = null;
	 // first find which xref id we are talking about - get the xref id and then do the rest of the work - 
	 
	 Query query = session.createQuery("Select k FROM  KidEventXref as k where k.kidProfile.userid.id = :kidUserId and k.event.id = :eventId").setString("kidUserId", kidId).setString("eventId" ,  eventId);
	  
	  List list = query.list();
	  
	  if ( list == null || list.size() == 0 ) {
			System.out.println("*** ERROR condition ");
	  }else {
		  xref = (KidEventXref)list.get(0);
		  // already exist - so dont register again 
		  System.out.println("Registration  already exist for this event and kiddo " + xref.toString());
		  System.out.println("Registration  already exist for this event and kiddo " + xref.getId());
		 // xref = (KidEventXref)list.get(0);
		  
	  }
	 // find a xref ledger record for xref id and todays date and if not found , create one and add the data 
	 // if one exist then just add the data  --- 
	 
	// KidEventXref xref = (KidEventXref)session.get(KidEventXref.class, xrefId);
	 
	 if ( xref != null ) {
		 // move to get the next step 
	 
	   query = session.createQuery("Select k FROM  KidEventXrefLedger as k where k.kidEventXref.id = :xrefId and k.dateTime= :dateTime").setString("xrefId", xref.getId()).setString("dateTime", todaysDate);
	  
	   list = query.list();
	  
	  if ( list == null || list.size() == 0 ) {
		  // no record exist yet so create one 
		  ledger = new KidEventXrefLedger();
		  ledger.setId(java.util.UUID.randomUUID().toString());
		  ledger.setKidEventXref(xref);
		 // ledger.setPickupReadyDateTime(new Date());
		 // ledger.setPickupReady("Y");
		  ledger.setDateTime(todaysDate);
		  ledger.setPickedUpDateTime(new Date());
		  session.saveOrUpdate(ledger);
	  }else {
		  ledger = (KidEventXrefLedger)list.get(0);
		  
		  if ( ledger != null ) {
			  
			  System.out.println("*** Ledger already exist for today " + ledger);
			  if ( ledger.getPickedUpDateTime() != null) {
				  System.out.println(" Cant take any action , as the kid picked up date time  has been recorded already ");
				  // not sure if i want to add the flag to show the warning or not .... will decide later on 
				  
			  }else {
				 // ledger.setPickupReadyDateTime(new Date());
				  ledger.setPickedUpDateTime(new Date());
				 // ledger.setPickupReady("Y");
				  session.saveOrUpdate(ledger);
			  }
		  }
	  }
	// KidEventXref xref = (KidEventXref)session.get(KidEventXref.class, xrefId);

	 }else {
		 
	
		 System.out.println("*** no xref could be located ");
	 }
	 
	 
	 
	 return ledger;
		 
	 }
 
 
 
 public void unRegisterEvent ( String xrefId , String userId) {
		 
		 // later on i need to make sure that there are not an entry existing for these three guys and if yes then just update the time stamp or dont do anything.
		 
		 System.out.println(" Going to unregister event for kid " + xrefId);
		 Session session = sessionFactory.getCurrentSession();
		 
		 User user = (User)session.get(User.class, userId);
		 
		 KidEventXref xref = (KidEventXref)session.get(KidEventXref.class, xrefId);

		 
		 if ( xref != null ) {
			 
			 System.out.println("**** xref located ");
			 // can validate it also to make sure that all the parent and kid user id are correct 
			 xref.setUnRegisterDate(new Date());
			 xref.setUnRegisterUserId(user);
			 xref.setActive("0");
			 session.saveOrUpdate(xref);
			// session.delete(xref);
		 }else {
			 System.out.println("*** no xref could be located ");
		 }
		
		 
	 }
	 
	 /**
	  * Retrieves all persons
	  *
	  * @return a list of persons
	  */
	 public List<Event> getEventListByUser( String userId) {
	  //logger.debug("Retrieving all persons");
	   
	  // Retrieve session from Hibernate
	  Session session = sessionFactory.getCurrentSession();
	   
	  KidEventXrefLedger ledger = null;
		 
		 //Date todaysDate = new Date();
		 
		 SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		 
		 String todaysDate = format.format(new Date());
		 
		 System.out.println("Todays date " + todaysDate);
	  // Create a Hibernate query (HQL)
	  Query query = session.createQuery("Select e FROM  Event as e  left join e.eventUserSet as u where u.id = :id").setString("id", userId);
	  
	   
	  // Retrieve all
	  List list =   query.list();
	  
	  
	  System.out.println("**** Event list size " + list.size());
		
		for ( int i = 0 ; i < list.size() ; i++ ) {
			
			Event event = (Event)list.get(i);
			System.out.println("Event desc " + event.getDescription());
			System.out.println("Event desc " + event.getEventType().getDescription());
			System.out.println("Event desc " + event.getEventType().getEventActionsSet().size());
			
			// for total count - get all the registered users for this event id where row is acive - 
			 Query  query2 = session.createQuery("Select k FROM  KidEventXref as k where  k.event.id = :eventId").setString("eventId" ,  event.getId());
			  List kidList = query2.list();
			  
			  System.out.println("*** total reg users " + kidList.size());
			  event.setTotalCount( "" + kidList.size());
			  event.setNotPickedCount("" + kidList.size());
			  event.setNotDroppedOffCount("" + kidList.size());
			  for ( int k = 0 ; k < kidList.size() ; k++ ) {
				  
				  
				  KidEventXref xref = (KidEventXref) kidList.get(k);
				  
				  
				  if ( xref != null ) {
						 // move to get the next step 
					 
					   Query query3 = session.createQuery("Select k FROM  KidEventXrefLedger as k where k.kidEventXref.id = :xrefId and k.dateTime= :dateTime").setString("xrefId", xref.getId()).setString("dateTime", todaysDate);
					  
					   List list4 = query3.list();
					  
					 
					 if ( list4 != null && list4.size() > 0 ) {
						  ledger = (KidEventXrefLedger)list4.get(0);
						  
						  if ( ledger != null ) {
							  
							  System.out.println("*** Ledger already exist for today " + ledger);
							  
							  // now dependig on which event we are interested in , we will do different counts - 
							  
							  if ( event.getId().equals("1")) {
								  
								  if ( ledger.getPickupReadyDateTime() != null  && ledger.getPickedUpDateTime() == null ) {
									  
									  
									//  event.setInactiveCount("" + (new Integer (event.getInactiveCount()).intValue() + 1));
								 // } else {
									  event.setReadyCount("" + (new Integer (event.getReadyCount()).intValue() + 1));
								  //}
								  }
								  
								  if ( ledger.getPickedUpDateTime() != null  && ledger.getPickupReadyDateTime() != null ) {
									  event.setPickedUpCount("" + (new Integer (event.getPickedUpCount()).intValue() + 1));
								 // }else  {
									  event.setNotPickedCount("" + (new Integer (event.getNotPickedCount()).intValue() - 1));
								  //}
								  
								  }
								  
							  }else if ( event.getId().equals("2")) {
								  
								  
								  if ( ledger.getCheckedInDateTime() != null  && ledger.getCheckInVerifiedDateTime() == null ) {
									  event.setCheckedInCount("" + (new Integer (event.getCheckedInCount()).intValue() + 1));
								  }
								 // }else {
									//  event.setNotDroppedOffCount("" + (new Integer (event.getNotDroppedOffCount()).intValue() + 1));
								 // }
								  
								  if ( ledger.getCheckInVerifiedDateTime() != null  && ledger.getCheckedInDateTime() != null ) {
									  event.setDroppedOffCount( "" + (new Integer (event.getDroppedOffCount()).intValue() + 1) );
									  event.setNotDroppedOffCount("" + ( new Integer ( event.getNotDroppedOffCount()).intValue() - 1  ));
								  }
								  
								  
								  if ( ledger.getCheckedOutDateTime() != null ) {
									  event.setCheckedOutCount("" + (new Integer (event.getCheckedOutCount()).intValue() + 1));
								  }
								  
								  
							  }else if ( event.getId().equals("3")) {
								  
								  
								  
								  if ( ledger.getCheckedInDateTime() != null ) {
									  event.setCheckedInCount("" + (new Integer (event.getCheckedInCount()).intValue() + 1));
								  }
								  
								  if ( ledger.getPickedUpDateTime() != null ) {
									  event.setPickedUpCount("" + (new Integer (event.getPickedUpCount()).intValue() + 1));
									  event.setNotPickedCount("" + (new Integer (event.getNotPickedCount()).intValue() - 1));
								//  }else {
								//	  event.setNotPickedCount("" + (new Integer (event.getNotPickedCount()).intValue() + 1));
								 // }
								  }
								  
								  
								  if ( ledger.getPickedUpVerifiedDateTime() != null ) {
									  event.setCheckedOutCount("" + (new Integer (event.getCheckedOutCount()).intValue() + 1));
								  }
								  
								  
							  }else {
								  System.out.println("*** Some other event to be handled");
							  }
							  
						  }
					  }
					// KidEventXref xref = (KidEventXref)session.get(KidEventXref.class, xrefId);

					 }else {
						 
					
						 System.out.println("*** no xref could be located ");
					 }
				  
				  
			  }
			  
			  System.out.println("*** event details " + event.toString());
		}
		
		
		System.out.println("**** Reached this point or not  " +list.size() );
		
		return list;
	 }
	 
	 
	 
	 /**
	  * Retrieves all persons
	  *
	  * @return a list of persons
	  */
	 public List<Event> getUnregisteredEventListByUserId( String userId) {
	  //logger.debug("Retrieving all persons");
	   
	  // Retrieve session from Hibernate
	  Session session = sessionFactory.getCurrentSession();
	  
	  
	 // Query query = session.createQuery("Select k FROM  KidProfile as e  left join e.eventGroupSet as u where u.id in (" + groupIds + ")" );
	  KidProfile profile = this.kidProfileService.getKidById(userId);
	  
	  if ( profile != null ) {
		  System.out.println("Kid profile " + profile);
		  System.out.println("Kid profile " + profile.getEventGroup().getId());
		  
		  Date date = new Date();
		  String groupIds = "4" + "," + profile.getEventGroup().getId();
		   
		  // Create a Hibernate query (HQL)
		  Query query = session.createQuery("Select e FROM  Event as e  left join e.eventGroupSet as u where u.id in (" + groupIds + ") and e.regEndDate >= :regEndDate)").setDate("regEndDate", date);
		  
		   
		  // Retrieve all
		  List list =   query.list();
		  
	  
		  System.out.println("**** Event list size " + list.size());
			
			for ( int i = 0 ; i < list.size() ; i++ ) {
				
				Event event = (Event)list.get(i);
				System.out.println("Event desc " + event.getDescription());
				System.out.println("Event desc " + event.getEventType().getDescription());
				System.out.println("Event desc " + event.getRegStartDate());
				
				if ( event.getRegStartDate().after(date)) {
					
					// evnet is not ready for registration as the current date is before reg start date
					event.setEventRegistrationState("0");
				}else {
					// regstration starts-- as reg is open 
					event.setEventRegistrationState("1");
				}
				//System.out.println("Event desc " + event.getEventType().getEventActionsSet().size());
				
				
			}
			
			return list;
		 }else {
			 System.out.println("ERR OR - kid not located ");
			 return null;
		 }
	 }
}
