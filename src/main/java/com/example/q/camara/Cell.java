package com.example.q.camara;

import java.io.Serializable;

/**
 * Created by jordi on 24/03/2015.
 */
public class Cell implements Serializable {
    private boolean isClicked;
    private String number;
    private int id;
    Cell(String number) {
        this.number = number;
        this.id = id;
        this.isClicked = false;
    }

    public String getNumber() {
        if (!isClicked){
            return "?";
        }else{
            return this.number;

        }
    }

    public int getId() {
        return id;
    }

    public boolean isClicked(){
        return this.isClicked;
    }
    public void setClicked(boolean isClicked) {
        this.isClicked = isClicked;
    }
}
