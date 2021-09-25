package com.test.GDAS;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Arrays;

public class DAO {
    private final DatabaseHelper helper;

    public DAO(Context context){
        helper= new DatabaseHelper(context);
    }
    public void insert(String[] a){
        SQLiteDatabase db=helper.getWritableDatabase();
        String sql="insert into "+Constants.TABLE_NAME+"(word,exp,aid) values('%s','%s','%s')";
        String word=a[0];
        String exp=a[1];
        String aid= a.length>2? a[2]:"";
        sql= String.format(sql,word,exp,aid);

        try{
            System.out.println(sql);
            db.execSQL(sql);
        }catch (Exception e){
            Log.d("insert","dup");
        }
        db.close();
    }

    public void delete(String[] a){
        SQLiteDatabase db=helper.getWritableDatabase();
        String sql="insert into "+Constants.TABLE_NAME+"(word,exp,aid) values('%s','%s','%s')";
    }

    public String[][] Query(String[] a){
        SQLiteDatabase db=helper.getWritableDatabase();
        String sql="select * from "+Constants.TABLE_NAME;
        switch (a[1]){
            case "word":
                sql+=" where word= '"+a[0]+"'";
                break;
            case "exp":
                sql+=" where exp= '"+a[0]+"'";
        }
        sql+=" order by mark desc,word asc";

        Cursor cursor=db.rawQuery(sql,null);

        String[][] b=new String[cursor.getCount()][4];
        System.out.println(b.length);
        int count=0;
        while (cursor.moveToNext()){
            String word=cursor.getString(0);
            String exp=cursor.getString(1);
            String aid=cursor.getString(3);
            int marked=cursor.getInt(2);

            String [] tmp={word,exp,aid,String.valueOf(marked)};

            b[count]=tmp;
            count++;
        }

        return b;
    }

    public String[] query_keyword(String a, int type){
        SQLiteDatabase db=helper.getWritableDatabase();
        String sql;
        switch (type){
            case 0:
                sql="select distinct word from "+Constants.TABLE_NAME+" where "+a+" order by mark desc, word asc";
                break;
            case 1:
                sql="select distinct exp from "+Constants.TABLE_NAME+" where exp like '%"+a+"%' order by exp";
                break;
            case 2:
                sql="select distinct exp from "+Constants.TABLE_NAME+" where word='"+a+"' order by word";
                break;
            case 3:
                sql="select distinct word from "+Constants.TABLE_NAME+" where exp='"+a+"' order by word";
                break;
            case 4:
                sql="select sum(mark) from (select * from "+Constants.TABLE_NAME+" where word='"+a+"') a";
                break;
            default:
                sql="";
        }

        //String kv= type==1? "exp":"word";
//        String sql="SELECT distinct "+kv+" from "+Constants.TABLE_NAME;
//        if (type==1){
//            sql+=" where exp LIKE '%"+a+"%'";
//        }
//        else if(type==2){
//            sql+=" where word='"+a+"'";
//        }
//        sql+=" order by "+kv;
        Log.d("db",sql);
        Cursor cursor=db.rawQuery(sql,null);
        String[] b;
        switch (type){
            case 4:
                int ct=0;
                while (cursor.moveToNext()){
                    ct+=cursor.getInt(0);

                }
                b=new String[]{String.valueOf(ct)};
                break;
            default:

                b= new String[cursor.getCount()];
                int count=0;
                while (cursor.moveToNext()){
                    String exp=cursor.getString(0);
                    b[count]=exp;
                    count++;
                }
        }
        return b;

    }

    public void Mark(String word,String exp){
        SQLiteDatabase db=helper.getWritableDatabase();
        String sql;
        if (exp==null){
            sql="update "+Constants.TABLE_NAME+" set mark=mark+1 where word='"+word+"'";
        }else{
            sql="update "+Constants.TABLE_NAME+" set mark=mark+1 where word='"+word+"' and exp='"+exp+"'";
        }

        db.execSQL(sql);

    }

    public void CreateDB(String dbname){
        SQLiteDatabase db=helper.getWritableDatabase();
        String sql="create table "+dbname+" (word varchar,exp varchar,mark int default 0,aid varchar default '',primary key (word,exp))";
        db.execSQL(sql);
        sql="insert into "+dbname+" (word,exp,aid) values ('Congratulations','恭喜','您看见当前页面，表明数据库创建成功，请手动添加或导入已有数据')";
        db.execSQL(sql);
    }

    public void Update(String[] params, int type, String addition){
        Log.d("DB",Arrays.toString(params));
        SQLiteDatabase db=helper.getWritableDatabase();
        String sql="update "+Constants.TABLE_NAME;
        if(type==1){
            sql+= " set aid='"+params[2]+"' where word='"+params[0]+"' and exp='"+addition+"'";
        }else{
            sql+=" set exp='"+params[1]+"' where word='"+params[0]+"' and exp='"+addition+"'";
        }
        Log.d("DB",sql);
        db.execSQL(sql);
    }

    public Cursor Shell(String sql){
        SQLiteDatabase db=helper.getWritableDatabase();
        Log.d("shell",sql);
        if(sql.charAt(0)=='s'){
            return db.rawQuery(sql,null);
        }else{
            db.execSQL(sql);
            return null;
        }

    }





}
