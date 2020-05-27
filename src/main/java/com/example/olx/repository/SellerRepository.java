package com.example.olx.repository;

import com.example.olx.model.Seller;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface SellerRepository extends CrudRepository<Seller, Long> {
}
