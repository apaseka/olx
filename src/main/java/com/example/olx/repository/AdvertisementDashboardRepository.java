package com.example.olx.repository;

import com.example.olx.model.AdvertisementDashboard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface AdvertisementDashboardRepository extends CrudRepository<AdvertisementDashboard, Long> {
}
