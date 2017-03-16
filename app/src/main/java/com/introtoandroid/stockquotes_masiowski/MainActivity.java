package com.introtoandroid.stockquotes_masiowski;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.io.IOException;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;


//================================================================================================//

public class MainActivity extends AppCompatActivity {

    // VARIABLE DECLARATIONS======================================================================//

    String input;
    String symbol;
    String name;
    String tradePrice;
    String tradeTime;
    String change;
    String range;

    EditText editText;
    Button button;

    TextView symbolTV;
    TextView nameTV;
    TextView tradePriceTV;
    TextView tradeTimeTV;
    TextView changeTV;
    TextView rangeTV;


    boolean invalidSymbol = false;
    Stock stock;
    Context context;


    // ONCREATE===================================================================================//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);

        symbolTV = (TextView) findViewById(R.id.symbol);
        nameTV = (TextView) findViewById(R.id.name);
        tradePriceTV = (TextView) findViewById(R.id.tradePrice);
        tradeTimeTV = (TextView) findViewById(R.id.tradeTime);
        changeTV = (TextView) findViewById(R.id.change);
        rangeTV = (TextView) findViewById(R.id.range);

        context = getApplicationContext();


        // ONCLICK--------------------------------------------------------------------------------//

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                stockQuote();
            }
        });
    }

    // CALCULATE STOCKQUOTE=======================================================================//
    public void stockQuote(){

        input = editText.getText().toString();

        if (input.length()>0){

            stock = new Stock(input);

            // NEW THREAD-------------------------------------------------------------------------//
            new Thread() {
                public void run() {

                    // ERROR HANDLING
                    try {
                        stock.load();
                    }
                    catch (IOException ex) {
                        Toast.makeText(context, "IO EXCEPTION",
                                Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception ex){
                        Toast.makeText(context, "GENERAL EXCEPTION",
                                Toast.LENGTH_SHORT).show();

                    }

                    // GET DATA FROM STOCK CLASS
                    symbol = stock.getSymbol();
                    name = stock.getName();
                    tradePrice = stock.getLastTradePrice();
                    tradeTime = stock.getLastTradeTime();
                    change = stock.getChange();
                    range = stock.getRange();


                    if (stock.getName().contains("/")) {

                        invalidSymbol = true;

                        symbol = null;
                        name = null;
                        change = null;
                        range = null;

                    }
                }

            }.start();
        }
        else{
            invalidSymbol = true;
        }


        symbolTV.setText(symbol);
        nameTV.setText(name);
        tradePriceTV.setText(tradePrice);
        tradeTimeTV.setText(tradeTime);
        changeTV.setText(change);
        rangeTV.setText(range);


        // ERROR MESSAGE
        if(invalidSymbol == true) {
            Toast.makeText(context, "Not a valid stock", Toast.LENGTH_LONG).show();
            invalidSymbol=false;

        }



    }



    // SAVE INSTANCE STATE========================================================================//

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("input",editText.getText().toString());
        outState.putString("symbol",symbolTV.getText().toString());
        outState.putString("name",nameTV.getText().toString());
        outState.putString("tradePrice",tradePriceTV.getText().toString());
        outState.putString("tradeTime",tradeTimeTV.getText().toString());
        outState.putString("change",changeTV.getText().toString());
        outState.putString("range",rangeTV.getText().toString());

    }

    // RESTORE INSTANCE STATE=====================================================================//

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        editText.setText(savedInstanceState.getString("input"));
        symbolTV.setText(savedInstanceState.getString("symbol"));
        nameTV.setText(savedInstanceState.getString("name"));
        tradePriceTV.setText(savedInstanceState.getString("tradePrice"));
        tradeTimeTV.setText(savedInstanceState.getString("tradeTime"));
        changeTV.setText(savedInstanceState.getString("change"));
        rangeTV.setText(savedInstanceState.getString("range"));
    }


}
