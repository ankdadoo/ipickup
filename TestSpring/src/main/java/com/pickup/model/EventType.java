package com.pickup.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "EventType")
public class EventType {

	@Id
	@Column(name = "id")
	private String id ;
	
	@Column(name="description")
	private String description;
	
	
	@ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
    	name="EventType_Action_Xref",
    	joinColumns=@JoinColumn(name="EVENTTYPEID", updatable=false, insertable=false),
    	inverseJoinColumns=@JoinColumn(name="EVENTACTIONID",  updatable=false, insertable=false)
    )
	private Set<EventAction> eventActionsSet;
	
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
	public Set getEventActionsSet() {
		return eventActionsSet;
	}
	public void setEventActionsSet(Set eventActionsSet) {
		this.eventActionsSet = eventActionsSet;
	}
	
	
}
