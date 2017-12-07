package com.fintechviet.loyalty.respository;

import com.fintechviet.loyalty.model.*;
import com.google.inject.ImplementedBy;

import java.util.List;
import java.util.concurrent.CompletionStage;

@ImplementedBy(JPALoyaltyRepository.class)
public interface LoyaltyRepository {
    CompletionStage<List<Phonecard>> getPhonecards();
    CompletionStage<List<Giftcode>> getGiftcodes();
    CompletionStage<List<Gamecard>> getGamecards();
    CompletionStage<List<Voucher>> getVouchers();
    CompletionStage<List<VoucherImages>> getVoucherImages(int voucherId);
    CompletionStage<Voucher> getVoucherInfo(int voucherId);
    CompletionStage<String> addToCart(String deviceToken, int itemId, int quantity, double price, String type);
    CompletionStage<String> deleteCart(String deviceToken);
    CompletionStage<Cart> getCartInfo(String deviceToken);
    CompletionStage<String> placeOrder(String deviceToken, String customerName, String address, String phone, String email);
    CompletionStage<OrderLoyalty> getOrderInfo(long orderId);
    CompletionStage<List<OrderLoyalty>> getOrders(String deviceToken);
    CompletionStage<String> cancelOrder(long orderId);
}
