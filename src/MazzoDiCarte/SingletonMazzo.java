package MazzoDiCarte;

import Regole.*;

import java.util.Iterator;

public class SingletonMazzo implements Iterable{

    private static Carta[] mazzo=new Carta[40];
    private static SingletonMazzo singletonMazzo=null;
    private int corrente=-1;

    public SingletonMazzo(){};

    public static synchronized SingletonMazzo getInstance(){

        if(singletonMazzo==null) {
            singletonMazzo = new SingletonMazzo();
        }
        return singletonMazzo;
    }

    public static void reset(){ mazzo=new Carta[40];}

    @Override
    public Iterator<Carta> iterator() {
        return new MazzoIterator();
    }


    /**Iterator circolare. Simula il mettere alla fine del mazzo la carta*/
    private class MazzoIterator implements Iterator<Carta>{


        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public Carta next() {
            if(corrente== mazzo.length-1){
                corrente=-1;
            }
            corrente=corrente+1;
            if(mazzo[corrente]==null) {
                double num = Math.random();
                if (SingletonRegole.getInstance().contains(Regole.H)) {
                    if (num < 0.2)
                        mazzo[corrente] = Carta.PANCHINA;
                    else if (num < 0.4)
                        mazzo[corrente] = Carta.LOCANDA;
                    else if (num < 0.6)
                        mazzo[corrente] = Carta.DADI;
                    else if (num < 0.8)
                        mazzo[corrente] = Carta.MOLLA;
                    else
                        mazzo[corrente] = Carta.DIVIETO_DI_SOSTA;
                } else {
                    if (num < 0.25)
                        mazzo[corrente] = Carta.PANCHINA;
                    else if (num < 0.5)
                        mazzo[corrente] = Carta.LOCANDA;
                    else if (num < 0.75)
                        mazzo[corrente] = Carta.DADI;
                    else
                        mazzo[corrente] = Carta.MOLLA;
                }
            }
            return mazzo[corrente];
        }

        @Override
        public void remove(){}
    }
}



