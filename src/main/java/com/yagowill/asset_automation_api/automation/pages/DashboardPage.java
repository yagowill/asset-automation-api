package com.yagowill.asset_automation_api.automation.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class DashboardPage extends BasePage {

    private static final String SISPAT_URL = "https://www.sistemas.pa.gov.br/sispat";

    public DashboardPage(Page page) {
        super(page);
    }

    public void navigateTo() {
        navigateTo(SISPAT_URL);
        assertThat(page).hasTitle("Sispat  | Principal");
    }

    public void goToIncorporationPage() {
        Locator linkIncorporationPage = page.locator("a.item_pendencia_descricao").first();
        assertThat(linkIncorporationPage).hasText("Entrada por Transferência Não Incorporado");
        linkIncorporationPage.click();
    }
}
