package com.example.stockquotes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import edu.cofc.stock.Stock;

public class MainActivity extends AppCompatActivity {
Stock stock;
String symbol;
TextView stockSymbol;
TextView compName;
TextView lastTradePrice;
TextView lastTradeTime;
TextView change;
TextView range;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText stockInput = findViewById(R.id.stockInput);
        stockSymbol = findViewById(R.id.stockSymbol);
        compName = findViewById(R.id.compName);
        lastTradePrice = findViewById(R.id.lastTradePrice);
        lastTradeTime = findViewById(R.id.lastTradeTime);
        change = findViewById(R.id.change);
        range = findViewById(R.id.range);

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
            catch (Exception ex){
                System.out.println("Error occured");
                System.out.println(ex.getClass());
            }
            return stock;
        }

        @Override
        protected void onPostExecute(Stock stock) {
            super.onPostExecute(stock);
            stockSymbol.setText(stock.getSymbol());
            compName.setText(stock.getName());
            lastTradePrice.setText(stock.getLastTradePrice());
            lastTradeTime.setText(stock.getLastTradeTime());
            change.setText(stock.getChange());
            range.setText(stock.getRange());

        }
    }
}
