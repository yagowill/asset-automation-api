package com.yagowill.asset_automation_api.automation.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import java.util.regex.Pattern;

public class IncorporationPage extends BasePage {

    public IncorporationPage(Page page) {
        super(page);
    }

    public void filterAssets(String originAgency, String description, String termNumber) {
        page.locator("tr > td:nth-child(2) > select").selectOption(originAgency);
        page.locator("//input[@id=\"incorporar_bem_destinado_ao_orgao_form_pesq:descricaobem\"]").fill(termNumber);
        page.locator("//*[@id=\"incorporar_bem_destinado_ao_orgao_form_pesq:descricaomaterial\"]").fill(description);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(Pattern.compile("pesquisar", Pattern.CASE_INSENSITIVE))).click();
        assertThat(page.locator("//table[@id=\"incorporar_bem_destinado_ao_orgao_form_lista:patrimonios\"]")).isVisible();
    }

    public void assertItemDescription(String expectedDescription) {
        assertThat(page.locator("td[id*=\"incorporar_bem_destinado_ao_orgao_form_lista:patrimonios:0:j_id446\"]")).hasText(expectedDescription);
    }

    public void clickIncorporateFirstItem() {
        page.click("//a[@id=\"incorporar_bem_destinado_ao_orgao_form_lista:patrimonios:0:incorporarbens\"]");
    }

    public void fillRpNumber(String rpNumber) {
        page.locator("//input[@id=\"incorporar_bem_destinado_ao_orgao_form_cad:rpInicial\"]").fill(rpNumber);
    }

    public void selectDestinationUnit(String destinationUnit) {
        page.getByTitle("Localizar Unidade de Localização de Destino").click();
        page.locator("//*[@id=\"modal_searchUnidadeDestino_unidade_search_form\"]/table[2]/tbody/tr/td[1]/table/tbody/tr[1]/td[2]/input").fill(destinationUnit);
        page.locator("//*[@id=\"modal_searchUnidadeDestino_unidade_search_form:j_id778\"]").click();
        page.locator("//*[@id=\"modal_searchUnidadeDestino_unidade_search_form:unidadesearchUnidadeDestino:0:confirmacaoorigem\"]").click();
    }

    public void confirmIncorporation() {
        page.pause();
        // page.locator("//input[@id=\"incorporar_bem_destinado_ao_orgao_form_cad:Incorporar\"]").click();
        page.locator("input[id*=\"incorporar_bem_destinado_ao_orgao_form_lista:cancelaimpressao\"]").click();
    }

    public void incorporateItem(String rpNumber, String destinationUnit) {
        clickIncorporateFirstItem();
        fillRpNumber(rpNumber);
        selectDestinationUnit(destinationUnit);
        confirmIncorporation();
    }

    public String getErrorMessage() {
        return page.locator("div.erros > table > tbody > tr > td > span").innerText();
    }

    public void cancelIncorporation() {
        page.locator("input[value=\"Cancelar\"]").click();
    }
}
