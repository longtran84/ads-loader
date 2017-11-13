package com.fintechviet.loyalty.service;

import com.fintechviet.loyalty.model.*;
import com.fintechviet.loyalty.respository.LoyaltyRepository;
import play.Configuration;

import javax.inject.Inject;
import java.util.*;

import java.util.concurrent.CompletionStage;


public class LoyaltyService {
	private final LoyaltyRepository loyaltyRepository;

	@Inject
	private Configuration configuration;

	@Inject
	public LoyaltyService(LoyaltyRepository loyaltyRepository){
		this.loyaltyRepository = loyaltyRepository;
	}

	public CompletionStage<List<Phonecard>> getPhonecards() {
		return loyaltyRepository.getPhonecards();
	}

	public CompletionStage<List<Giftcode>> getGiftcodes() {
		return loyaltyRepository.getGiftcodes();
	}

	public CompletionStage<List<Gamecard>> getGamecards() {
		return loyaltyRepository.getGamecards();
	}

	public CompletionStage<List<Voucher>> getVouchers() {
		return loyaltyRepository.getVouchers();
	}

	public CompletionStage<List<VoucherImages>> getVoucherImages(int voucherId) {
		return loyaltyRepository.getVoucherImages(voucherId);
	}

	public CompletionStage<Voucher> getVoucherInfo(int voucherId) {
		return loyaltyRepository.getVoucherInfo(voucherId);
	}

	public CompletionStage<String> addToCart(String deviceToken, int itemId, int quantity, double price, String type) {
		return loyaltyRepository.addToCart(deviceToken, itemId, quantity, price, type);
	}

	public CompletionStage<String> deleteCart(String deviceToken) {
		return loyaltyRepository.deleteCart(deviceToken);
	}

	public CompletionStage<Cart> getCartInfo(String deviceToken) {
		return loyaltyRepository.getCartInfo(deviceToken);
	}

	public CompletionStage<String> placeOrder(String deviceToken, String customerName, String address, String phone) {
		return loyaltyRepository.placeOrder(deviceToken, customerName, address, phone);
	}

	public CompletionStage<OrderLoyalty> getOrderInfo(long orderId) {
		return loyaltyRepository.getOrderInfo(orderId);
	}

	public CompletionStage<List<OrderLoyalty>> getOrders(String deviceToken) {
		return loyaltyRepository.getOrders(deviceToken);
	}
}
