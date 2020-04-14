package com.example.stockquotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import edu.cofc.stock.Stock;

import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity {
Stock stock;
String symbol;
TextView stockSymbol;
TextView compName;
TextView lastTradePrice;
TextView lastTradeTime;
TextView change;
TextView range;
Button hist1;
Button hist2;
Button hist3;
Button submitBtn;
boolean retrievalFailed = false;
Toast toast;
SharedPreferences sharedPref;
SharedPreferences.Editor editor;
String[] history = new String[3];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create sharedPref
        sharedPref =this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        //Instantiate View objects and restore saved states
        final EditText stockInput = findViewById(R.id.stockInput);
        stockSymbol = findViewById(R.id.stockSymbol);
        stockSymbol.setText(sharedPref.getString("stockSymbol", getString(R.string.blank)));
        compName = findViewById(R.id.compName);
        compName.setText(sharedPref.getString("compName", getString(R.string.blank)));
        lastTradePrice = findViewById(R.id.lastTradePrice);
        lastTradePrice.setText(sharedPref.getString("lastTradePrice", getString(R.string.blank)));
        lastTradeTime = findViewById(R.id.lastTradeTime);
        lastTradeTime.setText(sharedPref.getString("lastTradeTime", getString(R.string.blank)));
        change = findViewById(R.id.change);
        change.setText(sharedPref.getString("change", getString(R.string.blank)));
        range = findViewById(R.id.range);
        range.setText(sharedPref.getString("range", getString(R.string.blank)));

        //Prepare Toast object for invalid stock input
        toast = makeText(getApplicationContext(), "Invalid Stock Symbol", Toast.LENGTH_SHORT);

        submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                symbol = stockInput.getText().toString();
                cycleHistory(symbol);
                new StockRetrievalTask().execute(symbol);
            }
        });

        //Set up previous search buttons
        history[0] = sharedPref.getString("hist1", "N/A");
        history[1] = sharedPref.getString("hist2", "N/A");
        history[2] = sharedPref.getString("hist3", "N/A");
        hist1 = findViewById(R.id.history1);
        hist1.setText(history[0]);
        hist2 = findViewById(R.id.history2);
        hist2.setText(history[1]);
        hist3 = findViewById(R.id.history3);
        hist3.setText(history[2]);

        hist1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                symbol = hist1.getText().toString();
                new StockRetrievalTask().execute(symbol);
            }
        });

        hist2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                symbol = hist2.getText().toString();
                new StockRetrievalTask().execute(symbol);
            }
        });

        hist3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                symbol = hist3.getText().toString();
                new StockRetrievalTask().execute(symbol);
            }
        });
    }

    private void cycleHistory(String symbol){
        history[2] = history[1];
        history[1] = history[0];
        history[0] = symbol;
        hist1.setText(history[0]);
        hist2.setText(history[1]);
        hist3.setText(history[2]);
        editor.putString("hist1", history[0]);
        editor.putString("hist2", history[1]);
        editor.putString("hist3", history[2]);
        editor.commit();
    }

    private class StockRetrievalTask extends AsyncTask<String, Void, Stock>{

        @Override
        protected Stock doInBackground(String... strings) {
            stock = new Stock(symbol);
            try {
                stock.load();
            }
            catch (IOException ioe){
                retrievalFailed = true;
            }
            catch (Exception ex){
                System.out.println("Error occurred");
                System.out.println(ex.getClass());
            }
            return stock;
        }

        @Override
        protected void onPostExecute(Stock stock) {
            super.onPostExecute(stock);
            if (retrievalFailed){
                toast.show();
                retrievalFailed = false;
            }
            else{
                stockSymbol.setText(stock.getSymbol());
                editor.putString("stockSymbol", stock.getSymbol());
                compName.setText(stock.getName());
                editor.putString("compName", stock.getName());
                lastTradePrice.setText(stock.getLastTradePrice());
                editor.putString("lastTradePrice", stock.getLastTradePrice());
                lastTradeTime.setText(stock.getLastTradeTime());
                editor.putString("lastTradeTime", stock.getLastTradeTime());
                change.setText(stock.getChange());
                editor.putString("change", stock.getChange());
                range.setText(stock.getRange());
                editor.putString("range", stock.getRange());
                editor.commit();
            }
        }
    }
}
