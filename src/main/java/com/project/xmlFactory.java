package com.project;

public class xmlFactory  implements LoaderFactory{

    public Loader createLoader(){
        return new xmlLoader();
    }
    
}