package vn.fintechviet.location.repository;

import vn.fintechviet.location.model.AdLocation;
import com.google.inject.ImplementedBy;

import java.util.List;

@ImplementedBy(JPALocationRepository.class)
public interface LocationRepository {
    List<AdLocation> findAdLocationsNearBy(String lng, String lat);
}
