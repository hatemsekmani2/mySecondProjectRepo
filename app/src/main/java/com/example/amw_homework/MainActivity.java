package com.example.amw_homework;

import Models.Violation;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import java.util.Collection;
import java.util.concurrent.ExecutionException;

import static com.example.amw_homework.Login.context;
import static com.example.amw_homework.Login.hasPermissions;


public class MainActivity extends AppCompatActivity {
    public static RecyclerView recyclerView;
    public static Collection<Violation> all;
    private int height , width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String num = getSharedPreferences("edit",Context.MODE_PRIVATE).getString("number","");
       // Toast.makeText(context,num,Toast.LENGTH_LONG).show();
        findViewById(R.id.progressBar_main).setVisibility(View.VISIBLE);
        String[] per = {Manifest.permission.INTERNET};
        int notification_h = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            notification_h= getResources().getDimensionPixelSize(resourceId);
        }
        if(!hasPermissions(this,per)){
            ActivityCompat.requestPermissions(this, per, 1);

        }
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ImageView im = findViewById(R.id.imagemain3);
        Spannable spannablerTitle = new SpannableString("Violations App");
        spannablerTitle.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")), 0, spannablerTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        actionBar.setTitle(spannablerTitle);
        actionBar.setElevation(0);
        actionBar.getHeight();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
         height = displayMetrics.heightPixels - notification_h- actionBar.getHeight();
         width = displayMetrics.widthPixels;
        im.getLayoutParams().width = 2*width/3;
        im.setScaleType(ImageView.ScaleType.FIT_CENTER);
        recyclerView = findViewById(R.id.elements);
        try {
            all= new DoHttpCall_main().execute(findViewById(R.id.progressBar_main),this,recyclerView,width,height,num).get();
            //init();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


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
        RecyclerView.Adapter adapter =new Main_Adabter(MainActivity.this,all, width, height);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }


    public void details(View view, Context context) {
        System.out.println("yyyyyyyyyyyyyyyy"+view.getContext());
        TextView t = view.findViewById(R.id.main_id_value);
        if(view.getContext().toString().contains("MainActivity"))
        context.startActivity(new Intent(context , Details.class).putExtra("id", Integer.parseInt(t.getText().toString())-1));
    }

    }


class Main_Adabter extends RecyclerView.Adapter<mainHolder>{
    private ArrayList<Violation> mData;
    private LayoutInflater mInflater;
    int w;
    int h;

    public Main_Adabter(Context context, Collection<Violation> data, int width, int height){
        if(data==null)
            mData = new ArrayList<Violation>();
        else
      mData = new ArrayList<Violation>(data);

      w = width;
      h = height;

    }
    @NonNull
    @Override
    public mainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context
                = parent.getContext();
        LayoutInflater inflater
                = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.main_item, parent, false);
        return  new mainHolder(view , w,h);
    }

    @Override
    public void onBindViewHolder(@NonNull mainHolder holder, int position) {
       String pos =String.valueOf(position+1);
      holder.Number.setText(pos);
      holder.Date.setText(mData.get(position).Date);
      holder.Location.setText(mData.get(position).ViolationsType);
      if(mData.get(position).isPaid.equals("0"))
      holder.Paid.setText("NO");
      else
          holder.Paid.setText("Yes");
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
class mainHolder extends RecyclerView.ViewHolder{
    TextView Number;
    TextView Date;
    TextView Location;
    TextView Paid;
    View view;
    public mainHolder(@NonNull View itemView, int w, int h) {
        super(itemView);
        System.out.println(w +" "+h);
        Number = itemView.findViewById(R.id.main_id_value);
        Date = itemView.findViewById(R.id.main_date_value);
        Location = itemView.findViewById(R.id.main_location_value);
        Paid = itemView.findViewById(R.id.main_paid_value);
        itemView.findViewById(R.id.grid).getLayoutParams().width = w;
        itemView.findViewById(R.id.grid).getLayoutParams().height = h/2-50;
        itemView.findViewById(R.id.rl).getLayoutParams().height=h/2-50;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               new MainActivity().details(view,context);
            }
        });
       // itemView.findViewById(R.id.grid).getLayoutParams().height = h/2-20;


        view = itemView;
    }


}

class DoHttpCall_main extends AsyncTask<Object, Void, Collection<Violation>> {

    public interface Navigate{
        void onTaskComplete(String result);
    }

    private Navigate mNavigate;



    RelativeLayout dialog;
    Space space;
    Context context;
    Context context2;
    RecyclerView recycler;
    String num;
    int width;
    int height;

    @Override
    protected Collection<Violation> doInBackground(Object... strings) {
        dialog = (RelativeLayout) strings[0];
        context = (Context)strings[1];
        recycler = (RecyclerView) strings[2];
        width = (int) strings[3];
        height = (int) strings[4];
        num = strings[5].toString();
        String result = "";

        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL("https://hatemwebsite.000webhostapp.com/AWM/getVehicleViolations.php?vehicle_num="+num);
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
            Type collectionType = new TypeToken<Collection<Violation>>() {
            }.getType();

            Collection<Violation> allViolations = new Gson().fromJson(result, collectionType);
            System.out.println(allViolations.size());
             //MainActivity.all = allViolations;
            return allViolations;

        } else {
            return null;
        }


    }

    @Override
    protected void onPostExecute(Collection<Violation> violations) {
        System.out.println(violations);
        MainActivity.all=violations;

        RecyclerView.Adapter adapter =new Main_Adabter(context,violations,width,height);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(context));
        dialog.setVisibility(View.INVISIBLE);
        super.onPostExecute(violations);
    }
}

