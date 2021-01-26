package com.cloudator.repository;

import com.cloudator.entity.ForecastAlert;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForecastAlertRepository extends CrudRepository<ForecastAlert, Long> {
    List<ForecastAlert> findAllByTimestampGreaterThanEqualOrderByTimestamp(long timestamp);
}
