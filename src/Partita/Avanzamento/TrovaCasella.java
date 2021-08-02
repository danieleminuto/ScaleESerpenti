package Partita.Avanzamento;

import Giocatori.Giocatore;
import Partita.Gioco;
import Partita.WinException;
import Scacchiera.Casella;
import Scacchiera.CasellaSpeciale;
import Scacchiera.Matrice;


public class TrovaCasella {
    private AvanzamentoStrategy avanzamento;

    public void setAvanzamentoStrategy(AvanzamentoStrategy avanzamento){
        this.avanzamento=avanzamento;
    }


    /**La modalità di avanzamento viene scelta in base alla tipologia di casella sulla quale bisogna andare*/
    static public AvanzamentoStrategy trovaCasella(Giocatore g, int caselle) throws WinException {
        Matrice scacchiera= Gioco.getScacchiera();

        int nSuccessiva=calcolaCasellaSuccessiva(g, caselle);

        Casella successiva= scacchiera.getCasella(nSuccessiva);
        CasellaSpeciale s=successiva.isSpecial();
        if(s==null)
            return new CasellaNormale();
        switch (s){
            case INIZIO_SCALA:

            case TESTA_SERPENTE:
                return new ScalaESerpente();

            case DADI:
                g.setDoppioSei(false);
                System.out.println("Casella Dadi");
                return new CasellaDadi();

            case MOLLA:
                g.setDoppioSei(false);
                System.out.println("casella Molla");
                return new CasellaMolla();

            case PESCA_UNA_CARTA:
                g.setDoppioSei(false);
                return new CasellaPescaUnaCarta();

            case LOCANDA:
                g.setFermo(3);
                return new CasellaNormale();
            case PANCHINA:
                g.setFermo(1);
                return new CasellaNormale();
            default:
                return new CasellaNormale();

        }
    }

    public Giocatore avanza(Giocatore g, int c) throws WinException {
        return avanzamento.avanza(g,c);
    }


    /** Restituisce il numero della casella su cui dovrà andare il giocatore.
     * @return
     */
    public static int calcolaCasellaSuccessiva(Giocatore g, int caselle) {
        Matrice scacchiera= Gioco.getScacchiera();
        int tmp=g.getCasellaCorrente()+caselle, nc=scacchiera.getNumeroCaselle();

        return tmp>nc? nc-(tmp%nc) : tmp;

    }


}
