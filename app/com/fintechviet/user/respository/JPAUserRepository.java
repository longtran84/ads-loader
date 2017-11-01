package com.fintechviet.user.respository;

import com.fintechviet.content.model.MobileUserInterestItems;
import com.fintechviet.user.UserExecutionContext;
import com.fintechviet.user.model.*;
import com.fintechviet.utils.DateUtils;
import io.vavr.Tuple2;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.Session;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class JPAUserRepository implements UserRepository {
	
    private final JPAApi jpaApi;
	private final UserExecutionContext ec;
	private static String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
	private static String MESSAGE_BODY = "Chào mừng bạn đến với SMA,\nSMA xin tặng bạn 2.000đ cho lần dùng ứng dụng đầu tiên.";
	
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
			List<UserDeviceToken> tokens = em.createQuery("SELECT u FROM UserDeviceToken u WHERE u.deviceToken = :deviceToken", UserDeviceToken.class)
					.setParameter("deviceToken", deviceToken).getResultList();
			if (!tokens.isEmpty()) {
				return "AlreadyInstallApp";
			}
		}

    	User user = findByDeviceToken(em, deviceToken);
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
			if (StringUtils.isNotEmpty(inviteCode)) {
				user.setInviteCodeUsed(inviteCode);
			}
			UserDeviceToken userDeviceToken = new UserDeviceToken();
			userDeviceToken.setDeviceToken(deviceToken);
			user.addDeviceToken(userDeviceToken);
			em.persist(user);
			EarningDetails ed = new EarningDetails();
			ed.setRewardCode("INSTALL");
			ed.setAmount(2000l);
			ed.setUser(user);
			em.persist(ed);
			Message message = new Message();
			message.setBody(MESSAGE_BODY);
			message.setUser(user);
			em.persist(message);
		} else {
			if(StringUtils.isNotEmpty(username)) user.setUsername(username);
			if(StringUtils.isNotEmpty(gender)) user.setGender(gender);
			if(dob > 0) user.setDob(dob);
			if(StringUtils.isNotEmpty(location)) user.setLocation(location);
			if (StringUtils.isNotEmpty(inviteCode)) {
				user.setInviteCodeUsed(inviteCode);
			}
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
    	try {
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
		} catch (Exception ex) {
    		ex.printStackTrace();
    		return "error";
		}

		return "ok";
	}

	@Override
	public CompletionStage<String> updateInviteCode(String deviceToken, String inviteCode) {
		return supplyAsync(() -> wrap(em -> updateInviteCode(em, deviceToken, inviteCode)), ec);
	}

	@Override
	public CompletionStage<List<UserLuckyNumber>> getUserLuckyNumberByToken(String deviceToken) {
		return supplyAsync(() -> wrap(em -> getLuckyNumbers(em, deviceToken)), ec);
	}

	private List<UserLuckyNumber> getLuckyNumbers(EntityManager em, String deviceToken) {
		StringBuilder updateStr = new StringBuilder("UPDATE UserLuckyNumber uln SET uln.status = 'VALID'");
		updateStr.append("  WHERE uln.userMobileId = (SELECT udt.userMobile.id FROM UserDeviceToken udt WHERE udt.deviceToken = :deviceToken)");
		updateStr.append("  AND uln.createDate= :nowDate");
		Query queryUpdate = em.createQuery(updateStr.toString()).setParameter("deviceToken", deviceToken).setParameter("nowDate", Calendar.getInstance().getTime());

		int rowUpdated = queryUpdate.executeUpdate();
		if (rowUpdated == 0) {
			return null;
		}

		Tuple2<Date, Date> weekInterval = DateUtils.getCurrentWeekInterval();
		StringBuilder queryStr = new StringBuilder("SELECT uln FROM UserLuckyNumber uln WHERE uln.userMobileId = (SELECT udt.userMobile.id FROM UserDeviceToken udt WHERE udt.deviceToken = :deviceToken)");
		queryStr.append(" AND uln.startDateWeek= :startDate AND uln.endDateWeek = :endDate");
		Query query = em.createQuery(queryStr.toString()).setParameter("deviceToken", deviceToken).setParameter("startDate", weekInterval._1).setParameter("endDate", weekInterval._2);
		List<UserLuckyNumber> results = (List<UserLuckyNumber>) query.getResultList();

		if (results == null || results.isEmpty()) {
			return null;
		}

		Date now = today();
		return results.stream().filter(e -> now.equals(e.getCreateDate()) || "INVALID".equals(e.getStatus())).collect(Collectors.toList());
	}

	private Date today() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
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
		//remove old category
		Session session=em.unwrap(Session.class);
		Long uid = interests.get(0).getMobileUserId();
		String hql = "Delete from MobileUserInterestItems where mobileUserId = :uid";
		session.createQuery(hql).setLong("uid", uid).executeUpdate();
		for(MobileUserInterestItems interest: interests){
			try {
				em.persist(interest);
				returnList.add(interest);
			} catch (Exception e) {
				if(e.getCause() instanceof ConstraintViolationException){
					interest.setErrorMessage("Already existed");
					returnList.add(interest);		
				}
			}
		}
		return returnList;
	}

	@Override
	public CompletionStage<List<Message>> getMessages(String deviceToken, String type) {
		return supplyAsync(() -> wrap(em -> getMessages(em, deviceToken, type)), ec);
	}

	private List<Message> getMessages(EntityManager em, String deviceToken, String type) {
		StringBuilder queryStr = new StringBuilder("SELECT mes FROM Message mes WHERE mes.user.id = (SELECT udt.userMobile.id FROM UserDeviceToken udt WHERE udt.deviceToken = :deviceToken)");
		if (StringUtils.isNotEmpty(type)) {
			queryStr.append(" AND mes.read = 0 AND mes.type = :type");
		}
		Query query = em.createQuery(queryStr.toString());
		query.setParameter("deviceToken", deviceToken);

		if (StringUtils.isNotEmpty(type)) {
			query.setParameter("type", type);
		}

		List<Message> messages= query.getResultList();
		return messages;
	}

	@Override
	public CompletionStage<String> updateMessage(long messageId, String status) {
		return supplyAsync(() -> wrap(em -> updateMessage(em, messageId, status)), ec);
	}

	private String updateMessage(EntityManager em, long messageId, String status) {
    	try {
    		if (status.equals("READ")) {
				em.createQuery("UPDATE Message mes SET mes.read = 1 WHERE mes.id = :messageId")
						.setParameter("messageId", messageId).executeUpdate();
			}
			if (status.equals("RECEIVE")) {
				em.createQuery("UPDATE Message mes SET mes.receive = 1 WHERE mes.id = :messageId")
						.setParameter("messageId", messageId).executeUpdate();
			}
		} catch (Exception ex) {
    		ex.printStackTrace();
    		return "error";
		}

		return "ok";
	}
}
