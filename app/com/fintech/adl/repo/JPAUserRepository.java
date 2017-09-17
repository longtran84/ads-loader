package com.fintech.adl.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.fintech.adl.model.User;
import com.fintechviet.content.model.MobileUserInterest;
import com.fintechviet.content.model.MobileUserInterestItems;
import com.fintechviet.content.model.News;

import play.db.jpa.JPAApi;

public class JPAUserRepository implements UserRepository {
	
    private final JPAApi jpaApi;
	
    @Inject
    public JPAUserRepository(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }	
    
    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
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

	@Override
	public List<MobileUserInterestItems> updateUserInterest(String deviceToken, List<MobileUserInterestItems> interests) {
        try {
			return wrap(em -> updateUserInterest(em, interests));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return interests;
	}
	private List<MobileUserInterestItems> updateUserInterest(EntityManager em, List<MobileUserInterestItems> interests){
		for(MobileUserInterestItems interest: interests){
			em.merge(interest);
		}
		return interests;
	}

	@Override
	public Long getUserIdByDeviceToken(String deviceToken) {
        return wrap(em -> {
            String queryStr = "SELECT id FROM User where deviceToken = '" + deviceToken + "'";
            Query query = em.createQuery(queryStr);
            return  (Long)query.getSingleResult();
        });
	}
	
}
