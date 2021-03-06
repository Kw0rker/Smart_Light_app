package com.example.student.smartlighttest1;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Settimgs extends AppCompatActivity implements Button.OnClickListener {
    Button DEL_SCEN;
    Button DEL_G;
    Button CHANGE;
    Button Refresh;
    EditText past_ard;
    EditText new_ard;
    EditText password_field;
    EditText off;
    EditText arduino_info;
    EditText init;
    EditText delete_ard;
    EditText delete_lamp;
    static boolean confirm;
    static boolean from_setings = false;
    static Context contexT;
    final static String password = "Smart Light";//uses to unlock dev mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settimgs);
        DEL_SCEN = findViewById(R.id.DEL_SCEN);
        DEL_G = findViewById(R.id.DEL_G);
        DEL_SCEN.setOnClickListener(this);
        DEL_G.setOnClickListener(this);
        past_ard = findViewById(R.id.past_ard);
        new_ard = findViewById(R.id.new_ard);
        CHANGE = findViewById(R.id.CHANGE);
        CHANGE.setOnClickListener(this);
        Refresh = findViewById(R.id.Refresh);
        Refresh.setOnClickListener(this);
        password_field = findViewById(R.id.password);
        off = findViewById(R.id.off);
        delete_lamp = findViewById(R.id.deleate_lamp);
        contexT = this.getApplicationContext();
        delete_lamp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("") && charSequence.toString().contains("\n")) {
                    new multithread().execute("send", "delete_l");
                    new multithread().execute("send", String.valueOf(charSequence));
                    delete_lamp.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        delete_ard = findViewById(R.id.deleate_ard1);
        delete_ard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("") && charSequence.toString().contains("\n")) {
                    new multithread().execute("send", "delete_a");
                    new multithread().execute("send", String.valueOf(charSequence));
                    delete_ard.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        init = findViewById(R.id.arduino_init);
        init.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("") && charSequence.toString().contains("\n")) {
                    new multithread().execute("send", "init");
                    new multithread().execute("send", String.valueOf(charSequence));
                    init.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
        arduino_info = findViewById(R.id.arduino_info);
        arduino_info.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("") && charSequence.toString().contains("\n")) {
                    new multithread().execute("send", "arduino_info");
                    new multithread().execute("send", String.valueOf(charSequence));
                    arduino_info.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        off.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("") && charSequence.toString().contains("\n")) {
                    new multithread().execute("send", "off");
                    new multithread().execute("send", String.valueOf(charSequence));
                    off.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
        Button pass = findViewById(R.id.Pass);
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password_field.getText().toString().equals(password)) {
                    off.setVisibility(View.VISIBLE);
                    arduino_info.setVisibility(View.VISIBLE);
                    init.setVisibility(View.VISIBLE);
                    delete_ard.setVisibility(View.VISIBLE);
                    delete_lamp.setVisibility(View.VISIBLE);
                    findViewById(R.id.deleate_lamp_layout).setVisibility(View.VISIBLE);
                    findViewById(R.id.off_layout).setVisibility(View.VISIBLE);
                    findViewById(R.id.arduino_info_layout).setVisibility(View.VISIBLE);
                    findViewById(R.id.deleate_ard1_layout).setVisibility(View.VISIBLE);
                    findViewById(R.id.arduino_init_layout).setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void confirmation_alert(String measedge, Context context, final View v) {
        from_setings = false;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("ВНИМАНИЕ!!!");
        alertDialog.setMessage(measedge);
        alertDialog.setPositiveButton("ДА!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Settimgs.confirm = true;
                from_setings = true;
                v.performClick();
                return;
            }
        });
        alertDialog.setNegativeButton("НЕТ!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Settimgs.confirm = false;
                from_setings = false;
                return;
            }
        });
        alertDialog.show();
        return;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.DEL_SCEN:
                if (!from_setings) {
                    confirmation_alert("Вы действительно собираетесь удалить все  сценарии?", this, v);
                    break;
                }
                if (Settimgs.confirm) {
                    new multithread().execute("send", "delete_s");
                    new multithread().execute("send", "all");
                    file.writeToFile("", "scenarios.txt", MODE_PRIVATE);
                    from_setings = false;
                    Settimgs.confirm = false;
                }
                break;
            case R.id.DEL_G:
                if (!from_setings) {
                    confirmation_alert("Вы действительно собираетесь удалить все группы?", this, v);
                    break;
                }
                if (Settimgs.confirm) {
                    new multithread().execute("send", "delete_g");
                    new multithread().execute("send", "all");
                    from_setings = false;
                    Settimgs.confirm = false;

                    file.writeToFile("", "groups.txt", MODE_PRIVATE);
                }

                break;
            case R.id.CHANGE:
                if (past_ard.getText() != null && new_ard.getText() != null) {
                    if (!from_setings) {
                        confirmation_alert("Вы действительно собираетесь изменить принадлежность светильников?", this, v);
                        break;
                    }
                    if (Settimgs.confirm) {
                        String past_a, new_a;
                        past_a = "" + past_ard.getText();
                        new_a = "" + new_ard.getText();
                        if (past_ard.getText().length() == 1) past_a = "00" + past_ard.getText();
                        else if (past_ard.getText().length() == 2)
                            past_a = "0" + past_ard.getText();

                        if (new_ard.getText().length() == 1) new_a = "00" + new_ard.getText();
                        else if (new_ard.getText().length() == 2) new_a = "0" + new_ard.getText();

                        new multithread().execute("send", "rename");
                        from_setings = false;
                        Settimgs.confirm = false;

                        new multithread().execute("send", past_a + " " + new_a);
                    }
                }

                break;
            case R.id.Refresh:
                if (!from_setings) {
                    confirmation_alert("Вы действительно собираетесь запустить процесс обновления базы данных? Это может занять некоторое время", this, v);
                    break;
                }
                if (Settimgs.confirm) {
                    new multithread().execute("send", "refresh");
                    from_setings = false;
                    Settimgs.confirm = false;
                }
                break;
        }

    }

}







