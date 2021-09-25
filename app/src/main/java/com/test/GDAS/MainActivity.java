package com.test.GDAS;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private Button button,button2;
    private static final String DATABASE_PATH = "/data/data/com.test.GDAS/databases/";
    private static final String DATABASE_NAME = "words.db";
    DAO dao=new DAO(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences=getSharedPreferences("is",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        boolean isfer=sharedPreferences.getBoolean("isfer", true);
        if(isfer){
            AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
            View scrollView= LayoutInflater.from(MainActivity.this).inflate(R.layout.sampl4,null);
            adb.setTitle("开发者的话");
            String message;
            message="   本软件可能会影响Google服务框架完整性并造成部分基于Google框架的其他应用崩溃。如安装后其他软件发生闪退等问题，请尝试重装Google框架。\n    本软件完全本地化，没有任何联网功能，也不需要您提供任何权限。本软件不开源但不做代码混淆，您可自行反编译获取源代码。\n__________________________________\n";
            message+="更新日志：\n版本更新1.0.3:\n1.新增了shell功能，可以使用shell执行sql对数据库进行直接更改\n2.更新了导出模式，可以将数据库内容导出到本地存储/Android/data/com.test.GDAS/files下，文件名为’words.txt,可以直接进行本地导入‘\n3.现在可以分别学习不会的，没学过的和掌握的单词了\n4.据词选意的错误答案和正确答案做了突出显示\n__________________________________\n版本更新1.0.2:\n1.完成了词义对对碰（这玩意的逻辑复杂是这个应用程序之最，文档就写了1500+字）和抽词卡功能\n2.错误单词现在点击可以查看正确词义\n3.复杂模式默认关闭，从首页可以开启\n__________________________________\n";
            message+= "版本更新1.0.1:\n1.新增了发音功能，点击单词即可发音\n2.新增了数据库选择功能,现在可以根据个人需求新建数据库及切换数据库,不选的话,默认为第一个\n3.支持一键导入功能,可以导入用户个人的词典文件到数据库内。词典文件格式要求如下：\n";
            message+="\t●txt格式\n\t●每一行的格式都是’单词 词义 助记(可不填)‘,各项之间以空格分隔\n选择对应数据库之后点击选择文件，选择您的txt文件之后确认提交即可。\n4.左上角的按钮是个装饰";
            TextView Body=scrollView.findViewById(R.id.BODY);
            Body.setText(message);
            adb.setView(scrollView);
            adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            adb.create().show();
            editor.putBoolean("isfer",false);
            editor.putInt("endcolor",0xe52d27);
            editor.commit();
        }
        button=findViewById(R.id.btn1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,ATY1.class);
                startActivity(i);
            }
        });
        button2=findViewById(R.id.btn2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,ATY2.class);
                startActivity(i);
            }
        });
