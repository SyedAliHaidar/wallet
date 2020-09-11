package com.example.demo.dao;

import com.example.demo.model.AmountTeller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;

@Repository
public interface AmountTellerRepository extends JpaRepository<AmountTeller, Integer> {
    @Query(value="SELECT final_amount FROM amount_teller WHERE serial_no = (:serial_no) ",nativeQuery = true)
    Collection<Integer> balanceTeller(@Param("serial_no") Integer serialNo);

    @Modifying
    @Transactional
    @Query(value="update amount_teller set final_amount=(:final_amount), updated = (:updated) WHERE serial_no = (:serial_no) ",nativeQuery = true)
    void updateAmount( @Param("final_amount") Integer finalAmount,@Param("updated") String updated,@Param("serial_no") Integer serialNo);
}
