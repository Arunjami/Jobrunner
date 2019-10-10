package com.tcl;

import org.apache.commons.io.FileUtils;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@Service("JobService")
@Component
public class JobServiceImpl implements JobService{
    private static Logger logger = LoggerFactory.getLogger(JobController.class);

    @Override
    public String performJobExecution(MultipartFile file) throws IOException {
        File targetFile = new File(System.getProperty("java.io.tmpdir")+"/sample.xlsx");
        file.transferTo(targetFile);
        File dest = new File("/var/log/"+getRandomName()+".xlsx");
        try {
            FileUtils.copyFile(targetFile, dest);
        } catch (IOException e) {
            logger.error("Cannot process");
            throw new CouldNotProcessException("Could not copy the files", e.getMessage(), e);
        }

        try{
            String jobFile ="/var/itsm/jobs/assetupload" + "/Dynamic parsing of Excel File.kjb";
            Repository repository = null;
            try{
                KettleEnvironment.init();
                JobMeta jobmeta = new JobMeta(jobFile, repository);
                Job job = new Job(repository, jobmeta);
                job.setLogLevel(LogLevel.DEBUG);

                job.shareVariablesWith(jobmeta);
                jobmeta.setParameterValue("EXCEL_PATH", targetFile.getAbsolutePath());
                jobmeta.setParameterValue("TASK_ID", "58851");
                jobmeta.setParameterValue("USER_ID", "arunmani");

                jobmeta.setInternalKettleVariables(job);
                jobmeta.activateParameters();

                job.start();
                job.waitUntilFinished();

                if (job.getErrors() > 0) {
                    logger.error("--------------Error Executing Job------------------");
                    return "Error while processing data";
                }

                job.setFinished(true);
                jobmeta.eraseParameters();
                job.eraseParameters();
                if (targetFile.delete()) {
                    logger.info("Deleted the temp file [{}] created for asset upload");
                }

                return "Job Success";
            }
            catch (KettleException e) {
                /* TODO Auto-generated catch block */

                logger.error(e.getMessage(), e);
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();

            logger.error(e.getMessage(), e);

            throw new CouldNotProcessException("Could not run bul update job", e.getMessage(), e);
        }


        return "Job Success";
    }

    final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";
    final java.util.Random rand = new java.util.Random();
    final Set<String> identifiers = new HashSet<String>();

    private String getRandomName()
    {
        StringBuilder builder = new StringBuilder();
        while(builder.toString().length() == 0) {
            int length = rand.nextInt(5)+5;
            for(int i = 0; i < length; i++) {
                builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            }
            if(identifiers.contains(builder.toString())) {
                builder = new StringBuilder();
            }
        }
        DateTimeFormatter formatType = DateTimeFormatter.ofPattern("_yyyy-MM-dd.HH.mm.ss");
        LocalDateTime currentTime = LocalDateTime.now();
        builder.append(formatType.format(currentTime));
        return builder.toString();
    }

}
