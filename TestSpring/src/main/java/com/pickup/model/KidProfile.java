package com.pickup.model;

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
import javax.persistence.Transient;

@Entity
@Table(name = "Kid_Profile")
public class KidProfile {

	@Id
	@Column(name = "id")
	private String id ;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "Userid")
	private User userid;
	

	
	@Column(name = "dob")
	private  String dob ;
	
	@Column(name = "schoolid")
	private  String schoolId ;
	
	
	@Column(name = "school")
	private  String school ;
	
	@Transient 
	private String kidEventXrefId;
	
	
	@ManyToMany( fetch=FetchType.EAGER)
    @JoinTable(
    	name="kid_parent_xref", 
    	joinColumns=@JoinColumn(name="KIDPROFILEID", updatable=false, insertable=false),
    	inverseJoinColumns=@JoinColumn(name="PARENTID",  updatable=false, insertable=false)
    )
	private Set<User> authorizedUserSet;
	
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "EventGroupId")
	private EventGroup eventGroup;
	
	@OneToMany( mappedBy = "kidProfile" , fetch=FetchType.EAGER)
	private Set<KidEventXref> registeredEventSet;

	
	
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getUserid() {
		return userid;
	}

	public void setUserid(User userid) {
		this.userid = userid;
	}



	public Set getAuthorizedUserSet() {
		return authorizedUserSet;
	}

	public void setAuthorizedUserSet(Set authorizedUserSet) {
		this.authorizedUserSet = authorizedUserSet;
	}

	public EventGroup getEventGroup() {
		return eventGroup;
	}

	public void setEventGroup(EventGroup eventGroup) {
		this.eventGroup = eventGroup;
	}

	public Set getRegisteredEventSet() {
		return registeredEventSet;
	}

	public void setRegisteredEventSet(Set registeredEventSet) {
		this.registeredEventSet = registeredEventSet;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getKidEventXrefId() {
		return kidEventXrefId;
	}

	public void setKidEventXrefId(String kidEventXrefId) {
		this.kidEventXrefId = kidEventXrefId;
	}

	
	
	
}
