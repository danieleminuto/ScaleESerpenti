package GUI;


import Dadi.SingletonDadi;
import Giocatori.Giocatore;
import Giocatori.SingletonGiocatoriList;
import MazzoDiCarte.SingletonMazzo;
import Partita.Gioco;
import Regole.*;
import Scacchiera.Scala;
import Scacchiera.Serpente;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class PaginaInizialeGui extends JFrame {

    private JCheckBox D, E,F,G,H,C;
    private JTextArea titolo;
    private JButton inserisciScala, inserisciSerpente, inserisciGiocatore, manuale, automatico, salva, carica;
    private JTextField nRighe, nColonne, inizioSerpente,fineSerpente, inizioScala, fineScala, nomeGiocatore;
    private ButtonGroup dadi =new ButtonGroup();
    private JRadioButton uno, due;
    private ArrayList<Integer> speciali =new ArrayList<>();
    private ArrayList<Giocatore> giocatori=new ArrayList<>();
    private ArrayList<Serpente> serpenti=new ArrayList<>();
    private ArrayList<Regole> regole=new ArrayList<>();
    private ArrayList<Scala> scale=new ArrayList<>();
    private File fileSalvataggio=null;


    private void salva(String nomeFile) throws IOException {
        PrintWriter pw=new PrintWriter( new FileWriter(nomeFile));
        //prima riga: nRighe nColonne
        pw.println("Inizio:");
        String tmp=nRighe.getText()+" "+nColonne.getText();
        pw.println(tmp);
        pw.println("Serpenti:");
        for(Serpente s: serpenti){
            //coda-testa
            tmp=s.getInizio()+" "+s.getFine();
            pw.println(tmp);
        }
        pw.println("/Serpenti");

        pw.println("Scale:");
        for(Scala s: scale){
            //inizio-fine
            tmp=s.getInizio()+" "+s.getFine();
            pw.println(tmp);

        }
        pw.println("/Scale");

        tmp=uno.isSelected()? "1":"2";
        pw.println("Dadi "+tmp);

        pw.println("Regole");
            if(C.isSelected())
                pw.println("C");
            if(D.isSelected())
                pw.println("D");
            if(E.isSelected())
                pw.println("E");
            if(F.isSelected())
                pw.println("F");
            if(G.isSelected())
                pw.println("G");
            if(H.isSelected())
                pw.println("H");

        pw.println("/Regole");

        pw.close();
    }//salva


    private void save() {
        JFileChooser chooser=new JFileChooser();
        try{
            if( fileSalvataggio!=null ){
                int ans=JOptionPane.showConfirmDialog(null,"Sovrascrivere "+fileSalvataggio.getAbsolutePath()+" ?");
                if( ans==0 )//si
                    salva( fileSalvataggio.getAbsolutePath() );
                else
                    JOptionPane.showMessageDialog(null,"Nessun salvataggio");
                return;
            }
            if( chooser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION ){
                fileSalvataggio=chooser.getSelectedFile();
                setTitle("Scale e Serpenti"+" - "+fileSalvataggio.getName());
            }
            if( fileSalvataggio!=null ){
                salva( fileSalvataggio.getAbsolutePath() );
            }
            else
                JOptionPane.showMessageDialog(null,"Nessun Salvataggio");
        }catch( Exception exc ){
            exc.printStackTrace();
        }
    }




    private void ripristina(String nomeFile)throws IOException{
        BufferedReader br=new BufferedReader( new FileReader(nomeFile) );
        String linea=null;
        int nDadi;

        if(!br.readLine().equals("Inizio:"))
            throw new IOException();
        linea=br.readLine();
        String[] tmp=linea.split(" ");
         nRighe.setText(tmp[0]);
        nColonne.setText(tmp[1]);

        br.readLine();
        linea=br.readLine();
        while(!linea.equals("/Serpenti")){
            int testa,coda;


            tmp=linea.split(" ");
            coda=Integer.parseInt(tmp[0]);
            testa=Integer.parseInt(tmp[1]);
            serpenti.add(new Serpente(coda,testa));
            linea=br.readLine();
        }
        br.readLine();

        linea=br.readLine();
        while(!linea.equals("/Scale")){
            int inizio,fine;


            tmp=linea.split(" ");
            inizio=Integer.parseInt(tmp[0]);
            fine=Integer.parseInt(tmp[1]);
            scale.add(new Scala(inizio,fine));
            linea=br.readLine();
        }


        linea=br.readLine();
        tmp=linea.split(" ");
        nDadi=Integer.parseInt(tmp[1]);
        if(nDadi==1){
           uno.setSelected(true);
        }else
            due.setSelected(true);


        linea=br.readLine();
        while(!linea.equals("/Regole")){
            linea=br.readLine();
            switch(linea){
                case "B":
                    regole.add(Regole.B);
                    break;
                case "C":
                    regole.add(Regole.C);
                    C.setSelected(true);
                    break;
                case "D":
                    regole.add(Regole.D);
                    D.setSelected(true);
                    break;
                case "E":
                    regole.add(Regole.E);
                    E.setSelected(true);
                    break;
                case "F":
                    regole.add(Regole.F);
                    F.setSelected(true);
                    break;
                case "G":
                    regole.add(Regole.G);
                    G.setSelected(true);
                    break;
                case "H":
                    regole.add(Regole.H);
                    H.setSelected(true);
                    break;
            }
        }
        br.close();

    }//ripristina

    public PaginaInizialeGui(){

        Listener listener=new Listener();
        this.setTitle("Scale e Serpenti - Daniele Minuto");
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        titolo=new JTextArea("Inserisci i parametri di gioco");
        titolo.setEditable(false);
        titolo.setBounds(520,50,1000,50);
        this.getContentPane().add(titolo);

        nRighe =new JTextField("10");
        JTextArea r=new JTextArea("Righe Matrice:");
        r.setEditable(false);
        r.setBounds(680,100,80,20);
        nRighe.setBounds(760,100,40,20);
        this.getContentPane().add(nRighe);
        this.getContentPane().add(r);

        nColonne =new JTextField("10");
        JTextArea c=new JTextArea("Colonne Matrice:");
        c.setEditable(false);
        c.setBounds(820,100,90,20);
        nColonne.setBounds(915,100,40,20);
        this.getContentPane().add(nColonne);
        this.getContentPane().add(c);

        JTextArea inSer=new JTextArea("Inserisci un serpente:");
        inSer.setEditable(false);
        inSer.setBounds(680,140,200,20);
        this.getContentPane().add(inSer);

        JTextArea tSer=new JTextArea("Casella testa serpente:");
        tSer.setEditable(false);
        tSer.setBounds(680,160,125,20);
        this.getContentPane().add(tSer);
        fineSerpente =new JTextField("");
        fineSerpente.setBounds(805,160,40,20);
        this.getContentPane().add(fineSerpente);



        JTextArea cSer=new JTextArea("Casella coda serpente:");
        cSer.setEditable(false);
        cSer.setBounds(860,160,125,20);
        this.getContentPane().add(cSer);
        inizioSerpente =new JTextField("");
        inizioSerpente.setBounds(985,160,40,20);
        this.getContentPane().add(inizioSerpente);

        inserisciSerpente=new JButton("Inserisci");
        inserisciSerpente.setBounds(1060,160,90,20);
        inserisciSerpente.addActionListener(listener);
        this.getContentPane().add(inserisciSerpente);



        JTextArea inSc=new JTextArea("Inserisci una scala:");
        inSc.setEditable(false);
        inSc.setBounds(680,190,200,20);
        this.getContentPane().add(inSc);

        JTextArea iSc=new JTextArea("Casella inizio scala:");
        iSc.setEditable(false);
        iSc.setBounds(680,210,105,20);
        this.getContentPane().add(iSc);
        inizioScala =new JTextField("");
        inizioScala.setBounds(805,210,40,20);
        this.getContentPane().add(inizioScala);



        JTextArea fSc=new JTextArea("Casella fine scala:");
        fSc.setEditable(false);
        fSc.setBounds(860,210,100,20);
        this.getContentPane().add(fSc);
        fineScala =new JTextField("");
        fineScala.setBounds(985,210,40,20);
        this.getContentPane().add(fineScala);

        inserisciScala=new JButton("Inserisci");
        inserisciScala.setBounds(1060,210,90,20);
        inserisciScala.addActionListener(listener);
        this.getContentPane().add(inserisciScala);

        JTextArea nb=new JTextArea("NB\nSe non diversamente indicato, verranno inseriti 5 serpenti e 5 scale generati automaticamente");
        nb.setEditable(false);
        nb.setBounds(680,240,1000,40);
        this.getContentPane().add(nb);

        JTextArea inG=new JTextArea("Inserisci giocatore:");
        inG.setEditable(false);
        inG.setBounds(680,320,100,20);
        this.getContentPane().add(inG);
        nomeGiocatore =new JTextField("nome");
        nomeGiocatore.setBounds(805,320,100,20);
        this.getContentPane().add(nomeGiocatore);


        inserisciGiocatore=new JButton("Inserisci");
        inserisciGiocatore.setBounds(1060,320,90,20);
        inserisciGiocatore.addActionListener(listener);
        this.getContentPane().add(inserisciGiocatore);

        D=new JCheckBox("D - Doppio 6 - Richiede 2 dadi");
        E=new JCheckBox("E - Caselle sosta");
        F=new JCheckBox("F - Caselle premio");
        G=new JCheckBox("G - Caselle \"pesca una carta\"");
        H=new JCheckBox("H - Ulteriori carte - Richiede  \"pesca una carta\"");
        C=new JCheckBox("C - Un solo dado - Richiede  2 dadi");
        D.setBounds(100,130,300,20);
        C.setBounds(100,100,300,20);
        E.setBounds(100,160,300,20);
        F.setBounds(100,190,300,20);
        G.setBounds(100,220,300,20);
        H.setBounds(100,250,300,20);
        this.getContentPane().add(D);
        this.getContentPane().add(E);
        this.getContentPane().add(F);
        this.getContentPane().add(G);
        this.getContentPane().add(H);
        this.getContentPane().add(C);

        uno=new JRadioButton("1 Dado");
        dadi.add(uno);
        uno.setBounds(100,320, 100,20);
        this.getContentPane().add(uno);

        due=new JRadioButton("2 Dadi");
        dadi.add(due);
        due.setSelected(true);
        due.setBounds(200,320, 100,20);
        this.getContentPane().add(due);

        automatico=new JButton("Avvia Simulazione");
        automatico.setBounds(400,500,180,20);
        automatico.addActionListener(listener);
        this.getContentPane().add(automatico);

        manuale=new JButton("Avvia Gioco Manuale");
        manuale.setBounds(600,500,180,20);
        manuale.addActionListener(listener);
        this.getContentPane().add(manuale);

        salva=new JButton("Salva Configurazione");
        salva.setBounds(400,550,180,20);
        salva.addActionListener(listener);
        this.getContentPane().add(salva);

        carica=new JButton("Carica Configurazione");
        carica.setBounds(600,550,180,20);
        carica.addActionListener(listener);
        this.getContentPane().add(carica);

        JTextArea prova=new JTextArea("");
        prova.setBounds(700,500,100,50);
        this.getContentPane().add(prova);

        this.validate();
    }

    class Listener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==inserisciSerpente){
                int testaSerpente=Integer.parseInt(fineSerpente.getText());
                int codaSerpente=Integer.parseInt(inizioSerpente.getText());
                int nCaselleTot=Integer.parseInt(nRighe.getText())*Integer.parseInt(nColonne.getText());
                if(testaSerpente>=nCaselleTot)
                    JOptionPane.showMessageDialog(null,"Attenzione! La testa è maggiore del numero di caselle assegnabili");
                else if(testaSerpente<=codaSerpente){
                    JOptionPane.showMessageDialog(null,"Attenzione! La testa deve essere maggiore della coda");
                }
                else if(speciali.contains(testaSerpente) || speciali.contains(codaSerpente)){
                    JOptionPane.showMessageDialog(null,"Attenzione! Le caselle devono essere libere");
                }
                else{
                    serpenti.add(new Serpente(codaSerpente,testaSerpente));
                    speciali.add(testaSerpente);
                    speciali.add(codaSerpente);
                    JOptionPane.showMessageDialog(null,"Inserito serpente "+testaSerpente+" - "+codaSerpente);
                }
            }
            else if(e.getSource()==inserisciScala){
                int inScala=Integer.parseInt(inizioScala.getText());
                int finScala=Integer.parseInt(fineScala.getText());
                if(finScala>Integer.parseInt(nRighe.getText())*Integer.parseInt(nColonne.getText()))
                    JOptionPane.showMessageDialog(null,"Attenzione! La fine della scala è maggiore del numero di caselle");
                else if(finScala<=inScala){
                    JOptionPane.showMessageDialog(null,"Attenzione! Fine deve essere maggiore dell'inizio");
                }
                else if(speciali.contains(inScala) || speciali.contains(finScala)){
                    JOptionPane.showMessageDialog(null,"Attenzione! Le caselle devono essere libere");
                }
                else{
                    scale.add(new Scala(inScala,finScala));
                    speciali.add(finScala);
                    speciali.add(inScala);
                    JOptionPane.showMessageDialog(null,"Inserita scala "+inScala+" - "+finScala);
                }
            }
            else if(e.getSource()==inserisciGiocatore){
                if(giocatori.contains(new Giocatore(nomeGiocatore.getText()))){
                    JOptionPane.showMessageDialog(null,"Nome già in uso, prova ancora");
                }
                else {
                    giocatori.add(new Giocatore(nomeGiocatore.getText()));
                    JOptionPane.showMessageDialog(null,"Inserito "+nomeGiocatore.getText());

                }
            }
            else if(e.getSource()==automatico) {
                int nCaselleTot=Integer.parseInt(nRighe.getText())*Integer.parseInt(nColonne.getText());
                if(nCaselleTot<20 && serpenti.isEmpty() && scale.isEmpty() ) {
                    JOptionPane.showMessageDialog(null, "Attenzione! Per giocare su questa matrice usare scale e serpenti personalizzati");
                    return;
                }
                else if(nCaselleTot!=100) {
                    int nCaselleSpeciali = 1; //include casella 100
                    if (E.isSelected())
                        nCaselleSpeciali += 3;
                    if (F.isSelected())
                        nCaselleSpeciali += 3;
                    if (G.isSelected())
                        nCaselleSpeciali += 3;
                    if (serpenti.isEmpty() && scale.isEmpty())
                        nCaselleSpeciali += 20;
                    else {
                        nCaselleSpeciali += speciali.size();
                    }
                    if (nCaselleTot <= nCaselleSpeciali) {
                        JOptionPane.showMessageDialog(null, "Attenzione! Matrice troppo piccola");
                        return;
                    }
                }

                    if (giocatori.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Inserire almeno un giocatore");
                } else if ((C.isSelected() || D.isSelected()) && uno.isSelected()) {
                    JOptionPane.showMessageDialog(null, "Sono state selezionate regole incompatibili, riprovare");
                } else if (H.isSelected() && !G.isSelected()) {
                    JOptionPane.showMessageDialog(null, "Per usare la regola H è richiesta la regola G");
                }else
                 {
                     int num=Integer.parseInt(nRighe.getText())*Integer.parseInt(nColonne.getText());
                     for(Serpente s:serpenti) {
                        if(s.getFine()>num) {
                            JOptionPane.showMessageDialog(null, "Attenzione, serpenti non validi. Reinserire scale e serpenti");
                            serpenti=new ArrayList<>();
                            serpenti=new ArrayList<>();
                            speciali=new ArrayList<>();
                            return;
                        }
                     }
                     for(Scala s:scale) {
                         if(s.getFine()>num) {
                             JOptionPane.showMessageDialog(null, "Attenzione, scale non valide. Reinserire scale e serpenti");
                             scale=new ArrayList<>();
                             serpenti=new ArrayList<>();
                             speciali=new ArrayList<>();
                             return;
                         }
                     }

                    if (C.isSelected()) {
                        regole.add(Regole.C);
                    }
                    if (D.isSelected()) {
                        regole.add(Regole.D);
                    }
                    if (E.isSelected()) {
                        regole.add(Regole.E);
                    }
                    if (F.isSelected()) {
                        regole.add(Regole.F);
                    }
                    if (G.isSelected()) {
                        regole.add(Regole.G);
                    }
                    if (H.isSelected()) {
                        regole.add(Regole.H);
                    }
                    new SingletonRegole(regole);

                    new SingletonGiocatoriList(giocatori);
                    int nDadi = uno.isSelected() ? 1 : 2;
                    if (nDadi == 1)
                        regole.add(Regole.B);
                    Gioco gioco = new Gioco(Integer.parseInt(nRighe.getText()),
                            Integer.parseInt(nColonne.getText()),
                            scale, serpenti);
                    gioco.initScacchiera();
                    new SS(Gioco.getScacchiera().getCaselle(), gioco, false);
                    giocatori=new ArrayList<>();
                     scale=new ArrayList<>();
                     serpenti=new ArrayList<>();
                     regole=new ArrayList<>();
                     SingletonDadi.reset();
                     SingletonMazzo.reset();
                }
            }
            else if(e.getSource()==manuale){
                    int nCaselleTot=Integer.parseInt(nRighe.getText())*Integer.parseInt(nColonne.getText());
                    if(nCaselleTot<20 && serpenti.isEmpty() && scale.isEmpty() ) {
                        JOptionPane.showMessageDialog(null, "Attenzione! Per giocare su questa matrice usare scale e serpenti personalizzati");
                        return;
                    }
                    else if(nCaselleTot!=100) {
                        int nCaselleSpeciali = 1; //include casella 100
                        if (E.isSelected())
                            nCaselleSpeciali += 3;
                        if (F.isSelected())
                            nCaselleSpeciali += 3;
                        if (G.isSelected())
                            nCaselleSpeciali += 3;
                        if (serpenti.isEmpty() && scale.isEmpty())
                            nCaselleSpeciali += 20;
                        else {
                            nCaselleSpeciali += speciali.size();
                        }
                        if (nCaselleTot <= nCaselleSpeciali) {
                            JOptionPane.showMessageDialog(null, "Attenzione! Matrice troppo piccola");
                            return;
                        }
                    }
                    if(giocatori.isEmpty()){
                        JOptionPane.showMessageDialog(null,"Inserire almeno un giocatore");
                    }
                    else if((C.isSelected() || D.isSelected()) && uno.isSelected()){
                        JOptionPane.showMessageDialog(null,"Sono state selezionate regole incompatibili, riprovare");
                    }
                    else if(H.isSelected() && !G.isSelected()){
                        JOptionPane.showMessageDialog(null,"Per usare la regola H è richiesta la regola G");
                    }
                    else{

                        int num=Integer.parseInt(nRighe.getText())*Integer.parseInt(nColonne.getText());
                        for(Serpente s:serpenti) {
                            if(s.getFine()>num) {
                                JOptionPane.showMessageDialog(null, "Attenzione, serpenti non validi. Reinserire scale e serpenti");
                                serpenti=new ArrayList<>();
                                scale=new ArrayList<>();
                                speciali=new ArrayList<>();
                                return;
                            }
                        }
                        for(Scala s:scale) {
                            if(s.getFine()>num) {
                                JOptionPane.showMessageDialog(null, "Attenzione, scale non valide. Reinserire scale e serpenti");
                                scale=new ArrayList<>();
                                serpenti=new ArrayList<>();
                                speciali=new ArrayList<>();
                                return;
                            }
                        }

                        if(C.isSelected()){
                            regole.add(Regole.C);
                        }
                        if(D.isSelected()){
                            regole.add(Regole.D);
                        }
                        if(E.isSelected()){
                            regole.add(Regole.E);
                        }
                        if(F.isSelected()){
                            regole.add(Regole.F);
                        }
                        if(G.isSelected()){
                            regole.add(Regole.G);
                        }
                        if(H.isSelected()){
                            regole.add(Regole.H);
                        }
                        new SingletonRegole(regole);

                        new SingletonGiocatoriList(giocatori);
                        int nDadi= uno.isSelected()? 1:2;
                        if(nDadi==1)
                            regole.add(Regole.B);
                        Gioco gioco=new Gioco(Integer.parseInt(nRighe.getText()),
                                Integer.parseInt(nColonne.getText()),
                                scale,serpenti);
                        gioco.initScacchiera();
                        new SS(Gioco.getScacchiera().getCaselle(),gioco,true);
                        giocatori=new ArrayList<>();
                        scale=new ArrayList<>();
                        serpenti=new ArrayList<>();
                        regole=new ArrayList<>();
                        SingletonDadi.reset();
                        SingletonMazzo.reset();
                    }

                }
            else if(e.getSource()==carica){
                    JFileChooser chooser=new JFileChooser();
                    try{
                        if( chooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION ){
                            if( !chooser.getSelectedFile().exists() ){
                                JOptionPane.showMessageDialog(null,"File inesistente!");
                            }
                            else{
                                fileSalvataggio=chooser.getSelectedFile();
                                setTitle("Scale e Serpenti"+" - "+fileSalvataggio.getName());
                                try{
                                    ripristina( fileSalvataggio.getAbsolutePath() );
                                }catch(IOException ioe){
                                    JOptionPane.showMessageDialog(null,"Ops! C'è stato un problema");
                                }
                            }
                        }
                        else
                            JOptionPane.showMessageDialog(null,"Nessuna apertura!");
                    }catch( Exception exc ){
                        exc.printStackTrace();
                    }
               }
            else if(e.getSource()==salva){ save();
            }
        }
    }
}
