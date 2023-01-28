package com.example.amw_homework;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends AppCompatActivity {
    EditText num , name;
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

        context = Login.this;
        p = getSharedPreferences("edit",Context.MODE_PRIVATE);
        if(p.getString("edit","name")!=null){
          //  startActivity(new Intent(Login.this,MainActivity.class));
            //finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.progressBar_login).setVisibility(View.INVISIBLE);
        findViewById(R.id.space_loading).setVisibility(View.INVISIBLE);
        String[] per = {Manifest.permission.INTERNET,Manifest.permission.ACCESS_NETWORK_STATE};
        if(!hasPermissions(this,per)){
            ActivityCompat.requestPermissions(this, per, 1);

        }
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        Spannable spannablerTitle = new SpannableString("Violations App");
        spannablerTitle.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")), 0, spannablerTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        actionBar.setTitle(spannablerTitle);
        actionBar.setElevation(0);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        findViewById(R.id.space_loading).getLayoutParams().height = height;
        findViewById(R.id.space_loading).getLayoutParams().width = width;
        Space space1= findViewById(R.id.space1_login);
        Space space2= findViewById(R.id.space2_login);
        Space space3= findViewById(R.id.space3_login);
         num = findViewById(R.id.num);
         name = findViewById(R.id.name);
         num.setInputType(InputType.TYPE_CLASS_NUMBER);
        num.getLayoutParams().height=(height/15);
        name.getLayoutParams().height=(height/15);

        ImageView imagemain = findViewById(R.id.imagemain);
        space1.getLayoutParams().height=(height/15-40);
        space2.getLayoutParams().height=(height/20);
        space3.getLayoutParams().height=(height/10);
        imagemain.getLayoutParams().width = 2*width/3;
        imagemain.setScaleType(ImageView.ScaleType.FIT_CENTER);

    }


    public void save(String s, Context context, Context context2){

        if(p==null){
            System.out.println("nulllll");
        }

        String name = s.split("-")[1];
        String num = s.split("-")[2];
        SharedPreferences.Editor e = p.edit();
        e.putString("name",name);
        e.putString("number",num);
        if(s.split("-")[0].contains("admin")){
            e.putString("type","admin");
        }
        else{
            e.putString("type","normal");
        }
        e.apply();
       // startActivity(new Intent(context2,MainActivity.class));
        f = true;

    }

    public void login(View view) {
        findViewById(R.id.progressBar_login).setVisibility(View.VISIBLE);
        findViewById(R.id.space_loading).setVisibility(View.VISIBLE);
     try {
         if(((ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo().isConnected())

         new DoHttpCall(new DoHttpCall.Navigate() {
             @Override
             public void onTaskComplete(String result) {
                 if(result.contains("admin"))
                     startActivity(new Intent(Login.this, AdminMainActivity.class))  ;
                 else
                     startActivity(new Intent(Login.this, MainActivity.class)) ;
                 finish();
             }
         }).execute(name.getText().toString(), num.getText().toString(), this, findViewById(R.id.progressBar_login), findViewById(R.id.space_loading), Login.this);
     }catch (Exception e){
         Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
     }

    }

    public void Signup(View view) {
        startActivity(new Intent(Login.this,SignupActivity.class));
    }
}
class DoHttpCall extends AsyncTask<Object, Void, String> {

    public interface Navigate{
        void onTaskComplete(String result);
    }

    private Navigate mNavigate;

    public DoHttpCall(Navigate n){
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
        context = (Context) strings[2];
        dialog = (RelativeLayout) strings[3];
        space = (Space) strings[4];
        context2 = (Context) strings[5];
        String result="";

        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL("https://hatemwebsite.000webhostapp.com/AWM/loginUser.php?name="+login_name+"&number="+login_num);
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
        if(!s.contains("empty")){
            Toast.makeText(context,"Welcome "+s.split("-")[1],Toast.LENGTH_LONG).show();

            new Login().save(s,context,context2);
            mNavigate.onTaskComplete(s);

        }
        else
            Toast.makeText(context,"The Entered Data Does not exist",Toast.LENGTH_LONG).show();
        super.onPostExecute(s);
    }
}