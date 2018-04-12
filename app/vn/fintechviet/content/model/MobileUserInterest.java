package vn.fintechviet.content.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Embeddable
public class MobileUserInterest {

    
	@Column(name = "uid")
    private Long mobileUserId;
	@Column(name = "newsCategoryId")
    private Long newsCategoryId;
    
    public MobileUserInterest(Long uid, Long newsCategoryId) {
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


    
    
}
