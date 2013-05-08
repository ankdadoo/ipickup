package com.pickup.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pickup.model.Event;
import com.pickup.model.KidEventXref;
import com.pickup.model.KidEventXrefLedger;

@Service
public interface EventService {

	public List getEventList();
	
	 public List<Event> getEventListByUser( String userId) ;
	 
	 public KidEventXref registerEvent ( String parentUserId , String eventId , String kidUserId);
	 public List<Event> getUnregisteredEventListByUserId( String userId);
	 
	 public void unRegisterEvent ( String xrefId , String userid);
	 
	 public KidEventXrefLedger readyForPickup ( String xrefId );
	 
	// public KidEventXrefLedger pickedUp ( String xrefId );
	 
	 public KidEventXrefLedger pickedUp ( String xrefId );
	 
	 public KidEventXrefLedger cancelPickup ( String xrefId );
	 
	 public KidEventXrefLedger wontPickup ( String xrefId ) ;
	 
	 public KidEventXrefLedger walkInPickup ( String xrefId );
	 
	 
	 public KidEventXrefLedger walkInPickupAC ( String xrefId );
	 
	 
	 public KidEventXrefLedger checkIn ( String xrefId );
	 
	 public KidEventXrefLedger checkInVerified ( String xrefId );
	 
	 public KidEventXrefLedger walkInDropOff ( String xrefId );
	 
	 public KidEventXrefLedger checkOut ( String xrefId );
	 
	/// public KidEventXrefLedger pickedUpVerified ( String eventId , String kidId );
	 
	 public KidEventXrefLedger pickupVerified ( String xrefId );
}
