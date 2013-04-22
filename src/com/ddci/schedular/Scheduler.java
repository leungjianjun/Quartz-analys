package com.ddci.schedular;

/**
 * Created with IntelliJ IDEA.
 * User: ljj-lab
 * Date: 13-4-20
 * Time: 下午2:05
 * To change this template use File | Settings | File Templates.
 */
public interface Scheduler {

    /**
     * Returns the name of the <code>Scheduler</code>.
     */
    String getSchedulerName() throws SchedulerException;
}
