package com.webshoprest.api.v1.util;

public class FieldNamePrettyFormatter {

    public static String prettyFormatFieldName(String fieldName) {


        if (fieldName.contains(".")) {
            String[] tempArray = fieldName.split("\\.");
            fieldName = tempArray[tempArray.length - 1];
        }

        String[] stringArray = fieldName.split("(?=[A-Z])");


        if (stringArray.length > 1) {

            StringBuilder builder = new StringBuilder();

            builder.append(String.valueOf(stringArray[0].charAt(0)).toUpperCase())
                    .append(stringArray[0].substring(1))
                    .append(" ");

            for (int i = 1; i < stringArray.length; i++) {
                builder.append(stringArray[i].toLowerCase())
                        .append(" ");
            }

            builder.trimToSize();
            return builder.toString();

        } else {
            return stringArray[0].replace(String.valueOf(stringArray[0].charAt(0)), String.valueOf(stringArray[0].charAt(0)).toUpperCase());
        }


    }

}
