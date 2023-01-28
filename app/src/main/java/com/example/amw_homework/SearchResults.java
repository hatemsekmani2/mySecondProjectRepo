package com.example.amw_homework;

import Models.SearchResultModel;
import Models.Violation;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

public class SearchResults extends AppCompatActivity {

    private int height , width;
    String s;

    public  void doIt() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int total = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        s = getIntent().getStringExtra("title");
        init();
        String res = getIntent().getStringExtra("res");
        Type collectionType = new TypeToken<Collection<SearchResultModel>>() {
        }.getType();

        Collection<SearchResultModel> allViolations = new Gson().fromJson(res, collectionType);
        RecyclerView recyclerView = findViewById(R.id.search_elements);
        RecyclerView.Adapter adapter =new Search_Adabter(this,allViolations,width,height,s,
        new Navigate() {
            @Override
            public void onTaskComplete(String result) {
                finish();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<SearchResultModel> arr = new ArrayList<>(allViolations);
        TextView t = findViewById(R.id.textViewtotal);
        for(int i=0;i<arr.size();i++){
            total+=Integer.parseInt(arr.get(i).Tax);
        }
        t.setText(total+" SP");

        t.getLayoutParams().height = height/12;
        findViewById(R.id.textViewtotals).getLayoutParams().height = height/12;

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
    public void init(){
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        ImageView im = findViewById(R.id.imageresult3);
        int notification_h = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            notification_h= getResources().getDimensionPixelSize(resourceId);
        }
        LinearLayout ll = findViewById(R.id.ll);
        System.out.println("ddddddddddddd"+s);

        if(s.length()>1){
            ll.setVisibility(View.INVISIBLE);
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

}
class Search_Adabter extends RecyclerView.Adapter<searchHolder>{
    private Navigate mNavigate;



    private ArrayList<SearchResultModel> mData;
    private LayoutInflater mInflater;
    int w;
    int h;
    String s;
    Context c;
    public Search_Adabter(Context context, Collection<SearchResultModel> data, int width, int height, String s, Navigate navigate){
        c = context;
        mNavigate = navigate;

        this.s = s;
        if(data==null)
            mData = new ArrayList<SearchResultModel>();
        else
            mData = new ArrayList<SearchResultModel>(data);

        w = width;
        h = height;

    }
    @NonNull
    @Override
    public searchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context
                = parent.getContext();
        LayoutInflater inflater
                = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.main_item, parent, false);
        return  new searchHolder(view , w,h,s,mData,c,mNavigate);
    }

    @Override
    public void onBindViewHolder(@NonNull searchHolder holder, int position) {
        String pos =String.valueOf(position+1);
        holder.Number.setText(pos);
        holder.Date.setText(mData.get(position).Date);
        holder.Location.setText(mData.get(position).ViolationsType);
            holder.Paid.setText(mData.get(position).Tax);
            holder.t.setText("- Tax Value :");


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
class searchHolder extends RecyclerView.ViewHolder{
    TextView Number;
    TextView Date;
    TextView Location;
    TextView Paid;
    TextView t;
    View view;
    Context c;
    public searchHolder(@NonNull View itemView, int w, int h, String s, ArrayList<SearchResultModel> mData, Context c, Navigate mNavigate) {
        super(itemView);
        this.c = c;
        System.out.println(w +" "+h);
        Number = itemView.findViewById(R.id.main_id_value);
        Date = itemView.findViewById(R.id.main_date_value);
        Location = itemView.findViewById(R.id.main_location_value);
        Paid = itemView.findViewById(R.id.main_paid_value);
         t = itemView.findViewById(R.id.main_paid);
        itemView.findViewById(R.id.grid).getLayoutParams().width = w-20;
        itemView.findViewById(R.id.grid).getLayoutParams().height = h/2-50;
        itemView.findViewById(R.id.rl).getLayoutParams().height=h/2-50;
        if(s.length()>0){
            itemView.findViewById(R.id.grid).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemView.getContext().startActivity(new Intent(itemView.getContext(),addViolation.class).putExtra("data",mData.get(Integer.parseInt(Number.getText().toString()))).putExtra("EdititngMode",true));
                   mNavigate.onTaskComplete("");
                }
            });

        }
        else
        itemView.findViewById(R.id.grid).setOnClickListener(null);
        // itemView.findViewById(R.id.grid).getLayoutParams().height = h/2-20;


        view = itemView;
    }


}
 interface Navigate{
    void onTaskComplete(String result);

}
