package com.cloudator.repository;

import com.cloudator.entity.Location;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends CrudRepository<Location, Long> {
    @Query(
            value = "update Location set isMonitored = false"
    )
    @Modifying
    void setMonitoredFalse();

    List<Location> findAllByIsMonitoredTrue();

}
