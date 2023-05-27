package com.codingdreamtree.DtoToMyBatisResultMap.main;

import com.codingdreamtree.DtoToMyBatisResultMap.structure.RelationalFieldInfo;
import com.codingdreamtree.DtoToMyBatisResultMap.structure.ResultMapStructure;
import com.google.common.base.CaseFormat;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
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

    String createFieldResultMap(List<PsiField> fieldList) {
        StringBuilder resultMapBuilder = new StringBuilder();
        fieldList.forEach(field ->
                resultMapBuilder.append("\t<result column=\"")
                        .append(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName()))
                        .append("\" property=\"")
                        .append(field.getName())
                        .append("\"/>\n"));
        return getStringBuilderResult(resultMapBuilder);
    }

    String createAssociationResultMap(List<RelationalFieldInfo> associationList) {
        StringBuilder resultMapBuilder = new StringBuilder();
        associationList.forEach(relationalFieldInfo ->
                resultMapBuilder.append("\t<association property=\"")
                        .append(createRelationCommonText(relationalFieldInfo)));
        return getStringBuilderResult(resultMapBuilder);
    }

    private String createRelationCommonText(RelationalFieldInfo relationalFieldInfo) {
        StringBuilder resultMapBuilder = new StringBuilder();
        resultMapBuilder.append(relationalFieldInfo.getPsiField().getName())
                .append("\" columnPrefix=\"")
                .append(getFirstCharLowerCaseClassName(relationalFieldInfo.getPsiClass()))
                .append("_\" resultMap=\"")
                .append(getResultMapNameFromPsiClass(relationalFieldInfo.getPsiClass()))
                .append("\"/>\n");
        return getStringBuilderResult(resultMapBuilder);
    }

    String createCollectionResultMap(List<RelationalFieldInfo> collectionList) {
        StringBuilder resultMapBuilder = new StringBuilder();
        collectionList.forEach(relationalFieldInfo ->
                resultMapBuilder.append("\t<collection property=\"")
                        .append(createRelationCommonText(relationalFieldInfo)));
        return getStringBuilderResult(resultMapBuilder);
    }

    private String getResultMapNameFromPsiClass(PsiClass psiClass) {
        String className = psiClass.getName();
        if (className == null) return "";
        return className.substring(0, 1).toLowerCase() + className.substring(1) + "Map";
    }

    @NotNull
    private String getFirstCharLowerCaseClassName(PsiClass psiClass) {
        String className = psiClass.getName();
        if (className == null) return "";
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }


}