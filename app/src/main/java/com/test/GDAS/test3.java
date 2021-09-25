package com.test.GDAS;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

public class test3 extends AppCompatActivity {
    private  int[] words_id;
    private int[] exps_id;
    private ToggleButton[] word_toggles;
    private ToggleButton[] exp_toggles;
    private String[] wordlist,explist;
    private final DAO dao=new DAO(this);
    Bitmap b;
    Canvas canvas;
    Dictionary dict=new Hashtable();
    Dictionary match=new Hashtable();
    private static String current_color;

    private int last_triggered_id;//id不是真的id，0-9是word，10-19是exp,-1是之前没有；
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test3);
        words_id=new int[]{R.id.wd0,R.id.wd1,R.id.wd2,R.id.wd3,R.id.wd4,R.id.wd5,R.id.wd6,R.id.wd7,R.id.wd8,R.id.wd9};
        exps_id=new int[]{R.id.e0,R.id.e1,R.id.e2,R.id.e3,R.id.e4,R.id.e5,R.id.e6,R.id.e7,R.id.e8,R.id.e9};
        word_toggles=new ToggleButton[10];
        exp_toggles=new ToggleButton[10];
        Intent intent=getIntent();
        wordlist=intent.getStringArrayExtra("wordlist");
        explist=new String[10];
        Random random=new Random();
        String word,exp;
        String[] CS=new String[]{"#0f5ff","#7ccd7c","#ffd700","#ff4500","#ffa500","#cd853f","#228b22","#6a5acd","#bfefff","#ff83fa","#ffe4e1"};
        Stack<String> Colors=new Stack<String>();
        for (String c:CS) {
            Colors.push(c);
        }

        last_triggered_id=-114514;
        for (int i = 0; i < 10; i++) {
            word=wordlist[i];
            String[] exps=dao.query_keyword(wordlist[i],2 );
            int exp_ind=random.nextInt(exps.length);
            exp=exps[exp_ind];
            explist[i]=exp;
            dict.put(word, exp);
        }
        wordlist=random_array(wordlist);
        explist=random_array(explist);



        for (int i = 0; i < 10; i++) {
            ToggleButton wtg=findViewById(words_id[i]);
            ToggleButton etg=findViewById(exps_id[i]);
            word=wordlist[i];
            wtg.setText(word);
            wtg.setTextOn(word);
            wtg.setTextOff(word);
            exp=explist[i];
            etg.setText(exp);
            etg.setTextOn(exp);
            etg.setTextOff(exp);
            wtg.setBackgroundColor(Color.WHITE);
            etg.setBackgroundColor(Color.WHITE);
            word_toggles[i]=wtg;
            exp_toggles[i]=etg;
            int finalI = i;
            wtg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("before", String.valueOf(Colors.size()));
                      String this_word=wtg.getText().toString();
                      if(wtg.isChecked()){
                          if (last_triggered_id<0){
                                last_triggered_id=finalI;
                                 current_color =Colors.pop();
                                 wtg.setBackgroundColor(Color.parseColor(current_color));

                          }else if(last_triggered_id>=10){
                              wtg.setBackgroundColor(Color.parseColor(current_color));
                              String choosen_Exp=exp_toggles[last_triggered_id-10].getText().toString();

                              Boolean correct=dict.get(this_word)==choosen_Exp;
                              Object[] match_array=new Object[]{finalI,last_triggered_id,correct,current_color};
                              match.put(this_word,match_array);
                              match.put(choosen_Exp,match_array);
                              last_triggered_id=-114514;
                          }else{
                              ToggleButton last_choosen=word_toggles[last_triggered_id];
                              last_choosen.setBackgroundColor(Color.WHITE);
                              last_choosen.setChecked(false);
                              wtg.setBackgroundColor(Color.parseColor(current_color));
                              last_triggered_id=finalI;
                          }
                      }else{
                          Log.d("yant", String.valueOf(last_triggered_id));
                          Log.d("yant", String.valueOf(finalI));
                          if (last_triggered_id==finalI){
                              Colors.push(current_color);
                              wtg.setBackgroundColor(Color.WHITE);
                              last_triggered_id=-114514;
                          }else{
                              if(last_triggered_id>=0){
                                ToggleButton last_choosen=last_triggered_id<10?word_toggles[last_triggered_id]:exp_toggles[last_triggered_id-10];
                                last_choosen.setChecked(false);
                                last_choosen.setBackgroundColor(Color.WHITE);
                                Colors.push(current_color);
                              }
                              Object[] match_pairs= (Object[]) match.get(this_word);
                              int choosen_Exp= (int) match_pairs[1];
                              String used_color= (String) match_pairs[3];
                              match.remove(this_word);
                              match.remove(choosen_Exp);
                              exp_toggles[choosen_Exp-10].setBackgroundColor(Color.WHITE);
                              exp_toggles[choosen_Exp-10].setChecked(false);
                              wtg.setChecked(false);
                              wtg.setBackgroundColor(Color.WHITE);
                              last_triggered_id=-114514;
                              Colors.push(used_color);


                          }
                      }

                    Log.d("after", String.valueOf(Colors.size()));

            }});
            etg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("BEFORE", String.valueOf(Colors.size()));
                    String this_exp=etg.getText().toString();
                    if(etg.isChecked()){
                        if (last_triggered_id<0){
                            last_triggered_id=finalI+10;
                            current_color =Colors.pop();
                            etg.setBackgroundColor(Color.parseColor(current_color));

                        }else if(last_triggered_id<10){
                            etg.setBackgroundColor(Color.parseColor(current_color));
                            String choosen_word=word_toggles[last_triggered_id].getText().toString();

                            Boolean correct=dict.get(choosen_word)==this_exp;
                            Object[] match_array=new Object[]{last_triggered_id,finalI+10,correct,current_color};
                            match.put(choosen_word,match_array);
                            match.put(this_exp,match_array);
                            last_triggered_id=-114514;
                        }else{
                            ToggleButton last_choosen=exp_toggles[last_triggered_id-10];
                            last_choosen.setBackgroundColor(Color.WHITE);
                            last_choosen.setChecked(false);
                            etg.setBackgroundColor(Color.parseColor(current_color));
                            last_triggered_id=finalI+10;
                        }
                    }else{
                        Log.d("yant", String.valueOf(last_triggered_id));
                        Log.d("yant", String.valueOf(finalI));
                        if (last_triggered_id==finalI+10){
                            Colors.push(current_color);
                            etg.setBackgroundColor(Color.WHITE);
                            last_triggered_id=-114514;
                        }else{
                            if(last_triggered_id>=0){
                                ToggleButton last_choosen=last_triggered_id<10?word_toggles[last_triggered_id]:exp_toggles[last_triggered_id-10];
                                last_choosen.setChecked(false);
                                last_choosen.setBackgroundColor(Color.WHITE);
                                Colors.push(current_color);
                            }
                            Object[] match_pairs= (Object[]) match.get(this_exp);
                            int choosen_word= (int) match_pairs[0];
                            String used_color= (String) match_pairs[3];
                            match.remove(this_exp);
                            match.remove(choosen_word);
                            word_toggles[choosen_word].setBackgroundColor(Color.WHITE);
                            word_toggles[choosen_word].setChecked(false);
                            etg.setChecked(false);
                            etg.setBackgroundColor(Color.WHITE);
                            last_triggered_id=-114514;
                            Colors.push(used_color);
                        }
                    }
                    Log.d("AFTER", String.valueOf(Colors.size()));
                }
            });
            Button confirm=findViewById(R.id.submit_area);
            confirm.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Object[] o;
                            ToggleButton wdb,expb;
                            int correct=0,wrong=0,miss=0;
                            for (String wd: wordlist) {
                                try {
                                    o=(Object[]) match.get(wd);
                                    wdb=word_toggles[(int)o[0]];
                                    expb=exp_toggles[(int)o[1]-10];
                                    if((boolean)o[2]){
                                        wdb.setBackgroundColor(Color.GREEN);
                                        expb.setBackgroundColor(Color.GREEN);
                                        correct++;
                                    }else{
                                        wdb.setBackgroundColor(Color.RED);
                                        expb.setBackgroundColor(Color.RED);
                                        wrong++;
                                    }
                                    wdb.setChecked(false);
                                    expb.setChecked(false);

                                }catch (Exception e){
                                    Log.e("errs",e.toString());
                                    miss++;
                                }

                            }
                            for (int ai=0;ai<word_toggles.length;ai++) {
                                ToggleButton tb=word_toggles[ai];
                                if(tb.isChecked()){
                                    tb.setChecked(false);
                                    tb.setBackgroundColor(Color.GRAY);
                                }
                                int finalAi = ai;
                                tb.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent1=new Intent(test3.this,details.class);
                                        intent1.putExtra("type",0);
                                        intent1.putExtra("listid",-1);
                                        intent1.putExtra("keyword",wordlist);
                                        intent1.putExtra("index", finalAi);
                                        startActivity(intent1);
                                    }
                                });

                            }
                            for (int be=0;be<exp_toggles.length;be++) {
                                ToggleButton tb=exp_toggles[be];
                                if(tb.isChecked()){
                                    tb.setChecked(false);
                                    tb.setBackgroundColor(Color.GRAY);
                                }
                                int finalBe = be;
                                tb.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent1=new Intent(test3.this,details.class);
                                        intent1.putExtra("type",1);
                                        intent1.putExtra("listid",-1);
                                        intent1.putExtra("query_Exp",tb.getText());
                                        intent1.putExtra("index", finalBe);
                                        startActivity(intent1);
                                    }
                                });

                            }
                            AlertDialog.Builder adb=new AlertDialog.Builder(test3.this);
                            adb.setTitle("结果");
                            adb.setMessage("答对"+correct+"个，答错"+wrong+"个，未回答"+miss+"个\n点击每个单词可以查看详细解释");
                            adb.create().show();


                        }
                    }
            );
        }
        Button ret=findViewById(R.id.return_area);
        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private String[] random_array(String[] a){
        Integer[] indexs=new Integer[a.length];
        Set<Integer> set=new HashSet<Integer>(Arrays.asList(indexs));
        int local_count=0;
        Random random=new Random();
        int local_index=random.nextInt(a.length);
        while ((local_count<a.length)){
            Log.d("AGGL",String.valueOf(local_index));
            if(!set.contains(local_index)) {
                indexs[local_count] = local_index;
                set.add(local_index);
                local_count++;
            }
            local_index = random.nextInt(a.length);
        }
        Log.d("AGGL",Arrays.toString(indexs));
        local_count=0;
        String[] result=new String[a.length];
        for (int i:indexs) {
            result[local_count]=a[i];
            local_count++;
        }
        return result;
    }

}

