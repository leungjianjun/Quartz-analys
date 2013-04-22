package com.ddci.schedular;

/**
 * Base class for exceptions thrown by the Quartz <code>{@link Scheduler}</code>.
 *
 * <p>
 * <code>SchedulerException</code>s may contain a reference to another
 * <code>Exception</code>, which was the underlying cause of the <code>SchedulerException</code>.
 * </p>
 *
 * @author lianjianjun@gmal.com
 */
public class SchedulerException extends Exception {

    private static final long serialVersionUID = 174841398690789156L;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Constructors.
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public SchedulerException() {
        super();
    }

    public SchedulerException(String msg) {
        super(msg);
    }

    public SchedulerException(Throwable cause) {
        super(cause);
    }

    public SchedulerException(String msg, Throwable cause) {
        super(msg, cause);
    }



    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Interface.
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * <p>
     * Return the exception that is the underlying cause of this exception.
     * </p>
     *
     * <p>
     * This may be used to find more detail about the cause of the error.
     * </p>
     *
     * @return the underlying exception, or <code>null</code> if there is not
     *         one.
     */
    public Throwable getUnderlyingException() {
        return super.getCause();
    }

    @Override
    public String toString() {
        Throwable cause = getUnderlyingException();
        if (cause == null || cause == this) {
            return super.toString();
        } else {
            return super.toString() + " [See nested exception: " + cause + "]";
        }
    }


}