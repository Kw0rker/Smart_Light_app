package com.example.student.smartlighttest1;

import android.util.Log;

import java.util.ArrayList;

public class scenario {

    static void set_new(String brg) {
        String[] brighness = brg.split(",");
        ArrayList<String> messadge = new ArrayList<>();
        int counter = 0;
        for (int lamp_n = 2; lamp_n < udp.colvo; lamp_n++) {
            try {
                int a = Integer.parseInt(brighness[lamp_n]);
                if (a >= 0) {
                    Log.d("udp", "" + a);
                    counter++;
                    messadge.add(lamp_n + "," + a * 2.55);
                }
            } catch (NumberFormatException e) {
                Log.e("scenario", e.getMessage());
            }
        }
        new multithread().execute("send", "new");
        new multithread().execute("send", ("" + counter));
        for (String mes : messadge) {
            new multithread().execute("send", mes);
        }

    }
}