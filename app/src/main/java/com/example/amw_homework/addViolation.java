package com.example.amw_homework;

import Models.SearchResultModel;
import Models.ViolationModle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.Spinner;
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
import java.util.ListIterator;

public class addViolation extends AppCompatActivity {

    private int height , width;
    static Collection<ViolationModle> all2;
   static ArrayList<String> array = new ArrayList<>();
   String id_v;
   boolean isEdititng;
   SearchResultModel data;
    private String ispaid = "0";
    private Calendar myCalendar = Calendar.getInstance();

    public static void showToast(Context context, String violation_added_successfully) {
        Toast.makeText(context,violation_added_successfully , Toast.LENGTH_LONG).show();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return true;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_violation);

        init();
        data = (SearchResultModel) getIntent().getSerializableExtra("data");

        Spinner spinner = findViewById(R.id.spinner);
        try {
            new DoHttpCall_getViolations().execute(findViewById(R.id.progressBar_addviolation),this,width,height,data,spinner);
            //init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if(!selectedItem.equals("Select Type"))
                for(int i=0; i < new ArrayList<>(all2).size();i++){
                    if(new ArrayList<>(all2).get(i).ViolationsType.equals(selectedItem))
                        id_v = new ArrayList<>(all2).get(i).id;
                    System.out.println(id_v);

                }
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        ArrayAdapter ad
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                array);

        // set simple layout resource file
        // for each item of spinner
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);
        spinner.setAdapter(ad);
        findViewById(R.id.num_addviolation).getLayoutParams().height=(height/15);
        findViewById(R.id.spinner).getLayoutParams().height=(height/15);
        findViewById(R.id.type_addviolation).getLayoutParams().height=(height/15);
        findViewById(R.id.category_addviolation).getLayoutParams().height=(height/15);
        if(getIntent().getBooleanExtra("EdititngMode", false)){
            EditText t1 = findViewById(R.id.num_addviolation);
            EditText t2 = findViewById(R.id.type_addviolation);
            EditText t3 = findViewById(R.id.category_addviolation);
            t1.setText(data.PluggedNumber);
            t1.setInputType(InputType.TYPE_CLASS_NUMBER);
            t2.setText(data.Date);
            t3.setText(data.Location);
            System.out.println(data.ViolationsType);
            spinner.setSelection(ad.getPosition(data.ViolationsType));
            Button b = findViewById(R.id.button2_addviolation);
            RadioButton no = findViewById(R.id.no);
            RadioButton yes = findViewById(R.id.yes);

            if(data.isPaid.equals("0"))
                no.setChecked(true);
            else
                yes.setChecked(true);
            b.setText("- Edit Violation -");
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText num = findViewById(R.id.num_addviolation);
                    EditText date = findViewById(R.id.type_addviolation);
                    EditText location = findViewById(R.id.category_addviolation);
                    findViewById(R.id.progressBar_addviolation).setVisibility(View.VISIBLE);

                    new DoHttpCall_EditViolation(
                            new DoHttpCall_EditViolation.Navigate() {
                                @Override
                                public void onTaskComplete(String result) {
                                     startActivity(new Intent(addViolation.this, Search.class)) ;
                                    finish();
                                }
                            }
                    ).execute(findViewById(R.id.progressBar_addviolation),getApplicationContext(),width,height,data.PluggedNumber,array.indexOf(data.ViolationsType),data.Date,data.Location,data.isPaid,num.getText(),id_v,date.getText(),location.getText(),ispaid);
                }
            });
        }

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                EditText t = findViewById(R.id.type_addviolation);

                t.setText(year+"-"+monthOfYear+"-"+dayOfMonth);
            }

        };


        findViewById(R.id.type_addviolation).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(addViolation.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



    }

    public void addviolation1(View view) {
        EditText num = findViewById(R.id.num_addviolation);
        EditText date = findViewById(R.id.type_addviolation);
        EditText location = findViewById(R.id.category_addviolation);
        findViewById(R.id.progressBar_addviolation).setVisibility(View.VISIBLE);
       new DoHttpCall_addViolation(new DoHttpCall_addViolation.Navigate() {
           @Override
           public void onTaskComplete(String result) {

               finish();
           }}).execute(findViewById(R.id.progressBar_addviolation),this,width,height,num.getText(),id_v,date.getText(),location.getText(),ispaid);

    }
    public   void finishs(){

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
    public void init(){
        array = new ArrayList<>();
        all2 = new ArrayList<>();
        array.add("Select Type");
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        ImageView im = findViewById(R.id.imageadd);
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
        RadioGroup rg =  findViewById(R.id.pdate_addviolation);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checkedRadioButton = (RadioButton)radioGroup.findViewById(i);
                if(checkedRadioButton.isChecked()&&checkedRadioButton.getText()=="No")
                    ispaid  = "1";
                else
                    ispaid = "0";
                System.out.println(ispaid);

                //System.out.println(ispaid);
            }
        });



    }


}
class DoHttpCall_addViolation extends AsyncTask<Object, Void, String> {



    public interface Navigate{
        void onTaskComplete(String result);
    }

    public DoHttpCall_addViolation(Navigate navigate) {
        mNavigate = navigate;
    }
    private Navigate mNavigate;



