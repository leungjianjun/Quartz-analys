package com.ddci.schedular.impl;

import com.ddci.schedular.Scheduler;
import com.ddci.schedular.SchedulerConfigException;
import com.ddci.schedular.SchedulerException;
import com.ddci.schedular.SchedulerFactory;
import com.ddci.schedular.util.PropertiesParser;

import java.io.*;
import java.security.AccessControlException;
import java.util.Properties;

/**
 * <p>
 * An implementation of <code>{@link com.ddci.schedular.SchedulerFactory}</code> that
 * does all of its work of creating a <code>QuartzScheduler</code> instance
 * based on the contents of a <code>Properties</code> file.
 * </p>
 *
 * <p>
 * By default a properties file named "quartz.properties" is loaded from the
 * 'current working directory'. If that fails, then the "quartz.properties"
 * file located (as a resource) in the org/quartz package is loaded. If you
 * wish to use a file other than these defaults, you must define the system
 * property 'com.ddci.schedular.properties' to point to the file you want.
 * </p>
 *
 * <p>
 * Alternatively, you can explicitly initialize the factory by calling one of
 * the <code>initialize(xx)</code> methods before calling <code>getScheduler()</code>.
 * </p>
 *
 * <p>
 * See the sample properties files that are distributed with Quartz for
 * information about the various settings available within the file.
 * Full configuration documentation can be found at
 * http://www.quartz-scheduler.org/docs/index.html
 * </p>
 *
 * <p>
 * Instances of the specified <code>{@link com.ddci.schedular.spi.JobStore}</code>,
 * <code>{@link com.ddci.schedular.spi.ThreadPool}</code>, and other SPI classes will be created
 * by name, and then any additional properties specified for them in the config
 * file will be set on the instance by calling an equivalent 'set' method. For
 * example if the properties file contains the property
 * 'com.ddci.schedular.jobStore.myProp = 10' then after the JobStore class has been
 * instantiated, the method 'setMyProp()' will be called on it. Type conversion
 * to primitive Java types (int, long, float, double, boolean, and String) are
 * performed before calling the property's setter method.
 * </p>
 *
 * <p>
 * One property can reference another property's value by specifying a value
 * following the convention of "$@other.property.name", for example, to reference
 * the scheduler's instance name as the value for some other property, you
 * would use "$@com.ddci.schedular.scheduler.instanceName".
 * </p>
 *
 * @author leungjianjun@gmail.com
 */
public class StdSchedulerFactory implements SchedulerFactory {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Constants.
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public static final String PROPERTIES_FILE = "com.ddci.schedular.properties";

    public static final String PROP_SCHED_INSTANCE_NAME = "org.quartz.scheduler.instanceName";

    public static final String PROP_SCHED_INSTANCE_ID = "org.quartz.scheduler.instanceId";

    public static final String PROP_SCHED_INSTANCE_ID_GENERATOR_PREFIX = "org.quartz.scheduler.instanceIdGenerator";

    public static final String PROP_SCHED_INSTANCE_ID_GENERATOR_CLASS =
            PROP_SCHED_INSTANCE_ID_GENERATOR_PREFIX + ".class";

    public static final String PROP_SCHED_THREAD_NAME = "org.quartz.scheduler.threadName";

    public static final String PROP_SCHED_SKIP_UPDATE_CHECK = "org.quartz.scheduler.skipUpdateCheck";

    public static final String PROP_SCHED_BATCH_TIME_WINDOW = "org.quartz.scheduler.batchTriggerAcquisitionFireAheadTimeWindow";

    public static final String PROP_SCHED_MAX_BATCH_SIZE = "org.quartz.scheduler.batchTriggerAcquisitionMaxCount";

    public static final String PROP_SCHED_JMX_EXPORT = "org.quartz.scheduler.jmx.export";

    public static final String PROP_SCHED_JMX_OBJECT_NAME = "org.quartz.scheduler.jmx.objectName";

    public static final String PROP_SCHED_JMX_PROXY = "org.quartz.scheduler.jmx.proxy";

    public static final String PROP_SCHED_JMX_PROXY_CLASS = "org.quartz.scheduler.jmx.proxy.class";

