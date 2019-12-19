package com.example.student.smartlighttest1;

public class ExceptionCacher implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        StackTraceElement arr[]=throwable.getStackTrace();
        for (int i = 0; i < arr.length; i++) {
            file.writeToSDFile("logs.txt",arr[i].toString(),true);

        }
        Throwable cause = throwable.getCause();
        if(cause != null) {
            file.writeToSDFile("logs.txt", cause.toString(), true);
            arr = cause.getStackTrace();
            for (int i = 0; i < arr.length; i++) {
                file.writeToSDFile("logs.txt", "    " + arr[i].toString(), true);
            }
        }

    }
}
