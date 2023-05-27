package com.codingdreamtree.DtoToMyBatisResultMap.structure;

import com.intellij.psi.PsiClass;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ExtractResultMapDto {

    private PsiClass psiClass;
    private Map<String, ResultMapStructure> resultMap;
    private String prefixName;
    private AtomicInteger maxCount;
    public ExtractResultMapDto(PsiClass psiClass, Map<String, ResultMapStructure> resultMap, String prefixName, AtomicInteger maxCount) {
        this.psiClass = psiClass;
        this.resultMap = resultMap;
        this.prefixName = prefixName;
        this.maxCount = maxCount;
    }

    public ExtractResultMapDto() {
    }

    public static ExtractResultMapDtoBuilder builder() {
        return new ExtractResultMapDtoBuilder();
    }

    public PsiClass getPsiClass() {
        return this.psiClass;
    }

    public Map<String, ResultMapStructure> getResultMap() {
        return this.resultMap;
    }

    public String getPrefixName() {
        return this.prefixName;
    }

    public AtomicInteger getMaxCount() {
        return this.maxCount;
    }

    public static class ExtractResultMapDtoBuilder {
        private PsiClass psiClass;
        private Map<String, ResultMapStructure> resultMap;
        private String prefixName;
        private AtomicInteger maxCount;

        ExtractResultMapDtoBuilder() {
        }

        public ExtractResultMapDtoBuilder psiClass(PsiClass psiClass) {
            this.psiClass = psiClass;
            return this;
        }

        public ExtractResultMapDtoBuilder resultMap(Map<String, ResultMapStructure> resultMap) {
            this.resultMap = resultMap;
            return this;
        }

        public ExtractResultMapDtoBuilder prefixName(String prefixName) {
            this.prefixName = prefixName;
            return this;
        }

        public ExtractResultMapDtoBuilder maxCount(AtomicInteger maxCount) {
            this.maxCount = maxCount;
            return this;
        }

        public ExtractResultMapDto build() {
            return new ExtractResultMapDto(this.psiClass, this.resultMap, this.prefixName, this.maxCount);
        }

        public String toString() {
            return "ExtractResultMapDto.ExtractResultMapDtoBuilder(psiClass=" + this.psiClass + ", resultMap=" + this.resultMap + ", prefixName=" + this.prefixName + ", maxCount=" + this.maxCount + ")";
        }
    }
}
