package Giocatori;

import java.util.Objects;

public class Giocatore {
    private String name;
    private int turniFermo=0;
    private int cartaSosta=0;
    private int casellaCorrente;
    private boolean doppioSei=false;

    public Giocatore(String name){
        this.name=name;
        this.casellaCorrente=0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Giocatore giocatore = (Giocatore) o;
        return Objects.equals(name, giocatore.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getName() {
        return name;
    }

    public void setCasellaCorrente(int casella){
        casellaCorrente=casella;
    }

    public int getCasellaCorrente(){
        return casellaCorrente;
    }

    public boolean isDoppioSei(){return doppioSei;}

    public void setDoppioSei(boolean flag){doppioSei=flag;}

    public void setFermo(int fermo){
        turniFermo=fermo;
    }

    public void setBonusSosta(int carte){
        cartaSosta=carte;
    }

    public int getTurniFermo() {
        return turniFermo;
    }

    public int getCartaSosta() {
        return cartaSosta;
        
    }
}
