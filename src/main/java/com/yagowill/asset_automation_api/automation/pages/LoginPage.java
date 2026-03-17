package com.yagowill.asset_automation_api.automation.pages;

import com.microsoft.playwright.Page;

public class LoginPage extends BasePage {

    private static final String LOGIN_URL = "https://www.sistemas.pa.gov.br/governodigital/public/main/index.xhtml";

    public LoginPage(Page page) {
        super(page);
    }

    public void navigateTo() {
        navigateTo(LOGIN_URL);
    }

    public void login(String username, String password) {
        System.out.println("Logging into sistemas.pa.gov.br...");
        navigateTo();
        page.getByPlaceholder("Usuário").fill(username);
        page.getByPlaceholder("Senha").fill(password);
        page.getByTitle("Entrar").click();
        waitForDomContentLoaded();
    }
}
