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
@Table(name = "Kid_Event_Xref_ledger")
public class KidEventXrefLedger {

	@Id
	@Column(name = "id")
	private String id ;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "XrefID")
	@JsonIgnore
	private KidEventXref kidEventXref ;
	
	
	
	@Column(name = "DateTime")
	private String dateTime ;
	
	

	@Column(name = "CancelPickupDateTime")
	private Date cancelPickupDateTime ;
	
	

	
	
	@Column(name = "PickupReadyDateTime")
	private Date pickupReadyDateTime ;
	
	@Column(name = "checkedInDateTime")
	private Date checkedInDateTime ;
	
	@Column(name = "checkInVerifiedDateTime")
	private Date checkInVerifiedDateTime ;
	
	@Column(name = "checkedOutDateTime")
	private Date checkedOutDateTime ;
	
	@Column(name = "pickedUpVerifiedDateTime")
	private Date pickedUpVerifiedDateTime ;
	

	
	@Column(name = "PickedUpDateTime")
	private Date pickedUpDateTime ;
	
	@Column(name = "SpecialInstructions")
	private String specialInstructions ;
	
	

	
	@Column(name = "SpecialInsturctionDateTime")
	private Date specialInsturctionDateTime ;
	
	
	
	
	@Column(name = "WontPickUpDateTime")
	private Date wontPickUpDateTime ;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@JsonIgnore
	public KidEventXref getKidEventXref() {
		return kidEventXref;
	}
	
	@JsonIgnore
	public void setKidEventXref(KidEventXref kidEventXref) {
		this.kidEventXref = kidEventXref;
	}
	
	
	public Date getPickupReadyDateTime() {
		return pickupReadyDateTime;
	}
	public void setPickupReadyDateTime(Date pickupReadyDateTime) {
		this.pickupReadyDateTime = pickupReadyDateTime;
	}
	public Date getPickedUpDateTime() {
		return pickedUpDateTime;
	}
	public void setPickedUpDateTime(Date pickedUpDateTime) {
		this.pickedUpDateTime = pickedUpDateTime;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getSpecialInstructions() {
		return specialInstructions;
	}
	public void setSpecialInstructions(String specialInstructions) {
		this.specialInstructions = specialInstructions;
	}
	
	public Date getSpecialInsturctionDateTime() {
		return specialInsturctionDateTime;
	}
	public void setSpecialInsturctionDateTime(Date specialInsturctionDateTime) {
		this.specialInsturctionDateTime = specialInsturctionDateTime;
	}
	
	public Date getWontPickUpDateTime() {
		return wontPickUpDateTime;
	}
	public void setWontPickUpDateTime(Date wontPickUpDateTime) {
		this.wontPickUpDateTime = wontPickUpDateTime;
	}
	public Date getCancelPickupDateTime() {
		return cancelPickupDateTime;
	}
	public void setCancelPickupDateTime(Date cancelPickupDateTime) {
		this.cancelPickupDateTime = cancelPickupDateTime;
	}
	public Date getCheckedInDateTime() {
		return checkedInDateTime;
	}
	public void setCheckedInDateTime(Date checkedInDateTime) {
		this.checkedInDateTime = checkedInDateTime;
	}
	public Date getCheckInVerifiedDateTime() {
		return checkInVerifiedDateTime;
	}
	public void setCheckInVerifiedDateTime(Date checkInVerifiedDateTime) {
		this.checkInVerifiedDateTime = checkInVerifiedDateTime;
	}
	public Date getCheckedOutDateTime() {
		return checkedOutDateTime;
	}
	public void setCheckedOutDateTime(Date checkedOutDateTime) {
		this.checkedOutDateTime = checkedOutDateTime;
	}
	public Date getPickedUpVerifiedDateTime() {
		return pickedUpVerifiedDateTime;
	}
	public void setPickedUpVerifiedDateTime(Date pickedUpVerifiedDateTime) {
		this.pickedUpVerifiedDateTime = pickedUpVerifiedDateTime;
	}
	
	
	
	
	
	
	
}
