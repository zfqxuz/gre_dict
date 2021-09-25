package com.test.GDAS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.graphics.drawable.ColorDrawable;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class details extends AppCompatActivity{
    private ArrayAdapter<String> mAdapter;
    private ListView detail;
    private TextToSpeech textToSpeech;
    private TextView word;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        SpeechUtils speechUtils=new SpeechUtils(this);
        Intent intent=getIntent();
        int type=intent.getIntExtra("type",0);//0是单词详情，1是exp详情
        Button prev=findViewById(R.id.button4);
        Button next=findViewById(R.id.button5);
        TextView title=findViewById(R.id.textView6);
        word=findViewById(R.id.textView4);
        detail=findViewById(R.id.detail);

        int list_id=intent.getIntExtra("listid",0);
        String ttl="List "+String.valueOf(list_id);
        title.setText(ttl);
        String[] kwd=intent.getStringArrayExtra("keyword");
        String KWD=intent.getStringExtra("query_Exp");
        int index=intent.getIntExtra("index",0);
        if (list_id==-1){
            prev.setVisibility(View.GONE);
            next.setVisibility(View.GONE);
            title.setVisibility(View.GONE);
        }
        if(type==0){
            word.setText(kwd[index]);
            word.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String text=word.getText().toString();
                    speechUtils.speakText(text);
                }
            });
        }else{
            word.setText(KWD);
            prev.setVisibility(View.GONE);
            next.setVisibility(View.GONE);
        }

        DAO dao=new DAO(this);

        String [][] exps=type==0?dao.Query(new String[]{kwd[index],"word"}):dao.Query(new String[]{KWD,"exp"});
        String [] tmp=new String[exps.length+1];
        int count=1;
        int marked=0;
        for (String [] i:exps) {
            i[2]= i[2].equals("") ?"暂无":i[2];
            String j;
            if (type==0){
                j= String.format("解释%d:%s\n助记:%s", count, i[1], i[2]);
            }else{
                j= String.format("单词%d:%s\n助记:%s", count, i[0], i[2]);
            }
            marked=Integer.parseInt(i[3]);
            tmp[count]=j;
            count++;
        }
        tmp[0]="共标记"+marked+"次";
        Log.d("details",Arrays.toString(tmp));
        mAdapter=new ArrayAdapter<String>(this,R.layout.sampl1,tmp);
        detail.setAdapter(mAdapter);
        detail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int j, long l) {
//                detailDialog dlog=new detailDialog(details.this,exps[i][1],exps[i][2]);
//                dlog.show();
                AlertDialog.Builder adBUILDER=new AlertDialog.Builder(details.this);
                View dialogView= LayoutInflater.from(details.this).inflate(R.layout.sampl2,null);
                int i=j-1;
                final EditText oldexp=dialogView.findViewById(R.id.Oexp);
                final EditText oldaid=dialogView.findViewById(R.id.Oaid);
                Log.d("dbg",Arrays.toString(exps[0]));
                oldaid.setText(exps[i][2]);
                oldaid.setEnabled(true);
                oldexp.setText(exps[i][1]);
                adBUILDER.setTitle("选择您的操作");
                Button refexpbtn = dialogView.findViewById(R.id.renew_exp);
                Button refaidbtn = dialogView.findViewById(R.id.renew_aid);
                Button same=dialogView.findViewById(R.id.same_exp);
                if (type==1){
                    same.setVisibility(View.GONE);
                    refaidbtn.setVisibility(View.GONE);
                    refexpbtn.setVisibility(View.GONE);
                }

                Log.d("TYPE",String.valueOf(refaidbtn ==null));
                refaidbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("TYPE",Arrays.toString(exps[i]));
                        Intent j=new Intent(details.this,ATY1.class);
                        j.putExtra("current_word",exps[i][0]);
                        j.putExtra("current_exp",exps[i][1]);
                        startActivity(j);

                    }
                });
                refexpbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("TYPE",Arrays.toString(exps[i]));
                        Intent j=new Intent(details.this,ATY1.class);
                        j.putExtra("current_word",exps[i][0]);
                        j.putExtra("current_aid",exps[i][2]);
                        startActivity(j);
                    }
                });
                Log.d("same_mean",String.valueOf(type));
                same.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent1=new Intent(details.this,details.class);
                        intent1.putExtra("type",1);
                        intent1.putExtra("query_Exp",exps[i][1]);
                        startActivity(intent1);
                    }
                });

                adBUILDER.setView(dialogView);
                AlertDialog ad=adBUILDER.create();
                ad.show();


            }
        });


        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(index==0){
                    Toast.makeText(details.this,"*不*",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent itt=new Intent(details.this,details.class);
                    itt.putExtra("keyword",kwd);
                    itt.putExtra("index",index-1);
                    itt.putExtra("type",0);
                    itt.putExtra("listid",list_id);
                    details.this.finish();
                    startActivity(itt);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(index==9){
                    Toast.makeText(details.this,"*不*",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent itt=new Intent(details.this,details.class);
                    itt.putExtra("keyword",kwd);
                    itt.putExtra("index",index+1);
                    itt.putExtra("type",0);
                    itt.putExtra("listid",list_id);
                    details.this.finish();
                    startActivity(itt);
                }
            }
        });
        Button ret=findViewById(R.id.ret);
        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button mark=findViewById(R.id.mark);
        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dao.Mark(kwd[index],null);
                Toast.makeText(details.this,"标记成功",Toast.LENGTH_SHORT).show();
            }
        });
        Button pass=findViewById(R.id.pass);
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dao.Shell("update "+Constants.TABLE_NAME+" set mark=-1 where word='"+kwd[index]+"'");
                Toast.makeText(details.this,"已经掌握了！",Toast.LENGTH_SHORT).show();
            }
        });

    }



//    private ArrayList<Map<String,Object>> getData(String[][] exps) {
//        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
//        Map<String, Object> map;
//        int count=1;
//        for (String [] i:exps) {
//            map = new HashMap<String, Object>();
//            String j="解释%d:%s\n助记:%s";
//            i[2]= i[2].equals("") ?"暂无":i[2];
//            j= String.format(j, count, i[1], i[2]);
//            map.put("sentence",j);
//            map.put("button","修改");
//            count++;
//        }
//        return data;
//


}