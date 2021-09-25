package com.test.GDAS;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AGGlearn extends AppCompatActivity {
    private  GridView gview;
    private SimpleAdapter wordlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agglearn);
        DAO dao=new DAO(this);
        SharedPreferences sharedPreferences=getSharedPreferences("is",MODE_PRIVATE);
        int Endcol=sharedPreferences.getInt("endcolor",0);
        int begincol=0x00ff00;
        int step=(Endcol-begincol)/9;
        Log.d("SETCOL", String.valueOf(step));
        gview=findViewById(R.id.gview);
        Intent intent=getIntent();
        String[] words=intent.getStringArrayExtra("words");
        Log.d("cratedb",Arrays.toString(words));
        int ct=0;
        for (int i = 0; i < 10; i++) {
            if (words[i]!=null){
                ct++;
            }
        }
        words=Arrays.copyOf(words,ct);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < words.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            String w=words[i];
            int freq= Integer.parseInt(dao.query_keyword(w,4)[0]);
            map.put("text",w);
            freq= Math.min(freq, 10);
            map.put("textColor",begincol+freq*step);
            map.put("textSize",20+0.5*freq);


            list.add(map);
        }
        Log.d("cratedb",Arrays.toString(words));
        int unit_num=intent.getIntExtra("listnum",0);
        wordlist=new SimpleAdapter(this,list,R.layout.sampl1,new String[]{"text","textColor","textSize"},new int[]{R.id.text1});

        gview.setAdapter(wordlist);


        TextView title=findViewById(R.id.textView5);
        String ttl="List "+String.valueOf(unit_num);
        title.setText(ttl);
        String[] finalWords = words;
        gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent j=new Intent(AGGlearn.this,details.class);
                j.putExtra("keyword", finalWords);
                j.putExtra("index",i);
                j.putExtra("type",0);
                j.putExtra("listid",unit_num);
                startActivity(j);
            }
        });
        Button Test1=findViewById(R.id.test1);
        String[] finalWords1 = words;
        Test1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AGGlearn.this,test1.class);
                i.putExtra("wordlist", finalWords1);
                startActivity(i);
            }
        });
        Button Test3=findViewById(R.id.test3);
        Test3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AGGlearn.this,test3.class);
                i.putExtra("wordlist",finalWords1);
                startActivity(i);
            }
        });
        Button Test5=findViewById(R.id.test5);
        Test5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AGGlearn.this,test5.class);
                i.putExtra("wordlist",finalWords1);
                startActivity(i);
            }
        });

    }
}