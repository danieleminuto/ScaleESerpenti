package Scacchiera;

import java.util.Random;

public class Serpente {
    private int inizio;
    private int fine;

    public Serpente(int nCaselle){
        fine=new Random().nextInt(nCaselle-1)+1;
        while(fine==1) {
            fine = new Random().nextInt(nCaselle-1) + 1;
        }
        inizio = new Random().nextInt(fine)+1;
        while (inizio==fine){
            inizio = new Random().nextInt(fine)+1;
        }
    }

    public Serpente(int inizio, int fine){
        this.inizio=inizio; this.fine=fine;
    }

    public int getInizio(){ return inizio;}

    /** è la testa del serpente */
    public int getFine(){ return fine;}

}
