package com.fintech.adl.repo;

import java.util.List;

import javax.inject.Inject;

import com.fintech.adl.model.User;

import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;

public class JPAAdvertismentRepository implements AdvertismentRepository {
	
    private final JPAApi jpaApi;
	
    @Inject
    public JPAAdvertismentRepository(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

	@Override
	public List getAdsByVideo(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getAdsByImage(String username) {
		// TODO Auto-generated method stub
		return null;
	}	

}
