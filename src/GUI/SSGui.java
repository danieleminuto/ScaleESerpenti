package GUI;

import Giocatori.Giocatore;
import Giocatori.GiocatoriList;
import Giocatori.SingletonGiocatoriList;
import Observer.Observer;
import Partita.*;
import Partita.Avanzamento.*;
import Scacchiera.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;

public class SSGui {


    public static void main(String[] args) {

        PaginaInizialeGui p=new PaginaInizialeGui();

    }
}
class SS extends JFrame{

    private JButton avvia, lanciaDadi, avanza;
    private JTextArea[][] matrice;
    private JTextArea giocatoreAttuale, dadi, msgInfo;
    private JTextArea[] infoGiocatori;
    private Casella[] caselle;
    private int nRighe=Matrice.getRighe(),nColonne=Matrice.getColonne();
    private Gioco gioco;
    private boolean giocoManuale;

    public void aggiornaGiocatori(){

        GiocatoriList giocatori=SingletonGiocatoriList.getInstance();
        Iterator<Giocatore> it=giocatori.iterator();
        int index=0;
        for(int i=0;i<giocatori.getSize();i++){
            Giocatore g=it.next();
            infoGiocatori[index].setText("Nome: "+g.getName()+" - Casella Corrente: "+g.getCasellaCorrente());//+" - Turni Fermo: "+g.getTurniFermo());
            index++;
        }
    }

    public void aggiornaGiocatoreAttuale(String pl){
        for(int i=0;i<nRighe;i++){
            for(int j=0;j<nColonne;j++){
                matrice[i][j].setBackground(Color.white);
            }
        }
        giocatoreAttuale.setVisible(true);
        giocatoreAttuale.setText("Gioca: "+pl);
        dadi.setText("Tiro:  - ");

    }

    public void aggiornaDadi(int[] n){
        if(n.length==1){
            dadi.setText("Tiro: "+n[0]);
        }
        else
            dadi.setText("Tiro: "+n[0]+"-"+n[1]);
    }



    class Osservatore implements Observer{
        public void Osservatore(){}
        @Override
        public void update(Object o) {

            aggiornaGiocatori();
        }
    }

    class OsservaMazzo extends Thread{
        public void run(){
             Osservatore o=new Osservatore();
             CasellaPescaUnaCarta.addObserver(o);
        }
        class Osservatore implements Observer{
            public void Osservatore(){}
            @Override
            public void update(Object o) {
                msgInfo.setText((String) o);
                msgInfo.setVisible(true);

            }
        }
    }

    /** Delegato a cambiare colore alle caselle*/
    class OsservaCaselle extends Thread{
        public void run(){
            Osservatore o=new Osservatore();;
            gioco.setCambiaColore(o);
        }
        class Osservatore implements Observer{
            public void Osservatore(){}
            @Override
            public void update(Object o) {

                Object[] obj=(Object[]) o;
                int i=(Integer) obj[0]/nColonne;
                int j=(Integer) obj[0]%nColonne;
                boolean flag=(boolean) obj[1];
                if(flag){
                    matrice[i][j].setBackground(Color.yellow);
                }
                else {
                    //inizio<fine
                    if((Integer)obj[2]<(Integer) obj[0]) {
                        for (int x = (Integer) obj[2]+1; x <= (Integer) obj[0]; x++) {
                            i = x / nColonne;
                            j = x % nColonne;
                            matrice[i][j].setBackground(Color.green);
                        }
                    }
                    else {
                        for (int x = (Integer) obj[0]; x < (Integer) obj[2]; x++) {
                            i = x / nColonne;
                            j = x % nColonne;
                            matrice[i][j].setBackground(Color.red);
                        }
                    }
                }
            }
        }
    }

    class OsservatoreGiocatore implements Observer{
        public void OsservatoreGiocatore(){}
        @Override
        public void update(Object o) {
            msgInfo.setVisible(false);
            aggiornaGiocatoreAttuale((String) o);

        }
    }


    class OsservatoreDadi implements Observer{
        public void OsservatoreDadi(){}
        @Override
        public void update(Object o) {
            aggiornaDadi((int[]) o);
        }
    }

    class OsservatoreVincitore implements Observer{
        public void OsservatoreVincitore(){}
        @Override
        public void update(Object o) {
            JOptionPane.showMessageDialog(null,"Ha vinto "+(String)o+"!");
        }
    }

    class OsservatoreCarteECaselle implements Observer{
        public void OsservatoreCarteECaselle(){}
        @Override
        public void update(Object o) {
           msgInfo.setText((String) o);
           msgInfo.setVisible(true);
        }
    }

