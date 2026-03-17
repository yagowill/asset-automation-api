package com.yagowill.asset_automation_api.automation;

import com.microsoft.playwright.*;
import com.yagowill.asset_automation_api.automation.pages.DashboardPage;
import com.yagowill.asset_automation_api.automation.pages.IncorporationPage;
import com.yagowill.asset_automation_api.automation.pages.LoginPage;
import com.yagowill.asset_automation_api.dto.AssetItemDTO;
import com.yagowill.asset_automation_api.model.Asset;
import com.yagowill.asset_automation_api.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public class SispatScraper {

    private final AssetRepository repository;

    @Value("${sispat.credentials.username}")
    private String username;

    @Value("${sispat.credentials.password}")
    private String password;

    public SispatScraper(AssetRepository repository) {
        this.repository = repository;
    }

    public void executeIncorporation(Map<String, List<AssetItemDTO>> groupedItems, String originAgency, String termNumber, String destinationUnit) {
        System.out.println("Starting Playwright automation...");

        try (Playwright playwright = Playwright.create()) {

            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            try {
                LoginPage loginPage = new LoginPage(page);
                loginPage.login(username, password);

                DashboardPage dashboardPage = new DashboardPage(page);
                dashboardPage.navigateTo();
                dashboardPage.goToIncorporationPage();

                IncorporationPage incorporationPage = new IncorporationPage(page);

                for (Map.Entry<String, List<AssetItemDTO>> entry : groupedItems.entrySet()) {
                    String currentDescription = entry.getKey();
                    List<AssetItemDTO> itemsOfThisDescription = entry.getValue();

                    System.out.println("Filtering by description: " + currentDescription);
                    incorporationPage.filterAssets(originAgency, currentDescription, termNumber);

                    for (AssetItemDTO item : itemsOfThisDescription) {
                        System.out.println("Processing RP: " + item.getRpNumber());

                        try {
                            incorporationPage.assertItemDescription(currentDescription);
                            incorporationPage.incorporateItem(item.getRpNumber(), destinationUnit);
                            // saveHistory(item.getRpNumber(), currentDescription, "SUCCESS", "Incorporated successfully.");

                        } catch (PlaywrightException e) {
                            String errorMessage = incorporationPage.getErrorMessage();
                            System.err.println("Failed to process RP " + item.getRpNumber() + ": " + errorMessage + ": " + e.getMessage());
                            // saveHistory(item.getRpNumber(), currentDescription, "ERROR", e.getMessage());
                            incorporationPage.cancelIncorporation();
                            incorporationPage.filterAssets(originAgency, currentDescription, termNumber);
                        }
                    }
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                context.close();
                browser.close();
            }
        }
    }

    private void saveHistory(String rpNumber, String description, String status, String logMessage) {
        Asset history = new Asset();
        history.setRpNumber(rpNumber);
        history.setDescription(description);
        history.setIncorporationDate(LocalDateTime.now());
        history.setStatus(status);

        if (logMessage != null && logMessage.length() > 500) {
            logMessage = logMessage.substring(0, 497) + "...";
        }
        history.setLogMessage(logMessage);

        // repository.save(history);
    }
}
