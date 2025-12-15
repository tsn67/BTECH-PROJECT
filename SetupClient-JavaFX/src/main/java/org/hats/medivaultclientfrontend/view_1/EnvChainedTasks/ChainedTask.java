package org.hats.medivaultclientfrontend.view_1.EnvChainedTasks;

public abstract class ChainedTask {
    protected ChainedTask nextTask;
    public abstract void run();
    public void setChainedTask(ChainedTask nextTask) {
        this.nextTask = nextTask;
    }
}
