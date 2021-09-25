package com.test.GDAS;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Arrays;
import java.util.Locale;

public class test5 extends AppCompatActivity {
    int count=0;
    boolean marked=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test5);
        Intent intent=getIntent();
        String[] wordlist=intent.getStringArrayExtra("wordlist");
        ToggleButton tb=findViewById(R.id.toggleButton3);
        TextView counter=findViewById(R.id.counter);
        DAO dao=new DAO(this);

        tb.setText(wordlist[count]);
        tb.setTextOff(wordlist[count]);
        String[] exps=dao.query_keyword(tb.getText().toString(),2);
        tb.setTextOn(Arrays.toString(exps));
        counter.setText("第1个/共10个");
        Button next=findViewById(R.id.N);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count<9){
                    count++;
                    marked=false;
                    tb.setText(wordlist[count].toLowerCase(Locale.ROOT));
                    tb.setTextOff(wordlist[count].toLowerCase(Locale.ROOT));
                    tb.setChecked(false);
                    String[] exps=dao.query_keyword(tb.getText().toString(),2);
                    tb.setTextOn(Arrays.toString(exps));
                    counter.setText(new StringBuilder().append("第").append(count + 1).append("个，共10个").toString());
                }
                else {
                    Toast.makeText(test5.this,"*你够了*",Toast.LENGTH_SHORT).show();
                }


            }
        });
        Button mark=findViewById(R.id.M);
        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!marked){
                    dao.Mark(wordlist[count],null);
                    Toast.makeText(test5.this,"标记成功",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(test5.this,"已经标记过了",Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
}