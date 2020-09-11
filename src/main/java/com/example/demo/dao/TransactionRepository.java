package com.example.demo.dao;

import com.example.demo.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query(value="SELECT refundable FROM transactions WHERE transaction_id = (:transactionId) ",nativeQuery = true)
    Collection<Boolean> checkRefund(@Param("transactionId")String transactionId);

    @Query(value="SELECT * FROM transactions WHERE user_id = (:userId) ",nativeQuery = true)
    List<Transaction> findAllByUserId(@Param("userId")int userId);

    @Query(value="SELECT * FROM transactions WHERE transaction_id = (:transactionId)",nativeQuery = true)
    Collection<Transaction> findByTransactionId(String transactionId);

    @Modifying
    @Transactional
    @Query(value="update transactions set refundable = false where transaction_id=(:transactionId) ",nativeQuery = true)
    void setNonRefundable(String transactionId);
}
