package com.vivid.courseval;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends Activity {

    HttpURLConnection Conn1 = null;
    BufferedReader reader = null;
    StringBuffer buffer = null;

    TextView tvUSDBuy;
    TextView tvUSDSell;
    TextView tvUSDBuyNBU;
    TextView tvUSDSellNBU;

    TextView tvEURBuy;
    TextView tvEURSell;
    TextView tvEURBuyNBU;
    TextView tvEURSellNBU;

    TextView tvRUBBuy;
    TextView tvRUBSell;
    TextView tvRUBBuyNBU;
    TextView tvRUBSellNBU;

    ImageView btnRefresh;

    ImageView btnExit;
    ImageView btnAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //--

       /* Toast toast = Toast.makeText(getApplicationContext(), Locale.getDefault().getLanguage(),
                Toast.LENGTH_SHORT);
        toast.show();*/


        new JSONTask().execute("http://openrates.in.ua/rates");
        tvUSDBuy = (TextView) findViewById(R.id.tvUSDBuy);
        tvUSDSell = (TextView) findViewById(R.id.tvUSDSell);
        tvUSDBuyNBU = (TextView) findViewById(R.id.tvUSDBuyNBU);
        tvUSDSellNBU = (TextView) findViewById(R.id.tvUSDSellNBU);

        tvEURBuy = (TextView) findViewById(R.id.tvEURBuy);
        tvEURSell = (TextView) findViewById(R.id.tvEURSell);
        tvEURBuyNBU = (TextView) findViewById(R.id.tvEURBuyNBU);
        tvEURSellNBU = (TextView) findViewById(R.id.tvEURSellNBU);

        tvRUBBuy = (TextView) findViewById(R.id.tvRUBBuy);
        tvRUBSell = (TextView) findViewById(R.id.tvRUBSell);
        tvRUBBuyNBU = (TextView) findViewById(R.id.tvRUBBuyNBU);
        tvRUBSellNBU = (TextView) findViewById(R.id.tvRUBSellNBU);

        btnExit = (ImageView) findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

        btnRefresh = (ImageView) findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JSONTask().execute("http://openrates.in.ua/rates");
                Toast toast = Toast.makeText(getApplicationContext(), "Данi оновленo!",
                        Toast.LENGTH_SHORT);
                toast.show();

            }
        });

        btnAbout = (ImageView) findViewById(R.id.btnAbout);
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Information.class);
                startActivity(intent);
            }
        });

        if (getIntent().getBooleanExtra("EXIT", false)) {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    public class JSONTask extends AsyncTask<String , String , String > {
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                Conn1 = (HttpURLConnection) url.openConnection();
                Conn1.connect();
                InputStream stream = Conn1.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                buffer = new StringBuffer();

                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (Conn1 != null) {
                    Conn1.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            if (result != null) {


                String finalJSON = result;

                JSONObject parentObj = null;
                try {
                    parentObj = new JSONObject(finalJSON);
                    JSONObject usd = parentObj.getJSONObject("USD");
                    JSONObject IBusd = usd.getJSONObject("interbank");
                    String IBusdBuy = IBusd.getString("buy");
                    String IBusdSell = IBusd.getString("sell");
                    JSONObject NBUusd = usd.getJSONObject("nbu");
                    String NBUusdBuy = NBUusd.getString("buy");
                    String NBUusdSell = NBUusd.getString("sell");

                    JSONObject eur = parentObj.getJSONObject("EUR");
                    JSONObject IBeur = eur.getJSONObject("interbank");
                    String IBeurBuy = IBeur.getString("buy");
                    String IBeurSell = IBeur.getString("sell");
                    JSONObject NBUeur = eur.getJSONObject("nbu");
                    String NBUeurBuy = NBUeur.getString("buy");
                    String NBUeurSell = NBUeur.getString("sell");

                    JSONObject rub = parentObj.getJSONObject("RUB");
                    JSONObject IBrub = rub.getJSONObject("interbank");
                    String IBrubBuy = IBrub.getString("buy");
                    String IBrubSell = IBrub.getString("sell");
                    JSONObject NBUrub = rub.getJSONObject("nbu");
                    String NBUrubBuy = NBUrub.getString("buy");
                    String NBUrubSell = NBUrub.getString("sell");

                    tvUSDBuy.setText(IBusdBuy);
                    tvUSDSell.setText(IBusdSell);
                    tvUSDBuyNBU.setText(NBUusdBuy);
                    tvUSDSellNBU.setText(NBUusdSell);

                    tvEURBuy.setText(IBeurBuy);
                    tvEURSell.setText(IBeurSell);
                    tvEURBuyNBU.setText(NBUeurBuy);
                    tvEURSellNBU.setText(NBUeurSell);

                    tvRUBBuy.setText(IBrubBuy);
                    tvRUBSell.setText(IBrubSell);
                    tvRUBBuyNBU.setText(NBUrubBuy);
                    tvRUBSellNBU.setText(NBUrubSell);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Помилка! Перевiрте пiдключення до мережi та спробуйте пiзнiше",
                        Toast.LENGTH_SHORT);
                toast.show();
            }


        }
    }

}

