package com.yagowill.asset_automation_api.repository;

import com.yagowill.asset_automation_api.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    List<Asset> findByRpNumber(String rpNumber);

    List<Asset> findByDescription(String description);

    List<Asset> findByStatus(String status);

    List<Asset> findByOriginAgency(String originAgency);

    List<Asset> findByTermNumber(String termNumber);

    List<Asset> findByDestinationUnit(String destinationUnit);
}