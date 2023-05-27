package com.codingdreamtree.DtoToMyBatisResultMap.structure;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;

public class RelationalFieldInfo {
    private PsiField psiField;
    private PsiClass psiClass;

    public RelationalFieldInfo(PsiField psiField, PsiClass psiClass) {
        this.psiField = psiField;
        this.psiClass = psiClass;
    }

    public PsiField getPsiField() {
        return psiField;
    }

    public PsiClass getPsiClass() {
        return psiClass;
    }
}
