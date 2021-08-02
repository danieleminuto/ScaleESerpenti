package Dadi;

import Regole.Regole;
import Regole.SingletonRegole;

public class SingletonDadi{


    private static Dadi DADI=null;
    private SingletonDadi(){

    }

    public static synchronized Dadi getInstance(){
        if(DADI==null){
            DADI= SingletonRegole.getInstance().contains(Regole.B)? new SingoloDado(): new DoppioDado();
        }
        return DADI;
    }

    public static void reset(){ DADI=null;}

}
