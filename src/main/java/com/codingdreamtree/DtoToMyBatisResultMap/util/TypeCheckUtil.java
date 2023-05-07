package com.codingdreamtree.DtoToMyBatisResultMap.util;

import com.intellij.psi.CommonClassNames;
import com.intellij.psi.PsiClass;

import java.util.HashSet;
import java.util.Set;

public class TypeCheckUtil {
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


    public static boolean isTimeClass(String canonicalText) {
        return JAVA_TIME_SET.contains(canonicalText);
    }

    public static boolean isCollection(String canonicalText) {
        return COLLECTION_SET.stream()
                .anyMatch(canonicalText::contains);
    }

    public static boolean isWrapperClass(String canonicalText) {
        return WRAPPER_CLASS_SET.contains(canonicalText);
    }


    public static boolean isString(String canonicalText) {
        return CommonClassNames.JAVA_LANG_STRING.equals(canonicalText);
    }

    public static boolean isEnumClass(PsiClass fieldClass) {
        return fieldClass != null && fieldClass.isEnum();
    }

}
