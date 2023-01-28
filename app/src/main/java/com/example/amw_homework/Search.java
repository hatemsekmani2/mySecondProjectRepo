package com.example.amw_homework;

import Models.SearchResultModel;
import Models.Violation;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

public class Search extends AppCompatActivity {

    private int height , width;
    public static Collection<Violation> all;
    private EditText num,name,date,location;
    String s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
         s = getIntent().getStringExtra("title");
        if(s==null)
            s="";
        else
            s = "- "+s;
        init(s);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
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

    public void init(String s){
         Calendar myCalendar = Calendar.getInstance();

        findViewById(R.id.progressBar_searchviolation).setVisibility(View.INVISIBLE);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        ImageView im = findViewById(R.id.imagesearch2);
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
        findViewById(R.id.num_searchviolation).getLayoutParams().height=height/15;
        findViewById(R.id.date_searchviolation).getLayoutParams().height=height/15;
        findViewById(R.id.location_searchviolation).getLayoutParams().height=height/15;
        findViewById(R.id.driver_searchviolation).getLayoutParams().height=height/15;
        im.setScaleType(ImageView.ScaleType.FIT_CENTER);
         num= findViewById(R.id.num_searchviolation);
         num.setInputType(InputType.TYPE_CLASS_NUMBER);;
         name= findViewById(R.id.driver_searchviolation);
         date= findViewById(R.id.date_searchviolation);
         location= findViewById(R.id.location_searchviolation);
         date.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                     @Override
                     public void onDateSet(DatePicker view, int year, int monthOfYear,
                                           int dayOfMonth) {
                         // TODO Auto-generated method stub

                         myCalendar.set(Calendar.YEAR, year);
                         myCalendar.set(Calendar.MONTH, monthOfYear);
                         myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                         if (monthOfYear<10&&dayOfMonth<10){

                             EditText t = findViewById(R.id.date_searchviolation);

                             t.setText(year + "-0" + monthOfYear + "-0" + dayOfMonth);
                         }
                         else if(monthOfYear<10) {

                             EditText t = findViewById(R.id.date_searchviolation);

                             t.setText(year + "-0" + monthOfYear + "-" + dayOfMonth);
                         }
                         else if(dayOfMonth<10){

                             EditText t = findViewById(R.id.date_searchviolation);

                             t.setText(year + "-" + monthOfYear + "-0" + dayOfMonth);
                         }

                         else{

                             EditText t = findViewById(R.id.date_searchviolation);

                             t.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                         }
                     }


                 };
                 new DatePickerDialog(Search.this, date, myCalendar
                         .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                         myCalendar.get(Calendar.DAY_OF_MONTH)).show();
             }
         });
         TextView te = findViewById(R.id.textView_title_searchviolation);
         if(!s.contains("Enter"))
             te.getLayoutParams().height=0;
         te.setText(s);
         te.setTextSize(15);



    }

    public void search(View view ) {
        findViewById(R.id.progressBar_searchviolation).setVisibility(View.VISIBLE);
        new DoHttpCall_Search().execute(findViewById(R.id.progressBar_searchviolation),this,num.getText().toString(),name.getText().toString(),date.getText().toString(),location.getText().toString(),s);
    }
}


class DoHttpCall_Search extends AsyncTask<Object, Void, String> {

    public interface Navigate{
        void onTaskComplete(String result);
    }

    private Navigate mNavigate;



    RelativeLayout dialog;
    Space space;
    Context context;
    Context context2;
    RecyclerView recycler;
    String num , driver , location , date ,s;
    int width;
    int height;

    @Override
    protected String doInBackground(Object... strings) {
        dialog = (RelativeLayout) strings[0];
        context = (Context)strings[1];

        num = strings[2].toString();
        driver = strings[3].toString();
        location = strings[5].toString();
        date = strings[4].toString();
        s = strings[6].toString();

        String result = "";

        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL("https://hatemwebsite.000webhostapp.com/AWM/search.php?number="+num+"&name="+driver+"&date="+date+"&location="+location);
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
        if (!result.contains("empty")) {


            //MainActivity.all = allViolations;
            return result;

        } else {
            return "empty";
        }


    }

    @Override
    protected void onPostExecute(String violations) {
        System.out.println(violations);
        dialog.setVisibility(View.INVISIBLE);
        if(!violations.contains("empty"))
        context.startActivity(new Intent(context, SearchResults.class).putExtra("res",violations).putExtra("title",s));
        else{
            Toast.makeText(context,"No Data Found",Toast.LENGTH_LONG).show();
        }
        super.onPostExecute(violations);
    }
}