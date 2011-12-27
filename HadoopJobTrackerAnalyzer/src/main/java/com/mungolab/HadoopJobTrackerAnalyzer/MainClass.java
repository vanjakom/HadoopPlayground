package com.mungolab.HadoopJobTrackerAnalyzer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @author Vanja Komadinovic
 * @author vanjakom@gmail.com
 */
public class MainClass {
    protected static Logger logger = LoggerFactory.getLogger(MainClass.class);

    public static void main(String[] args) {
        JobClient jobClient = null;

        String jobTrackerHost = null;
        int jobTrackerPort = -1;

        if (args.length == 2) {
            try {
                jobTrackerHost = args[0];
                jobTrackerPort = Integer.parseInt(args[1]);
            } catch (Exception e) {
                logger.error("Unable to extract command line parameters");
                System.exit(-1);
            }
        } else {
            logger.error("Usage: JobTrackerAnalyzer host port");
            System.exit(-1);
        }

        try {
            jobClient = new JobClient(new InetSocketAddress(jobTrackerHost, jobTrackerPort), new Configuration());
        } catch (Exception e) {
            logger.error("Unable to initialize JobClient", e);
            System.exit(-1);
        }

        while (true) {
            JobStatus[] activeJobs = null;
            try {
                 activeJobs = jobClient.jobsToComplete();
            } catch (Exception e) {
                logger.error("Unable to get active jobs", e);
                System.exit(-1);
            }

            logger.info("Active jobs: " + activeJobs.length);
            for (JobStatus jobStatus: activeJobs) {
                logger.info("\t\tJob: " + jobStatus.getUsername() + " - " + jobStatus.getJobID().getJtIdentifier() +
                        " - " + jobStatus.getJobID().getId());
            }

            try {
                Thread.sleep(30000);
            } catch (Exception e) {
                logger.error("Unable to sleep until next cycle", e);
            }
        }
    }
}
