package com.fintechviet.user.respository;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.fintechviet.user.model.EarningDetails;
import com.fintechviet.user.model.User;
import com.fintechviet.user.model.UserDeviceToken;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.ConstraintViolationException;

import com.fintechviet.content.model.MobileUserInterestItems;
import com.fintechviet.user.UserExecutionContext;

import play.db.jpa.JPAApi;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import java.util.ArrayList;

public class JPAUserRepository implements UserRepository {
	
    private final JPAApi jpaApi;
	private final UserExecutionContext ec;
	private static String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
	
    @Inject
    public JPAUserRepository(JPAApi jpaApi, UserExecutionContext ec) {
        this.jpaApi = jpaApi;
        this.ec = ec;
    }

	private <T> T wrap(Function<EntityManager, T> function) {
		return jpaApi.withTransaction(function);
	}

	@Override
	public CompletionStage<User> getUserInfo(String deviceToken) {
		return supplyAsync(() -> wrap(em -> findByDeviceToken(em, deviceToken)), ec);
	}

	private User findByDeviceToken(EntityManager em, String deviceToken) {
		User user = null;
		List<User> users = em.createQuery("SELECT u FROM User u WHERE u.id = (SELECT udt.userMobile.id FROM UserDeviceToken udt WHERE udt.deviceToken = :deviceToken)", User.class)
				               .setParameter("deviceToken", deviceToken).getResultList();
		if (!users.isEmpty()) {
			user = users.get(0);
		}
		return user;
	}

	@Override
	public CompletionStage<List<Object[]>> getRewardInfo(String deviceToken) {
		return supplyAsync(() -> wrap(em -> getRewardInfo(em, deviceToken)), ec);
	}

	private List<Object[]> getRewardInfo(EntityManager em, String deviceToken) {
		List<Object[]> rewardInfo= em.createQuery("SELECT ed.rewardCode, (SELECT rc.rewardName FROM RewardCategory rc WHERE rc.rewardCode = ed.rewardCode) AS rewardName, SUM(ed.amount) FROM EarningDetails ed WHERE ed.user.id = (SELECT udt.userMobile.id FROM UserDeviceToken udt WHERE udt.deviceToken = :deviceToken) GROUP BY ed.rewardCode")
				.setParameter("deviceToken", deviceToken).getResultList();
		return rewardInfo;
	}

	private User findUserByInviteCode(EntityManager em, String inviteCode) {
		List<User> users = em.createQuery("SELECT u FROM User u WHERE u.inviteCode = :inviteCode", User.class)
				.setParameter("inviteCode", inviteCode).getResultList();
		if (!users.isEmpty()) {
			return users.get(0);
		} else {
			return null;
		}
	}

	@Override
	public CompletionStage<String> updateUserInfo(String deviceToken, String email, String gender, int dob, String location, String inviteCode) {
		return supplyAsync(() -> wrap(em -> updateUserInfo(em, deviceToken, email, gender, dob, location, inviteCode)), ec);
	}

	private String updateUserInfo(EntityManager em, String deviceToken, String username, String gender, int dob, String location, String inviteCode) {
    	if (StringUtils.isNotEmpty(inviteCode)) {
			List<User> users = em.createQuery("SELECT u FROM User u WHERE u.inviteCode = :inviteCode", User.class)
					.setParameter("inviteCode", inviteCode).getResultList();
			if (users.isEmpty()) {
				return "InviteCodeInvalid";
			}
		}

    	User user = findByDeviceToken(em, deviceToken);
		if (StringUtils.isNotEmpty(inviteCode)) {
			user.setInviteCodeUsed(inviteCode);
		}

    	if (user == null) {
			user = new User();
			if(StringUtils.isNotEmpty(username)) user.setUsername(username);
			if(StringUtils.isNotEmpty(gender)) user.setGender(gender);
			if(dob > 0) user.setDob(dob);
			if(StringUtils.isNotEmpty(location)) user.setLocation(location);
			user.setEarning(2000l);
			String inviteCodeGen = generateRandomChars(CHARACTERS, 8);

			User u = findUserByInviteCode(em, inviteCodeGen);

			while(u != null) {
				inviteCodeGen = generateRandomChars(CHARACTERS, 8);
				u = findUserByInviteCode(em, inviteCode);
			}
			user.setInviteCode(inviteCodeGen);
			UserDeviceToken userDeviceToken = new UserDeviceToken();
			userDeviceToken.setDeviceToken(deviceToken);
			user.addDeviceToken(userDeviceToken);
			em.persist(user);
		} else {
			if(StringUtils.isNotEmpty(username)) user.setUsername(username);
			if(StringUtils.isNotEmpty(gender)) user.setGender(gender);
			if(dob > 0) user.setDob(dob);
			if(StringUtils.isNotEmpty(location)) user.setLocation(location);
			em.merge(user);
		}
		return "ok";
	}

