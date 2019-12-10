package com.example.student.smartlighttest1;

import android.util.Log;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;

public class lamp implements View.OnClickListener, View.OnLongClickListener,selectable {
    //private boolean is_active;
    //private int firstLampMode;
    static RotateAnimation rotate;
    private final String[] IDS = new String[2];
    public Button button;

    private String ID;
    private int bright, bright2;
    private boolean[] booleans = {false, false, false};
    //private Resources privResurces;

    //    private String[] modes = {"FIRST", "SECOND", "BOTH"};
    public int iterator = 2;

    public int getBright1() {
        return bright;
    }

    public int getBright2() {
        return bright2;
    }

    public void setBright2(int bright2) {
        this.bright2 = bright2;
    }
    public void setBright(int bright){
        this.bright=bright;
    }

    lamp(Button bt, String line) throws RuntimeException {
        //if (rotate == null) {
         //   rotate = (RotateAnimation) AnimationUtils.loadAnimation(MainActivity.context_g, R.anim.rotate);
        //}
        String[] params = line.split(" ");
        boolean turned = params[2].contains("1");
        String[] ids = params[0].split(",");
        int id1, id2;
        id1 = Integer.parseInt(ids[0]);
        id2 = Integer.parseInt(ids[1]);
        String[] coordinates = params[1].split("#");
        bright = udp.brignes[id1-1];
        bright2 = udp.brignes[id2-1];
        IDS[0] = normId(id1);
        IDS[1] = normId(id2);
        ID=IDS[0]+","+IDS[1];
        button = bt;
        changeBackground();
        //if (turned) button.setAnimation(rotate);
        button.setTranslationX(Integer.parseInt(coordinates[0]));
        button.setTranslationY(Integer.parseInt(coordinates[1]));
        bt.setOnClickListener(this);
        bt.setOnLongClickListener(this);
    }

    private static String normId(int id_) {
        String ID;
        ID = "" + id_;
        if (id_ < 10) ID = "000" + id_;
        else if (id_ < 100) ID = "00" + id_;
        else if (id_ < 1000) ID = "0" + id_;
        return ID;
    }

    void set_priv_img() {
        changeBackground();
    }

    public boolean[] getBooleans() {
        return booleans;
    }

    @Override
    public void onClick(View view) {
        switch (iterator) {
            case 0:
                if (!booleans[0]) MainActivity.selected.add(this);
                else MainActivity.selected.remove(this);
                booleans[0] = !booleans[0];
                changeBackground();
                break;
            case 1:
                if (!booleans[1]) MainActivity.selected.add(this);
                else MainActivity.selected.remove(this);
                booleans[1] = !booleans[1];
                changeBackground();
                break;

            case 2:
                if (!booleans[2]) {
                    MainActivity.selected.add(this);
                } else {
                    MainActivity.selected.remove(this);
                }
                booleans[2] = !booleans[2];
                changeBackground();

                break;
        }
        //privResurces=button.getResources();
    }

    @Override
    public boolean onLongClick(View view) {
        iterator++;
        if (iterator == 3) iterator = 0;
        switch (iterator) {
            case 0:
                ID = IDS[0];
                break;
            case 1:
                ID = IDS[1];
                break;

            case 2:
                ID = IDS[0] + "," + IDS[1];
                break;
        }
        Log.d("iterator",String.valueOf(iterator));
        MainActivity.vibrate(300 * (iterator+1));
        return false;
    }

    public void changeBackground() {
        if (booleans[0]) {
            if (booleans[1]) {
                if (bright > 0) {
                    if (bright2 > 0) {
                        button.setBackgroundResource(R.drawable.both_selected);
                    } else {
                        button.setBackgroundResource(R.drawable.first_on_selected0second_onff_selected);
                    }
                } else {
                    if (bright2 > 0) {
                        button.setBackgroundResource(R.drawable.first_off_selected0second_on_selected);
                    } else {
                        button.setBackgroundResource(R.drawable.both_off_selected);

                    }
                }
            } else {
                if (bright > 0) {
                    if (bright2 > 0) {
                        button.setBackgroundResource(R.drawable.first_on_selected0second_on);

                    } else {
                        button.setBackgroundResource(R.drawable.first_on_selected);

                    }
                } else {
                    if (bright2 > 0) {
                        button.setBackgroundResource(R.drawable.first_off_selected0second_on);
                    } else {
                        button.setBackgroundResource(R.drawable.first_off_selected0second_off);

                    }
                }
            }
        } else {
            if (booleans[1]) {
                if (bright > 0) {
                    if (bright2 > 0) {
                        button.setBackgroundResource(R.drawable.first_on0second_onselected);
                    } else {
                        button.setBackgroundResource(R.drawable.first_on0second_off_selected);

                    }
                } else {
                    if (bright2 > 0) {
                        button.setBackgroundResource(R.drawable.second_on_selected);

                    } else {
                        button.setBackgroundResource(R.drawable.first_off0second_off_selected);
                    }
                }
            } else {
                if (bright > 0) {
                    if (bright2 > 0) {
                        button.setBackgroundResource(R.drawable.both_on);
                    } else {
                        button.setBackgroundResource(R.drawable.first_on0second_off);
                    }
                } else {
                    if (bright2 > 0) {
                        button.setBackgroundResource(R.drawable.firxt_off0second_on);
                    } else {
                        button.setBackgroundResource(R.drawable.both_off);
                    }
                }
            }
        }
       /* if (booleans[2]&&(!booleans[1]||booleans[0])){
            if (bright>0&&bright2>0)button.setBackgroundResource(R.drawable.both_selected);
            else button.setBackgroundResource(R.drawable.both_off_selected);

        }
        else if (!booleans[2]&&(!booleans[1]||booleans[0])) {
            if (bright>0&&bright2>0)button.setBackgroundResource(R.drawable.both_on);
        else button.setBackgroundResource(R.drawable.both_off);}*/
       if (iterator==2) {
           if (booleans[2]) {
               if (bright > 0 && bright2 > 0) button.setBackgroundResource(R.drawable.both_selected);
               else button.setBackgroundResource(R.drawable.both_off_selected);
           } else {
               if (bright>0&&bright2>0) button.setBackgroundResource(R.drawable.both_on);
              else button.setBackgroundResource(R.drawable.both_off);
           }
       }

    }

    @Override
    public String getId() {
        if (iterator == 3) throw new RuntimeException("Id var contains 2 ids");
        return ID;
    }
}

