package com.codingdreamtree.DtoToMyBatisResultMap.util;

import com.intellij.psi.CommonClassNames;
import com.intellij.psi.PsiClass;

import java.util.HashSet;
import java.util.Set;

public class TypeCheckUtil {

    private static final Set<String> JAVA_TIME_SET = new HashSet<>() {
        {
            add(CommonClassNamesTwo.JAVA_TIME_LOCAL_DATE);
            add(CommonClassNamesTwo.JAVA_TIME_LOCAL_DATETIME);
            add(CommonClassNamesTwo.JAVA_TIME_ZONED_DATE);
            add(CommonClassNamesTwo.JAVA_TIME_ZONED_DATETIME);
        }
    };

    private static final Set<String> CHAR_CLASS_SET = new HashSet<>() {
        {
            add(CommonClassNames.JAVA_LANG_CHARACTER);
            add(CommonClassNames.JAVA_LANG_STRING);
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

    private static final Set<String> NUMBER_CLASS_SET = new HashSet<>() {
        {

            add(CommonClassNames.JAVA_LANG_NUMBER);
            add(CommonClassNames.JAVA_LANG_BYTE);
            add(CommonClassNames.JAVA_LANG_SHORT);
            add(CommonClassNames.JAVA_LANG_INTEGER);
            add(CommonClassNames.JAVA_LANG_LONG);
            add(CommonClassNames.JAVA_LANG_FLOAT);
            add(CommonClassNames.JAVA_LANG_DOUBLE);
            add(CommonClassNamesTwo.BYTE);
            add(CommonClassNamesTwo.SHORT);
            add(CommonClassNamesTwo.INT);
            add(CommonClassNamesTwo.LONG);
            add(CommonClassNamesTwo.FLOAT);
            add(CommonClassNamesTwo.DOUBLE);
            add(CommonClassNamesTwo.JAVA_MATH_BIG_DECIMAL);
            add(CommonClassNamesTwo.JAVA_MATH_BIG_INTEGER);
        }
    };

    private static final Set<String> BOOLEAN_CLASS_SET = new HashSet<>() {
        {
            add(CommonClassNamesTwo.JAVA_LANG_BOOLEAN);
            add(CommonClassNamesTwo.BOOLEAN);
        }
    };


    public static boolean isTimeClass(String canonicalText) {
        return JAVA_TIME_SET.contains(canonicalText);
    }

    public static boolean isCollection(String canonicalText) {
        return COLLECTION_SET.stream()
                .anyMatch(canonicalText::contains);
    }

    public static boolean isCharClass(String canonicalText) {
        return CHAR_CLASS_SET.contains(canonicalText);
    }

    public static boolean isEnumClass(PsiClass fieldClass) {
        return fieldClass != null && fieldClass.isEnum();
    }

    public static boolean isNumberClass(String canonicalText) {
        return NUMBER_CLASS_SET.contains(canonicalText);
    }

    public static boolean isBooleanClass(String canonicalText) {
        return BOOLEAN_CLASS_SET.contains(canonicalText);
    }
}
