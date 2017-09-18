package com.fintechviet.content.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "user_mobile_interest")
public class MobileUserInterestItems {
	
	@Id
	@Column (name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "uid")
    private Long mobileUserId;
	@Column(name = "newsCategoryId")
    private Long newsCategoryId;
	@JsonInclude()
	@Transient
	private String errorMessage;
	
	
    public MobileUserInterestItems() {
	}
    
    public MobileUserInterestItems(Long uid, Long newsCategoryId) {
    	this.mobileUserId = uid;
    	this.newsCategoryId = newsCategoryId;
	}
	
	
	public Long getMobileUserId() {
		return mobileUserId;
	}

	public void setMobileUserId(Long mobileUserId) {
		this.mobileUserId = mobileUserId;
	}

	
	public Long getNewsCategoryId() {
		return newsCategoryId;
	}

	public void setNewsCategoryId(Long newsCategoryId) {
		this.newsCategoryId = newsCategoryId;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	
	
    
}
