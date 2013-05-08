package com.pickup.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;



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
import com.pickup.model.KidEventXrefLedger;
import com.pickup.model.KidProfile;
import com.pickup.model.User;
import com.pickup.service.EventService;
import com.pickup.service.KidProfileService;
import com.pickup.service.UserService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class KidProfileController {
	
	private static final Logger logger = LoggerFactory.getLogger(KidProfileController.class);
	
	
@Autowired
private KidProfileService kidProfileService;


@Autowired
private EventService eventService;
	
	

	@RequestMapping(method=RequestMethod.GET, value="/getKids", headers="Accept=application/json")
	public @ResponseBody ExtResult getKids(@RequestParam("parentUserId")  String userId  , HttpServletResponse response  ) throws Exception{
		//Employee e = employeeDS.get(Long.parseLong(id));
		System.out.println("***Get Kidsby  User");
		System.out.println("***kid id " + userId);
	
		ArrayList<KidProfile> list = kidProfileService.getKidProfileListByParentId(userId);
		
		System.out.println("**** list size " + list.size());
		
		
		//return list;
		//ObjectMapper mapper = new ObjectMapper();
	
		//mapper.writeValue(response.getWriter(), list);
		ExtResult extResult = new ExtResult();
		
		extResult.setData(list);
		///ListWrapper wrapper = new ListWrapper(list);
		return extResult;
	}
	
	

	@RequestMapping(method=RequestMethod.GET, value="/getKidsForEvent", headers="Accept=application/json")
	public @ResponseBody ExtResult getKidsForEvent(@RequestParam("eventId")  String eventId , 
			@RequestParam("listType")  String listType  
			, HttpServletResponse response  ) throws Exception{
		//Employee e = employeeDS.get(Long.parseLong(id));
		System.out.println("***Get list type " + listType);
		System.out.println("***Login User" + eventId);
	
		ArrayList<KidProfile> list = null ;
		ArrayList<KidProfile> returnList = new ArrayList() ;
		if ( listType.equals("ALL")) {
		list = kidProfileService.getKidProfileListByEventId(eventId);
		}else if ( listType.equals("ReadyForPickup")) {
			if ( eventId.equals("1")) {
				list = kidProfileService.getKidProfileListByEventIdAndPickupReady(eventId);
			}else if ( eventId.equals("2")) {
				list = kidProfileService.getKidProfileListByEventIdAndCheckedIn(eventId);
			}else if ( eventId.equals("3")) {
				list = kidProfileService.getKidProfileListByEventIdAndCheckedInAndPickedUp(eventId);
			}else {
				System.out.println("***** ERROR - list type expected but didnt got any ");
				list = new ArrayList();
			}
		}else {
			System.out.println("***** ERROR - list type expected but didnt got any ");
			list = kidProfileService.getKidProfileListByEventId(eventId);
		}
		System.out.println("**** list size " + list.size());
		
		
		if ( list.size() > 0 ) {
			
			
			for ( int i = 0 ; i < list.size(); i++) {
				
				KidProfile kid = (KidProfile)list.get(i);
				
				if ( kid != null ) {
					
					System.out.println("### Event id " + eventId);
					// setting initial state depending on each event type
					if ( eventId.equals("1")) {
						 kid.getUserid().setState("NotPicked");
						 kid.getUserid().setStateDescription("Not Picked Up");
					}else if ( eventId.equals("2")) {
						 kid.getUserid().setState("NotCheckedIn");
						 kid.getUserid().setStateDescription("Not Checked In");
					}else {
						 kid.getUserid().setState("NotCheckedIn");
						 kid.getUserid().setStateDescription("Not Checked In");
					}
					Iterator it = kid.getRegisteredEventSet().iterator();
					
					while ( it.hasNext()) {
						
						KidEventXref xref = (KidEventXref)it.next();
						
						kid.getUserid().setKidEventXrefId(xref.getId());
						kid.getUserid().setKidSchoolId(kid.getSchoolId());
						// so i willl find this xref id and can use this to be passed back in profile id 
						// next time when user makes the action then i will just take this xref id and look for the record in xref table
						// and do the action 
						if ( xref.getEvent().getId().equals(eventId)) { // for this kid and for this event - 
							
							//we found the match so now we can look at the ledger for today 
							
							Set  ledgerSet = xref.getKidEventXrefLedgerSet();
							
							
							if ( ledgerSet.size() == 0 ) {
								 if ( listType.equals("NotPickedUp") ||   listType.equals("Total") || listType.equals("ReadyForPickup") || listType.equals("ALL")) {
										
									   
									 returnList.add(kid);
									 }
							}
							
							// and i need to get the one for today right -- to check the things - 
							boolean foundLedger = false ;
							SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
							 
							 String todaysDate = format.format(new Date());
							 
							 System.out.println("*** going to check for 222ledgers");
							 Iterator its = ledgerSet.iterator();
							 while ( its.hasNext()) {
							 	 
								 
								 KidEventXrefLedger ledger = (KidEventXrefLedger)its.next();
								 
								 if ( ledger.getDateTime().equals(todaysDate)) {
									 
									 System.out.println("*** found the right ledger ");
									 
									 
									 if ( eventId.equals("1")) {
										 
										 
										 if ( ledger.getPickupReadyDateTime() != null ) {
												kid.getUserid().setState("Ready");

												 kid.getUserid().setStateDescription("Ready");
											 }
											 if ( ledger.getPickedUpDateTime() != null) {
												 kid.getUserid().setState("PickedUp");

												 kid.getUserid().setStateDescription("Picked Up");
											 }
											 if (ledger.getWontPickUpDateTime() != null) {
												 kid.getUserid().setState("Wont");

												 kid.getUserid().setStateDescription("Won't Pick Up");
											 }
										 
										 
									 }else if ( eventId.equals("2")) {
										 
										 if ( ledger.getCheckedInDateTime() != null ) {
											 kid.getUserid().setState("CheckedIn");

											 kid.getUserid().setStateDescription("Checked In");
										 }
										 
										 if ( ledger.getCheckInVerifiedDateTime() != null ) {
											 kid.getUserid().setState("CheckedInVerified");

											 kid.getUserid().setStateDescription("Dropped Off");
										 }
										 
										 
										 if ( ledger.getCheckedOutDateTime() != null ) {
											 kid.getUserid().setState("CheckedOut");

											 kid.getUserid().setStateDescription("Checked Out");
										 }
										 
										 
										 
									 }else if ( eventId.equals("3")) {
										 
										 if ( ledger.getCheckedInDateTime() != null ) {
											 kid.getUserid().setState("CheckedIn");

											 kid.getUserid().setStateDescription("Checked In");
										 }
										 
										 if ( ledger.getPickedUpDateTime() != null) {
											 kid.getUserid().setState("PickedUp");

											 kid.getUserid().setStateDescription("Picked Up");
										 }
										 
										 if ( ledger.getPickedUpVerifiedDateTime() != null ) {
											 kid.getUserid().setState("PickedUpVerified");

											 kid.getUserid().setStateDescription("Checked Out");
										 }
										 
									 }
									
									 // found the right one for the date in question ....
									
									
									 if ( listType.equals("Total") || listType.equals("ReadyForPickup") || listType.equals("ALL")) {
											
										 returnList.add( kid);
													
									}else if ( listType.equals("Active")) {
										if ( ledger.getPickupReadyDateTime() != null && ledger.getPickedUpDateTime() == null ) {
											returnList.add( kid);
										}
									}else if ( listType.equals("PickedUp")) {
										if ( ledger.getPickedUpDateTime() != null  ) {
											returnList.add(kid);
										}
									}else if ( listType.equals("NotActive")) {
										if ( ledger.getPickupReadyDateTime() == null  ) {
											returnList.add(kid);
										}
									}else if ( listType.equals("NotPickedUp")) {
										if ( ledger.getPickupReadyDateTime() == null  ) {
											returnList.add(kid);
										}
									}else if ( listType.equals("NotPickedUpAC")) {
										if ( ledger.getCheckedInDateTime() != null && ledger.getPickedUpDateTime() ==  null  ) {
											returnList.add(kid);
										}
									}else if ( listType.equals("CheckedIn")) {
										if ( ledger.getCheckedInDateTime() != null  ) {
											returnList.add(kid);
										}
									}else if ( listType.equals("DroppedOff")) {
										if ( ledger.getCheckInVerifiedDateTime() != null  ) {
											returnList.add(kid);
										}
									}else if ( listType.equals("NotDroppedOff")) {
										if ( ledger.getCheckedInDateTime() != null && ledger.getCheckInVerifiedDateTime() == null  ) {
											returnList.add(kid);
										}
									}else if ( listType.equals("CheckedOut")) {
										if ( ledger.getCheckedOutDateTime() != null  ) {
											returnList.add(kid);
										}
									}else if ( listType.equals("CheckedOutAC")) {
										if ( ledger.getPickedUpVerifiedDateTime() != null  ) {
											returnList.add(kid);
										}
									}
											
										
											
											
											
											
											
										
										
									 
									foundLedger = true ;
									 
									 
									 break;
								 }
								 
							 }
							 
							 if ( !foundLedger) {
								 if ( listType.equals("NotPickedUp") ||   listType.equals("Total") || listType.equals("ReadyForPickup") || listType.equals("ALL")) {
										
									   
								 returnList.add(kid);
								 }
							 }
							 
							 break;
							
						}
					}
					
				}
				
				
				
			}
			
		}
		
		
		// now we can add either the column to say what kind of logic i want to add - 
		
		
		
		
		
		
		
		
		ExtResult extResult = new ExtResult();
		
		System.out.println("*** return list size " + returnList.size());
		extResult.setData(returnList);
		///ListWrapper wrapper = new ListWrapper(list);
		return extResult;
	}
	
	
	

	@RequestMapping(method=RequestMethod.GET, value="/registerEvent", headers="Accept=application/json")
	public @ResponseBody ExtResult registerEvent(@RequestParam("parentUserId")  String parentUserId  ,
			@RequestParam("eventId")  String eventId , @RequestParam("kidUserId")  String kidUserId, HttpServletResponse response  ) throws Exception{
		//Employee e = employeeDS.get(Long.parseLong(id));
		System.out.println("***Register for events ");
		System.out.println("***Login User" + parentUserId);
	
		
		
		
		
		KidEventXref xref = eventService.registerEvent(parentUserId, eventId, kidUserId);
		
		
	
		ExtResult extResult = new ExtResult();
		extResult.setMsg(xref.getKidProfile().getUserid().getFirstName() + "  "  + xref.getKidProfile().getUserid().getLastName() + " registered for " + xref.getEvent().getDescription() + " at " + new Date().toString());
		extResult.setData(xref);
		///ListWrapper wrapper = new ListWrapper(list);
		return extResult;
	}
	
	
	
	@RequestMapping(method=RequestMethod.GET, value="/unRegisterEvent", headers="Accept=application/json")
	public @ResponseBody ExtResult unRegisterEvent(@RequestParam("xrefId")  String xrefId  ,@RequestParam("parentUserId")  String parentUserId, 
			 HttpServletResponse response  ) throws Exception{
		//Employee e = employeeDS.get(Long.parseLong(id));
		System.out.println("***Register for events ");
		System.out.println("***Login User" + xrefId);
	
		
	
		eventService.unRegisterEvent(xrefId , parentUserId);
		
		
	
		ExtResult extResult = new ExtResult();
		extResult.setMsg("Un-Register Event request received successfully at " + new Date().toString());
		extResult.setData("success");
		///ListWrapper wrapper = new ListWrapper(list);
		return extResult;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/cancelPickupAction", headers="Accept=application/json")
	public @ResponseBody ExtResult cancelPickupAction(@RequestParam("xrefId")  String xrefId  ,
			 HttpServletResponse response  ) throws Exception{
		//Employee e = employeeDS.get(Long.parseLong(id));
		System.out.println("***cancel pickup action  for events ");
		System.out.println("***Login User" + xrefId);
	
		
	
		KidEventXrefLedger ledger = eventService.cancelPickup(xrefId);
		
		
	
		ExtResult extResult = new ExtResult();
		extResult.setMsg("Cancel Pickup request received successfully at " + new Date().toString());
		extResult.setData(ledger);
		///ListWrapper wrapper = new ListWrapper(list);
		return extResult;
	}
	
	
	@RequestMapping(method=RequestMethod.GET, value="/wontPickupAction", headers="Accept=application/json")
	public @ResponseBody ExtResult wontPickupAction(@RequestParam("xrefId")  String xrefId  ,
			 HttpServletResponse response  ) throws Exception{
		//Employee e = employeeDS.get(Long.parseLong(id));
		System.out.println("***wont pickup action  for events ");
		System.out.println("***Login User" + xrefId);
	
		
	
		KidEventXrefLedger ledger = eventService.wontPickup(xrefId);
		
		
	
		ExtResult extResult = new ExtResult();
		extResult.setMsg("Won't Pickup request received successfully at " + ledger.getWontPickUpDateTime().toString());
		extResult.setData(ledger);
		///ListWrapper wrapper = new ListWrapper(list);
		return extResult;
	}
	
	
	
	@RequestMapping(method=RequestMethod.GET, value="/readyForPickupAction", headers="Accept=application/json")
	public @ResponseBody ExtResult readyForPickupAction(@RequestParam("xrefId")  String xrefId  ,
			 HttpServletResponse response  ) throws Exception{
		//Employee e = employeeDS.get(Long.parseLong(id));
		System.out.println("***ready for pickup  for events ");
		System.out.println("***Login User" + xrefId);
	
		
	
		KidEventXrefLedger ledger = eventService.readyForPickup(xrefId);
		
		
	
		ExtResult extResult = new ExtResult();
		extResult.setMsg("Ready For Pickup request received successfully at " + ledger.getPickupReadyDateTime().toString());
		extResult.setData(ledger);
		///ListWrapper wrapper = new ListWrapper(list);
		return extResult;
	}
	
	
	@RequestMapping(method=RequestMethod.GET, value="/checkInAction", headers="Accept=application/json")
	public @ResponseBody ExtResult checkIn(@RequestParam("xrefId")  String xrefId  ,
			 HttpServletResponse response  ) throws Exception{
		//Employee e = employeeDS.get(Long.parseLong(id));
		System.out.println("***checkin action  for events ");
		System.out.println("***Login User" + xrefId);
	
		
	
		KidEventXrefLedger ledger = eventService.checkIn(xrefId);
		
		
	
		ExtResult extResult = new ExtResult();
		extResult.setMsg("Check In request received successfully at " + ledger.getCheckedInDateTime().toString());
		extResult.setData(ledger);
		///ListWrapper wrapper = new ListWrapper(list);
		return extResult;
	}
	
	
	@RequestMapping(method=RequestMethod.GET, value="/checkInVerifiedAction", headers="Accept=application/json")
	public @ResponseBody ExtResult checkInVerified(@RequestParam("xrefId")  String xrefId  ,
			 HttpServletResponse response  ) throws Exception{
		//Employee e = employeeDS.get(Long.parseLong(id));
		System.out.println("***check in verified for events ");
		System.out.println("***Login User" + xrefId);
	
		
	
		KidEventXrefLedger ledger = eventService.checkInVerified(xrefId);
		
		
	
		ExtResult extResult = new ExtResult();
		extResult.setMsg("Check In Verified request received successfully at " + ledger.getCheckInVerifiedDateTime().toString());
		extResult.setData(ledger);
		///ListWrapper wrapper = new ListWrapper(list);
		return extResult;
	}
	
	
	@RequestMapping(method=RequestMethod.GET, value="/checkOutAction", headers="Accept=application/json")
	public @ResponseBody ExtResult checkOut(@RequestParam("xrefId")  String xrefId  ,
			 HttpServletResponse response  ) throws Exception{
		//Employee e = employeeDS.get(Long.parseLong(id));
		System.out.println("***check out action  for events ");
		System.out.println("***Login User" + xrefId);
	
		
	
		KidEventXrefLedger ledger = eventService.checkOut(xrefId);
		
		
	
		ExtResult extResult = new ExtResult();
		extResult.setMsg("Check Out request received successfully at " + ledger.getCheckedOutDateTime().toString());
		extResult.setData(ledger);
		///ListWrapper wrapper = new ListWrapper(list);
		return extResult;
	}
	
	
	@RequestMapping(method=RequestMethod.GET, value="/pickupVerifiedAction", headers="Accept=application/json")
	public @ResponseBody ExtResult pickupVerified(@RequestParam("xrefId")  String xrefId  ,
			 HttpServletResponse response  ) throws Exception{
		//Employee e = employeeDS.get(Long.parseLong(id));
		System.out.println("***check out action  for events ");
		System.out.println("***Login User" + xrefId);
	
		
	
		KidEventXrefLedger ledger = eventService.pickupVerified(xrefId);
		
		
	
		ExtResult extResult = new ExtResult();
		extResult.setMsg("Check Out request received successfully at " + ledger.getPickedUpVerifiedDateTime().toString());
		extResult.setData(ledger);
		///ListWrapper wrapper = new ListWrapper(list);
		return extResult;
	}
	@RequestMapping(method=RequestMethod.GET, value="/walkInDropOffAction", headers="Accept=application/json")
	public @ResponseBody ExtResult walkInDropOff(@RequestParam("xrefId")  String xrefId  ,
			 HttpServletResponse response  ) throws Exception{
		//Employee e = employeeDS.get(Long.parseLong(id));
		System.out.println("***Walk in drop off  for events ");
		System.out.println("***Login User" + xrefId);
	
		
	
		KidEventXrefLedger ledger = eventService.walkInDropOff(xrefId);
		
		
	
		ExtResult extResult = new ExtResult();
		extResult.setMsg("Walk In Drop Off request received successfully at " + ledger.getCheckInVerifiedDateTime().toString());
		extResult.setData(ledger);
		///ListWrapper wrapper = new ListWrapper(list);
		return extResult;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/walkInPickupAction", headers="Accept=application/json")
	public @ResponseBody ExtResult walkInPickup(@RequestParam("xrefId")  String xrefId  ,
			 HttpServletResponse response  ) throws Exception{
		//Employee e = employeeDS.get(Long.parseLong(id));
		System.out.println("***Walk in Pickup Register ");
		System.out.println("***Login User" + xrefId);
	
		
	
		KidEventXrefLedger ledger = eventService.walkInPickup(xrefId);
		
		
	
		ExtResult extResult = new ExtResult();
		extResult.setMsg("Walk In Pickup request received successfully at " + ledger.getPickedUpDateTime().toString());
		extResult.setData(ledger);
		///ListWrapper wrapper = new ListWrapper(list);
		return extResult;
	}
	
	
	
	
	@RequestMapping(method=RequestMethod.GET, value="/walkInPickupACAction", headers="Accept=application/json")
	public @ResponseBody ExtResult walkInPickupAC(@RequestParam("xrefId")  String xrefId  ,
			 HttpServletResponse response  ) throws Exception{
		//Employee e = employeeDS.get(Long.parseLong(id));
		System.out.println("***Walk in Pickup Register ");
		System.out.println("***Login User" + xrefId);
	
		
	
		KidEventXrefLedger ledger = eventService.walkInPickupAC(xrefId);
		
		
	
		ExtResult extResult = new ExtResult();
		extResult.setMsg("Walk In Pickup request received successfully at " + ledger.getPickedUpDateTime().toString());
		extResult.setData(ledger);
		///ListWrapper wrapper = new ListWrapper(list);
		return extResult;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/pickedupAction", headers="Accept=application/json")
	public @ResponseBody ExtResult pickedUpAction(@RequestParam("xrefId")  String xrefId ,
			 HttpServletResponse response  ) throws Exception{
		//Employee e = employeeDS.get(Long.parseLong(id));
		System.out.println("***Register for events ");
		System.out.println("***Login User" + xrefId);
	
		//System.out.println("***Login User" + kidId);
	
		KidEventXrefLedger ledger = eventService.pickedUp(xrefId);
		
		
	
		ExtResult extResult = new ExtResult();
		
		extResult.setData("success");
		extResult.setMsg("Picked Up request received successfully at " + ledger.getPickedUpDateTime().toString());
		///ListWrapper wrapper = new ListWrapper(list);
		return extResult;
	}
	
	
	
	@RequestMapping(method=RequestMethod.GET, value="/getEventsForKids", headers="Accept=application/json")
	public @ResponseBody ExtResult getKidEvents(@RequestParam("kidId")  String userId  ) {
		//Employee e = employeeDS.get(Long.parseLong(id));
		System.out.println("***Get Evetns for kids ");
		System.out.println("***kid id " + userId);
	
		List<KidEventXref> list = kidProfileService.getKidEventsById(userId);
		
		// need to iterate and get the list of events only 
		HashMap eventMap = new HashMap();
		Set existingEvents = new HashSet();
		List eventList = new ArrayList();
		
		
		System.out.println("** Registered Event List " + list.size());
		for ( int i = 0 ; i < list.size() ; i++) {
			
			System.out.println("*** going through the reg event list - ledger set ");
			KidEventXref xref = list.get(i);
			xref.getEvent().setKidEventXrefId(xref.getId());
			
			
			System.out.println("**** Event id " + xref.getEvent().getId());
			
			if (xref.getEvent().getId().equals("1") ) {
				
				 xref.getEvent().setState("ReadyForPickup");
			}else if (xref.getEvent().getId().equals("2") ) {
				
				 xref.getEvent().setState("NotCheckedIn");
			}else {
				 xref.getEvent().setState("NotCheckedIn");
			}
			Set  ledgerSet = xref.getKidEventXrefLedgerSet();
			
			System.out.println("*** ledger set size " + ledgerSet.size());
			// and i need to get the one for today right -- to check the things - 
			
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			 
			 String todaysDate = format.format(new Date());
			 
			 System.out.println("*** going to check for ledgers for reg events --- ");
			 Iterator it = ledgerSet.iterator();
			 while ( it.hasNext()) {
			 	 
				 
				 KidEventXrefLedger ledger = (KidEventXrefLedger)it.next();
				 
				 if ( ledger.getDateTime().equals(todaysDate)) {
					 
					 System.out.println("*** found the right ledger ");
					 // found the right one for the date in question ....
					
					 if (xref.getEvent().getId().equals("1") ) { 
						 if ( ledger.getPickupReadyDateTime() == null ) {
							 xref.getEvent().setState("ReadyForPickup");
						 }else {
							 xref.getEvent().setState("NotReadyForPickup");
						 }
					 }
					 
					 if (xref.getEvent().getId().equals("2") ) {
						 if (ledger.getCheckedInDateTime() == null ) {
							 xref.getEvent().setState("NotCheckedIn");
						 }else {
							 xref.getEvent().setState("CheckedIn");
						 }
					 }
					 
					 if (xref.getEvent().getId().equals("3") ) {
						 if (ledger.getCheckedInDateTime() != null  && ledger.getPickedUpDateTime() == null) {
							 xref.getEvent().setState("CheckedIn");
						 }else {
							 xref.getEvent().setState("NotCheckedIn");
						 }
					 }
					 break;
				 }
				 
			 }
			 
			 
			 
			
			
			eventList.add( xref.getEvent());
			existingEvents.add(xref.getEvent().getId());
			
		}
		
		System.out.println("*** event list size " + eventList.size());
		
		
		List unRegEventList = new ArrayList();
		System.out.println("Going to get the next list ");
		
		List unregList = eventService.getUnregisteredEventListByUserId(userId);
		
		for ( int i = 0 ; i < unregList.size(); i++ ) {
			Event event = (Event)unregList.get(i);
			if ( existingEvents.contains(event.getId())) {
				
			}else {
				unRegEventList.add(event);
				
			}
		}
		System.out.println("Size unreg list " + unRegEventList.size());
		
		eventMap.put("RegisteredEvents", eventList);
		eventMap.put("UnRegisteredEvents", unRegEventList);
		ExtResult result = new ExtResult();
		result.setData(eventMap);
		return result;
	}
}