package com.example.olx.repository;

import com.example.olx.model.AdvertisementDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface AdvertisementDetailsRepository extends JpaRepository<AdvertisementDetail, Long> {

}
