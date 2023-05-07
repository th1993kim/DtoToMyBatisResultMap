package com.codingdreamtree.DtoToMyBatisResultMap.structure;

import java.util.Arrays;

public enum ResultMapMenu {
    DTO_TO_SINGLE_RESULT_MAP("ConvertDtoToSingleResultMap", 1),
    DTO_TO_MULTIPLE_RESULT_MAP("ConvertDtoToMultipleResultMap", 10);

    private final String menuText;
    private final int maxRecursiveCount;

    ResultMapMenu(String menuText, int maxRecursiveCount) {
        this.menuText = menuText;
        this.maxRecursiveCount = maxRecursiveCount;
    }

    public String getMenuText() {
        return menuText;
    }

    public int getMaxRecursiveCount() {
        return maxRecursiveCount;
    }

    public static ResultMapMenu findResultMapMenu(String menuText) {
        return Arrays.stream(ResultMapMenu.values())
                .filter(resultMapMenu -> resultMapMenu.getMenuText().equals(menuText))
                .findFirst()
                .orElse(null);
    }
}
