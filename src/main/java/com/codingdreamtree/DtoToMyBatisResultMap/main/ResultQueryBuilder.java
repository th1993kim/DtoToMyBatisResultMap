package com.codingdreamtree.DtoToMyBatisResultMap.main;

import com.codingdreamtree.DtoToMyBatisResultMap.structure.ResultMapStructure;
import com.google.common.base.CaseFormat;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ResultQueryBuilder {
    public ResultQueryBuilder() {
    }

    String createResultQueryBuilder(Map<String, ResultMapStructure> resultMap) {
        final StringBuilder resultMapBuilder = new StringBuilder();
        final String firstClassName = resultMap.keySet().iterator().next();
        if (firstClassName != null) {

            resultMapBuilder.append("<select id=\"\"  resultMap=\"")
                    .append(getResultMapName(firstClassName))
                    .append("\">\n")
                    .append(createSelectColumnsQuery(resultMap))
                    .append(createFromQuery(firstClassName))
                    .append(createJoinQuery(resultMap))
                    .append("</select>");
        }

        return getStringBuilderResult(resultMapBuilder);
    }

    @NotNull
    private String getResultMapName(String fieldName) {
        return fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1) + "Map";
    }

    private String createSelectColumnsQuery(Map<String, ResultMapStructure> resultMap) {
        StringBuilder resultMapBuilder = new StringBuilder();
        resultMap.forEach((className, resultMapStructure) -> {
            final String dbName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, className);
            resultMapStructure.getFieldList()
                    .forEach(field -> {
                        resultMapBuilder.append(dbName)
                                .append(".")
                                .append(field)
                                .append(resultMapStructure.getJoinColumnAlias())
                                .append(field)
                                .append(",\n");
                    });
        });

        return getStringBuilderResult(resultMapBuilder);
    }
    private String createFromQuery(String firstClassName) {
        final String dbName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, firstClassName);
        return "FROM " + dbName + " " + dbName + "\n";
    }

    private String createJoinQuery(Map<String, ResultMapStructure> resultMap) {
        StringBuilder queryBuilder = new StringBuilder();
        String[] classNameArray = resultMap.keySet().toArray(new String[0]);
        for (int i = 0; i < classNameArray.length; i++) {
            if (i == 0) continue;

            final String dbName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, classNameArray[i]);
            queryBuilder.append("JOIN ")
                    .append(dbName)
                    .append(" ")
                    .append(dbName)
                    .append("\n")
                    .append("ON \n");
        }
        return getStringBuilderResult(queryBuilder);
    }

    @NotNull
    private String getStringBuilderResult(StringBuilder resultMapBuilder) {
        String result = resultMapBuilder.toString();
        resultMapBuilder.setLength(0);
        return result;
    }
}