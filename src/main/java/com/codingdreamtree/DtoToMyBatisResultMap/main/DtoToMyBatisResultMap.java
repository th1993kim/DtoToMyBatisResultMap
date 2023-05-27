package com.codingdreamtree.DtoToMyBatisResultMap.main;

import com.codingdreamtree.DtoToMyBatisResultMap.structure.ExtractResultMapDto;
import com.codingdreamtree.DtoToMyBatisResultMap.structure.RelationalFieldInfo;
import com.codingdreamtree.DtoToMyBatisResultMap.structure.ResultMapMenu;
import com.codingdreamtree.DtoToMyBatisResultMap.structure.ResultMapStructure;
import com.codingdreamtree.DtoToMyBatisResultMap.util.TypeCheckUtil;
import com.google.common.base.CaseFormat;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DtoToMyBatisResultMap extends AnAction {


    private final ResultMapBuilder resultMapBuilder = new ResultMapBuilder();
    private final ResultQueryBuilder resultQueryBuilder = new ResultQueryBuilder();

    @Override
    public void actionPerformed(AnActionEvent e) {

        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (psiFile == null || editor == null) return;

        PsiElement currentCaretElement = psiFile.findElementAt(editor.getCaretModel().getOffset());
        String menuText = e.getPresentation().getText();
        ResultMapMenu resultMapMenu = ResultMapMenu.findResultMapMenu(menuText);

        if (resultMapMenu == null) return;

        PsiClass psiClass = PsiTreeUtil.getParentOfType(currentCaretElement, PsiClass.class);
        int maxRecursiveCount = resultMapMenu.getMaxRecursiveCount();
        ExtractResultMapDto extractResultMapDto = ExtractResultMapDto.builder()
                .psiClass(psiClass)
                .resultMap(new LinkedHashMap<>())
                .prefixName("")
                .maxCount(new AtomicInteger(maxRecursiveCount))
                .build();
        extractResultMap(extractResultMapDto);
        String resultMapString = resultMapBuilder.createResultMapBuilder(extractResultMapDto.getResultMap());
        String resultQueryString = resultQueryBuilder.createResultQueryBuilder(extractResultMapDto.getResultMap());
        Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .setContents(new StringSelection(resultMapString + resultQueryString), null);
    }

    private void extractResultMap(ExtractResultMapDto extractResultMapDto) {
        AtomicInteger maxCount = extractResultMapDto.getMaxCount();
        PsiClass psiClass = extractResultMapDto.getPsiClass();
        String prefixName = extractResultMapDto.getPrefixName();
        Map<String, ResultMapStructure> resultMap = extractResultMapDto.getResultMap();
        if (maxCount.getAndAdd(-1) <= 0) {
            return;
        }
        String className = psiClass.getName();
        if (StringUtils.isNotBlank(className)) {

            String packageName = psiClass.getQualifiedName();
            if (resultMap.containsKey(className)) {
                return;
            }
            resultMap.put(className, null);
            List<PsiField> fieldList = new ArrayList<>();
            List<RelationalFieldInfo> associationList = new ArrayList<>();
            List<RelationalFieldInfo> collectionList = new ArrayList<>();

            PsiField[] fields = psiClass.getFields();
            Arrays.stream(fields)
                    .forEach(field ->
                            {
                                PsiType type = field.getType();
                                String canonicalText = type.getCanonicalText();
                                if (type instanceof PsiPrimitiveType) {
                                    fieldList.add(field);
                                }
                                if (type instanceof PsiClassType) {
                                    PsiClass fieldClass = ((PsiClassType) type).resolve();
                                    if (isCommonClass(canonicalText, fieldClass)) {
                                        fieldList.add(field);
                                    } else if (TypeCheckUtil.isCollection(canonicalText)) {
                                        PsiType parameter = ((PsiClassType) type).getParameters()[0];
                                        PsiClass genericClass = ((PsiClassType) parameter).resolve();
                                        collectionList.add(new RelationalFieldInfo(field, genericClass));
                                        extractResultMap(ExtractResultMapDto.builder()
                                                .psiClass(genericClass)
                                                .resultMap(resultMap)
                                                .prefixName(createPrefixName(prefixName, genericClass))
                                                .maxCount(maxCount)
                                                .build());

                                    } else {
                                        associationList.add(new RelationalFieldInfo(field, fieldClass));
                                        extractResultMap(ExtractResultMapDto.builder()
                                                .psiClass(fieldClass)
                                                .resultMap(resultMap)
                                                .prefixName(createPrefixName(prefixName, fieldClass))
                                                .maxCount(maxCount)
                                                .build());
                                    }
                                }
                            }
                    );

            ResultMapStructure resultMapStructure = ResultMapStructure.builder()
                    .className(className)
                    .packageName(packageName)
                    .prefixName(prefixName)
                    .fieldList(fieldList)
                    .associationList(associationList)
                    .collectionList(collectionList)
                    .build();

            resultMap.put(className, resultMapStructure);
        }
    }

    private boolean isCommonClass(String canonicalText, PsiClass fieldClass) {
        return TypeCheckUtil.isCharClass(canonicalText)
                || TypeCheckUtil.isNumberClass(canonicalText)
                || TypeCheckUtil.isTimeClass(canonicalText)
                || TypeCheckUtil.isEnumClass(fieldClass);
    }

    @NotNull
    private String createPrefixName(String prefixName, PsiClass psiClass) {
        if (psiClass != null && psiClass.getName() != null) {
            return prefixName + getFirstCharLowerCase(psiClass.getName()) + "_";
        }
        return prefixName;
    }
    @NotNull
    private String getFirstCharLowerCase(String originName) {
        return originName.substring(0, 1).toLowerCase() + originName.substring(1);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        final PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        boolean visible = false;
        if (psiFile != null && editor != null) {
            visible = isClassElement(psiFile, editor);
        }
        e.getPresentation().setEnabledAndVisible(visible);
    }

    private boolean isClassElement(PsiFile psiFile, Editor editor) {
        PsiElement currentCaretElement = psiFile.findElementAt(editor.getCaretModel().getOffset());
        PsiClass psiClass = PsiTreeUtil.getParentOfType(currentCaretElement, PsiClass.class);
        return psiClass != null;
    }
}