	public static String generateRandomChars(String candidateChars, int length) {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(candidateChars.charAt(random.nextInt(candidateChars
					.length())));
		}

		return sb.toString();
	}

	@Override
	public CompletionStage<String> updateReward(String deviceToken, String rewardCode, long point) {
		return supplyAsync(() -> wrap(em -> updateReward(em, deviceToken, rewardCode, point)), ec);
	}

	private String updateReward(EntityManager em, String deviceToken, String rewardCode, long point) {
		em.createQuery("UPDATE User u SET u.earning = u.earning + :point WHERE u.id = (SELECT udt.userMobile.id FROM UserDeviceToken udt WHERE udt.deviceToken = :deviceToken)")
				      .setParameter("point", point).setParameter("deviceToken", deviceToken).executeUpdate();

		List<EarningDetails> earningDetails = em.createQuery("SELECT ed FROM EarningDetails ed WHERE ed.rewardCode = :rewardCode AND ed.user.id = (SELECT udt.userMobile.id FROM UserDeviceToken udt WHERE udt.deviceToken = :deviceToken)", EarningDetails.class)
				.setParameter("deviceToken", deviceToken).setParameter("rewardCode", rewardCode).getResultList();

		if (earningDetails.isEmpty()) {
			User user = findByDeviceToken(em, deviceToken);
			EarningDetails ed = new EarningDetails();
			ed.setRewardCode(rewardCode);
			ed.setAmount(point);
			ed.setUser(user);
			em.persist(ed);
		} else {
			em.createQuery("UPDATE EarningDetails ed SET ed.amount = ed.amount + :point WHERE ed.rewardCode = :rewardCode AND ed.user.id = (SELECT udt.userMobile.id FROM UserDeviceToken udt WHERE udt.deviceToken = :deviceToken)")
					.setParameter("deviceToken", deviceToken).setParameter("point", point).setParameter("rewardCode", rewardCode).executeUpdate();
		}

		return "ok";
	}

	@Override
	public CompletionStage<String> updateInviteCode(String deviceToken, String inviteCode) {
		return supplyAsync(() -> wrap(em -> updateInviteCode(em, deviceToken, inviteCode)), ec);
	}
	private String updateInviteCode(EntityManager em, String deviceToken, String inviteCode) {
		List<User> users = em.createQuery("SELECT u FROM User u WHERE u.inviteCode = :inviteCode", User.class)
				.setParameter("inviteCode", inviteCode).getResultList();
		if (!users.isEmpty()) {
			em.createQuery("UPDATE User u SET u.inviteCodeUsed = :inviteCode WHERE u.id = (SELECT udt.userMobile.id FROM UserDeviceToken udt WHERE udt.deviceToken = :deviceToken)")
					.setParameter("deviceToken", deviceToken).setParameter("inviteCode", inviteCode).executeUpdate();
			return "ok";
		} else {
			return "InviteCode invalid";
		}
	}

	@Override
	public Long getUserIdByDeviceToken(String deviceToken) {
    	return wrap(em -> {
			String queryStr = "SELECT u.id FROM User u where u.id = (SELECT udt.userMobile.id FROM UserDeviceToken udt WHERE udt.deviceToken = :deviceToken)";
			Query query = em.createQuery(queryStr).setParameter("deviceToken", deviceToken);
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
	private List<MobileUserInterestItems> updateUserInterest(EntityManager em, List<MobileUserInterestItems> interests) {
		List<MobileUserInterestItems> returnList = new ArrayList<MobileUserInterestItems>();
		for(MobileUserInterestItems interest: interests){
			try {
				returnList.add(em.merge(interest));
			} catch (Exception e) {
				if(e.getCause() instanceof ConstraintViolationException){
					interest.setErrorMessage("Already existed");
					returnList.add(interest);		
				}
			}
		}
		return returnList;
	}
}
