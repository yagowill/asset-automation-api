package com.yagowill.asset_automation_api.automation;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.yagowill.asset_automation_api.dto.AssetItemDTO;
import com.yagowill.asset_automation_api.model.Asset;
import com.yagowill.asset_automation_api.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
                performLogin(page);

                page.navigate("https://www.sistemas.pa.gov.br/sispat");
                assertThat(page).hasTitle("Sispat  | Principal");
                Locator linkIncorporationPage = page.locator("a.item_pendencia_descricao").first();
                assertThat(linkIncorporationPage).hasText("Entrada por Transferência Não Incorporado");
                linkIncorporationPage.click();


                for (Map.Entry<String, List<AssetItemDTO>> entry : groupedItems.entrySet()) {
                    String currentDescription = entry.getKey();
                    List<AssetItemDTO> itemsOfThisDescription = entry.getValue();

                    System.out.println("Filtering by description: " + currentDescription);



                    performFilter(page, originAgency, currentDescription, termNumber);


                    for (AssetItemDTO item : itemsOfThisDescription) {
                        System.out.println("Processing RP: " + item.getRpNumber());

                        try {
                            // TODO AÇÃO: INCORPORAR O RP ESPECÍFICO
                            // page.click("botao_selecionar_primeiro_item");
                            // page.fill("input_rp", item.getRpNumber());
                            // page.click("botao_confirmar");
                            // page.waitForSelector("mensagem_sucesso");
                            // saveHistory(item.getRpNumber(), currentDescription, "SUCCESS", "Incorporated successfully.");

                        } catch (PlaywrightException e) {
                            System.err.println("Failed to process RP " + item.getRpNumber() + ": " + e.getMessage());
                            // saveHistory(item.getRpNumber(), currentDescription, "ERROR", e.getMessage());

                        }
                    }
                }

            } catch (Exception e) {
                System.err.println("Critical error navigating SispatWeb: " + e.getMessage());
            } finally {
                context.close();
                browser.close();
            }
        }
    }

    private void performLogin(Page page) {
        System.out.println("Logging into sistemas.pa.gov.br...");
        page.navigate("https://www.sistemas.pa.gov.br/governodigital/public/main/index.xhtml");
        page.getByPlaceholder("Usuário").fill(username);
        page.getByPlaceholder("Senha").fill(password);
        page.getByTitle("Entrar").click();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }
    private void performFilter(Page page, String originAgency, String description, String termNumber) {
         page.locator("tr > td:nth-child(2) > select").selectOption(originAgency);
         page.locator("css=input#incorporar_bem_destinado_ao_orgao_form_pesq:descricaobem").fill(termNumber);
         page.locator("css=input#incorporar_bem_destinado_ao_orgao_form_pesq:descricaomaterial").fill(description);
         page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(Pattern.compile("pesquisar", Pattern.CASE_INSENSITIVE))).click();
         assertThat(page.locator("#incorporar_bem_destinado_ao_orgao_form_lista:patrimonios")).isVisible();
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
