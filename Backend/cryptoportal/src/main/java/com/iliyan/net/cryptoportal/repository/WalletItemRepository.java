package com.iliyan.net.cryptoportal.repository;

import com.iliyan.net.cryptoportal.entity.Client;
import com.iliyan.net.cryptoportal.entity.WalletItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletItemRepository extends JpaRepository<WalletItem, Long> {

    Optional<WalletItem> findByClientAndSymbol(Client client, String symbol);

    List<WalletItem> findByClientId(Long clientId);

}
