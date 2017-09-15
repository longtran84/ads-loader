package com.fintechviet.content.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_mobile_interest")
public class MobileUserInterest {
    private long id;
    
    private String mobileUserId;
    private Long newsCategoryId;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }
    
	public void setId(long id) {
		this.id = id;
	}
	
	@Column(name = "uid")
	public String getMobileUserId() {
		return mobileUserId;
	}

	public void setMobileUserId(String mobileUserId) {
		this.mobileUserId = mobileUserId;
	}

	@Column(name = "newsCategoryId")
	public Long getNewsCategoryId() {
		return newsCategoryId;
	}

	public void setNewsCategoryId(Long newsCategoryId) {
		this.newsCategoryId = newsCategoryId;
	}


    
    
}
