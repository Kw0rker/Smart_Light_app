package com.example.student.smartlighttest1;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Settimgs extends AppCompatActivity implements Button.OnClickListener{
    static Button DEL_SCEN;
    static Button DEL_G;
    static Button CHANGE;
    static EditText past_ard;
    static EditText new_ard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settimgs);
        DEL_SCEN= (Button)findViewById(R.id.DEL_SCEN);
        DEL_G = (Button)findViewById(R.id.DEL_G) ;
        DEL_SCEN.setOnClickListener(this);
        DEL_G.setOnClickListener(this);
        past_ard = (EditText) findViewById(R.id.past_ard);
        new_ard =(EditText) findViewById(R.id.new_ard);
        CHANGE =(Button)findViewById(R.id.CHANGE);
        CHANGE.setOnClickListener(this);



    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.DEL_SCEN:
                new multithread().execute("send","delete_s");
                new multithread().execute("send","all");
                getter_from_app.writeToFile("","scenarios.txt",MODE_PRIVATE);

                break;
            case R.id.DEL_G:
                new multithread().execute("send","delete_g");
                new multithread().execute("send","all");
                MainActivity.last_grup_n=0;
                getter_from_app.writeToFile("","groups.txt",MODE_PRIVATE);


                break;
            case R.id.CHANGE:
                if(past_ard.getText()!=null&&new_ard.getText()!=null){
                    String past_a,new_a;
                    past_a=""+past_ard.getText();
                    new_a=""+new_ard.getText();
                    if (past_ard.getText().length()==1)past_a="00"+past_ard.getText();
                        else if (past_ard.getText().length()==2)past_a="0"+past_ard.getText();

                    if (new_ard.getText().length()==1)new_a="00"+new_ard.getText();
                        else if (new_ard.getText().length()==2)new_a="0"+new_ard.getText();

                    new multithread().execute("send","rename");
                    new multithread().execute("send",past_a+" "+new_a);
        }
        }
    }

}







