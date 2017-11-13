package com.fintechviet.loyalty.respository;

import com.fintechviet.content.ContentExecutionContext;
import com.fintechviet.loyalty.model.*;
import com.fintechviet.user.model.User;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class JPALoyaltyRepository implements LoyaltyRepository {

    private final JPAApi jpaApi;
    private final ContentExecutionContext ec;

    @Inject
    public JPALoyaltyRepository(JPAApi jpaApi, ContentExecutionContext ec) {
        this.jpaApi = jpaApi;
        this.ec = ec;
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    @Override
    public CompletionStage<List<Phonecard>> getPhonecards() {
        return supplyAsync(() -> wrap(em -> {
            return  (List<Phonecard>)em.createQuery("SELECT pc FROM Phonecard pc WHERE pc.status = 'ACTIVE'").getResultList();
        }), ec);
    }

    @Override
    public CompletionStage<List<Giftcode>> getGiftcodes() {
        return supplyAsync(() -> wrap(em -> {
            return  (List<Giftcode>)em.createQuery("SELECT gc FROM Giftcode gc WHERE gc.status = 'ACTIVE'").getResultList();
        }), ec);
    }

    @Override
    public CompletionStage<List<Gamecard>> getGamecards() {
        return supplyAsync(() -> wrap(em -> {
            return  (List<Gamecard>)em.createQuery("SELECT gc FROM Gamecard gc WHERE gc.status = 'ACTIVE'").getResultList();
        }), ec);
    }

    @Override
    public CompletionStage<List<Voucher>> getVouchers() {
        return supplyAsync(() -> wrap(em -> {
            return  (List<Voucher>)em.createQuery("SELECT vc FROM Voucher vc WHERE vc.status = 'ACTIVE'").getResultList();
        }), ec);
    }

    @Override
    public CompletionStage<List<VoucherImages>> getVoucherImages(int voucherId) {
        return supplyAsync(() -> wrap(em -> {
            return  (List<VoucherImages>)em.createQuery("SELECT vc FROM VoucherImages vc WHERE vc.voucherId = :voucherId AND vc.status = 'ACTIVE'").setParameter("voucherId", voucherId).getResultList();
        }), ec);
    }

    @Override
    public CompletionStage<Voucher> getVoucherInfo(int voucherId) {
        return supplyAsync(() -> wrap(em -> {
            List<Voucher> vouchers = (List<Voucher>)em.createQuery("SELECT vc FROM Voucher vc WHERE vc.id = :voucherId").setParameter("voucherId", voucherId).getResultList();
            if (vouchers.size() > 0) {
                return vouchers.get(0);
            }
            return null;
        }), ec);
    }

    private Voucher getVoucherById(int voucherId) {
        return wrap(em -> {
            List<Voucher> vouchers = (List<Voucher>)em.createQuery("SELECT vc FROM Voucher vc WHERE vc.id = :voucherId").setParameter("voucherId", voucherId).getResultList();
            if (vouchers.size() > 0) {
                return vouchers.get(0);
            }
            return null;
        });
    }

    private Gamecard getGameCardById(int gameCardId) {
        return wrap(em -> {
            List<Gamecard> gamecards = (List<Gamecard>)em.createQuery("SELECT gc FROM Gamecard gc WHERE gc.id = :gameCardId").setParameter("gameCardId", gameCardId).getResultList();
            if (gamecards.size() > 0) {
                return gamecards.get(0);
            }
            return null;
        });
    }

    private Phonecard getPhoneCardById(int phoneCardId) {
        return wrap(em -> {
            List<Phonecard> phonecards = (List<Phonecard>)em.createQuery("SELECT pc FROM Phonecard pc WHERE pc.id = :phoneCardId").setParameter("phoneCardId", phoneCardId).getResultList();
            if (phonecards.size() > 0) {
                return phonecards.get(0);
            }
            return null;
        });
    }

    private Giftcode getGiftCodeById(int giftCodeId) {
        return wrap(em -> {
            List<Giftcode> giftcodes = (List<Giftcode>)em.createQuery("SELECT gc FROM Giftcode gc WHERE gc.id = :giftCodeId").setParameter("giftCodeId", giftCodeId).getResultList();
            if (giftcodes.size() > 0) {
                return giftcodes.get(0);
            }
            return null;
        });
    }

    private User getUserByDeviceToken(String deviceToken) {
        return wrap(em -> {
            List<User> users = em.createQuery("SELECT u FROM User u WHERE u.id = (SELECT udt.userMobile.id FROM UserDeviceToken udt WHERE udt.deviceToken = :deviceToken)", User.class)
                    .setParameter("deviceToken", deviceToken).getResultList();
            if (!users.isEmpty()) {
                return users.get(0);
            }
            return null;
        });
    }

    @Override
    public CompletionStage<String> addToCart(String deviceToken, int itemId, int quantity, double price, String type) {
        return supplyAsync(() -> wrap(em -> {
            Cart cart = new Cart();
            cart.setQuantity(quantity);
            cart.setPrice(price);
            if (type.equals("VOUCHER")) {
                Voucher voucher = getVoucherById(itemId);
                cart.setVoucher(voucher);
            } else if (type.equals("GAME_CARD")) {
                Gamecard gamecard = getGameCardById(itemId);
                cart.setGameCard(gamecard);
            } else if (type.equals("PHONE_CARD")) {
                Phonecard phonecard = getPhoneCardById(itemId);
                cart.setPhoneCard(phonecard);
            } else {
                Giftcode giftcode = getGiftCodeById(itemId);
                cart.setGiftCode(giftcode);
            }
            User user = getUserByDeviceToken(deviceToken);
            cart.setUser(user);
            em.persist(cart);
            return "ok";
        }), ec);
    }

    @Override
    public CompletionStage<String> deleteCart(String deviceToken) {
        return supplyAsync(() -> wrap(em -> {
            List<Cart> carts = (List<Cart>)em.createQuery("SELECT c FROM Cart c WHERE c.user.id = (SELECT udt.userMobile.id FROM UserDeviceToken udt WHERE udt.deviceToken = :deviceToken)").setParameter("deviceToken", deviceToken).getResultList();
            if (carts.size() > 0) {
                em.remove(carts.get(0));
            }
            return "ok";
        }), ec);
    }

    @Override
    public CompletionStage<Cart> getCartInfo(String deviceToken) {
        return supplyAsync(() -> wrap(em -> {
            List<Cart> carts = (List<Cart>)em.createQuery("SELECT c FROM Cart c WHERE c.user.id = (SELECT udt.userMobile.id FROM UserDeviceToken udt WHERE udt.deviceToken = :deviceToken)").setParameter("deviceToken", deviceToken).getResultList();
            if (carts.size() > 0) {
                return carts.get(0);
            }
            return null;
        }), ec);
    }

    @Override
    public CompletionStage<String> placeOrder(String deviceToken, String customerName, String address, String phone) {
        return supplyAsync(() -> wrap(em -> {
            List<Cart> carts = (List<Cart>)em.createQuery("SELECT c FROM Cart c WHERE c.user.id = (SELECT udt.userMobile.id FROM UserDeviceToken udt WHERE udt.deviceToken = :deviceToken)").setParameter("deviceToken", deviceToken).getResultList();
            Cart cart = null;
            if (carts.size() > 0) {
                cart =  carts.get(0);
            }
            OrderLoyalty order = new OrderLoyalty();
            order.setUser(cart.getUser());
            order.setQuantity(cart.getQuantity());
            order.setCashout(cart.getPrice());
            if (cart.getVoucher() != null) {
                order.setVoucher(cart.getVoucher());
            } else if (cart.getGameCard() != null) {
                order.setGameCard(cart.getGameCard());
            } else if (cart.getPhoneCard() != null) {
                order.setPhoneCard(cart.getPhoneCard());
            } else {
                order.setGiftCode(cart.getGiftCode());
            }
            em.persist(order);
            em.remove(cart);
            return "ok";
        }), ec);
    }

    @Override
    public CompletionStage<OrderLoyalty> getOrderInfo(long orderId) {
        return supplyAsync(() -> wrap(em -> {
            OrderLoyalty order = (OrderLoyalty)em.createQuery("SELECT o FROM OrderLoyalty o WHERE o.id = :orderId").setParameter("orderId", orderId).getSingleResult();
            return order;
        }), ec);

    }

    @Override
    public CompletionStage<List<OrderLoyalty>> getOrders(String deviceToken) {
        return supplyAsync(() -> wrap(em -> {
            List<OrderLoyalty> orders = (List<OrderLoyalty>)em.createQuery("SELECT o FROM OrderLoyalty o WHERE o.user.id = (SELECT udt.userMobile.id FROM UserDeviceToken udt WHERE udt.deviceToken = :deviceToken)").setParameter("deviceToken", deviceToken).getResultList();
            return orders;
        }), ec);
    }
}
