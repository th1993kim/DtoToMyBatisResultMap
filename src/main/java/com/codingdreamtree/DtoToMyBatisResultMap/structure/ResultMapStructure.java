package com.codingdreamtree.DtoToMyBatisResultMap.structure;

import java.util.List;

public class ResultMapStructure {

    private String packageName;
    private String className;
    private List<String> fieldList;
    private List<String> associationList;
    private List<String> collectionList;

    public ResultMapStructure(String packageName, String className, List<String> fieldList, List<String> associationList, List<String> collectionList) {
        this.packageName = packageName;
        this.className = className;
        this.fieldList = fieldList;
        this.associationList = associationList;
        this.collectionList = collectionList;
    }

    public ResultMapStructure() {
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public List<String> getFieldList() {
        return fieldList;
    }

    public List<String> getAssociationList() {
        return associationList;
    }

    public List<String> getCollectionList() {
        return collectionList;
    }

    public static ResultMapStructureBuilder builder() {
        return new ResultMapStructureBuilder();
    }

    public static class ResultMapStructureBuilder {
        private String packageName;
        private String className;
        private List<String> fieldList;
        private List<String> associationList;
        private List<String> collectionList;

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

        public ResultMapStructureBuilder fieldList(List<String> fieldList) {
            this.fieldList = fieldList;
            return this;
        }

        public ResultMapStructureBuilder associationList(List<String> associationList) {
            this.associationList = associationList;
            return this;
        }

        public ResultMapStructureBuilder collectionList(List<String> collectionList) {
            this.collectionList = collectionList;
            return this;
        }

        public ResultMapStructure build() {
            return new ResultMapStructure(this.packageName, this.className, this.fieldList, this.associationList, this.collectionList);
        }

        public String toString() {
            return "ResultMapStructure.ResultMapStructureBuilder(packageName=" + this.packageName + ", className=" + this.className + ", fieldList=" + this.fieldList + ", associationList=" + this.associationList + ", collectionList=" + this.collectionList + ")";
        }

    }
}
