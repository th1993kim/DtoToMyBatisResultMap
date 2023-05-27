package com.codingdreamtree.DtoToMyBatisResultMap.main;

import com.codingdreamtree.DtoToMyBatisResultMap.structure.ResultMapStructure;
import com.google.common.base.CaseFormat;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        List<String> selectColumnQueryList = new ArrayList<>();
        resultMap.forEach((className, resultMapStructure) -> {
            final String dbName = getLowerSnakeName(className);

            List<String> selectColumnQueryResultList = resultMapStructure.getFieldList()
                    .stream()
                    .map(field -> {
                        StringBuilder selectColumnQueryBuilder = new StringBuilder();
                        selectColumnQueryBuilder.append(dbName)
                                .append(".")
                                .append(getLowerSnakeName(field.getName()))
                                .append(resultMapStructure.getJoinColumnAlias(getLowerSnakeName(field.getName())));
                        return getStringBuilderResult(selectColumnQueryBuilder);
                    })
                    .collect(Collectors.toList());

            selectColumnQueryList.addAll(selectColumnQueryResultList);
        });

        return String.join(",\n", selectColumnQueryList) + "\n";
    }
    private String createFromQuery(String firstClassName) {
        final String dbName = getLowerSnakeName(firstClassName);
        return "FROM " + dbName + " " + dbName + "\n";
    }

    @NotNull
    private String getLowerSnakeName(String originalName) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, originalName);
    }

    private String createJoinQuery(Map<String, ResultMapStructure> resultMap) {
        StringBuilder queryBuilder = new StringBuilder();
        String[] classNameArray = resultMap.keySet().toArray(new String[0]);
        for (int i = 0; i < classNameArray.length; i++) {
            if (i == 0) continue;

            final String dbName = getLowerSnakeName(classNameArray[i]);
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