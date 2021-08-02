package Partita.Avanzamento;

import Giocatori.Giocatore;
import Observer.Observer;
import Partita.Gioco;
import Partita.WinException;
import Scacchiera.Casella;
import Scacchiera.CasellaSpeciale;
import Scacchiera.Matrice;

import java.util.ArrayList;
import java.util.List;

/** avanza e trova l'inizio di una scala e la testa di un serpente*/
public class ScalaESerpente implements AvanzamentoStrategy{
    public ScalaESerpente(){}

    private TrovaCasella avanzamento =new TrovaCasella();
    private static List<Observer> osservatori=new ArrayList<>();

    public static void addObserver(Observer o){
        osservatori.add(o);
    }

    @Override
    public Giocatore avanza(Giocatore player, int caselle) throws WinException {
        player.setCasellaCorrente(TrovaCasella.calcolaCasellaSuccessiva(player, caselle));
        Matrice scacchiera=Gioco.getScacchiera();

        Casella c= scacchiera.getCasella(player.getCasellaCorrente());
        CasellaSpeciale cs=c.isSpecial();
        for(Observer o: osservatori){
            o.update("Casella "+(c.getId()+1)+" "+cs.name()+" vai a "+(c.getCorrispondenteSS()+1));
        }
        //nel caso di scale e serpenti mi muovo sulla casella corrispondente
        player.setCasellaCorrente(c.getCorrispondenteSS()+1);

        if(player.isDoppioSei()){
            player.setDoppioSei(false);
            avanzamento.setAvanzamentoStrategy(TrovaCasella.trovaCasella(player, caselle));
            avanzamento.avanza(player, caselle);
        }

        if(player.getCasellaCorrente()== Gioco.getScacchiera().getNumeroCaselle())
            throw new WinException();

        return player;

    }
}
