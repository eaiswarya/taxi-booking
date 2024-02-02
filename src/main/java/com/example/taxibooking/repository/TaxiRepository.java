package com.example.taxibooking.repository;

import com.example.taxibooking.model.Taxi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxiRepository extends JpaRepository<Taxi,Long>{
    List<Taxi> findByCurrentLocation(String pickupLocation);

}
