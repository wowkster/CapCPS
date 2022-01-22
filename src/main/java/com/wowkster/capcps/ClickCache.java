package com.wowkster.capcps;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ClickCache {
    private final HashSet<Long> clicks = new HashSet<>();

    public void increment() {
        clicks.add(System.currentTimeMillis());
    }

    public int getCPS() {
        int acc = 0;

        long currentTIme = System.currentTimeMillis();

        List<Long> rem = new ArrayList<>();

        for (long click : clicks) {
            if (currentTIme - click < 1000) acc++;
            else rem.add(click);
        }

        for (long click : rem)
            clicks.remove(click);

        return acc;
    }
}
