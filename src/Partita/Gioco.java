package Partita;

import Dadi.Dadi;
import Dadi.SingletonDadi;
import Giocatori.Giocatore;
import Giocatori.GiocatoriList;
import Giocatori.SingletonGiocatoriList;
import Observer.Observer;
import Partita.Avanzamento.TrovaCasella;
import Regole.*;
import Scacchiera.Matrice;
import Scacchiera.Scala;
import Scacchiera.Serpente;
import Scacchiera.SsNonValido;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Gioco {
    private static Matrice scacchiera;
    public static Semaphore semDadi=new Semaphore(0);
    public static Semaphore semAvanti=new Semaphore(0);
    private ArrayList<Observer> osservatori=new ArrayList<>();
    private ArrayList<Observer> osservatoriDadi=new ArrayList<>();
    private ArrayList<Observer> osservatoriPlayer=new ArrayList<>();
    private ArrayList<Observer> osservatoriVincitore=new ArrayList<>();
    private ArrayList<Observer> osservatoriMsg=new ArrayList<>();
    private Observer cambiaColore;
    private boolean abbiamoVincitore=false;
    GiocoManuale giocoManuale;
    GiocoAutomatico giocoAutomatico;


    /* ++++++++++++++++++++ DA PRENDERE IN INPUT +++++++++++++++++++++*/
    private  List<Regole> regole;
    private GiocatoriList giocatori;
    private Iterator<Giocatore> gIterator;
    private int nScale=5, nSerpenti=5, nCaselle, nRighe, nColonne;
    private List<Scala> scalePersonalizzate;
    private List<Serpente> serpentiPersonalizzati;

    public void addObserver(Observer o){
        osservatori.add(o);
    }

    public void addObserverGiocatore(Observer o){
        osservatoriPlayer.add(o);
    }

    public void addObserverMsg(Observer o){
        osservatoriMsg.add(o);
    }

    public void addObserverDadi(Observer o){
        osservatoriDadi.add(o);
    }

    public void addObserverVincitore(Observer o){
        osservatoriVincitore.add(o);
    }

    public void setCambiaColore(Observer o){
      cambiaColore=o;
    }


    public Gioco( int nRighe, int nColonne, List<Scala> scale, List<Serpente> serpenti){
        this.nRighe=nRighe; this.nColonne=nColonne; this.nCaselle=nColonne*nRighe;
        this.scalePersonalizzate=scale;this.serpentiPersonalizzati=serpenti; this.giocatori= SingletonGiocatoriList.getInstance();
        this.regole= SingletonRegole.getInstance(); gIterator= giocatori.iterator();
    }

    public static Matrice getScacchiera(){
        return scacchiera;
    }

    public void initScacchiera(){
        /* ++++++++++++++++ inizializzo scacchiera ++++++++++++++++++++++++*/
        scacchiera=new Matrice(nRighe,nColonne,nCaselle);
        if(serpentiPersonalizzati.isEmpty()&&scalePersonalizzate.isEmpty()){
            scacchiera.inizializzaScacchiera(nSerpenti,nScale);
        }else {
            try {
                scacchiera.inizializzaScacchieraPersonalizzata(serpentiPersonalizzati,scalePersonalizzate);
            } catch (SsNonValido e) {
                e.printStackTrace();
            }
        }
        scacchiera.caselleSpeciali();

    }


    public void avviaPartita() {
       giocoAutomatico= new GiocoAutomatico();
       giocoAutomatico.start();
    }

    public void avviaManuale() {
        giocoManuale=new GiocoManuale();
        giocoManuale.start();
    }

    public void stop(){
        if(giocoAutomatico==null && giocoManuale==null)
            return;
        if(giocoAutomatico!=null)
            giocoAutomatico.stop();
        else
            giocoManuale.stop();
    }



    class GiocoAutomatico extends Thread{
        public GiocoAutomatico(){}

        public void run(){
            TrovaCasella avanzamento = new TrovaCasella();


            Giocatore player = null;
            while (!abbiamoVincitore) {
                player = gIterator.next();
                System.out.println("gioca: " + player.getName());
                for(Observer o: osservatoriPlayer){
                    o.update(player.getName());
                }
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (regole.contains(Regole.E) && player.getTurniFermo() > 0) {
                    //assumo che si possa usare una sola carta sosta alla volta
                    if (regole.contains(Regole.H) && player.getCartaSosta() > 0 && player.getTurniFermo() == 1) {
                        player.setBonusSosta(player.getCartaSosta() - 1);
                        player.setFermo(player.getTurniFermo() - 1);
                        System.out.println("Carta sosta evita di bloccarsi");
                        for(Observer o: osservatoriMsg){o.update("Carta sosta evita di bloccarsi");}
                    } else {
                        player.setFermo(player.getTurniFermo() - 1);
                        System.out.println(player.getName() + " bloccato");
                        for(Observer o: osservatoriMsg){o.update(player.getName() + " bloccato");}
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                }
                Object[] color=new Object[2];
                if(player.getCasellaCorrente()!=0) {
                    color[0] = player.getCasellaCorrente() - 1;
                    color[1] = true;
                    cambiaColore.update(color);
                }
                Dadi d;
                int[] ris;
                //DADO SINGOLO
                if (regole.contains(Regole.C) && player.getCasellaCorrente() > scacchiera.getNumeroCaselle() - 7 && player.getCasellaCorrente() < scacchiera.getNumeroCaselle()) {
                    d = SingletonDadi.getInstance();
                    ris = new int[1];
                    ris[0] = d.tiraIDadi()[0];
                    System.out.println("un dado :" + ris[0]);
                } else {
                    d = SingletonDadi.getInstance();
                    ris = d.tiraIDadi();
                    String dadi = ris.length == 2 ? ris[0] + "-" + ris[1] : "" + ris[0];
                    System.out.println("dadi: " + dadi);

                }
                for(Observer o:osservatoriDadi){
                    o.update(ris);
                }
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //devo applicare, eventualmente, regola doppio 6
                if (regole.contains(Regole.D) && ris.length == 2 && ris[0] == ris[1] && ris[0] == 6)
                    player.setDoppioSei(true);

                int nCaselle = ris.length == 1 ? ris[0] : ris[0] + ris[1];

                try {
                    avanzamento.setAvanzamentoStrategy(TrovaCasella.trovaCasella(player, nCaselle));
                    player=avanzamento.avanza(player, nCaselle);
                    System.out.println("Mi sono spostato: casella corrente+ " + player.getCasellaCorrente());

                    int tmp;
                    if(color[0]!=null)
                        tmp=(Integer) color[0];
                    else
                        tmp=-1;
                    color=new Object[3];
                    color[0]=player.getCasellaCorrente()-1;
                    color[1]=false;
                    color[2]=tmp;

                    cambiaColore.update(color);
                    for(Observer o: osservatori){
                        o.update(null);
                    }
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        this.interrupt();
                    }
                } catch (WinException e) {
                    abbiamoVincitore = true;
                    int tmp;
                    if(color[0]!=null)
                        tmp=(Integer) color[0];
                    else
                        tmp=-1;
                    color=new Object[3];
                    color[0]=player.getCasellaCorrente()-1;
                    color[1]=false;
                    color[2]=tmp;

                    cambiaColore.update(color);
                    for(Observer o: osservatori){
                        o.update(null);
                    }
                    System.out.println("Ha vinto " + player.getName());
                    for(Observer o:osservatoriVincitore){
                        o.update(player.getName());
                    }
                }

            }
        }
    }
    /**Permette di fare avanzare il gioco in maniera manuale tramite un semaforo.
             * Il giocatore, tramite un apposito bottone, fa una prima release che permette al thread di
             * tirare i dadi ed una seconda release che consente di muoversi
             * */
            class GiocoManuale extends Thread{

                public GiocoManuale(){};

                public void run(){
                    TrovaCasella avanzamento =new TrovaCasella();


                    Giocatore player=null;
                    while (!abbiamoVincitore) {
                        player = gIterator.next();
                        System.out.println("gioca: " + player.getName());
                        for(Observer o: osservatoriPlayer){
                            o.update(player.getName());
                        }

                        if (regole.contains(Regole.E) && player.getTurniFermo() > 0) {
                            //assumo che si possa usare una sola carta sosta alla volta
                            if (regole.contains(Regole.H) && player.getCartaSosta() > 0 && player.getTurniFermo() == 1) {
                                player.setBonusSosta(player.getCartaSosta() - 1);
                                player.setFermo(player.getTurniFermo() - 1);
                                System.out.println("Carta sosta evita di bloccarsi");
                                for(Observer o: osservatoriMsg){o.update("Carta sosta evita di bloccarsi");}
                            } else {
                                player.setFermo(player.getTurniFermo() - 1);
                                System.out.println(player.getName() + " bloccato");
                                for(Observer o: osservatoriMsg){o.update(player.getName() + " bloccato");}
                                try {
                                    sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                continue;
                            }
                        }
                        try {
                            semDadi.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        Object[] color=new Object[2];
                        if(player.getCasellaCorrente()!=0) {
                            color[0] = player.getCasellaCorrente() - 1;
                            color[1] = true;
                            cambiaColore.update(color);
                        }
                        Dadi d;
                        int[] ris;
                        //DADO SINGOLO
                        if (regole.contains(Regole.C) && player.getCasellaCorrente() > scacchiera.getNumeroCaselle() - 7 && player.getCasellaCorrente() < scacchiera.getNumeroCaselle()) {
                            d = SingletonDadi.getInstance();
                            ris = new int[1];
                            ris[0] = d.tiraIDadi()[0];
                            System.out.println("un dado :" + ris[0]);
                        } else {
                            d = SingletonDadi.getInstance();
                            ris = d.tiraIDadi();
                            String dadi = ris.length == 2 ? ris[0] + "-" + ris[1] : "" + ris[0];
                            System.out.println("dadi: " + dadi);

                        }
                        for(Observer o:osservatoriDadi){
                            o.update(ris);
                        }
                        //devo applicare, eventualmente, regola doppio 6
                        if (regole.contains(Regole.D) && ris.length == 2 && ris[0] == ris[1] && ris[0] == 6)
                            player.setDoppioSei(true);

                        int nCaselle = ris.length == 1 ? ris[0] : ris[0] + ris[1];
                        try {
                            semAvanti.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                            avanzamento.setAvanzamentoStrategy(TrovaCasella.trovaCasella(player, nCaselle));
                            avanzamento.avanza(player, nCaselle);
                            System.out.println("Mi sono spostato: casella corrente+ " + player.getCasellaCorrente());

                            int tmp;
                            if(color[0]!=null)
                                tmp=(Integer) color[0];
                            else
                                tmp=-1;
                            color=new Object[3];
                            color[0]=player.getCasellaCorrente()-1;
                            color[1]=false;
                            color[2]=tmp;
                            cambiaColore.update(color);
                            for(Observer o: osservatori){
                                o.update(null);
                            }
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        } catch (WinException e) {
                            abbiamoVincitore = true;
                            int tmp;
                            if(color[0]!=null)
                                tmp=(Integer) color[0];
                            else
                                tmp=-1;
                            color=new Object[3];
                            color[0]=player.getCasellaCorrente()-1;
                            color[1]=false;
                            color[2]=tmp;
                            cambiaColore.update(color);
                            for(Observer o: osservatori){
                                o.update(null);
                            }
                            System.out.println("Ha vinto " + player.getName());
                            for(Observer o:osservatoriVincitore){
                                o.update(player.getName());
                            }
                        }
                    }
                }
            }








}
