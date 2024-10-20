package com.obakeng.MovieApi.service;

import com.obakeng.MovieApi.dto.MovieDto;
import com.obakeng.MovieApi.exceptions.MovieNotFoundException;
import com.obakeng.MovieApi.models.Movie;
import com.obakeng.MovieApi.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileService implements iFileService{
    @Autowired
    private MovieRepository movieRepository;
    @Value("${project.poster}")
    private String path;
    @Override
    public String uploadFile(String path, MultipartFile file) throws IOException {
        //get name of file
        if (Files.exists(Paths.get(path + File.separator + file.getOriginalFilename()))){
            throw new RuntimeException("File already exists, please enter another file name ");
        }
        String fileName = file.getOriginalFilename();
        //get the file path
        String filePath = path + File.separator + fileName;
        // create file object
        File f = new File(path);
        if (!f.exists()){
            f.mkdir();
        }
        // copy the file or upload file to the path
        Files.copy(file.getInputStream()
                ,Paths.get(filePath));
        return fileName;
    }
    @Override
    public InputStream getResourceFile(String fileName) throws FileNotFoundException {
        String filePath = path + File.separator + fileName;
        return new FileInputStream(filePath);
    }
    @Override
    public String updateFile(String fileName, MultipartFile file) throws IOException {
        Movie theFile = movieRepository.findByPoster(fileName);
        String getFileName = file.getOriginalFilename();
        theFile.setPoster(getFileName);
        if (file != null){
            Files.deleteIfExists(Paths.get(path + File.separator + getFileName));
            // copy the file or upload file to the path
            uploadFile(path, file);
        }
        movieRepository.save(theFile);
        return "This file has been updated : " + getFileName;
    }
    @Override
    public String deleteImage(String fileName) throws IOException {
        Files.deleteIfExists(Paths.get(path + File.separator + fileName));
        return "This file has been deleted: " + fileName ;
    }
}
