package com.fintech.adl.repo;

import java.util.List;

import com.google.inject.ImplementedBy;

@ImplementedBy(JPAAdvertismentRepository.class)
public interface AdvertismentRepository{
    List getAdsByVideo(String username);
    List getAdsByImage(String username);
}