    public static final String PROP_SCHED_RMI_EXPORT = "org.quartz.scheduler.rmi.export";

    public static final String PROP_SCHED_RMI_PROXY = "org.quartz.scheduler.rmi.proxy";

    public static final String PROP_SCHED_RMI_HOST = "org.quartz.scheduler.rmi.registryHost";

    public static final String PROP_SCHED_RMI_PORT = "org.quartz.scheduler.rmi.registryPort";

    public static final String PROP_SCHED_RMI_SERVER_PORT = "org.quartz.scheduler.rmi.serverPort";

    public static final String PROP_SCHED_RMI_CREATE_REGISTRY = "org.quartz.scheduler.rmi.createRegistry";

    public static final String PROP_SCHED_RMI_BIND_NAME = "org.quartz.scheduler.rmi.bindName";

    public static final String PROP_SCHED_WRAP_JOB_IN_USER_TX = "org.quartz.scheduler.wrapJobExecutionInUserTransaction";

    public static final String PROP_SCHED_USER_TX_URL = "org.quartz.scheduler.userTransactionURL";

    public static final String PROP_SCHED_IDLE_WAIT_TIME = "org.quartz.scheduler.idleWaitTime";

    public static final String PROP_SCHED_DB_FAILURE_RETRY_INTERVAL = "org.quartz.scheduler.dbFailureRetryInterval";

    public static final String PROP_SCHED_MAKE_SCHEDULER_THREAD_DAEMON = "org.quartz.scheduler.makeSchedulerThreadDaemon";

    public static final String PROP_SCHED_SCHEDULER_THREADS_INHERIT_CONTEXT_CLASS_LOADER_OF_INITIALIZING_THREAD = "org.quartz.scheduler.threadsInheritContextClassLoaderOfInitializer";

    public static final String PROP_SCHED_CLASS_LOAD_HELPER_CLASS = "org.quartz.scheduler.classLoadHelper.class";

    public static final String PROP_SCHED_JOB_FACTORY_CLASS = "org.quartz.scheduler.jobFactory.class";

    public static final String PROP_SCHED_JOB_FACTORY_PREFIX = "org.quartz.scheduler.jobFactory";

    public static final String PROP_SCHED_INTERRUPT_JOBS_ON_SHUTDOWN = "org.quartz.scheduler.interruptJobsOnShutdown";

    public static final String PROP_SCHED_INTERRUPT_JOBS_ON_SHUTDOWN_WITH_WAIT = "org.quartz.scheduler.interruptJobsOnShutdownWithWait";

    public static final String PROP_SCHED_CONTEXT_PREFIX = "org.quartz.context.key";

    public static final String PROP_THREAD_POOL_PREFIX = "org.quartz.threadPool";

    public static final String PROP_THREAD_POOL_CLASS = "org.quartz.threadPool.class";

    public static final String PROP_JOB_STORE_PREFIX = "org.quartz.jobStore";

    public static final String PROP_JOB_STORE_LOCK_HANDLER_PREFIX = PROP_JOB_STORE_PREFIX + ".lockHandler";

    public static final String PROP_JOB_STORE_LOCK_HANDLER_CLASS = PROP_JOB_STORE_LOCK_HANDLER_PREFIX + ".class";

    public static final String PROP_TABLE_PREFIX = "tablePrefix";

    public static final String PROP_SCHED_NAME = "schedName";

    public static final String PROP_JOB_STORE_CLASS = "org.quartz.jobStore.class";

    public static final String PROP_JOB_STORE_USE_PROP = "org.quartz.jobStore.useProperties";

    public static final String PROP_DATASOURCE_PREFIX = "org.quartz.dataSource";

    public static final String PROP_CONNECTION_PROVIDER_CLASS = "connectionProvider.class";

    private SchedulerException initException = null;

    private String propSrc = null;

