package com.yagowill.asset_automation_api.dto;

import lombok.Data;
import java.util.List;

@Data
public class AutomationRequestDTO {
    private String originAgency;
    private String termNumber;
    private String destinationUnit;
    private List<AssetItemDTO> items;
}