    RelativeLayout dialog;
    Space space;
    Context context;
    Context context2;
    RecyclerView recycler;
    String num,id,date,location,ispaid;
    int width;
    int height;

    @Override
    protected String doInBackground(Object... strings) {
        dialog = (RelativeLayout) strings[0];
        context = (Context)strings[1];
      //  recycler = (RecyclerView) strings[2];
        width = (int) strings[2];
        height = (int) strings[3];
        num = strings[4].toString();
        id = strings[5].toString();
        date = strings[6].toString();
        location = strings[7].toString();
        ispaid = strings[8].toString();
        String result = "";

        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL("https://hatemwebsite.000webhostapp.com/AWM/Add_Violation.php?number="+num+"&id="+id+"&date="+date+"&location="+location+"&ispaid="+ispaid);
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
        dialog.setVisibility(View.INVISIBLE);
        if (result.contains("NO Vehicle")) {
            Toast.makeText(context,"There is no Car matches this number" , Toast.LENGTH_LONG).show();

        }

        else if(result.contains("successfully")) {
            Toast.makeText(context,"Violation Added Successfully",Toast.LENGTH_LONG).show();
            mNavigate.onTaskComplete("");


        }

        else{
            Toast.makeText(context,result, Toast.LENGTH_LONG).show();

        }
    }
}
class DoHttpCall_EditViolation extends AsyncTask<Object, Void, String> {
    private Navigate mNavigate;

    public DoHttpCall_EditViolation(Navigate navigate) {
        mNavigate = navigate;
    }

    public interface Navigate{
        void onTaskComplete(String result);
    }





    RelativeLayout dialog;
    Space space;
    Context context;
    Context context2;
    RecyclerView recycler;
    String num,id,date,location,ispaid;
    String n_num,n_id,n_date,n_location,n_ispaid;
    int width;
    int height;

    @Override
    protected String doInBackground(Object... strings) {
        dialog = (RelativeLayout) strings[0];
        context = (Context)strings[1];
      //  recycler = (RecyclerView) strings[2];
        width = (int) strings[2];
        height = (int) strings[3];
        num = strings[4].toString();
        id = strings[5].toString();
        date = strings[6].toString();
        location = strings[7].toString();
        ispaid = strings[8].toString();
        n_num = strings[9].toString();
        n_id = strings[10].toString();
        n_date = strings[11].toString();
        n_location = strings[12].toString();
        n_ispaid = strings[13].toString();
        System.out.println(num+" "+id+" "+date+" "+location+" "+ispaid+" "+n_num+" "+n_id+" "+n_date+" "+n_location+" "+n_ispaid);
        String result = "";
       // dialog.setVisibility(View.VISIBLE);
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL("https://hatemwebsite.000webhostapp.com/AWM/Update_Violation.php?number="+num+"&id="+id+"&date="+date+"&location="+location+"&ispaid="+ispaid+"&new_number="+n_num+"&new_id="+n_id+"&new_date="+n_date+"&new_location="+n_location+"&new_ispaid="+n_ispaid);
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
        dialog.setVisibility(View.INVISIBLE);
        if (result.contains("NO Vehicle")) {
            Toast.makeText(context,"There is no Car matches this number" , Toast.LENGTH_LONG).show();

        }

        else if(result.contains("successfully")) {
            Toast.makeText(context,"Violation Edited Successfully",Toast.LENGTH_LONG).show();
            context.startActivity(new Intent(context,Search.class));
            mNavigate.onTaskComplete("");

        }

        else{
            Toast.makeText(context,result, Toast.LENGTH_LONG).show();

        }
    }
}
class DoHttpCall_getViolations extends AsyncTask<Object, Void, Collection<ViolationModle>> {

    public interface Navigate{
        void onTaskComplete(String result);
    }

    private Navigate mNavigate;



    RelativeLayout dialog;
    Space space;
    Context context;
    Context context2;
    RecyclerView recycler;
    SearchResultModel data;
    Spinner spinner;
    String num;
    int width;
    int height;

    @Override
    protected Collection<ViolationModle> doInBackground(Object... strings) {
        dialog = (RelativeLayout) strings[0];
        context = (Context)strings[1];
        width = (int) strings[2];
        height = (int) strings[3];
        data = (SearchResultModel) strings[4];
        spinner = (Spinner) strings[5];
        String result = "";

        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL("https://hatemwebsite.000webhostapp.com/AWM/allViolations.php");
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
            Type collectionType = new TypeToken<Collection<ViolationModle>>() {
            }.getType();

            Collection<ViolationModle> allViolations = new Gson().fromJson(result, collectionType);
            System.out.println(allViolations.size());

            //MainActivity.all = allViolations;
            return allViolations;

        } else {
            return null;
        }


    }

    @Override
    protected void onPostExecute(Collection<ViolationModle> violations) {
        System.out.println(violations);
        addViolation.all2=violations;
        ArrayList<ViolationModle> s = new ArrayList<ViolationModle>(addViolation.all2);
        for(int i=0;i<new ArrayList<>(addViolation.all2).size();i++){
            addViolation.array .add(s.get(i).ViolationsType);

        }
        if(data!=null) {
            spinner.setSelection(addViolation.array.indexOf(data.ViolationsType));
            super.onPostExecute(violations);
        }
        dialog.setVisibility(View.INVISIBLE);

    }
}