    private PropertiesParser cfg;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Constructor.
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * Create a StdSchedulerFactory that has been initialized via
     * <code>{@link #initialize(java.util.Properties)}</code>.
     *
     * @see #initialize(java.util.Properties)
     */
    public StdSchedulerFactory(Properties props) throws SchedulerException {
        initialize(props);
    }

    /**
     * Create a StdSchedulerFactory that has been initialized via
     * <code>{@link #initialize(String)}</code>.
     *
     * @see #initialize(String)
     */
    public StdSchedulerFactory(String fileName) throws SchedulerException {
        initialize(fileName);
    }

    /**
     * <p>
     * Initialize the <code>{@link com.ddci.schedular.SchedulerFactory}</code> with
     * the contents of a <code>Properties</code> file and overriding System
     * properties.
     * </p>
     *
     * <p>
     * By default a properties file named "quartz.properties" is loaded from
     * the 'current working directory'. If that fails, then the
     * "quartz.properties" file located (as a resource) in the org/quartz
     * package is loaded. If you wish to use a file other than these defaults,
     * you must define the system property 'com.ddci.schedular.properties' to point to
     * the file you want.
     * </p>
     *
     * <p>
     * System properties (environment variables, and -D definitions on the
     * command-line when running the JVM) override any properties in the
     * loaded file.  For this reason, you may want to use a different initialize()
     * method if your application security policy prohibits access to
     * <code>{@link java.lang.System#getProperties()}</code>.
     * </p>
     */
    public void initialize() throws SchedulerException {
        // short-circuit if already initialized
        if (cfg != null) {
            return;
        }
        if (initException != null) {
            throw initException;
        }

        String requestedFile = System.getProperty(PROPERTIES_FILE);
        String propFileName = requestedFile != null ? requestedFile
                : "quartz.properties";
        File propFile = new File(propFileName);

        Properties props = new Properties();

        InputStream in = null;

        try {
            if (propFile.exists()) {
                try {
                    if (requestedFile != null) {
                        propSrc = "specified file: '" + requestedFile + "'";
                    } else {
                        propSrc = "default file in current working dir: 'quartz.properties'";
                    }

                    in = new BufferedInputStream(new FileInputStream(propFileName));
                    props.load(in);

                } catch (IOException ioe) {
                    initException = new SchedulerException("Properties file: '"
                            + propFileName + "' could not be read.", ioe);
                    throw initException;
                }
            } else if (requestedFile != null) {
                in =
                        Thread.currentThread().getContextClassLoader().getResourceAsStream(requestedFile);

                if(in == null) {
                    initException = new SchedulerException("Properties file: '"
                            + requestedFile + "' could not be found.");
                    throw initException;
                }

                propSrc = "specified file: '" + requestedFile + "' in the class resource path.";

                in = new BufferedInputStream(in);
                try {
                    props.load(in);
                } catch (IOException ioe) {
                    initException = new SchedulerException("Properties file: '"
                            + requestedFile + "' could not be read.", ioe);
                    throw initException;
                }

            } else {
                propSrc = "default resource file in Quartz package: 'quartz.properties'";

                ClassLoader cl = getClass().getClassLoader();
                if(cl == null)
                    cl = findClassloader();
                if(cl == null)
                    throw new SchedulerConfigException("Unable to find a class loader on the current thread or class.");

                in = cl.getResourceAsStream(
                        "quartz.properties");

                if (in == null) {
                    in = cl.getResourceAsStream(
                            "/quartz.properties");
                }
                if (in == null) {
                    in = cl.getResourceAsStream(
                            "org/quartz/quartz.properties");
                }
                if (in == null) {
                    initException = new SchedulerException(
                            "Default quartz.properties not found in class path");
                    throw initException;
                }
                try {
                    props.load(in);
                } catch (IOException ioe) {
                    initException = new SchedulerException(
                            "Resource properties file: 'org/quartz/quartz.properties' "
                                    + "could not be read from the classpath.", ioe);
                    throw initException;
                }
            }
        } finally {
            if(in != null) {
                try { in.close(); } catch(IOException ignore) { /* ignore */ }
            }
        }

        initialize(overrideWithSysProps(props));
    }

    /**
     * Add all System properties to the given <code>props</code>.  Will override
     * any properties that already exist in the given <code>props</code>.
     */
    private Properties overrideWithSysProps(Properties props) {
        Properties sysProps = null;
        try {
            sysProps = System.getProperties();
        } catch (AccessControlException e) {
            System.out.println("Skipping overriding quartz properties with System properties " +
                            "during initialization because of an AccessControlException.  " +
                            "This is likely due to not having read/write access for " +
                            "java.util.PropertyPermission as required by java.lang.System.getProperties().  " +
                            "To resolve this warning, either add this permission to your policy file or " +
                            "use a non-default version of initialize().");
        }

        if (sysProps != null) {
            props.putAll(sysProps);
        }

        return props;
    }

    /**
     * <p>
     * Initialize the <code>{@link com.ddci.schedular.SchedulerFactory}</code> with
     * the contents of the <code>Properties</code> file with the given
     * name.
     * </p>
     */
    public void initialize(String filename) throws SchedulerException {
        // short-circuit if already initialized
        if (cfg != null) {
            return;
        }

        if (initException != null) {
            throw initException;
        }

        InputStream is = null;
        Properties props = new Properties();

        is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);

        try {
            if(is != null) {
                is = new BufferedInputStream(is);
                propSrc = "the specified file : '" + filename + "' from the class resource path.";
            } else {
                is = new BufferedInputStream(new FileInputStream(filename));
                propSrc = "the specified file : '" + filename + "'";
            }
            props.load(is);
        } catch (IOException ioe) {
            initException = new SchedulerException("Properties file: '"
                    + filename + "' could not be read.", ioe);
            throw initException;
        }
        finally {
            if(is != null)
                try { is.close(); } catch(IOException ignore) {}
        }

        initialize(props);
    }

