package Partita.Avanzamento;

import Giocatori.Giocatore;
import Observer.Observer;
import Partita.Gioco;
import Partita.WinException;
import Scacchiera.CasellaSpeciale;

import java.util.ArrayList;
import java.util.List;

/** avanza del numero di caselle*/
public class CasellaNormale implements AvanzamentoStrategy{

    public CasellaNormale(){}

    private TrovaCasella avanzamento =new TrovaCasella();
    private static List<Observer> osservatori=new ArrayList<>();

    public static void addObserver(Observer o){
        osservatori.add(o);
    }

    @Override
    public Giocatore avanza(Giocatore player, int caselle) throws WinException {
        //avanzo del numero di caselle
        player.setCasellaCorrente(TrovaCasella.calcolaCasellaSuccessiva(player, caselle));
        if(player.isDoppioSei()){
            player.setDoppioSei(false);
            for (Observer o : osservatori) {
                o.update("Doppio 6: +12");
            }
           avanzamento.setAvanzamentoStrategy(TrovaCasella.trovaCasella(player, caselle));
           avanzamento.avanza(player, caselle);
        }
        CasellaSpeciale cs=Gioco.getScacchiera().getCasella(player.getCasellaCorrente()).isSpecial();
        if(cs!=null) {
            if (cs.equals(CasellaSpeciale.LOCANDA) || cs.equals(CasellaSpeciale.PANCHINA)) {
                String tmp = cs.equals(CasellaSpeciale.LOCANDA) ? "3 turni" : "1 turno";
                for (Observer o : osservatori) {
                    o.update("Casella corrente: " + cs + " fermo " + tmp);
                }
            }
        }
        if(player.getCasellaCorrente()==Gioco.getScacchiera().getNumeroCaselle())
            throw new WinException();

        return player;

    }
}
