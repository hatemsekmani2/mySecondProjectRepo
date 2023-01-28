package com.example.amw_homework;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;

import Models.Violation;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import sqip.Card;
import sqip.CardDetails;
import sqip.CardEntry;
import sqip.CardEntryActivityCommand;
import sqip.CardNonceBackgroundHandler;

public class Details extends AppCompatActivity {

    private int height , width;
    private PaymentsClient paymentsClient;
    int id;
    ArrayList<Violation> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        init();
         id = getIntent().getIntExtra("id",0);
        TextView date =  findViewById(R.id.details_id_value);
        TextView location =  findViewById(R.id.details_date_value);
        TextView type =  findViewById(R.id.details_location_value);
        TextView val =  findViewById(R.id.details_paid_value);
        Button pay =  findViewById(R.id.button_pay);
        arrayList = new ArrayList<Violation>(MainActivity.all);
        date.setText(arrayList.get(id).Date);
        location.setText(arrayList.get(id).Location);
        type.setText(arrayList.get(id).ViolationsType);
        val.setText(arrayList.get(id).Tax);
        if(arrayList.get(id).isPaid.equals("1")){
            pay.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CardEntry.handleActivityResult(data, result -> {
            if (result.isSuccess()) {
                CardDetails cardResult = result.getSuccessValue();
                Card card = cardResult.getCard();
                String nonce = cardResult.getNonce();
            } else if (result.isCanceled()) {
                findViewById(R.id.progressBar_payviolation).setVisibility(View.VISIBLE);
                new DoHttpCall_PayViolation(new DoHttpCall_PayViolation.Navigate() {
                    @Override
                    public void onTaskComplete(String result) {
                        Intent intent = new Intent(Details.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(intent);
                        finish();
                    }}).execute(findViewById(R.id.progressBar_payviolation),getApplicationContext(),width,height,arrayList.get(id).PluggedNumber,arrayList.get(id).Violations_Id,arrayList.get(id).Date,arrayList.get(id).Location,arrayList.get(id).isPaid);
                Toast.makeText(Details.this,
                        "Canceled",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
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
        findViewById(R.id.progressBar_payviolation).setVisibility(View.INVISIBLE);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        ImageView im = findViewById(R.id.imagemain4);
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

    public void goPayment(View view)

    {
        CardEntry.startCardEntryActivity(Details.this, true,
                CardEntry.DEFAULT_CARD_ENTRY_REQUEST_CODE);
    }





}

class DoHttpCall_PayViolation extends AsyncTask<Object, Void, String> {
    private DoHttpCall_PayViolation.Navigate mNavigate;

    public DoHttpCall_PayViolation(Navigate navigate) {
        mNavigate = navigate;
    }

    public interface Navigate{
        void onTaskComplete(String result);
    }





    RelativeLayout dialog;
    Space space;
    Context context;
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

        System.out.println(num+" "+id+" "+date+" "+location+" "+ispaid);
        String result = "";
        // dialog.setVisibility(View.VISIBLE);
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL("https://hatemwebsite.000webhostapp.com/AWM/payViolation.php?number="+num+"&id="+id+"&date="+date+"&location="+location+"&ispaid="+ispaid);
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
            mNavigate.onTaskComplete(num);

        }

        else{
            Toast.makeText(context,result, Toast.LENGTH_LONG).show();

        }
    }
}







