package com.fintechviet.user.respository;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.fintechviet.content.model.MobileUserInterestItems;
import com.fintechviet.user.UserExecutionContext;
import com.fintechviet.user.model.User;

import play.db.jpa.JPAApi;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class JPAUserRepository implements UserRepository {
	
    private final JPAApi jpaApi;
	private final UserExecutionContext ec;
	
    @Inject
    public JPAUserRepository(JPAApi jpaApi, UserExecutionContext ec) {
        this.jpaApi = jpaApi;
        this.ec = ec;
    }

	private <T> T wrap(Function<EntityManager, T> function) {
		return jpaApi.withTransaction(function);
	}


	public List findByUsername(String username) {
		System.out.println("Query for user by username");
		List<User> persons = jpaApi.em().createQuery("select p from Person p", User.class).getResultList();
		return persons;
	}

	@Override
	public CompletionStage<User> getUserInfo(String deviceToken) {
		return supplyAsync(() -> wrap(em -> findByDeviceToken(em, deviceToken)), ec);
	}

	private User findByDeviceToken(EntityManager em, String deviceToken) {
		User user = null;
		List<User> users = em.createQuery("SELECT u FROM User u WHERE u.deviceToken = :deviceToken", User.class)
				               .setParameter("deviceToken", deviceToken).getResultList();
		if (!users.isEmpty()) {
			user = users.get(0);
		}
		return user;
	}

	/*@Override
	public CompletionStage<User> getRewardInfo(String deviceToken) {
		return supplyAsync(() -> wrap(em -> findByDeviceToken(em, deviceToken)), ec);
	}

	private List<Object[]> getRewardInfos(EntityManager em, String deviceToken) {
		List<Object[]> rewardInfo= em.createQuery("SELECT ed.event, SUM(ed.amount) FROM EarningDetails ed WHERE ed.user.deviceToken = :deviceToken")
				.setParameter("deviceToken", deviceToken).getResultList();
		return rewardInfo;
	}*/

	@Override
	public CompletionStage<String> updateUserInfo(String deviceToken, String email, String gender, int dob, String location) {
		return supplyAsync(() -> wrap(em -> updateUserInfo(em, deviceToken, email, gender, dob, location)), ec);
	}

	private String updateUserInfo(EntityManager em, String deviceToken, String email, String gender, int dob, String location) {
    	User user = findByDeviceToken(em, deviceToken);
    	if (user == null) {
			user = new User();
			user.setDeviceToken(deviceToken);
			user.setEmail(email);
			user.setGender(gender);
			user.setDob(dob);
			user.setLocation(location);
			em.persist(user);
		} else {
			user.setDeviceToken(deviceToken);
			user.setEmail(email);
			user.setGender(gender);
			user.setDob(dob);
			user.setLocation(location);
			em.merge(user);
		}
		return "ok";
	}

	@Override
	public CompletionStage<String> updateReward(String deviceToken, String event, long point) {
		return supplyAsync(() -> wrap(em -> updateReward(em, deviceToken, event, point)), ec);
	}

	private String updateReward(EntityManager em, String deviceToken, String event, long point) {
		int updateUserEarningCount = em.createQuery("UPDATE User SET earning = earning + :point WHERE deviceToken = :deviceToken")
				      .setParameter("point", point).setParameter("deviceToken", deviceToken).executeUpdate();

		int updateEarningDetailCount = em.createQuery("UPDATE EarningDetails SET amount = amount + :point WHERE event = :event")
				.setParameter("point", point).setParameter("event", event).executeUpdate();
		return "ok";
	}

	@Override
	public Long getUserIdByDeviceToken(String deviceToken) {
    	return wrap(em -> {
			String queryStr = "SELECT id FROM User where deviceToken = '" + deviceToken + "'";
			Query query = em.createQuery(queryStr);
			return  (Long)query.getSingleResult();

		});
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
}
