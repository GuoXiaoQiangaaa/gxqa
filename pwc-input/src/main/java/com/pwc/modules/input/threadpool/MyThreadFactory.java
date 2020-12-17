package com.pwc.modules.input.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyThreadFactory {

    public static ExecutorService executorService = Executors.newCachedThreadPool();

}