    public SS(Casella[] scacchiera, Gioco gioco, boolean manuale) {
        Osservatore o=new Osservatore();
        gioco.addObserver(o);

        OsservatoreGiocatore o1=new OsservatoreGiocatore();
        gioco.addObserverGiocatore(o1);

        OsservatoreDadi o2=new OsservatoreDadi();
        gioco.addObserverDadi(o2);

        OsservatoreVincitore o3=new OsservatoreVincitore();
        gioco.addObserverVincitore(o3);

        OsservatoreCarteECaselle o4=new OsservatoreCarteECaselle();
        CasellaNormale.addObserver(o4);
        CasellaMolla.addObserver(o4);
        CasellaDadi.addObserver(o4);
        ScalaESerpente.addObserver(o4);
        gioco.addObserverMsg(o4);



        giocoManuale=manuale;
        ActionListener listener=new Listener();
        this.caselle=scacchiera;
        this.gioco=gioco;

        GiocatoriList giocatori= SingletonGiocatoriList.getInstance();
        this.setTitle("Scale e Serpenti - Daniele Minuto");
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {

               gioco.stop();

            }
        });
        this.setVisible(true);


        infoGiocatori=new JTextArea[giocatori.getSize()];
        Iterator<Giocatore> itg=giocatori.iterator();
        for(int i=0;i< giocatori.getSize();i++){
            Giocatore g= itg.next();
            infoGiocatori[i]=new JTextArea("Nome: "+g.getName()+" - Casella Corrente: 0");
            infoGiocatori[i].setBounds(50, i*30+200,390,20);
            infoGiocatori[i].setEditable(false);
            this.getContentPane().add(infoGiocatori[i]);
        }


        int index=0;
        for(Scala s: Matrice.getScale()){
            JTextArea info=new JTextArea("Inizio Scala: "+s.getInizio()+" - Fine Scala: "+s.getFine());
            info.setEditable(false);
            info.setBounds(1000, index*30+100,500,20);
            this.getContentPane().add(info);
            index++;
        }
        for(Serpente s: Matrice.getSerpenti()){
            JTextArea info=new JTextArea("Testa Serpente: "+s.getFine()+" - Coda Serpente: "+s.getInizio());
            info.setEditable(false);
            info.setBounds(1000, index*30+100,500,20);
            this.getContentPane().add(info);
            index++;
        }

        matrice=new JTextArea[nRighe][nColonne];
        for (int i = 0; i <nRighe ; i++) {
            for (int j = 0; j < nColonne; j++) {

                int elemento = i * nColonne + j;

                CasellaSpeciale c = caselle[elemento].isSpecial();
                String tmp = c == null ? (elemento+ 1) + "" : convertiCS(c);

                matrice[i][j] = new JTextArea(tmp);
                matrice[i][j] .setEditable(false);
                matrice[i][j] .setBounds(450 + j * 40, 100 + i * 40, 30, 30);
                this.getContentPane().add(matrice[i][j] );
                matrice[i][j] = matrice[i][j];


            }
        }
        JTextArea legenda=new JTextArea("LEGENDA CASELLE: P: Panchina - L: Locanda - M: Molla - TS: Testa Serpente - CS: Coda Serpente" +
                " - IS: Inizio Scala - FS: Fine Scala - D: Dadi - C: Prendi una carta");
        legenda.setEditable(false);
        legenda.setBounds(180,20,1000,100);

        this.getContentPane().add(legenda);

        msgInfo = new JTextArea("");
        msgInfo.setEditable(false);
        msgInfo.setVisible(false);
        msgInfo.setBounds(560, 530, 300, 20);
        this.getContentPane().add(msgInfo);

        giocatoreAttuale = new JTextArea("Giocatore: ");
        giocatoreAttuale.setEditable(false);
        giocatoreAttuale.setVisible(false);
        giocatoreAttuale.setBounds(500, 550, 200, 20);
        this.getContentPane().add(giocatoreAttuale);

        dadi = new JTextArea(" ");
        dadi.setEditable(false);

        dadi.setBounds(750, 550, 300, 20);
        this.getContentPane().add(dadi);


            avvia = new JButton("Avvia");
            avvia.setBounds(600, 600, 100, 20);
            avvia.addActionListener(listener);
            this.getContentPane().add(avvia);


            lanciaDadi = new JButton("Lancia i dadi");
            lanciaDadi.setBounds(430, 600, 120, 20);
            lanciaDadi.addActionListener(listener);
            this.getContentPane().add(lanciaDadi);
            lanciaDadi.setVisible(giocoManuale);

            avanza = new JButton("Avanza");
            avanza.setBounds(750, 600, 100, 20);
            avanza.addActionListener(listener);
            avanza.setVisible(giocoManuale);
            this.getContentPane().add(avanza);



        JTextArea prova=new JTextArea("");
        prova.setBounds(700,500,100,50);
        this.getContentPane().add(prova);
    }

    public String convertiCS(CasellaSpeciale c){
        switch (c){
            case PANCHINA:
                return "P";
            case LOCANDA:
                return "L";
            case MOLLA:
                return "M";
            case TESTA_SERPENTE:
                return "TS";
            case INIZIO_SCALA:
                return "IS";
            case DADI:
                return "D";
            case FINE_SCALA:
                return "FS";
            case CODA_SERPENTE:
                return "CS";
            default:
                return "C";
        }
    }

    class Listener implements ActionListener {
        boolean flag=false;
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==avvia && !giocoManuale ){
                gioco.avviaPartita();
                new OsservaMazzo().start();
                new OsservaCaselle().start();
            }
            else if(e.getSource()==avvia && giocoManuale ){
                gioco.avviaManuale();
                new OsservaMazzo().start();
                new OsservaCaselle().start();
            }
            else if(e.getSource()==lanciaDadi){

                if(!flag){
                    Gioco.semDadi.release();
                    flag=!flag;

                }
            }
            else if(e.getSource()==avanza){
                if(flag) {
                    Gioco.semAvanti.release();
                    flag=!flag;
                }
            }
        }
    }
}
