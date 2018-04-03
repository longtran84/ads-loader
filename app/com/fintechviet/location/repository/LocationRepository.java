package com.fintechviet.location.repository;

import com.fintechviet.location.model.AdLocation;
import com.google.inject.ImplementedBy;

import java.util.List;

@ImplementedBy(JPALocationRepository.class)
public interface LocationRepository {
    List<AdLocation> findAdLocationsNearBy(double lng, double lat);
}
