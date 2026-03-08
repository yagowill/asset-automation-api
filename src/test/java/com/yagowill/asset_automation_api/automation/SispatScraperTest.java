package com.yagowill.asset_automation_api.automation;

import com.yagowill.asset_automation_api.dto.AssetItemDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class SispatScraperTest {

    @Autowired
    private SispatScraper scraper;

    @Test
    public void testarAutomacaoNoNavegador() {
        // 1. Prepara os dados falsos para o teste
        AssetItemDTO item1 = new AssetItemDTO();
        item1.setRpNumber("99999");
        item1.setDescription("NOBREAK 0,7KVA");

        Map<String, List<AssetItemDTO>> dadosMocados = Map.of(
                "NOBREAK 0,7KVA", List.of(item1)
        );

        // 2. Chama o robô diretamente
        // Ele vai abrir o navegador e tentar fazer tudo
        scraper.executeIncorporation(dadosMocados, "SEGUP - SECRETARIA DE ESTADO DE SEGURANÇA PÚBLICA E DEFESA SOCIAL", "2025/138", "unidade de patrimonio");

        // Se o código rodar até o final sem lançar exceção, o teste passa!
    }
}
