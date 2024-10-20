package com.obakeng.MovieApi.service;


import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface iFileService {

    String uploadFile(String path, MultipartFile file) throws IOException;
    InputStream getResourceFile( String fileName) throws FileNotFoundException;

    String updateFile(String fileName, MultipartFile file) throws IOException;

    String deleteImage(String fileName) throws IOException;
}
