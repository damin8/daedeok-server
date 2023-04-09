package hours22.daedeokserver.common.service;

import java.util.Random;

public class RandMaker {
    public static String generateKey(boolean lowerCheck, int size) {
        Random random = new Random();
        StringBuffer ranKey = new StringBuffer();
        int num;
        boolean flag = false;
        do {
            num = random.nextInt(75) + 48;
            if (num <= 57 || num >= 65 && num <= 90 || num >= 97) {
                if (num <= 57)
                    flag = true;

                if(ranKey.length() + 1 == size && !flag)
                    continue;

                ranKey.append((char) num);
            }
        } while (ranKey.length() < size);

        if (lowerCheck) {
            return ranKey.toString().toLowerCase();
        }
        return ranKey.toString();
    }
}
