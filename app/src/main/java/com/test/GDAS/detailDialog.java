package com.test.GDAS;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class detailDialog extends AlertDialog implements View.OnClickListener {
    Context mcontext;
    EditText oldexp,oldaid;
    Button ren_exp,ren_aid,same;
    String OEXP,OAID;
    protected detailDialog(@NonNull Context context,String oexp,String oaid) {
        super(context);
        mcontext=context;
        OEXP=oexp;
        OAID=oaid;
    }

    @Override
    public void onClick(View view)  {
        switch (view.getId()){
            case R.id.renew_exp:
                Log.d("gdas",oldexp.getText().toString());
                break;
            case R.id.renew_aid:
                Log.d("gdas",oldaid.getText().toString());

                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sampl2);
        oldexp=findViewById(R.id.Oexp);
       // oldexp.setHint(OEXP);
        oldaid=findViewById(R.id.Oaid);
//        oldaid.setHint(OAID);
        oldaid.setEnabled(true);
        oldexp.setEnabled(true);
        oldexp.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        setCancelable(true);
        ren_exp=findViewById(R.id.renew_exp);
        ren_aid=findViewById(R.id.renew_aid);
        ren_aid.setOnClickListener(this);
        ren_exp.setOnClickListener(this);
    }
}