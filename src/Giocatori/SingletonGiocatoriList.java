package Giocatori;



import java.util.List;

public class SingletonGiocatoriList {

    private static GiocatoriList GIOCATORI=null;

    public SingletonGiocatoriList(List<Giocatore> giocatori){
        this.GIOCATORI=new GiocatoriList(giocatori);
    }

    public static synchronized GiocatoriList getInstance(){
        return GIOCATORI;
    }
}
