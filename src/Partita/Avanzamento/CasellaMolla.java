package Partita.Avanzamento;

import Giocatori.Giocatore;
import Observer.Observer;
import Partita.Gioco;
import Partita.WinException;

import java.util.ArrayList;
import java.util.List;

/** avanza due volte del numero di caselle*/
public class CasellaMolla implements AvanzamentoStrategy{
    private TrovaCasella avanzamento =new TrovaCasella();
    private static List<Observer> osservatori=new ArrayList<>();

    public static void addObserver(Observer o){
        osservatori.add(o);
    }

    public CasellaMolla(){}

    @Override
    public Giocatore avanza(Giocatore player, int caselle) throws WinException {
        player.setCasellaCorrente(TrovaCasella.calcolaCasellaSuccessiva(player, caselle));
        for(Observer o: osservatori){
            o.update("MOLLA: "+player.getCasellaCorrente()+"+"+caselle);
        }
        avanzamento.setAvanzamentoStrategy(TrovaCasella.trovaCasella(player, caselle));
        avanzamento.avanza(player, caselle);

        if(player.getCasellaCorrente()== Gioco.getScacchiera().getNumeroCaselle())
            throw new WinException();

        return player;
    }
}
