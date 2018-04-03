package com.fintechviet.location.repository;

import com.fintechviet.location.LocationExecutionContext;
import com.fintechviet.location.model.AdLocation;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import java.util.function.Function;

public class JPALocationRepository implements LocationRepository {

    private final JPAApi jpaApi;
    private final LocationExecutionContext ec;

    @Inject
    public JPALocationRepository(JPAApi jpaApi, LocationExecutionContext ec) {
        this.jpaApi = jpaApi;
        this.ec = ec;
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    @Override
    public List<AdLocation> findAdLocationsNearBy(double lng, double lat) {
        return wrap(em -> {
            StringBuilder queryStr = new StringBuilder("SELECT ad.id, ad.name FROM ad_location ad WHERE ad.flightId IN (SELECT id FROM flight WHERE startDate <= NOW() AND (endDate >= NOW() OR endDate IS NULL)) " +
                    "AND (6731 * acos(cos(radians(:lat)) * cos(radians(ad.lat)) * cos(radians(ad.lng) - radians(:lng)) + sin(radians(:lat)) * sin(radians(lat)))) < 0.5 AND ad.status = 'ACTIVE'");
            Query queryIds = em.createNativeQuery(queryStr.toString());
            queryIds.setParameter("lng", lng);
            queryIds.setParameter("lat", lat);
            List<Object[]> locationObjs = queryIds.getResultList();
            List<Long> adLocationIds = new ArrayList<Long>();
            for (Object row[] : locationObjs) {
                BigInteger id = (BigInteger)row[0];
                adLocationIds.add(id.longValue());
//                String name = (String)row[1];
//                String longitude = (String)row[2];
//                String latitude = (String)row[3];
//                String adContent = (String)row[4];
//                AdLocation adLocation = new AdLocation();
//                adLocation.setName(name);
//                adLocation.setLng(longitude);
//                adLocation.setLat(latitude);
//                adLocation.setAdContent(adContent);
//                adLocations.add(adLocation);
            }

            List<AdLocation> adLocations = em.createQuery("SELECT ad FROM AdLocation ad WHERE ad.id IN (:ids)").setParameter("ids", adLocationIds).getResultList();

            return adLocations;
        });
    }
}
