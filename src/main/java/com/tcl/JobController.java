package com.tcl;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

//petaho dependency
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.Repository;

// Basic Java io Headers
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;


@Controller    // This means that this class is a Controller
@RequestMapping(path="/demo") // This means URL's start with /demo (after Application path)
public class JobController{

    private static Logger logger = LoggerFactory.getLogger(JobController.class);
    @GetMapping(path="/add") // Map ONLY GET Requests
    public @ResponseBody String addNewUser () {

        return "Saved";
    }

    @GetMapping(path="/all") // Map ONLY GET Requests
    public @ResponseBody Iterable<String> alluser () {

        return new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return null;
            }
        };
    }
    @RequestMapping(value = "/gettempuploadurl", method = RequestMethod.GET)
    @ResponseBody
    public String getTempUrlInfo(@RequestParam(value = "fileName", required = true) String fileName){
        try {
            return "tempUrl";
        } catch (Exception e) {
            logger.error("Error while generating temporary URL",e);
            throw new CouldNotProcessException("Error while generating temporary upload url");
        }
    }


    @Autowired
    private JobService jobService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String handleFileUpload(@RequestParam("file") MultipartFile file) throws Exception {

      return  jobService.performJobExecution(file);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String saveFileInLocal(@RequestParam("file") MultipartFile file) throws Exception {

        return  jobService.saveFileInLocalStorage(file);
    }

}
