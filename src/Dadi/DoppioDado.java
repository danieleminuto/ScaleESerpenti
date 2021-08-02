package Dadi;

import java.util.Random;


public class DoppioDado implements Dadi {

    @Override
    public int[] tiraIDadi() {
        int[] ret=new int[2];
        ret[0]=new Random().nextInt(6)+1;
        ret[1]=new Random().nextInt(6)+1;
        return ret;
    }
}
