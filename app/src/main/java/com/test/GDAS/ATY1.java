package com.test.GDAS;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.Arrays;

public class ATY1 extends AppCompatActivity {
    private AutoCompleteTextView IPT2;
    private EditText IPT1,IPT3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aty1);
        Button button=findViewById(R.id.button);
        Button button2=findViewById(R.id.button2);
        IPT2=findViewById(R.id.exp);
        IPT1=findViewById(R.id.word);
        IPT3=findViewById(R.id.aid);
        Intent intent=getIntent();
        String current_word=intent.getStringExtra("current_word");
        String current_exp=intent.getStringExtra("current_exp");
        String current_aid=intent.getStringExtra("current_aid");
        if (current_word != null){
            current_aid=current_aid==null? "":current_aid;
            current_exp=current_exp==null? "":current_exp;
            Log.d("jumper",current_aid);
            Log.d("jumper",current_exp);
            Log.d("jumper",current_word);
            IPT1.setText(current_word);
            IPT2.setText(current_exp);
            IPT3.setText(current_aid);
            button2.setVisibility(View.GONE);
            button.setText("更新");
        }
        String finalCurrent_exp = current_exp;
        String finalCurrent_aid = current_aid;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText IPT1=findViewById(R.id.word);
                String input=IPT1.getText().toString();
                EditText IPT2=findViewById(R.id.exp);
                String input2=IPT2.getText().toString();
                EditText IPT3=findViewById(R.id.aid);
                String input3=IPT3.getText().toString();
                String your_inputs="添加成功！\n单词："+input+"，解释："+input2+"，助记："+input3;
                Toast.makeText(ATY1.this,your_inputs,Toast.LENGTH_SHORT).show();
                DAO dao=new DAO(ATY1.this);
                String[]  a = new String[]{input,input2,input3};
                if (current_word==null) {
                    dao.insert(a);
                } else if(finalCurrent_aid.equals("")) {

                    dao.Update(a,1, finalCurrent_exp);
                }else{
                    dao.Update(a,0,finalCurrent_aid);
                    finish();
                }

                for (EditText ET:new EditText[]{IPT1,IPT2,IPT3}) {
                    ET.setText("");
                }

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input=IPT1.getText().toString();
                String input2=IPT2.getText().toString();
                String input3=IPT3.getText().toString();
                String your_inputs="添加成功！\n单词："+input+"，解释："+input2+"，助记："+input3;
                Toast.makeText(ATY1.this,your_inputs,Toast.LENGTH_SHORT).show();
                DAO dao=new DAO(ATY1.this);
                String[]  a = new String[]{input,input2,input3};
                dao.insert(a);
                for (EditText ET:new EditText[]{IPT2,IPT3}) {
                    ET.setText("");
                }
            }
        });
        Button button3=findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ATY1.this.finish();
            }
        });




        DAO dao=new DAO(this);
//        EditText EXP=findViewById(R.id.exp);
        this.IPT2.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("inputs",IPT2.getText().toString());
                String key=IPT2.getText().toString();
                String [] res=dao.query_keyword(key,1);
                Log.d("QRESUT",Arrays.toString(res));
                ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(ATY1.this,android.R.layout.simple_list_item_1,res);
                ATY1.this.IPT2.setAdapter(arrayAdapter);
            }
        });



    }
}