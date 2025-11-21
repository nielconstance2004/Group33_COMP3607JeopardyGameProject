package com.project;

public class jsonFactory  implements LoaderFactory{

    public Loader createLoader(){
        return new jsonLoader();
    }
    
}