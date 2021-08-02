package Dadi;

import java.util.Random;

public class SingoloDado implements Dadi{

    @Override
    public int[] tiraIDadi() {
        int[] ret=new int[1];
        ret[0]=new Random().nextInt(6)+1;
        return ret;
    }
}
