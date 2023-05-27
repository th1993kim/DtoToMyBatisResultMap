package com.codingdreamtree.DtoToMyBatisResultMap.structure;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ResultMapStructure {

    private String packageName;
    private String className;
    private String prefixName;
    private List<PsiField> fieldList;
    private List<RelationalFieldInfo> associationList;
    private List<RelationalFieldInfo> collectionList;

    public ResultMapStructure(String packageName, String className, String prefixName, List<PsiField> fieldList, List<RelationalFieldInfo> associationList, List<RelationalFieldInfo> collectionList) {
        this.packageName = packageName;
        this.className = className;
        this.prefixName = prefixName;
        this.fieldList = fieldList;
        this.associationList = associationList;
        this.collectionList = collectionList;
    }

    public ResultMapStructure() {
    }

    public static ResultMapStructureBuilder builder() {
        return new ResultMapStructureBuilder();
    }

    public String getPackageName() {
        return this.packageName;
    }

    public String getClassName() {
        return this.className;
    }

    public String getPrefixName() {
        return this.prefixName;
    }

    public List<PsiField> getFieldList() {
        return this.fieldList;
    }

    public List<RelationalFieldInfo> getAssociationList() {
        return this.associationList;
    }

    public List<RelationalFieldInfo> getCollectionList() {
        return this.collectionList;
    }

    public static class ResultMapStructureBuilder {
        private String packageName;
        private String className;
        private String prefixName;
        private List<PsiField> fieldList;
        private List<RelationalFieldInfo> associationList;
        private List<RelationalFieldInfo> collectionList;

        ResultMapStructureBuilder() {
        }

        public ResultMapStructureBuilder packageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public ResultMapStructureBuilder className(String className) {
            this.className = className;
            return this;
        }

        public ResultMapStructureBuilder prefixName(String prefixName) {
            this.prefixName = prefixName;
            return this;
        }

        public ResultMapStructureBuilder fieldList(List<PsiField> fieldList) {
            this.fieldList = fieldList;
            return this;
        }

        public ResultMapStructureBuilder associationList(List<RelationalFieldInfo> associationList) {
            this.associationList = associationList;
            return this;
        }

        public ResultMapStructureBuilder collectionList(List<RelationalFieldInfo> collectionList) {
            this.collectionList = collectionList;
            return this;
        }

        public ResultMapStructure build() {
            return new ResultMapStructure(this.packageName, this.className, this.prefixName, this.fieldList, this.associationList, this.collectionList);
        }

        public String toString() {
            return "ResultMapStructure.ResultMapStructureBuilder(packageName=" + this.packageName + ", className=" + this.className + ", prefixName=" + this.prefixName + ", fieldList=" + this.fieldList + ", associationList=" + this.associationList + ", collectionList=" + this.collectionList + ")";
        }
    }

    public String getJoinColumnAlias(String fieldName) {
        if (StringUtils.isEmpty(prefixName)) {
            return "";
        }

        return " AS " + prefixName + fieldName;
    }

}
