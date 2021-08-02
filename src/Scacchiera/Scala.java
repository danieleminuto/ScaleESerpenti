package Scacchiera;

import java.util.Random;

public class Scala {
    private int inizio;
    private int fine;

    public Scala(int nCaselle){
        fine=new Random().nextInt(nCaselle-1)+1;
        while(fine==1) {
            fine = new Random().nextInt(nCaselle-1) + 1;
        }
        inizio = new Random().nextInt(fine)+1;
        while(inizio==fine){
            inizio = new Random().nextInt(fine)+1;
        }
    }

    public Scala(int inizio, int fine){
        this.inizio=inizio; this.fine=fine;
    }

    public int getInizio(){ return inizio;}
    public int getFine(){ return fine;}

}
