package com.project;

public class client {
    private Loader load;

    public client(LoaderFactory factory){
        this.load=factory.createLoader();
    }
    public void executeLoad(){
        load.load();
    }   
}
