package com.test.GDAS;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class test1 extends AppCompatActivity {
    private int count=0;
    private DAO dao=new DAO(this);
    private Intent intent;

    private TextView TV1;
    private TextView TV2;
    private TextView TV3;
    private TextView TV4;
    private TextView WD;
    private String [] meanings,wordlist;
    private String truemening,word;
    private static boolean answered=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);
        TV1=findViewById(R.id.choose1);
        TV2=findViewById(R.id.choose2);
        TV3=findViewById(R.id.choose3);
        TV4=findViewById(R.id.choose4);

        WD=findViewById(R.id.word_to_display);
        intent=getIntent();
        wordlist=intent.getStringArrayExtra("wordlist");
        word=wordlist[count];
        WD.setText(word);
        do_alter();
        WD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("AGGL","KAWAATA~~~~");
                do_alter();
            }
        });

    }
    private void do_alter(){
        String[] exp=dao.query_keyword(word,2);
        Set<String> set=new HashSet<String>(Arrays.asList(exp));
        Random random=new Random();
        int truemeaning=random.nextInt(exp.length);
        truemening=exp[truemeaning];
        String[] Falsemeanings=dao.query_keyword("",1);
        meanings=new String[4];
        meanings[0]=truemening;
        Log.d("AGGL",Arrays.toString(exp));
        int ConfExps=0;
        String fmeaning=Falsemeanings[random.nextInt(Falsemeanings.length)];
        Log.d("AGGL",fmeaning);
        while (ConfExps<3){
            if(!set.contains(fmeaning)){
                ConfExps++;
                meanings[ConfExps]=fmeaning;
                set.add(fmeaning);
                fmeaning=Falsemeanings[random.nextInt(Falsemeanings.length)];
                Log.d("AGGL",String.valueOf(ConfExps)+fmeaning);
            }

        }

        meanings=random_array(meanings);
        Log.d("AGGL","hereyouare");
        int local_count=0;
        for (TextView tv:new TextView[]{TV1,TV2,TV3,TV4}) {
            tv.setText(meanings[local_count]);
            local_count++;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder adBUILDER=new AlertDialog.Builder(test1.this);
                    View dialogView= LayoutInflater.from(test1.this).inflate(R.layout.sampl3,null);
                    adBUILDER.setView(dialogView);
                    AlertDialog ad=adBUILDER.create();
                    ad.show();
                    TextView expl=dialogView.findViewById(R.id.textView7);
                    TextView correct=dialogView.findViewById(R.id.textView9);
                    TextView same=dialogView.findViewById(R.id.textView8);
                    Button mark=dialogView.findViewById(R.id.button7);
                    Button cont=dialogView.findViewById(R.id.next);
                    Button ret=dialogView.findViewById(R.id.back);
                    String current_exp=tv.getText().toString();
                    if (!answered) {
                        answered = true;
                        if (current_exp == truemening) {
                            correct.setText("正确");
                            correct.setBackgroundColor(Color.GREEN);

                        } else {
                            correct.setText("错误");
                            correct.setBackgroundColor(Color.RED);
                            correct.setTextColor(Color.WHITE);

                        }

                    }

                    expl.setText(wordlist[count]+":"+truemening);
                    expl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i=new Intent(test1.this,details.class);
                            i.putExtra("type",0);
                            i.putExtra("listid",-1);
                            i.putExtra("keyword",new String[]{wordlist[count]});
                            i.putExtra("index",0);
                            startActivity(i);
                        }
                    });
                    same.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(test1.this,"锐意开发中",Toast.LENGTH_SHORT).show();
                        }
                    });
                    String[] wl=dao.query_keyword(current_exp,3);
                    same.setText("近义词"+Arrays.toString(wl));
                    mark.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dao.Mark(wordlist[count],truemening);
                            Toast.makeText(test1.this,"标记成功",Toast.LENGTH_SHORT).show();
                        }
                    });
                    ret.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            for (TextView dv:new TextView[]{TV1,TV2,TV3,TV4}) {
                                if(!dv.getText().toString().equals(truemening)){
                                    dv.setBackgroundColor(Color.RED);
                                    dv.setTextColor(Color.WHITE);
                                }else{
                                    dv.setBackgroundColor(Color.GREEN);
                                }
                            }
                            ad.dismiss();
                        }
                    });
                    cont.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            answered=false;
                            Context context=getApplicationContext();
                            for (TextView dv:new TextView[]{TV1,TV2,TV3,TV4}) {
                                dv.setBackgroundColor(Color.WHITE);
                                dv.setTextColor(Color.BLACK);
                            }

                            if(count<9){
                                count++;
                                word=wordlist[count];
                                WD.setText(word);
                                ad.dismiss();
                            }else{
                                Log.d("AGGL",getApplicationContext().toString());
                                Log.d("AGGL",getBaseContext().toString());
                                Toast.makeText(context,"已经是最后一页了",Toast.LENGTH_SHORT).show();
                            }


                        }
                    });


                }


            });

        }

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