    /**
     * <p>
     * Initialize the <code>{@link com.ddci.schedular.SchedulerFactory}</code> with
     * the contents of the <code>Properties</code> file opened with the
     * given <code>InputStream</code>.
     * </p>
     */
    public void initialize(InputStream propertiesStream)
            throws SchedulerException {
        // short-circuit if already initialized
        if (cfg != null) {
            return;
        }

        if (initException != null) {
            throw initException;
        }

        Properties props = new Properties();

        if (propertiesStream != null) {
            try {
                props.load(propertiesStream);
                propSrc = "an externally opened InputStream.";
            } catch (IOException e) {
                initException = new SchedulerException(
                        "Error loading property data from InputStream", e);
                throw initException;
            }
        } else {
            initException = new SchedulerException(
                    "Error loading property data from InputStream - InputStream is null.");
            throw initException;
        }

        initialize(props);
    }

    /**
     * <p>
     * Initialize the <code>{@link com.ddci.schedular.SchedulerFactory}</code> with
     * the contents of the given <code>Properties</code> object.
     * </p>
     */
    public void initialize(Properties props) throws SchedulerException {
        if (propSrc == null) {
            propSrc = "an externally provided properties instance.";
        }

        this.cfg = new PropertiesParser(props);
    }

    private ClassLoader findClassloader() {
        // work-around set context loader for windows-service started jvms (QUARTZ-748)
        if(Thread.currentThread().getContextClassLoader() == null && getClass().getClassLoader() != null) {
            Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        }
        return Thread.currentThread().getContextClassLoader();
    }

    private String getSchedulerName() {
        return cfg.getStringProperty(PROP_SCHED_INSTANCE_NAME,
                "QuartzScheduler");
    }

    /**
     * <p>
     * Returns a handle to the Scheduler produced by this factory.
     * </p>
     *
     * <p>
     * If one of the <code>initialize</code> methods has not be previously
     * called, then the default (no-arg) <code>initialize()</code> method
     * will be called by this method.
     * </p>
     */
    @Override
    public Scheduler getScheduler() throws SchedulerException {
        if (cfg == null) {//if not initialize
            initialize();
        }
        SchedulerRepository schedRep = SchedulerRepository.getInstance();

        Scheduler sched = schedRep.lookup(getSchedulerName());
        if (sched != null) {
            if (sched.isShutdown()) {
                schedRep.remove(getSchedulerName());
            } else {
                return sched;
            }
        }
        sched = instantiate();
        return sched;
    }
}
