package com.ddci.schedular;

/**
 * Provides a mechanism for obtaining client-usable handles to <code>Scheduler</code>
 * instances.
 *
 * @see Scheduler
 * @see com.ddci.schedular.impl.StdSchedulerFactory
 *
 * @author leungjianjun@gmail.com
 */
public interface SchedulerFactory {
    /**
     * <p>
     * Returns a client-usable handle to a <code>Scheduler</code>.
     * </p>
     *
     * @throws SchedulerException
     *           if there is a problem with the underlying <code>Scheduler</code>.
     */
    public Scheduler getScheduler() throws SchedulerException;
}
