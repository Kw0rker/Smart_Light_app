package com.example.student.smartlighttest1;

public class ExceptionCacher implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        StackTraceElement arr[]=throwable.getStackTrace();
        for (int i = 0; i < arr.length; i++) {
            file.writeLog(arr[i].toString());

        }
        Throwable cause = throwable.getCause();
        if(cause != null) {
            file.writeLog(cause.toString());
            arr = cause.getStackTrace();
            for (int i = 0; i < arr.length; i++) {
                file.writeLog("--"+arr[i].toString());
            }
        }

    }
}
