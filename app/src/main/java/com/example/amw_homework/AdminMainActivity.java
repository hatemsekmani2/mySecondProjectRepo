package com.example.amw_homework;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.MessagePattern;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AdminMainActivity extends AppCompatActivity {

    private int height , width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        init();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_item_one) {
            SharedPreferences p = getSharedPreferences("edit",MODE_PRIVATE);
            SharedPreferences.Editor editor = p.edit();
            editor.clear();
            editor.apply();
            startActivity(new Intent(this,Login.class));
            finish();
            // Do something
            return true;
        }
        if(id==R.id.action_item_two){
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.about)
                    .setMessage("Thanks for using our application.\n\nthis application helps you to retrieve all the violations that made by a specific car , also make the user able to pay his violation tax whenever he/she wants.\nfor more information or contact\n\n- Whatsapp: 123456789\n- Email: violationsapp@test.com.")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
            ;
        }

        return super.onOptionsItemSelected(item);
    }

    public void init(){
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ImageView im = findViewById(R.id.imagemain5);
        int notification_h = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            notification_h= getResources().getDimensionPixelSize(resourceId);
        }
        Spannable spannablerTitle = new SpannableString("Violations App");
        spannablerTitle.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")), 0, spannablerTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        actionBar.setTitle(spannablerTitle);
        actionBar.setElevation(0);
        actionBar.getHeight();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels - notification_h- actionBar.getHeight();
        width = displayMetrics.widthPixels;
        im.getLayoutParams().width=2*width/3;
        im.setScaleType(ImageView.ScaleType.FIT_CENTER);


    }

    public void NavigateToAdd(MenuItem item) {
        startActivity(new Intent(AdminMainActivity.this, addViolation.class));
    }

    public void Cross(MenuItem item) {
        final LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.delete, null, false);
        EditText t = view.findViewById(R.id.delete2);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        t.getLayoutParams().height = height/15;
        t.setInputType(InputType.TYPE_CLASS_NUMBER);
        System.out.println("jjjjfjfjf"+t.getText().toString());

       AlertDialog.Builder alertDialog=  new AlertDialog.Builder(this);
//set icon
               alertDialog.setIcon(android.R.drawable.ic_dialog_alert)
//set title
                .setTitle("Cross Out Vehicle")
//set message
                 .setView(view)
//set positive button
                .setPositiveButton("Cross out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final Context context = alertDialog.getContext();
                         if(!t.getText().toString().equals(""))
                        new DoHttpCall_cross().execute(getApplicationContext(),t.getText().toString());
                         else
                             Toast.makeText(context,"Entered Value is worng",Toast.LENGTH_LONG).show();
                        //set what would happen when positive button is clicked

                    }
                })
//set negative button
                .show();

    }

    public void search(View view) {
        startActivity(new Intent(AdminMainActivity.this , Search.class));
    }

    public void update(MenuItem item) {
        startActivity(new Intent(AdminMainActivity.this , Search.class).putExtra("title","Enter specific violation information :"));

    }
}
class DoHttpCall_cross extends AsyncTask<Object, Void, String> {



    public interface Navigate{
        void onTaskComplete(String result);
    }


    private Navigate mNavigate;



    ProgressBar dialog;
    Space space;
    Context context;
    Context context2;
    RecyclerView recycler;
    String num;

    @Override
    protected String doInBackground(Object... strings) {
        context = (Context)strings[0];
        //  recycler = (RecyclerView) strings[2];
        num = strings[1].toString();
        System.out.println("number is" + num);

        String result = "";

        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL("https://hatemwebsite.000webhostapp.com/AWM/crossOutVehicle.php?number="+num);
            System.out.println(url.toString());

            urlConnection = (HttpURLConnection) url
                    .openConnection();

            InputStream in = urlConnection.getInputStream();

            InputStreamReader isw = new InputStreamReader(in);

            int data = isw.read();
            while (data != -1) {
                result += (char) data;
                data = isw.read();

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        System.out.println("HHHHHHHH" + result);

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result.contains("NO Vehicle")) {
            Toast.makeText(context,"There is no Car matches this number" , Toast.LENGTH_LONG).show();

        }

        else if(result.contains("successfully")) {
            Toast.makeText(context,"Vehicle Crossed Out Successfully",Toast.LENGTH_LONG).show();

        }

        else{
            Toast.makeText(context,result, Toast.LENGTH_LONG).show();

        }
    }
}
