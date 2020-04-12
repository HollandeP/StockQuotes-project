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
boolean retrievalFailed = false;
Toast toast;
SharedPreferences sharedPref;
SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref =this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();

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


        toast = makeText(getApplicationContext(), "Invalid Stock Symbol", Toast.LENGTH_SHORT);

        Button submitBtn = findViewById(R.id.submitBtn);


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                symbol = stockInput.getText().toString();
                StockRetrievalTask test = new StockRetrievalTask();
                test.execute(symbol);
            }
        });


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
