package com.example.q.camara;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fedora-2-jordi on 28/04/15.
 */
public class Tauler implements Serializable{
    private List<Cell> tauler;
    private int turn;  // si es 0 es el primer
    private char tirada1=' ';
    private char tirada2=' ';
    private boolean point;
    public Tauler(List<Cell> list, int turn){
        this.tauler = list;
        this.turn = turn;
    }
    public void setMove(int pos){
        Cell c = tauler.get(pos);
        if(turn == 0){

            tirada1 = c.getNumber().charAt(0);
            turn++;
        }else{
            tirada2 = c.getNumber().charAt(0);
            turn = 0;
            if(compareMoves()){
                point = true;
            }else{
                point = false;
            }
        }
        c.setClicked(true);
        tauler.set(pos, c);
    }
    public int getTurn(){
        return turn;
    }
    public char getMove2() {
        return tirada2;
    }
    public List<Cell> getTauler() {
        return tauler;
    }
    public boolean compareMoves(){
        return tirada2 == tirada1;
    }
    public boolean openentPoint(){
        return point;
    }
    public void setTauler(List<Cell> tauler) {
        this.tauler = tauler;
    }
    public void reset(){
        tirada1 =' ';
        tirada2 =' ';
    }

}
