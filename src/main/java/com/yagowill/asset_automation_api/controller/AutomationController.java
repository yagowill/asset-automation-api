package com.yagowill.asset_automation_api.controller;


import com.yagowill.asset_automation_api.dto.AutomationRequestDTO;
import com.yagowill.asset_automation_api.service.AutomationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/automation")
public class AutomationController {
    private final AutomationService automationService;

    // Injeção de dependência via construtor (Melhor prática do Spring)
    public AutomationController(AutomationService automationService) {
        this.automationService = automationService;
    }

    @PostMapping("/incorporation")
    public ResponseEntity<String> startAutomation(@RequestBody AutomationRequestDTO request) {

        if (request.getItems() == null || request.getItems().isEmpty()) {
            return ResponseEntity.badRequest().body("Error: The item list cannot be empty.");
        }

        if (request.getOriginAgency() == null || request.getOriginAgency().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Error: Origin agency is required.");
        }

        if (request.getTermNumber() == null || request.getTermNumber().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Error: Term number is required.");
        }

        if (request.getDestinationUnit() == null || request.getDestinationUnit().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Error: Destination Unit is required.");
        }

        automationService.processAssetsAsync(request);

        return ResponseEntity.accepted().body(
                "Automation started successfully. Processing " + request.getItems().size() + " items in the background."
        );
    }

}
