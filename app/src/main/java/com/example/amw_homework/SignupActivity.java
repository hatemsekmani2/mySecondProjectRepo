package com.example.amw_homework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.prefs.Preferences;
import java.util.prefs.PreferencesFactory;

public class SignupActivity extends AppCompatActivity {
    EditText num , name,type,cat,pdate,rdate;
    CheckBox iscross;
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day,year2,month2,day2;
    static SharedPreferences p;
    private boolean f = false;

    public static boolean hasPermissions(Context context, String... permissions)
    {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null)
        {
            for (String permission : permissions)
            {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                {
                    return false;
                }
            }
        }
        return true;
    }
    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        context = SignupActivity.this;
        p = getSharedPreferences("edit",Context.MODE_PRIVATE);



        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        year2 = calendar.get(Calendar.YEAR);
        month2 = calendar.get(Calendar.MONTH);
        day2 = calendar.get(Calendar.DAY_OF_MONTH);



        if(p.getString("edit","name")!=null){
            //  startActivity(new Intent(Login.this,MainActivity.class));
            //finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        findViewById(R.id.progressBar_signup).setVisibility(View.INVISIBLE);
        String[] per = {Manifest.permission.INTERNET};
        if(!hasPermissions(this,per)){
            ActivityCompat.requestPermissions(this, per, 1);

        }
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        Spannable spannablerTitle = new SpannableString("Violations App");
        spannablerTitle.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")), 0, spannablerTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        actionBar.setTitle(spannablerTitle);
        actionBar.setElevation(0);
        actionBar.setDisplayHomeAsUpEnabled(true);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;


        num = findViewById(R.id.num_signup);
        num.setInputType(InputType.TYPE_CLASS_NUMBER);
        name = findViewById(R.id.name_signup);
        type = findViewById(R.id.type_signup);
        cat = findViewById(R.id.category_signup);
        pdate = findViewById(R.id.pdate_signup);
        rdate = findViewById(R.id.rdate_signup);
        iscross = findViewById(R.id.iscross_signup);
        num.getLayoutParams().height=(height/15);
        name.getLayoutParams().height=(height/15);
        findViewById(R.id.type_signup).getLayoutParams().height=(height/15);
        findViewById(R.id.category_signup).getLayoutParams().height=(height/15);
        findViewById(R.id.pdate_signup).getLayoutParams().height=(height/15);
        findViewById(R.id.rdate_signup).getLayoutParams().height=(height/15);
        findViewById(R.id.iscross_signup).getLayoutParams().height=(height/15);


        ImageView imagemain = findViewById(R.id.imagesignup);
        findViewById(R.id.space1_signup).getLayoutParams().height=(height/15-40);
        findViewById(R.id.space2_signup).getLayoutParams().height=(height/20);
        findViewById(R.id.space3_signup).getLayoutParams().height=(height/20);
        findViewById(R.id.space4_signup).getLayoutParams().height=(height/20);
        findViewById(R.id.space5_signup).getLayoutParams().height=(height/20);
        findViewById(R.id.space6_signup).getLayoutParams().height=(height/20);
        findViewById(R.id.space7_signup).getLayoutParams().height=(height/20);
        findViewById(R.id.space8_signup).getLayoutParams().height=(height/10);
        imagemain.getLayoutParams().width = 2*width/3;
        imagemain.setScaleType(ImageView.ScaleType.FIT_CENTER);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void save(String s, Context context, Context context2){

        if(p==null){
            System.out.println("nulllll");
        }

        String name = s.split("-")[1];
        String num = s.split("-")[2];
        SharedPreferences.Editor e = p.edit();
        e.putString("name","");
        e.putString("number","");
        e.apply();
        // startActivity(new Intent(context2,MainActivity.class));
        f = true;

    }

    public void signup1(View view) {
        findViewById(R.id.progressBar_signup).setVisibility(View.VISIBLE);
        String iscros = "0";
        if(iscross.isChecked()){
            iscros = "1";
        }
        if(
                !name.getText().toString().equals("") && !num.getText().toString().equals("") && !type.getText().toString().equals("") && !cat.getText().toString().equals("") && !pdate.getText().toString().equals("") && !rdate.getText().toString().equals("")
        )
        new DoHttpCall2(new DoHttpCall2.Navigate() {
            @Override
            public void onTaskComplete(String result) {
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }}).execute(name.getText().toString(),num.getText().toString(),type.getText(),cat.getText(),pdate.getText(),rdate.getText(),iscros,this,findViewById(R.id.progressBar_signup),findViewById(R.id.space_loading),SignupActivity.this);
       else{
           Toast.makeText(context,"Missing Data",Toast.LENGTH_LONG).show();
           findViewById(R.id.progressBar_signup).setVisibility(View.INVISIBLE);
        }

    }

    public void Signup(View view) {
        startActivity(new Intent(SignupActivity.this,SignupActivity.class));
    }


    public void showdialoge1(View view) {
        showDialog(1);
    }
    public void showdialoge2(View view) {
        showDialog(2);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 1) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        if (id == 2) {
            return new DatePickerDialog(this,
                    myDateListener2, year2, month2, day2);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }


            };
    private DatePickerDialog.OnDateSetListener myDateListener2 = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate2(arg1, arg2+1, arg3);
                }


            };

    private void showDate(int year, int month, int day) {
        pdate.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }
    private void showDate2(int year, int month, int day) {
        rdate.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }



}
class DoHttpCall2 extends AsyncTask<Object, Void, String> {

    public interface Navigate{
        void onTaskComplete(String result);
    }

    private Navigate mNavigate;

    public DoHttpCall2(Navigate n){
        mNavigate = n;

    }


    RelativeLayout dialog;
    Space space;
    Context context;
    Context context2;

    @Override
    protected String doInBackground(Object... strings) {
        String login_name = strings[0].toString();
        String login_num = strings[1].toString();
        String type = strings[2].toString();
        String cat = strings[3].toString();
        String pdate = strings[4].toString();
        String rdate = strings[5].toString();
        String isCross = strings[6].toString();
        context = (Context) strings[7];
        dialog = (RelativeLayout) strings[8];
        space = (Space) strings[9];
        context2 = (Context) strings[10];
        String result="";

        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL("https://hatemwebsite.000webhostapp.com/AWM/Signup.php?name="+login_name+"&number="+login_num+"&type="+type+"&category="+cat+"&production_date="+pdate+"&registeration_date="+rdate+"&isCrossout="+isCross);
            System.out.println(url.toString());

            urlConnection = (HttpURLConnection) url
                    .openConnection();

            InputStream in = urlConnection.getInputStream();

            InputStreamReader isw = new InputStreamReader(in);

            int data = isw.read();
            while (data != -1) {
                result+= (char)data;
                data = isw.read();

            }
            result+="-"+login_name+"-"+login_num;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return result;


    }

    @Override
    protected void onPostExecute(String s) {
        dialog.setVisibility(View.INVISIBLE);



        System.out.println("HHHHHHHH"+s);
        if(s.contains("successfully")){
            Toast.makeText(context,"Welcome "+s.split("-")[1],Toast.LENGTH_LONG).show();

            new Login().save(s,context,context2);
            mNavigate.onTaskComplete(s);

        }
        else
            Toast.makeText(context,s,Toast.LENGTH_LONG).show();
        super.onPostExecute(s);
    }
}