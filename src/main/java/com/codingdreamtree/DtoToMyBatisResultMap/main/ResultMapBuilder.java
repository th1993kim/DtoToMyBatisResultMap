package com.codingdreamtree.DtoToMyBatisResultMap.main;

import com.codingdreamtree.DtoToMyBatisResultMap.structure.ResultMapStructure;
import com.google.common.base.CaseFormat;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class ResultMapBuilder {
    public ResultMapBuilder() {
    }

    String createResultMapBuilder(Map<String, ResultMapStructure> resultMap) {
        final StringBuilder resultMapBuilder = new StringBuilder();

        resultMap.forEach((className, resultMapStructure) ->
                resultMapBuilder.append("<resultMap id=\"")
                        .append(getResultMapName(className))
                        .append("\" type=\"")
                        .append(resultMapStructure.getPackageName())
                        .append("\"> \n")
                        .append(createFieldResultMap(resultMapStructure.getFieldList()))
                        .append(createAssociationResultMap(resultMapStructure.getAssociationList()))
                        .append(createCollectionResultMap(resultMapStructure.getCollectionList()))
                        .append("</resultMap>\n\n\n"));

        return getStringBuilderResult(resultMapBuilder);
    }

    @NotNull
    private String getResultMapName(String fieldName) {
        return fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1) + "Map";
    }

    @NotNull
    private String getStringBuilderResult(StringBuilder resultMapBuilder) {
        String result = resultMapBuilder.toString();
        resultMapBuilder.setLength(0);
        return result;
    }

    String createFieldResultMap(List<String> fieldList) {
        StringBuilder resultMapBuilder = new StringBuilder();
        fieldList.forEach(field ->
                resultMapBuilder.append("\t<result column=\"")
                        .append(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field))
                        .append("\" property=\"")
                        .append(field)
                        .append("\"/>\n"));
        return getStringBuilderResult(resultMapBuilder);
    }

    String createAssociationResultMap(List<String> associationList) {
        StringBuilder resultMapBuilder = new StringBuilder();
        associationList.forEach(field ->
                resultMapBuilder.append("\t<association property=\"")
                        .append(field)
                        .append("\" columnPrefix=\"")
                        .append(field)
                        .append("_\" resultMap=\"")
                        .append(getResultMapName(field))
                        .append("\"/>\n"));
        return getStringBuilderResult(resultMapBuilder);
    }

    String createCollectionResultMap(List<String> collectionList) {
        StringBuilder resultMapBuilder = new StringBuilder();
        collectionList.forEach(field ->
                resultMapBuilder.append("\t<collection property=\"")
                        .append(field)
                        .append("\" columnPrefix=\"")
                        .append(field)
                        .append("_\" resultMap=\"")
                        .append(getResultMapName(field))
                        .append("\"/>\n"));
        return getStringBuilderResult(resultMapBuilder);
    }


}