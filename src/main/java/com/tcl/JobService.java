package com.tcl;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface JobService {

    public String performJobExecution(MultipartFile file) throws IOException;
    public String saveFileInLocalStorage(MultipartFile file) throws IOException;
}
