package com.fintech.adl.repo;

import java.util.List;

import javax.inject.Inject;

import com.fintech.adl.model.User;

import play.db.jpa.JPAApi;

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

	@Override
	public User findByDeviceToken(String deviceToken) {
		User user = jpaApi.em().createQuery("SELECT u FROM User u WHERE u.deviceToken = :deviceToken", User.class)
				               .setParameter("deviceToken", deviceToken).getSingleResult();
		return user;
	}
}
