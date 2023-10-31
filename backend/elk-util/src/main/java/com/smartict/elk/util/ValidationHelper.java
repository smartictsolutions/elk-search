/* SmartICT Bilisim A.S. (C) 2023 */
package com.smartict.elk.util;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.springframework.util.StringUtils;

public class ValidationHelper {
    public ValidationHelper() {}

    public static boolean notNull(Object obj) {
        return obj != null;
    }

    public static boolean noNullElements(Object[] objects) {
        Object[] var1 = objects;
        int var2 = objects.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            Object obj = var1[var3];
            if (obj == null) {
                return false;
            }
        }

        return true;
    }

    public static <T> boolean noNullElements(List<T> elements) {
        Iterator var1 = elements.iterator();

        Object obj;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            obj = var1.next();
        } while (obj != null);

        return false;
    }

    public static boolean notEmpty(String string) {
        return !StringUtils.isEmpty(string);
    }

    public static boolean notEmpty(Integer integer) {
        return integer != null && integer != 0;
    }

    public static boolean notEmpty(BigDecimal bigDecimal) {
        return bigDecimal != null && bigDecimal != BigDecimal.ZERO;
    }

    public static boolean notEmpty(Boolean value) {
        return value != null && value;
    }

    public static boolean notEmpty(Long value) {
        return value != null && value != 0L;
    }
}
