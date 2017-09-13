package com.fintech.adl.repo;

import java.util.List;

import javax.inject.Inject;

import com.fintech.adl.model.User;

import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;

public class JPAUserRepository implements UserRepository {
	
    private final JPAApi jpaApi;
	
    @Inject
    public JPAUserRepository(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }	

	@Override
	public List findByUsername(String username) {
		System.out.println("Query for user by username");
		List<User> persons = jpaApi.em().createQuery("select p from Person p", User.class).getResultList();
		return persons;
	}

}
