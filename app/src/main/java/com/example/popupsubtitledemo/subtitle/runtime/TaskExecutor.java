package com.example.popupsubtitledemo.subtitle.runtime;

public abstract class TaskExecutor {

    public abstract void executeOnDeskIO(Runnable task);

    public void executeOnMainThread(Runnable task) {
        if (isMainThread()) {
            task.run();
        } else {
            postToMainThread(task);
        }
    }

    public abstract void postToMainThread(Runnable task);

    public abstract boolean isMainThread();
}
