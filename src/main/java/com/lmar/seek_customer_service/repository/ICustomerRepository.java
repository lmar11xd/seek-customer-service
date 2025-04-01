package com.lmar.seek_customer_service.repository;

import com.lmar.seek_customer_service.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ICustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT COALESCE(AVG(c.age), 0) FROM Customer c")
    Double calculateAverageAge();

    @Query("SELECT COALESCE(STDDEV(c.age), 0) FROM Customer c")
    Double calculateStandardDeviationAge();
}
