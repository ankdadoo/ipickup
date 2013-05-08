package com.pickup.model;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;




@Entity
@Table(name = "Kid_Event_Xref")
public class KidEventXref {

	@Id
	@Column(name = "id")
	private String id ;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "EVENTID")
	private Event event ;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "kidprofileid")
	@JsonIgnore 
	private KidProfile kidProfile ;
	
	/*@ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
    	name="KidEvent_EventData_Xref",
    	joinColumns=@JoinColumn(name="KIDEVENTID", updatable=false, insertable=false),
    	inverseJoinColumns=@JoinColumn(name="EVENTDATAID",  updatable=false, insertable=false)
    )
	private Set<EventData> eventKeyValueSet;
	
	*/
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "registerUserId")
	private User registerUserId;
	
	@Column(name = "registerDate")
	private Date registerDate ;
	
	@Column(name = "active")
	private String active ;
	
	
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "unregisterUserId")
	private User unRegisterUserId;
	
	@Column(name = "unregisterDate")
	private Date unRegisterDate ;
	
	@OneToMany( mappedBy = "kidEventXref" , fetch=FetchType.EAGER)
	private Set<KidEventXrefLedger> kidEventXrefLedgerSet;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}

	
	public KidProfile getKidProfile() {
		return kidProfile;
	}
	public void setKidProfile(KidProfile kidProfile) {
		this.kidProfile = kidProfile;
	}
	public User getRegisterUserId() {
		return registerUserId;
	}
	public void setRegisterUserId(User registerUserId) {
		this.registerUserId = registerUserId;
	}
	public Date getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	public Set<KidEventXrefLedger> getKidEventXrefLedgerSet() {
		return kidEventXrefLedgerSet;
	}
	public void setKidEventXrefLedgerSet(
			Set<KidEventXrefLedger> kidEventXrefLedgerSet) {
		this.kidEventXrefLedgerSet = kidEventXrefLedgerSet;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public User getUnRegisterUserId() {
		return unRegisterUserId;
	}
	public void setUnRegisterUserId(User unRegisterUserId) {
		this.unRegisterUserId = unRegisterUserId;
	}
	public Date getUnRegisterDate() {
		return unRegisterDate;
	}
	public void setUnRegisterDate(Date unRegisterDate) {
		this.unRegisterDate = unRegisterDate;
	}
	
	
	
	
}