//        File fileDB = new File(DATABASE_PATH + DATABASE_NAME);
//        if(!fileDB.exists()) {
//            File file = new File(DATABASE_PATH);
//            if(!file.exists())
//                file.mkdirs();
//            try {
//                Context mContext=getApplicationContext();
//                InputStream is = mContext.getAssets().open(DATABASE_NAME);
//                OutputStream os = new FileOutputStream(DATABASE_PATH + DATABASE_NAME);
//
//                byte[] buffer = new byte[1024];
//                int len;
//                while((len = is.read(buffer)) > 0) {
//                    os.write(buffer, 0, len);
//                }
//                os.flush();
//                os.close();
//                is.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        Cursor c=dao.Shell("select * from sqlite_master where type='table'");
        String[] tables=new String[c.getCount()];
        Set<String> set=new HashSet<String>(Arrays.asList(tables));

        int local_count=0;
        while (c.moveToNext()){
            String tab=c.getString(1);
            tables[local_count]=tab;
            local_count++;
        }
        EditText editText=findViewById(R.id.inputer);
        Log.d("chdb", Arrays.toString(tables));
        ListView listView=findViewById(R.id.lv);
        ArrayAdapter<String> adpt=new ArrayAdapter<String>(MainActivity.this,R.layout.sampl1,tables);
        listView.setAdapter(adpt);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editText.setText(tables[i]);
            }
        });

        Button cfm=findViewById(R.id.button6);
        cfm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tablename=editText.getText().toString();

                Constants.TABLE_NAME=tablename;
                if (!set.contains(tablename)){
                    dao.CreateDB(tablename);
                    set.add(tablename);
                    Toast.makeText(MainActivity.this,"数据库"+tablename+"新建成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this,"数据库"+tablename+"切换成功",Toast.LENGTH_SHORT).show();
                }
                editText.setText("");
                Log.d("chdb",Constants.TABLE_NAME);

            }
        });
        EditText SQL=findViewById(R.id.SQL);
        Button Execute=findViewById(R.id.EXECUTE);
        Execute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sql=SQL.getText().toString();
                Log.d("shell",sql);
                dao.Shell(sql);
                SQL.setText("");
            }
        });

        Button btn_loadfile=findViewById(R.id.load_f);
        Button export=findViewById(R.id.export);
        btn_loadfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent,1);

            }
        });
        ToggleButton tb=findViewById(R.id.toggleButton);
        if(!tb.isChecked()){
            for (View o:new View[]{editText,listView,cfm,export,SQL,Execute}) {
                o.setVisibility(View.INVISIBLE);
            }
//            editText.setVisibility(View.INVISIBLE);
//            listView.setVisibility(View.INVISIBLE);
//            cfm.setVisibility(View.INVISIBLE);
//            export.setVisibility(View.INVISIBLE);
//            SQL.setVisibility();
        }
        tb.setText("复杂模式：关");
        tb.setTextOff("复杂模式：关");
        tb.setTextOn("复杂模式：开");
        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (View o:new View[]{editText,listView,cfm,export,SQL,Execute}) {
                    if(tb.isChecked()){o.setVisibility(View.VISIBLE);}
                    else{o.setVisibility(View.INVISIBLE);}

                }
//                    listView.setVisibility(View.VISIBLE);
//                    cfm.setVisibility(View.VISIBLE);
//                    editText.setVisibility(View.VISIBLE);
//                    export.setVisibility(View.VISIBLE);

            }
        });
        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQL.setText(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath());
                File txt_db=new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath()+ "/words.txt");
                List<String> lst= new ArrayList<>();
                for (String table:tables) {
                    if(table.equals("android_metadata")){continue;}
                    Constants.TABLE_NAME=table;
                    String[][] qlist=dao.Query(new String[]{"1","1"});
                    for (String[] itemrst:qlist) {
                        lst.add(itemrst[0] + " "+itemrst[1]+" " + itemrst[2]);
                    }
                }
                writeTxt(MainActivity.this,txt_db,lst);
            }
        });


    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                try {
                    InputStream in = getContentResolver().openInputStream(uri);
                    BufferedReader r = new BufferedReader(new InputStreamReader(in));
                    AlertDialog.Builder adBuilder = new AlertDialog.Builder(MainActivity.this);
                    ArrayList<String[]> list=new ArrayList<String[]>();
                    for (String line; (line = r.readLine()) != null; ) {
                        String [] tmp=line.split(" ");

                        list.add(tmp);

                    }
                    Set<String[]> set = new HashSet<String[]>(list);
                    adBuilder.setTitle("此操作会将" + String.valueOf(set.size()) + "条记录插入" + Constants.TABLE_NAME + "，且操作不可逆，确认添加吗？");
                    adBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            for (String[] j : set) {
                                Log.d("tst", Arrays.toString(j));
                                dao.insert(j);
                            }
                            Toast.makeText(MainActivity.this, "导入成功", Toast.LENGTH_SHORT).show();

                        }
                    });
                    adBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    adBuilder.create().show();


                } catch (Exception e) {

                }
            }
        }
    }





    private void writeTxt(Context context, File file, List<String> stringList) {
        try {
            if (!file.exists()) {
                file.createNewFile();//文件不存在时，创建文件
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");//随机流（RandomAccessFile）不属于IO流，支持对文件的读取和写入随机访问。
            raf.seek(file.length());//续写文件
            for (String content : stringList) {
                String Content = content + "\r\n";
                raf.write(Content.getBytes());
            }
            raf.close();
            MediaScannerConnection.scanFile(context, new String[]{file.getPath()}, null, null);
            Toast.makeText(MainActivity.this,"导出成功",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            EditText et=findViewById(R.id.SQL);
            et.setText(e.toString());

        }
    }


    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }



}