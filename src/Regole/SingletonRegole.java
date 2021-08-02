package Regole;

import java.util.List;

public class SingletonRegole {

    private static List<Regole> REGOLE=null;

    public SingletonRegole(List<Regole> r){
        REGOLE=r;
    }

    public static synchronized List<Regole> getInstance(){
        return REGOLE;
    }

}
