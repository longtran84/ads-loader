package vn.fintechviet.location.repository;

import vn.fintechviet.location.LocationExecutionContext;
import vn.fintechviet.location.model.AdLocation;
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
    public List<AdLocation> findAdLocationsNearBy(String lng, String lat) {
        return wrap(em -> {
            StringBuilder queryStr = new StringBuilder("SELECT ad.id, ad.name FROM ad_location ad WHERE ad.flightId IN (SELECT id FROM flight WHERE startDate <= NOW() AND (endDate >= NOW() OR endDate IS NULL)) " +
                    "AND (6731 * acos(cos(radians(:lat)) * cos(radians(ad.lat)) * cos(radians(ad.lng) - radians(:lng)) + sin(radians(:lat)) * sin(radians(lat)))) < 5 AND ad.status = 'ACTIVE'");
            Query queryIds = em.createNativeQuery(queryStr.toString());
            queryIds.setParameter("lng", lng);
            queryIds.setParameter("lat", lat);
            List<Object[]> locationObjs = queryIds.getResultList();
            List<Long> adLocationIds = new ArrayList<Long>();
            for (Object row[] : locationObjs) {
                BigInteger id = (BigInteger)row[0];
                adLocationIds.add(id.longValue());
            }

            List<AdLocation> adLocations = new ArrayList<>();

            if (adLocationIds.size() > 0) {
                adLocations = em.createQuery("SELECT ad FROM AdLocation ad WHERE ad.id IN (:ids)").setParameter("ids", adLocationIds).getResultList();
            }

            return adLocations;
        });
    }
}
