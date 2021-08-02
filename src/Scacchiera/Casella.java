package Scacchiera;

public class Casella {
    private int id;
    private boolean speciale=false;
    private CasellaSpeciale tipologia;
    private int corrispondenteSS;

    /**Una casella semplice, bisogna indicare l'id*/
    public Casella(int i){
        id=i;
    }

    /**Rende la casella speciale, specificando la tipologia*/
    public Casella(CasellaSpeciale tipologia){
        speciale=true; this.tipologia=tipologia;
    }

    /** controlla se una casella è speciale e restituisce la tipologia,
     *  o null se la casella è ordinaria*/
    public CasellaSpeciale isSpecial(){
        return speciale? tipologia : null;
    }

    public void setSpeciale(CasellaSpeciale tipo){
        speciale=true; tipologia=tipo;
    }

    /** nel caso di scale o serpenti indica la casella finale dello spostamento */
    public void setCorrispondenteSS(int s){
        corrispondenteSS=s;
    }

    public int getId(){
        return id;
    }

    public int getCorrispondenteSS(){
        return corrispondenteSS;
    }

}
