package com.test.GDAS;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class ATY2 extends AppCompatActivity {

    private ArrayAdapter<String> mAdapter;
    //private Button button=findViewById(R.id.btn2);
    private ListView Unitlist;
    private int index;
    TextView unlearned,dont_know,learned;
    int type=0;
    DAO dao = new DAO(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aty2);
        this.Unitlist = findViewById(R.id.Ulist);
        unlearned = findViewById(R.id.unlearn);
        dont_know = findViewById(R.id.dontknow);
        learned = findViewById(R.id.learned);
        TextView[] btns = new TextView[]{unlearned, dont_know, learned};
        setter_page(0);
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            btns[i].setBackgroundColor(Color.GRAY);
            btns[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (finalI != type) {
                        btns[type].setBackgroundColor(Color.GRAY);
                        btns[finalI].setBackgroundColor(Color.YELLOW);
                        setter_page(finalI);
                        type=finalI;
                    }
                }
            });
            btns[0].setBackgroundColor(Color.YELLOW);
        }


    }

    private void setter_page(int cond){
        String kwd;
        switch (cond){
            case 0:
                kwd="mark=0";
                break;
            case 1:
                kwd="mark>0";
                break;
            case 2:
                kwd="mark<0";
                break;
            default:
                kwd="1=1";
                break;
        }
        String[] wordlist = dao.query_keyword(kwd, 0);
        String[] unitheader = new String[1 + (wordlist.length-1)/ 10];
        if(wordlist.length>0){
            for (int i = 0; i < unitheader.length; i++) {
                unitheader[i]=wordlist[10*i];
            }

        this.mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, unitheader);
        this.Unitlist.setAdapter(mAdapter);
        this.Unitlist.setVisibility(View.VISIBLE);
        this.Unitlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent j = new Intent(ATY2.this, AGGlearn.class);
                String[] words = new String[10];
                int limit = Math.min(wordlist.length - 10 * i, 10);
                System.arraycopy(wordlist, 10 * i, words, 0, limit);
                j.putExtra("words", words);
                j.putExtra("listnum", 10 * index + i);
                startActivity(j);
            }
        });
    }else{
            this.Unitlist.setVisibility(View.GONE);
        }
    }

}