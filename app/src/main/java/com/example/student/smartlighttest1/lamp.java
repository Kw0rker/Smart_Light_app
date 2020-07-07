package com.example.student.smartlighttest1;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

public class lamp extends selectable implements View.OnClickListener, View.OnLongClickListener {
    static RotateAnimation rotate;
    final String[] IDS = new String[2];
    private View button;
    static Random random=new Random();
    private String ID;
    private int bright1, bright2;
    private boolean[] booleans = {false, false, false};
    static private int test = 0;
    static int numberOfLamps;


    int iterator = 2;

    public View getButton() {
        return button;
    }

    int getBright1() {
        return bright1;
    }

    public int getBright2() {
        return bright2;
    }

    void setBright2(int bright2) {
        this.bright2 = bright2;
    }

    void setBright1(int bright) {
        this.bright1=bright;
    }

    lamp(Button bt, String line) {
        //button = bt;
        if (line==null){
            IDS[0] = udp.id[test++];
            IDS[1] = udp.id[test++];
            ID=IDS[0]+","+IDS[1];

            try {
                bright1 = MainActivity.brightnessMap.get(IDS[0]);
                bright2 = MainActivity.brightnessMap.get(IDS[1]);
            }
            catch (NullPointerException e){file.writeLog(e.getLocalizedMessage());return;}
            int x = random.nextInt(1280);
            int y = random.nextInt(800);

                /*Animation animation=new RotateAnimation(90,90,x,y);
                animation.setFillAfter(true);
                TouchDelegate delegate =button.getTouchDelegate();
                animation.setDuration(0);
                button.setAnimation(animation);*/
            if (x - 17 >= 0) bt.setTranslationX(x - 17);
            else bt.setTranslationX(0);
            bt.setTranslationY(y);
            Animation animation = new RotateAnimation(90, 90, x, y);
            bt.setBackgroundColor(Color.TRANSPARENT);
            bt.setLayoutParams(new ConstraintLayout.LayoutParams(17, 37));
            button = new ImageView(bt.getContext());
            button.setTranslationX(x);
            button.setTranslationY(y);
            button.setLayoutParams(new ConstraintLayout.LayoutParams(37, 17));
            MainActivity.layout.addView(button);
            button.setTranslationY(y);
            button.setTranslationX(x);
            animation.setFillAfter(true);
            animation.setDuration(0);

            button.setAnimation(animation);
          Log.e("buttton"," created");

        }
        else {
        String[] params = line.split(" ");
        boolean turned = params[2].contains("1");
        String[] ids = params[0].split(",");
        String[] coordinates = params[1].split("#");
        IDS[0] = ids[0];
        IDS[1] = ids[1];
        ID=IDS[0]+","+IDS[1];
        int x=Integer.parseInt(coordinates[0]);
            try {
                bright1 = MainActivity.brightnessMap.get(ids[0]);
                bright2 = MainActivity.brightnessMap.get(ids[1]);
            } catch (NullPointerException e) {
                file.writeLog("incorrect id in config or no such id in status received\n id is:" + ids[0]);
                return;
            }
            int y = Integer.parseInt(coordinates[1]) - 18; //
        Log.e("button created with",line);
        if (turned) {
            bt.setTranslationX(x);
            bt.setTranslationY(y);
            Animation animation=new RotateAnimation(90,90,x,y);
            bt.setBackgroundColor(Color.TRANSPARENT);
            button = new ImageView(bt.getContext());
            MainActivity.layout.addView(button);
            button.setTranslationY(y);
            button.setTranslationX(x);
            bt.setLayoutParams(new ConstraintLayout.LayoutParams(17, 37));
            button.setLayoutParams(new ConstraintLayout.LayoutParams(37, 17));
            animation.setFillAfter(true);
            animation.setDuration(0);
            button.setTranslationY(y - 17);//change to the button position to   height because of turn
            button.setAnimation(animation);

        } else {
            button = bt;
            button.setTranslationX(x);
            button.setTranslationY(y);
        }
        }
        changeBackground();

        if (bright1<0||bright2<0){
            bt.setOnClickListener(view -> Toast.makeText(bt.getContext(), "Данный светильник отключен", Toast.LENGTH_SHORT).show());
            return;
        }
        MainActivity.lampMap.put(IDS[0], this);
        MainActivity.lampMap.put(IDS[1], this);
        bt.setOnClickListener(this);
        bt.setOnLongClickListener(this);
    }

    boolean[] getBooleans() {
        return booleans;
    }

    @Override
    public void onClick(View view) {
        Pair<selectable, Integer> pair = new Pair<>(this, bright1);
        switch (iterator) {
            case 0:
                if (!booleans[0]) MainActivity.selected.add(pair);
                else MainActivity.selected.remove(pair);
                booleans[0] = !booleans[0];
                changeBackground();
                break;
            case 1:
                if (!booleans[1]) MainActivity.selected.add(pair);
                else MainActivity.selected.remove(pair);
                booleans[1] = !booleans[1];
                changeBackground();
                break;

            case 2:
                if (!booleans[2]) {
                    MainActivity.selected.add(pair);
                } else {
                    MainActivity.selected.remove(pair);
                }
                booleans[2] = !booleans[2];
                changeBackground();

                break;
        }

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
        file.writeLog("lamp mode changed");
        MainActivity.vibrate(300 * (iterator+1));
        return false;
    }

    void changeBackground() {
        if (booleans[0]) {
            if (booleans[1]) {
                if (bright1 > 0) {
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
                if (bright1 > 0) {
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
                if (bright1 > 0) {
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
                if (bright1 > 0) {
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
       if (iterator==2) {
           if (booleans[2]) {
               if (bright1 > 0 && bright2 > 0) button.setBackgroundResource(R.drawable.both_selected);
               else button.setBackgroundResource(R.drawable.both_off_selected);
           } else {
               if (bright1>0&&bright2>0) button.setBackgroundResource(R.drawable.both_on);
              else button.setBackgroundResource(R.drawable.both_off);
           }
       }

    }

    @Override
    public String getId() {
        return ID;
    }


    @Override
    public int compareTo(selectable selectable) {
        try {
            if (selectable instanceof lamp) {
                lamp l = (lamp) selectable;
                return Integer.parseInt(IDS[0]) - Integer.parseInt(l.IDS[0]);

            } else {
                return Integer.parseInt(IDS[0]) - Integer.parseInt(selectable.getId());
            }
        } catch (NumberFormatException e) {
            file.writeLog(e.toString());
            return 0;
        }
    }
}

