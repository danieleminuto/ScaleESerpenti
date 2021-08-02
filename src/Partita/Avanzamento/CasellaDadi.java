package Partita.Avanzamento;

import Dadi.Dadi;
import Dadi.SingletonDadi;
import Giocatori.Giocatore;
import Observer.Observer;
import Partita.Gioco;
import Regole.*;
import Partita.WinException;

import java.util.ArrayList;
import java.util.List;

/** Avanza del numero di caselle selezionato, tira i dadi e avanza di nuovo*/
public class CasellaDadi implements AvanzamentoStrategy{
    private TrovaCasella avanzamento =new TrovaCasella();
    private static List<Observer> osservatori=new ArrayList<>();

    public static void addObserver(Observer o){
        osservatori.add(o);
    }

    public CasellaDadi(){};

    @Override
    public Giocatore avanza(Giocatore player, int caselle) throws WinException {
        //avanzo del numero di caselle (numero casella, non indice)
        player.setCasellaCorrente(TrovaCasella.calcolaCasellaSuccessiva(player, caselle));

        /* lancio di nuovo dadi e mi muovo*/
        Dadi d;
        int[] ris;
        if (SingletonRegole.getInstance().contains(Regole.C) && player.getCasellaCorrente() > Gioco.getScacchiera().getNumeroCaselle() - 7 && player.getCasellaCorrente() < Gioco.getScacchiera().getNumeroCaselle()){
            ris=new int[1];
            d = SingletonDadi.getInstance();
            ris[0]=d.tiraIDadi()[0];
        }
        else {
            d = SingletonDadi.getInstance();
            ris = d.tiraIDadi();
        }
        int nCaselle= ris.length==1? ris[0] : ris[0]+ris[1];
        System.out.println("casella dadi: avanti di "+nCaselle);


        //devo applicare, eventualmente, regola doppio 6
        if(SingletonRegole.getInstance().contains(Regole.D) && ris.length==2 && ris[0]==ris[1] && ris[0]==6)
            player.setDoppioSei(true);


        String msg= ris.length==1? "Casella dadi, nuovo lancio: "+ris[0]: "Casella dadi, nuovo lancio: "+ris[0]+"-"+ris[1];
        for(Observer o: osservatori){
            o.update(msg);
        }

        avanzamento.setAvanzamentoStrategy(TrovaCasella.trovaCasella(player, nCaselle));
        avanzamento.avanza(player,nCaselle);

        if(player.getCasellaCorrente()== Gioco.getScacchiera().getNumeroCaselle())
            throw new WinException();


        return player;
    }

}
