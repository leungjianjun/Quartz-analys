package com.ddci.schedular;

/**
 * An exception that is thrown to indicate that there is a misconfiguration of
 * the <code>SchedulerFactory</code>- or one of the components it
 * configures.
 *
 * @author James House
 */
public class SchedulerConfigException extends SchedulerException {

    private static final long serialVersionUID = -5921239824646083098L;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Constructors.
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * <p>
     * Create a <code>JobPersistenceException</code> with the given message.
     * </p>
     */
    public SchedulerConfigException(String msg) {
        super(msg);
    }

    /**
     * <p>
     * Create a <code>JobPersistenceException</code> with the given message
     * and cause.
     * </p>
     */
    public SchedulerConfigException(String msg, Throwable cause) {
        super(msg, cause);
    }

}

