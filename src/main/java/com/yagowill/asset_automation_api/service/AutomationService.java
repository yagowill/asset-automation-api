package com.yagowill.asset_automation_api.service;

import com.yagowill.asset_automation_api.automation.SispatScraper;
import com.yagowill.asset_automation_api.dto.AssetItemDTO;
import com.yagowill.asset_automation_api.dto.AutomationRequestDTO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AutomationService {
    private final SispatScraper sispatScraper;

    public AutomationService(SispatScraper sispatScraper) {
        this.sispatScraper = sispatScraper;
    }

    @Async
    public void processAssetsAsync(AutomationRequestDTO request) {
        System.out.println("Starting background processing:\nagency: " + request.getOriginAgency() + "\nterm number: " + request.getTermNumber() + "\ndestination unit: " + request.getDestinationUnit());

        Map<String, List<AssetItemDTO>> groupedItems = request.getItems().stream()
                .collect(Collectors.groupingBy(AssetItemDTO::getDescription));

        System.out.println("Items grouped into " + groupedItems.size() + " unique descriptions.");

        try {
            sispatScraper.executeIncorporation(groupedItems, request.getOriginAgency(), request.getTermNumber(), request.getDestinationUnit());

            System.out.println("Automation finished successfully.");
        } catch (Exception e) {
            System.err.println("Critical error during automation: " + e.getMessage());
        }
    }

}
