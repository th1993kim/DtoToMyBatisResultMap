package com.codingdreamtree.DtoToMyBatisResultMap.main;

import com.codingdreamtree.DtoToMyBatisResultMap.structure.ResultMapMenu;
import com.codingdreamtree.DtoToMyBatisResultMap.structure.ResultMapStructure;
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
    private static final String JAVA_TIME_LOCAL_DATE = "java.time.LocalDate";
    private static final String JAVA_TIME_LOCAL_DATETIME = "java.time.LocalDateTime";
    private static final String JAVA_TIME_ZONED_DATE = "java.time.ZonedDate";
    private static final String JAVA_TIME_ZONED_DATETIME = "java.time.ZonedDateTime";

    private static final Set<String> JAVA_TIME_SET = new HashSet<>() {
        {
            add(JAVA_TIME_LOCAL_DATE);
            add(JAVA_TIME_LOCAL_DATETIME);
            add(JAVA_TIME_ZONED_DATE);
            add(JAVA_TIME_ZONED_DATETIME);
        }
    };

    private static final Set<String> WRAPPER_CLASS_SET = new HashSet<>() {
        {
            add(CommonClassNames.JAVA_LANG_NUMBER);
            add(CommonClassNames.JAVA_LANG_BOOLEAN);
            add(CommonClassNames.JAVA_LANG_BYTE);
            add(CommonClassNames.JAVA_LANG_SHORT);
            add(CommonClassNames.JAVA_LANG_INTEGER);
            add(CommonClassNames.JAVA_LANG_LONG);
            add(CommonClassNames.JAVA_LANG_FLOAT);
            add(CommonClassNames.JAVA_LANG_DOUBLE);
            add(CommonClassNames.JAVA_LANG_CHARACTER);
        }
    };

    private static final Set<String> COLLECTION_SET = new HashSet<>() {
        {
            add(CommonClassNames.JAVA_UTIL_ARRAYS);
            add(CommonClassNames.JAVA_UTIL_COLLECTIONS);
            add(CommonClassNames.JAVA_UTIL_COLLECTION);
            add(CommonClassNames.JAVA_UTIL_MAP);
            add(CommonClassNames.JAVA_UTIL_MAP_ENTRY);
            add(CommonClassNames.JAVA_UTIL_HASH_MAP);
            add(CommonClassNames.JAVA_UTIL_LINKED_HASH_MAP);
            add(CommonClassNames.JAVA_UTIL_CONCURRENT_HASH_MAP);
            add(CommonClassNames.JAVA_UTIL_LIST);
            add(CommonClassNames.JAVA_UTIL_ARRAY_LIST);
            add(CommonClassNames.JAVA_UTIL_LINKED_LIST);
            add(CommonClassNames.JAVA_UTIL_SET);
            add(CommonClassNames.JAVA_UTIL_HASH_SET);
            add(CommonClassNames.JAVA_UTIL_LINKED_HASH_SET);
            add(CommonClassNames.JAVA_UTIL_SORTED_SET);
        }
    };

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
        AtomicInteger maxCount = new AtomicInteger(maxRecursiveCount);
        Map<String, ResultMapStructure> resultMap = new HashMap<>();
        extractResultMap(psiClass, resultMap, maxCount);
        String resultMapString =  createResultMapBuilder(resultMap);

        Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .setContents(new StringSelection(resultMapString), null);
    }

    private String createResultMapBuilder(Map<String, ResultMapStructure> resultMap) {
        final StringBuilder resultMapBuilder = new StringBuilder();

        resultMap.forEach((mapName, resultMapStructure) -> {
            resultMapBuilder.append("<resultMap id=\"")
                    .append(mapName)
                    .append("\" type=\"")
                    .append(resultMapStructure.getPackageName())
                    .append("\"> \n")
                    .append(createFieldResultMap(resultMapStructure.getFieldList()))
                    .append(createAssociationResultMap(resultMapStructure.getAssociationList()))
                    .append(createCollectionResultMap(resultMapStructure.getCollectionList()));
            resultMapBuilder.append("</resultMap>\n\n\n");
        });

        String result = resultMapBuilder.toString();
        resultMapBuilder.setLength(0);
        return result;
    }

    private String createFieldResultMap(List<String> fieldList) {
        StringBuilder resultMapBuilder = new StringBuilder();
        fieldList.forEach(field -> {
            resultMapBuilder.append("\t<result column=\"")
                    .append(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field))
                    .append("\" property=\"")
                    .append(field)
                    .append("\"/>\n");
        });
        String result = resultMapBuilder.toString();
        resultMapBuilder.setLength(0);
        return result;
    }

    private String createAssociationResultMap(List<String> associationList) {
        StringBuilder resultMapBuilder = new StringBuilder();
        associationList.forEach(field -> {
            resultMapBuilder.append("\t<association property=\"")
                    .append(field)
                    .append("\" columnPrefix=\"")
                    .append(field)
                    .append("_\" resultMap=\"")
                    .append(field)
                    .append("Map")
                    .append("\"/>\n");
        });
        String result = resultMapBuilder.toString();
        resultMapBuilder.setLength(0);
        return result;
    }

    private String createCollectionResultMap(List<String> collectionList) {
        StringBuilder resultMapBuilder = new StringBuilder();
        collectionList.forEach(field -> {
            resultMapBuilder.append("\t<collection property=\"")
                    .append(field)
                    .append("\" columnPrefix=\"")
                    .append(field)
                    .append("_\" resultMap=\"")
                    .append(field)
                    .append("Map")
                    .append("\"/>\n");
        });
        String result = resultMapBuilder.toString();
        resultMapBuilder.setLength(0);
        return result;
    }

    private void extractResultMap(PsiClass aClass, Map<String, ResultMapStructure> resultMap, AtomicInteger maxCount) {
        if (maxCount.get() <= 0) {
            return;
        }
        maxCount.set(maxCount.get() - 1);
        String className = aClass.getName();

        if (StringUtils.isNotBlank(className)) {
            String packageName = aClass.getQualifiedName();
            String resultMapName = getResultMapName(className);
            if (resultMap.containsKey(resultMapName)) {
                return;
            }
            List<String> fieldList = new ArrayList<>();
            List<String> associationList = new ArrayList<>();
            List<String> collectionList = new ArrayList<>();

            PsiField[] fields = aClass.getFields();
            Arrays.stream(fields)
                    .forEach(field ->
                            {
                                PsiType type = field.getType();
                                String canonicalText = type.getCanonicalText();
                                String fieldName = field.getName();
                                if (type instanceof PsiPrimitiveType) {
                                    fieldList.add(fieldName);
                                }
                                if (type instanceof PsiClassType) {
                                    PsiClass fieldClass = ((PsiClassType) type).resolve();
                                    if (isWrapperClass(canonicalText)
                                            || isString(canonicalText)
                                            || isTimeClass(canonicalText)
                                            || isEnumClass(fieldClass)) {
                                        fieldList.add(fieldName);
                                    } else if (isCollection(canonicalText)) {
                                        collectionList.add(fieldName);
                                        PsiType parameter = ((PsiClassType) type).getParameters()[0];
                                        PsiClass genericClass = ((PsiClassType) parameter).resolve();
                                        extractResultMap(genericClass, resultMap, maxCount);
                                    } else {
                                        associationList.add(fieldName);
                                        extractResultMap(fieldClass, resultMap, maxCount);
                                    }
                                }
                            }
                    );

            ResultMapStructure resultMapStructure = ResultMapStructure.builder()
                    .className(className)
                    .packageName(packageName)
                    .fieldList(fieldList)
                    .associationList(associationList)
                    .collectionList(collectionList)
                    .build();

            resultMap.put(resultMapName, resultMapStructure);
        }
    }

    private boolean isString(String canonicalText) {
        return CommonClassNames.JAVA_LANG_STRING.equals(canonicalText);
    }

    private boolean isEnumClass(PsiClass fieldClass) {
        return fieldClass != null && fieldClass.isEnum();
    }

    @NotNull
    private String getResultMapName(String className) {
        return className.substring(0, 1).toLowerCase() + className.substring(1) + "Map";
    }

    private boolean isTimeClass(String canonicalText) {
        return JAVA_TIME_SET.contains(canonicalText);
    }

    private boolean isCollection(String canonicalText) {
        return COLLECTION_SET.stream()
                .anyMatch(canonicalText::contains);
    }

    private boolean isWrapperClass(String canonicalText) {
        return WRAPPER_CLASS_SET.contains(canonicalText);
    }


    @Override
    public void update(@NotNull AnActionEvent e) {
        final PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        PsiElement currentCaretElement = psiFile.findElementAt(editor.getCaretModel().getOffset());
        PsiClass psiClass = PsiTreeUtil.getParentOfType(currentCaretElement, PsiClass.class);
        e.getPresentation().setEnabledAndVisible(e.getProject() != null && psiClass != null);
    }
}

