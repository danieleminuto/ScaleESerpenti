package Partita.Avanzamento;

import Dadi.*;
import MazzoDiCarte.Carta;
import MazzoDiCarte.SingletonMazzo;
import Giocatori.Giocatore;
import Observer.Observer;
import Partita.Gioco;
import Regole.*;
import Partita.WinException;

import javax.swing.plaf.TableHeaderUI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**avanza, pesca una carta e si comporta di conseguenza*/
public class CasellaPescaUnaCarta implements AvanzamentoStrategy{

    private TrovaCasella avanzamento =new TrovaCasella();

    public CasellaPescaUnaCarta() {};
    private static List<Observer> osservatori=new ArrayList<>();

    public static void addObserver(Observer o){
        osservatori.add(o);
    }


    @Override
    public Giocatore avanza(Giocatore player, int caselle) throws WinException {
        player.setCasellaCorrente(TrovaCasella.calcolaCasellaSuccessiva(player, caselle));

        SingletonMazzo mazzo= SingletonMazzo.getInstance();
        Iterator<Carta> it=mazzo.iterator();
        Carta carta=it.next();
        System.out.println("pescata "+carta);
        TrovaCasella avanzamento =new TrovaCasella();
        switch (carta){
            case DADI:

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
                String msg= ris.length==1? "Carta pescata: Dadi, nuovo lancio: "+ris[0]: "Carta pescata: Dadi, nuovo lancio: "+ris[0]+"-"+ris[1];
                for(Observer o: osservatori){
                    o.update(msg);
                }
                System.out.println("+"+nCaselle+" caselle");
                avanzamento.setAvanzamentoStrategy(TrovaCasella.trovaCasella(player, nCaselle));
                avanzamento.avanza(player, nCaselle);
                break;
            case MOLLA:
                avanzamento.setAvanzamentoStrategy(TrovaCasella.trovaCasella(player, caselle));
                avanzamento.avanza(player, caselle);
                break;
            case LOCANDA:
                player.setFermo(3);
                for(Observer o: osservatori){
                    o.update("Carta pescata: Locanda, fermo 3 turni");
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case PANCHINA:

                if (SingletonRegole.getInstance().contains(Regole.H) && player.getCartaSosta()>0) {
                    player.setBonusSosta(player.getCartaSosta()-1);
                    for(Observer o: osservatori){
                        o.update("Carta pescata: Panchina, evitata grazie a carta sosta");
                    }
                }
                else {
                    player.setFermo(player.getTurniFermo() + 1);
                    for(Observer o: osservatori){
                        o.update("Carta pescata: Panchina, fermo un turno");
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case DIVIETO_DI_SOSTA:
                for(Observer o: osservatori){
                    o.update("Carta pescata: Divieto di Sosta");
                }
                player.setBonusSosta(player.getCartaSosta()+1);
                break;
            }

        if(player.getCasellaCorrente()==Gioco.getScacchiera().getNumeroCaselle())
            throw new WinException();

        return player;

        }


}
