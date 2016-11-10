package com.example.administrator.mysignin;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.provider.Browser;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends Activity {
    Button button_menu,button_quiet,next,back;//菜单、退出按钮
    PopupWindow popupWindow;
    TextView textView_1,textView_2,textView_3,textView_4,textView_5,text_name,text_id,text_class,stu_1,stu_2,stu_3,stu_4,stu_5;
    ListView listView;
    EditText editText_1,editText_2,editText_3,editText_4,editText_5,editText_dianming;
    RadioGroup radio_1, radio_2,radio_3,radio_4,radio_5,radio_0;
    MyDataBaseHlper myDataBaseHlper=new MyDataBaseHlper(this,"Student.db",null,1);
    List<Map<String,Object>> studentList= new ArrayList<Map<String,Object>>();
    ImageView imageView;
    int number=1;int number_1=0,number_2=0,number_3=0,number_4=0,number_5=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        createDataBase();
        fileToDataBase();
        input_photo();
        ListViewMethod();
        TitleMethod();
    }
    //Title方法
    public void TitleMethod()
    {
        menu();
        quiet();
    }
    //菜单选项
    public void menu()
    {
        button_menu=(Button)findViewById(R.id.menu);
        button_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popinit();
            }
        });
    }
    //初始化ICS下拉菜单
    protected void popinit() {
        // TODO Auto-generated method stub
        LayoutInflater inflater = LayoutInflater.from(this);
        View pop_view = inflater.inflate(R.layout.menu_layout, null);
        textView_1=(TextView) pop_view.findViewById(R.id.menu_1);
        textView_2=(TextView) pop_view.findViewById(R.id.menu_2);
        textView_4=(TextView) pop_view.findViewById(R.id.menu_4);
        textView_5=(TextView) pop_view.findViewById(R.id.menu_5);

        //设置下拉按钮父布局的高和宽
        popupWindow = new PopupWindow(pop_view, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);//没这行按钮下拉了，按手机返回会直接退出
//		popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new PaintDrawable());//没这行按钮下拉了不会消失
        popupWindow.showAsDropDown(button_menu);//设置下拉按钮在button下显示

        textView_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss(); //销毁popupwindow，没这个再返回下拉的按钮不会消失
                final AlertDialog alertDialog=new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("导入数据：");
                if(studentList==null)
                alertDialog.setMessage("确认导入？");
                if(studentList!=null)
                    alertDialog.setMessage("已有数据，是否覆盖？");
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        studentList.removeAll(studentList);
                        ListViewMethod();
                    }
                });
                alertDialog.show();
            }
        });
        textView_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                LinearLayout linearLayout=(LinearLayout)getLayoutInflater().inflate(R.layout.add_layout,null);
                final AlertDialog alertDialog=new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("添加学生信息：");
                alertDialog.setView(linearLayout);
                editText_1=(EditText)linearLayout.findViewById(R.id.edittext_1);
                editText_2=(EditText)linearLayout.findViewById(R.id.edittext_2);
                editText_3=(EditText)linearLayout.findViewById(R.id.edittext_3);
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //添加数据操作
                        if(editText_1.getText().toString()==null||editText_2.getText().toString()==null||editText_3.getText().toString()==null)
                        {
                            Toast.makeText(MainActivity.this,"请输入完整信息！",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            SQLiteDatabase db = myDataBaseHlper.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put("student_id", editText_1.getText().toString());
                            values.put("student_name", editText_2.getText().toString());
                            values.put("student_class", editText_3.getText().toString());
                            db.insert("student_info", null, values);
                            values.clear();
                            values.put("student_id", editText_1.getText().toString());
                            db.insert("student_lab_score", null, values);
                            values.clear();
                            values.put("student_id", editText_1.getText().toString());
                            db.insert("student_usual_score", null, values);
                            values.clear();
                            values.put("student_id", editText_1.getText().toString());
                            db.insert("student_exam_score", null, values);
                            values.clear();
                            Toast.makeText(MainActivity.this, "添加数据成功！", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }
                    }
                });
                alertDialog.show();
            }
        });
        textView_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                LinearLayout linearLayout=(LinearLayout)getLayoutInflater().inflate(R.layout.check_layout,null);
                editText_4=(EditText)linearLayout.findViewById(R.id.edittext_4);
                editText_5=(EditText)linearLayout.findViewById(R.id.edittext_5);
                final AlertDialog alertDialog=new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("查询学生信息：");
                alertDialog.setView(linearLayout);
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(editText_4.getText().toString()==null&&editText_5.getText().toString()==null)
                            Toast.makeText(MainActivity.this,"查询条件不能为空！",Toast.LENGTH_SHORT).show();
                        else
                        {
                            //读取数据库信息，用对话框显示出对应信息
                            SQLiteDatabase db=myDataBaseHlper.getWritableDatabase();
                            Cursor cursor=null;
                            if(editText_4.getText().toString()!=null)
                            {
                                cursor=db.query(true,"student_info",new String[]{"student_id","student_name","student_class"},"student_id="+"\""+editText_4.getText().toString()+"\"",null,null,null,null,null,null);
                            }
                            if(editText_5.getText().toString()!=null)
                            {
                                cursor=db.query(true,"student_info",new String[]{"student_id","student_name","student_class"},"student_name="+"\""+editText_5.getText().toString()+"\"",null,null,null,null,null,null);
                            }
                            if(cursor==null) return;
                            cursor.moveToNext();
                            if(cursor.getCount()==0)
                            {
                                Toast.makeText(MainActivity.this,"不存在该学生信息",Toast.LENGTH_SHORT).show();
                            }else
                            {
                                final AlertDialog alertDialog=new AlertDialog.Builder(MainActivity.this).create();
                                alertDialog.setTitle("查询结果：");
                                alertDialog.setMessage("学号："+cursor.getString(0)+"\n"+"姓名："+cursor.getString(1)+"\n"+"班级："+cursor.getString(2)+"\n"+"成绩：0"+"\n");
                                Toast.makeText(MainActivity.this,"查找成功！",Toast.LENGTH_SHORT).show();
                                alertDialog.show();
                            }
                        }
                    }
                });
                alertDialog.show();
            }
        });

        textView_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                final LinearLayout linearLayout=(LinearLayout)getLayoutInflater().inflate(R.layout.student_info_layout,null);
                text_name=(TextView)linearLayout.findViewById(R.id.text_name);
                text_id=(TextView)linearLayout.findViewById(R.id.text_id);
                text_class=(TextView)linearLayout.findViewById(R.id.text_class);
                next=(Button)linearLayout.findViewById(R.id.next);
                back=(Button)linearLayout.findViewById(R.id.back);
                radio_0=(RadioGroup)linearLayout.findViewById(R.id.radio_0);
                stu_1=(TextView)linearLayout.findViewById(R.id.stu_1);
                stu_2=(TextView)linearLayout.findViewById(R.id.stu_2);
                stu_3=(TextView)linearLayout.findViewById(R.id.stu_3);
                stu_4=(TextView)linearLayout.findViewById(R.id.stu_4);
                stu_5=(TextView)linearLayout.findViewById(R.id.stu_5);
                imageView=(ImageView)linearLayout.findViewById(R.id.icon);
                editText_dianming=(EditText)linearLayout.findViewById(R.id.edittext_dianming);
                final String []id=new String[1200];
                final String []name=new String[1200];
                final String []cla=new String[1200];
                int count=0;
                final ContentValues values=new ContentValues();
                final AlertDialog alertDialog=new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("学生详细信息：");
                alertDialog.setView(linearLayout);
                final SQLiteDatabase db = myDataBaseHlper.getWritableDatabase();
                String sql="select student_id,student_name,student_class from student_info";
                final Cursor cursor=db.rawQuery(sql,null);
                while (cursor.moveToNext()&&count<120) {
                    id[count]=cursor.getString(cursor.getColumnIndex("student_id"));
                    name[count]=cursor.getString(cursor.getColumnIndex("student_name"));
                    cla[count]=cursor.getString(cursor.getColumnIndex("student_class"));
                    count++;
                }
                String sql2="select student_image from student_photos where student_id="+"\""+id[0]+"\"";
                final Cursor cursor2=db.rawQuery(sql2,null);
                cursor2.moveToFirst();
                byte[] in = cursor2.getBlob(cursor2.getColumnIndex("student_image"));
                Bitmap bmpout = BitmapFactory.decodeByteArray(in, 0, in.length);
                BitmapDrawable bd= new BitmapDrawable(getResources(), bmpout);
                imageView.setImageDrawable(bd);
                text_id.setText(id[0]);
                text_name.setText(name[0]);
                text_class.setText(cla[0]);
                radio_0.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        RadioButton r=(RadioButton)linearLayout.findViewById(i);
                        editText_dianming.setText(r.getText());
                    }
                });
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(editText_dianming.getText().toString()==null||editText_dianming.getText().length()==0)
                        {
                            Toast.makeText(MainActivity.this,"请评判学生到课情况！",Toast.LENGTH_SHORT).show();
                        }
                        else {
//                            if(editText_dianming.getText().toString().equals("请假")) {
//                                number_1++;stu_1.setText(number_1);
//
//                                values.put("student_qingjia",1);
//                            }
//                            if(editText_dianming.getText().toString().equals("出勤")) {
//                                number_2++;stu_2.setText(number_2);
//                                values.put("student_chuqin",1);
//                            }
//                            if(editText_dianming.getText().toString().equals("早退")) {
//                                number_3++;stu_3.setText(number_3);
//                                values.put("student_zaotui",1);
//                            }
//                            if(editText_dianming.getText().toString().equals("旷课")) {
//                                number_4++;stu_4.setText(number_4);
//                                values.put("student_kuangke",1);
//                            }
//                            if(editText_dianming.getText().toString().equals("迟到")) {
//                                number_5++;stu_5.setText(number_5);
//                                values.put("student_chidao",1);
//                            }
//                            values.put("student_id",id[number]);
//                            db.insert("student_usual_score",null,values);
                            String sql3="select student_image from student_photos where student_id="+"\""+id[number]+"\"";
                            final Cursor cursor3=db.rawQuery(sql3,null);
                            cursor3.moveToFirst();
                            byte[] in = cursor3.getBlob(cursor3.getColumnIndex("student_image"));
                            Bitmap bmpout = BitmapFactory.decodeByteArray(in, 0, in.length);
                            BitmapDrawable bd= new BitmapDrawable(getResources(), bmpout);
                            imageView.setImageDrawable(bd);
                            text_id.setText(id[number]);
                            text_name.setText(name[number]);
                            text_class.setText(cla[number]);
                            number++;
                        }
                    }
                });
                alertDialog.show();
            }
        });
    }
    //退出程序
    public void quiet()
    {
        button_quiet=(Button)findViewById(R.id.quiet);
        button_quiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    //创建数据库
    public void createDataBase()
    {
        myDataBaseHlper.getWritableDatabase();
    }
    //导入文件到数据库
    public void fileToDataBase()
    {
        InputStream inputStream=getResources().openRawResource(R.raw.student);
        InputStreamReader inputStreamReader;
        String str_temp;
        ContentValues values=new ContentValues();
        try {
            inputStreamReader=new InputStreamReader(inputStream,"UTF-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);
            while((str_temp=reader.readLine())!=null)
            {
                if(str_temp.length()==0)
                    continue;
                String []strings=str_temp.split(" ");

                if((int)(strings[0].charAt(0))==65279)
                {
                    strings[0]=strings[0].substring(1);
                }
                for(int i=0;i<strings.length;i++) {
                    if(i==0) {
                        values.put("student_id", strings[i]);
                    }
                    if(i==1)
                        values.put("student_name", strings[i]);
                    if(i==2)
                        values.put("student_class", strings[i]);
                }
                myDataBaseHlper.getWritableDatabase().insert("student_info",null,values);
                values.clear();
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        myDataBaseHlper.close();
    }
    //导入图片到数据库
    void input_photo(){
        Cursor get=myDataBaseHlper.getWritableDatabase().rawQuery("select student_id from student_info",null);
        Context ctx=getBaseContext();
        int photoId;
        String stu_id;
        String img_name;
        String insert;
        byte[] imagedata1;
        Bitmap bitmap1;
        ByteArrayOutputStream baos;
        while (get.moveToNext()) {
            stu_id=get.getString(get.getColumnIndex("student_id"));
            img_name="a"+stu_id;
            photoId = getResources().getIdentifier(img_name, "drawable" , ctx.getPackageName());
            if(photoId==0) photoId=R.drawable.background;
            bitmap1 = BitmapFactory.decodeResource(getResources(), photoId);
            int size = 480 * 640*4;
            baos = new ByteArrayOutputStream(size);
            //设置位图的压缩格式，质量为100%，并放入字节数组输出流中
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            //将字节数组输出流转化为字节数组byte[]
            imagedata1 = baos.toByteArray();
            insert = "insert into student_photos(student_id,student_image) values(?,?)";
            myDataBaseHlper.getWritableDatabase().execSQL(insert, new Object[]{stu_id,imagedata1});
            //关闭字节数组输出流
            baos.reset();
            bitmap1.recycle();
        }
    }
    //listview布局方法
    public void ListViewMethod()
    {
        SQLiteDatabase db=myDataBaseHlper.getWritableDatabase();
        String sql="select student_id,student_name,student_class from student_info";
        Cursor cursor=db.rawQuery(sql,null);
        listView = (ListView) findViewById(R.id.list_view);
        int listview_id=0;
        while(cursor.moveToNext()){
            listview_id++;
            String student_id = cursor.getString(cursor.getColumnIndex("student_id"));
            String student_name = cursor.getString(cursor.getColumnIndex("student_name"));
            String student_class = cursor.getString(cursor.getColumnIndex("student_class"));
            Map<String,Object> item = new HashMap<String,Object>();
            item.put("listview_id", listview_id+"");
            item.put("student_id", student_id);
            item.put("student_name", student_name);
            item.put("student_class",student_class);
            item.put("student_score","0");
            studentList.add(item);
        }
        //定义一个SimpleAdapter
        SimpleAdapter adapter= new SimpleAdapter(this, studentList, R.layout.student_layout,
                new String[]{"listview_id","student_id","student_name","student_class","student_score"},
                new int[]{R.id.listview_id,R.id.id_student,R.id.name_student,R.id.class_student,R.id.score_student});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }
}
