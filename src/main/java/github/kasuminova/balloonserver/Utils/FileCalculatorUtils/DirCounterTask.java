package github.kasuminova.balloonserver.Utils.FileCalculatorUtils;

import cn.hutool.core.thread.ThreadUtil;
import github.kasuminova.balloonserver.Utils.FileObject.AbstractSimpleFileObject;
import github.kasuminova.balloonserver.Utils.FileObject.SimpleDirectoryObject;
import github.kasuminova.balloonserver.Utils.FileObject.SimpleFileObject;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

record DirCounterTask(File directory, ExecutorService fileThreadPool, String hashAlgorithm, AtomicLong completedBytes,
                      AtomicInteger completedFiles)
        implements Callable<SimpleDirectoryObject> {

    @Override
    public SimpleDirectoryObject call() {
        File[] fileList = directory.listFiles();
        if (fileList == null) {
            return new SimpleDirectoryObject(directory.getName(), new ArrayList<>());
        }
        ArrayList<FutureTask<SimpleFileObject>> fileCounterTaskList = new ArrayList<>();
        ArrayList<FutureTask<SimpleDirectoryObject>> direCounterTaskList = new ArrayList<>();
        ArrayList<AbstractSimpleFileObject> abstractSimpleFileObjectList = new ArrayList<>();

        for (File file : fileList) {
            if (file.isFile()) {
                FutureTask<SimpleFileObject> fileCounterTask = new FutureTask<>(
                        new FileCounterTask(file, hashAlgorithm, completedBytes, completedFiles));
                fileCounterTaskList.add(fileCounterTask);
                fileThreadPool.execute(fileCounterTask);
            } else {
                FutureTask<SimpleDirectoryObject> dirCounterTask = new FutureTask<>(
                        new DirCounterTask(file, fileThreadPool, hashAlgorithm, completedBytes, completedFiles));
                direCounterTaskList.add(dirCounterTask);
                ThreadUtil.execute(dirCounterTask);
            }
        }

        for (FutureTask<SimpleDirectoryObject> simpleDirectoryObjectFutureTask : direCounterTaskList) {
            try {
                abstractSimpleFileObjectList.add(simpleDirectoryObjectFutureTask.get());
            } catch (Exception ignored) {
            }
        }

        for (FutureTask<SimpleFileObject> simpleFileObjectFutureTask : fileCounterTaskList) {
            try {
                abstractSimpleFileObjectList.add(simpleFileObjectFutureTask.get());
            } catch (Exception ignored) {
            }
        }

        return new SimpleDirectoryObject(directory.getName(), abstractSimpleFileObjectList);
    }
}
