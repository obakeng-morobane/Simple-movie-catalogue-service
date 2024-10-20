package com.obakeng.MovieApi.exceptions;

import jdk.jshell.spi.ExecutionControl;

public class FileExistsException extends RuntimeException {
    public  FileExistsException(String message){
        super(message);
    }
}
