/* SmartICT Bilisim A.S. (C) 2021 */
package com.smartict.elk.util;

public class StringHelper {

    @SuppressWarnings("squid:S5852")
    public static String camelCaseToSnakeCase(String camelStr) {
        String ret = camelStr.replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2").replaceAll("([a-z])([A-Z])", "$1_$2");
        return ret.toLowerCase();
    }

    @SuppressWarnings("squid:S5852")
    public static String toCamelCase(String str) {
        String ret = str.replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2").replaceAll("([a-z])([A-Z])", "$1_$2");
        return ret.toLowerCase();
    }
}
