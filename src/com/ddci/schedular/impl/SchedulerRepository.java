package com.ddci.schedular.impl;

import com.ddci.schedular.Scheduler;
import com.ddci.schedular.SchedulerException;

import java.util.Collection;
import java.util.HashMap;

/**
 * <p>
 * Holds references to Scheduler instances - ensuring uniqueness, and
 * preventing garbage collection, and allowing 'global' lookups - all within a
 * ClassLoader space.
 * </p>
 *
 * @author James House
 */
public class SchedulerRepository {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Data members.
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private HashMap<String, Scheduler> schedulers;

    private static SchedulerRepository instance;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Constructors.
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private SchedulerRepository() {
        schedulers = new HashMap<String, Scheduler>();
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Interface.
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public static synchronized SchedulerRepository getInstance() {
        if (instance == null) {
            instance = new SchedulerRepository();
        }

        return instance;
    }

    public synchronized void bind(Scheduler sched) throws SchedulerException {

        if ((Scheduler) schedulers.get(sched.getSchedulerName()) != null) {
            throw new SchedulerException("Scheduler with name '"
                    + sched.getSchedulerName() + "' already exists.");
        }

        schedulers.put(sched.getSchedulerName(), sched);
    }

    public synchronized boolean remove(String schedName) {
        return (schedulers.remove(schedName) != null);
    }

    public synchronized Scheduler lookup(String schedName) {
        return schedulers.get(schedName);
    }

    public synchronized Collection<Scheduler> lookupAll() {
        return java.util.Collections
                .unmodifiableCollection(schedulers.values());
    }

}
