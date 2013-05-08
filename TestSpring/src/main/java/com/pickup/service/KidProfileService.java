package com.pickup.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pickup.model.KidProfile;
import com.pickup.model.User;

@Service
public interface KidProfileService {

	 public ArrayList<KidProfile> getKidProfileListByParentId( String userId);
	 
	 public List getKidEventsById( String userId) ;
	 
	 public KidProfile getKidById( String userId) ;
	 
	 public ArrayList<KidProfile> getKidProfileListByEventId( String eventId) ;
	 
	 public ArrayList<KidProfile> getKidProfileListByEventIdAndPickupReady( String eventId) ;
	 
	 public ArrayList<KidProfile> getKidProfileListByEventIdAndPickedUp( String eventId) ;
	 
	 public ArrayList<KidProfile> getKidProfileListByEventIdAndWaiting( String eventId);
	 
	 public ArrayList<KidProfile> getKidProfileListByEventIdAndCheckedInAndPickedUp( String eventId);
	 
	 public ArrayList<KidProfile> getKidProfileListByEventIdAndCheckedIn( String eventId) ;
}
