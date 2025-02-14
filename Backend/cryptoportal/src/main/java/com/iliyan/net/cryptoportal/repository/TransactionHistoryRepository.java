package com.iliyan.net.cryptoportal.repository;

import com.iliyan.net.cryptoportal.entity.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {
    List<TransactionHistory> findByClientId(Long clientId);
}
