package Giocatori;

import java.util.Iterator;
import java.util.List;

public class GiocatoriList implements Iterable<Giocatore>{
    private Giocatore[] giocatori=new Giocatore[10];
    private int n=10;
    private int size=0;


    public GiocatoriList(List<Giocatore> giocatori){
        giocatori.toArray(this.giocatori);
        size=giocatori.size();

    }

    public int getSize() {
        return size;
    }

    public boolean add(Giocatore g){
        for(int i=0;i<10;i++){
            if (giocatori[i]==null) {
                giocatori[i] = g;
                size++;
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<Giocatore> iterator() {
        return new GiocatoriIterator();
    }

    /** iterator circolare fino ad un massimo di n=10 elementi */
    private class GiocatoriIterator implements Iterator<Giocatore>{
        private int corrente=-1;

        @Override
        public boolean hasNext() {
            return true;
        }



        @Override
        public Giocatore next() {
            if(corrente==n-1 || giocatori[corrente+1]==null){
                corrente=-1;
            }
            corrente++;
            return giocatori[corrente];
        }

        @Override
        public void remove() {}
    }
}
