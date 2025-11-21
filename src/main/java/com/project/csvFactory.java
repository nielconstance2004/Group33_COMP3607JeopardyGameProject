package com.project;

public class csvFactory implements LoaderFactory {

    public Loader createLoader(){
        return new csvloader();
    }
    
}
