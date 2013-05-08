package com.pickup.model;




import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "Event")
public class Event {

	@Id
	@Column(name = "id")
	private String id ;
	
	@Column(name = "description")
	private  String description ;
	
	
	@Column(name = "canUnRegister")
	private  String canUnRegister ;
	
	@Transient
	private String endDateString;
	
	@Transient
	private String startDateString;
	
	
	@Transient
	private String kidEventXrefId;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "EVENTTYPEID")
	private EventType eventType;
	
	
	@ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
    	name="Event_Group_Xref",
    	joinColumns=@JoinColumn(name="EventID", updatable=false, insertable=false),
    	inverseJoinColumns=@JoinColumn(name="EventGroupID",  updatable=false, insertable=false)
    )
	private Set<EventGroup> eventGroupSet;
	
	
	
	@ManyToMany ( fetch=FetchType.EAGER)
    @JoinTable(
    	name="Event_User_Xref",
    	joinColumns=@JoinColumn(name="EventID", updatable=false, insertable=false),
    	inverseJoinColumns=@JoinColumn(name="UserID",  updatable=false, insertable=false)
    )
	private Set<User> eventUserSet;
	
	
	
	@Transient
	private String state;
	
	
	@Transient
	private String eventRegistrationState ;
	
	
	@Transient 
	private String totalCount = "0";
	
	@Transient
	private String inactiveCount= "0";
	
	@Transient
	private String readyCount= "0";
	
	@Transient 
	private String pickedUpCount= "0";
	
	@Transient
	private String notPickedCount= "0";
	
	@Transient
	private String checkedInCount= "0";
	
	@Transient 
	private String droppedOffCount= "0";
	
	@Transient 
	private String notDroppedOffCount= "0" ;
	
	@Transient 
	private String checkedOutCount= "0";
	
	
	
	
	
	@Transient
	public String getEndDateString() {
		if ( endDate != null) {
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			  String formattedDate = formatter.format(endDate);
			//  System.out.println("Formatted date is ==>"+formattedDate);
			  return formattedDate;
		}
		
		return "";
	}
	
	@Transient
	public void setEndDateString(String endDateString) {
		this.endDateString = endDateString;
	}
	
	@Transient
	public String getStartDateString() {
		if ( startDate != null) {
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			  String formattedDate = formatter.format(startDate);
			 // System.out.println("Formatted date is ==>"+formattedDate);
			  return formattedDate;
		}
		
		return "";
	}
	
	@Transient
	public void setStartDateString(String startDateString) {
		this.startDateString = startDateString;
	}
	@Column(name = "name")
	private String name ;
	
	
	@Column(name = "startDate")
	private Date startDate ;
	
	
	@Column(name = "endDate")
	private Date endDate ;
	
	
	@Column(name = "regStartDate")
	private Date regStartDate ;
	
	
	@Column(name = "regEndDate")
	private Date regEndDate ;
	
	
	@Column(name = "location")
	private String location ;
	
	
	@Column(name = "requiresReg")
	private String requiresRegistration ;
	
	
	
	@Column(name = "eventDuration")
	private String eventDuration ;
	
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public EventType getEventType() {
		return eventType;
	}
	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set getEventGroupSet() {
		return eventGroupSet;
	}
	public void setEventGroupSet(Set eventGroupSet) {
		this.eventGroupSet = eventGroupSet;
	}
	public Set getEventUserSet() {
		return eventUserSet;
	}
	public void setEventUserSet(Set eventUserSet) {
		this.eventUserSet = eventUserSet;
	}
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getKidEventXrefId() {
		return kidEventXrefId;
	}

	public void setKidEventXrefId(String kidEventXrefId) {
		this.kidEventXrefId = kidEventXrefId;
	}

	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getEventRegistrationState() {
		return eventRegistrationState;
	}

	public void setEventRegistrationState(String eventRegistrationState) {
		this.eventRegistrationState = eventRegistrationState;
	}

	public Date getRegStartDate() {
		return regStartDate;
	}

	public void setRegStartDate(Date regStartDate) {
		this.regStartDate = regStartDate;
	}

	public Date getRegEndDate() {
		return regEndDate;
	}

	public void setRegEndDate(Date regEndDate) {
		this.regEndDate = regEndDate;
	}

	public String getRequiresRegistration() {
		return requiresRegistration;
	}

	public void setRequiresRegistration(String requiresRegistration) {
		this.requiresRegistration = requiresRegistration;
	}

	public String getCanUnRegister() {
		return canUnRegister;
	}

	public void setCanUnRegister(String canUnRegister) {
		this.canUnRegister = canUnRegister;
	}

	public String getEventDuration() {
		return eventDuration;
	}

	public void setEventDuration(String eventDuration) {
		this.eventDuration = eventDuration;
	}

	public String getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

	public String getInactiveCount() {
		return inactiveCount;
	}

	public void setInactiveCount(String inactiveCount) {
		this.inactiveCount = inactiveCount;
	}

	public String getReadyCount() {
		return readyCount;
	}

	public void setReadyCount(String readyCount) {
		this.readyCount = readyCount;
	}

	public String getPickedUpCount() {
		return pickedUpCount;
	}

	public void setPickedUpCount(String pickedUpCount) {
		this.pickedUpCount = pickedUpCount;
	}

	public String getNotPickedCount() {
		return notPickedCount;
	}

	public void setNotPickedCount(String notPickedCount) {
		this.notPickedCount = notPickedCount;
	}

	public String getCheckedInCount() {
		return checkedInCount;
	}

	public void setCheckedInCount(String checkedInCount) {
		this.checkedInCount = checkedInCount;
	}

	public String getDroppedOffCount() {
		return droppedOffCount;
	}

	public void setDroppedOffCount(String droppedOffCount) {
		this.droppedOffCount = droppedOffCount;
	}

	public String getNotDroppedOffCount() {
		return notDroppedOffCount;
	}

	public void setNotDroppedOffCount(String notDroppedOffCount) {
		this.notDroppedOffCount = notDroppedOffCount;
	}

	public String getCheckedOutCount() {
		return checkedOutCount;
	}

	public void setCheckedOutCount(String checkedOutCount) {
		this.checkedOutCount = checkedOutCount;
	}

	
	
	
}

