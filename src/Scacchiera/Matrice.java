package Scacchiera;


import Regole.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Matrice {
    private static int righe=10;
    private static int colonne=10;
    private static int numeroCaselle=100;
    private Casella[] caselle;
    private int caselleSpecialiPerTipologia=3;
    private static List<Serpente> serpenti;
    private static List<Scala> scale;

    public static int getColonne() {
        return colonne;
    }

    public static int getRighe() {
        return righe;
    }


    public Matrice(int nRighe, int nColonne, int nCaselle){
        righe=nRighe;
        colonne=nColonne;
        numeroCaselle=nCaselle;
        caselle=new Casella[nCaselle];
        for(int i=0;i<nCaselle;i++)
            caselle[i]=new Casella(i);
        scale=new ArrayList<>();
        serpenti=new ArrayList<>();
    }

    public Casella[] getCaselle() {
        return caselle;
    }

    public static List<Scala> getScale() {
        return scale;
    }

    public static List<Serpente> getSerpenti() {
        return serpenti;
    }

    public int getNumeroCaselle(){
        return numeroCaselle;
    }

    /** imposta scale e serpenti sulla scacchiera in base al numero di scale e serpenti*/
    public void inizializzaScacchiera(int nSerpenti, int nScale){

        //imposto i serpenti
        for(int i=0;i<nSerpenti;i++){
            Serpente s=new Serpente(numeroCaselle);
            int testa=s.getFine()-1;
            int coda=s.getInizio()-1;
            //sottraggo 1 perchè i valori della testa e della cosa vengono calcolati sul numero delle caselle, non degli indici
            if(caselle[testa].isSpecial()!=null || caselle[coda].isSpecial()!=null){
                i--; continue; //una delle due caselle è già speciale, non può essere occupata
            }
            caselle[testa].setSpeciale(CasellaSpeciale.TESTA_SERPENTE);
            caselle[testa].setCorrispondenteSS(coda);
            caselle[coda].setSpeciale(CasellaSpeciale.CODA_SERPENTE);
            serpenti.add(s);
        }

        //imposto le scale
        for(int i=0;i<nScale;i++){
            Scala s=new Scala(numeroCaselle);
            int inizioScala=s.getInizio()-1;
            int fineScala=s.getFine()-1;

            if(caselle[inizioScala].isSpecial()!=null || caselle[fineScala].isSpecial()!=null){
                i--; continue; //una delle due caselle è già speciale, non può essere occupata
            }
            caselle[inizioScala].setSpeciale(CasellaSpeciale.INIZIO_SCALA);
            caselle[inizioScala].setCorrispondenteSS(fineScala);
            caselle[fineScala].setSpeciale(CasellaSpeciale.FINE_SCALA);
            scale.add(s);
        }
    }

    /** imposta scale e serpenti sulla scacchiera in base a liste di scale e serpenti personalizzate*/
    public void inizializzaScacchieraPersonalizzata(List<Serpente> serpenti, List<Scala> scale) throws SsNonValido{

        for(Serpente s: serpenti){
            int testa=s.getFine()-1;
            int coda=s.getInizio()-1;
            //sottraggo 1 perchè i valori della testa e della cosa vengono calcolati sul numero delle caselle, non degli indici
            if(caselle[testa].isSpecial()!=null || caselle[coda].isSpecial()!=null){
                throw new SsNonValido(); //una delle due caselle è già speciale, non può essere occupata
            }
            caselle[testa].setSpeciale(CasellaSpeciale.TESTA_SERPENTE);
            caselle[testa].setCorrispondenteSS(coda);
            caselle[coda].setSpeciale(CasellaSpeciale.CODA_SERPENTE);
        }

        for(Scala s: scale){
            int inizioScala=s.getInizio()-1;
            int fineScala=s.getFine()-1;

            if(caselle[inizioScala].isSpecial()!=null || caselle[fineScala].isSpecial()!=null){
                throw new SsNonValido();
            }
            caselle[inizioScala].setSpeciale(CasellaSpeciale.INIZIO_SCALA);
            caselle[inizioScala].setCorrispondenteSS(fineScala);
            caselle[fineScala].setSpeciale(CasellaSpeciale.FINE_SCALA);
        }
        this.serpenti=serpenti;
        this.scale=scale;
    }

    /** N intende il numero della casella, non l'indice. Attenzione!*/
    public Casella getCasella(int n){
        return caselle[n-1];
    }

    /**Popola la matrice con le caselle speciali in base alle regole selezionate nel gioco*/
    public void caselleSpeciali(){
        if( SingletonRegole.getInstance().contains(Regole.F)){
            for(int i=0;i<caselleSpecialiPerTipologia;i++){
                rendiSpeciale(CasellaSpeciale.DADI,false);
            }
            for(int i=0;i<caselleSpecialiPerTipologia;i++){
                rendiSpeciale(CasellaSpeciale.MOLLA,true);
            }
        }

        if(SingletonRegole.getInstance().contains(Regole.E)){
            for(int i=0;i<caselleSpecialiPerTipologia;i++){
                rendiSpeciale(CasellaSpeciale.PANCHINA,false);
            }
            for(int i=0;i<caselleSpecialiPerTipologia;i++){
                rendiSpeciale(CasellaSpeciale.LOCANDA,false);
            }
        }

        if(SingletonRegole.getInstance().contains(Regole.G)){
            for(int i=0;i<caselleSpecialiPerTipologia;i++){
                rendiSpeciale(CasellaSpeciale.PESCA_UNA_CARTA,false);
            }
        }
    }

    /** rende una casella speciale, assegnandole il tipo di tipologia specificato.
     * flag indica che la casella è di tipo molla e che non possono essere assegnate le ultime
     * 12 caselle della scacchiera
     * @param speciale
     * @param flag
     */
    private void rendiSpeciale(CasellaSpeciale speciale, boolean flag){
        if(flag){
            int num=new Random().nextInt(numeroCaselle-1-12);
            while(getCasella(num+1).isSpecial()!=null){
                num=new Random().nextInt(numeroCaselle-1-12);
            }
            caselle[num].setSpeciale(speciale);
        }
        else {
            int num = new Random().nextInt(numeroCaselle - 1);
            while (getCasella(num + 1).isSpecial() != null) {
                num = new Random().nextInt(numeroCaselle - 1);
            }
            caselle[num].setSpeciale(speciale);
        }
    }
}
