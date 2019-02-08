package com.sample.battery.hook;

public class Utils {


    public static String getStackTrace() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (null != stackTrace && stackTrace.length > 0) {
            StringBuilder sb = new StringBuilder();

            StackTraceElement stackTraceElement;
            String fileName;
            String className;
            String methodName;
            int lineNumber;
            for (int i = 1; i < stackTrace.length; i++) {
                stackTraceElement = stackTrace[i];
                className = stackTraceElement.getClassName();
                if (Utils.class.getName().equals(className)) {
                    continue;
                }
                methodName = stackTraceElement.getMethodName();
                fileName = stackTraceElement.getFileName();
                lineNumber = stackTraceElement.getLineNumber();
                sb.append(className).append(".").append(methodName).append("(").append(fileName).append(":").append(lineNumber).append(")\n");
            }
            return sb.toString();
        }
        return "";
    }
}