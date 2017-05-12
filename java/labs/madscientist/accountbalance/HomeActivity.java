package labs.madscientist.accountbalance;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class HomeActivity extends AppCompatActivity {
    TextView balanceDisplay;
    ViewGroup.LayoutParams tranParams;
    Float balance;
    JSONArray tranJSON;
    LinearLayout tranContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        TextView tranElementText = (TextView) findViewById(R.id.tranElement);
        tranParams = tranElementText.getLayoutParams();
        tranContainer = (LinearLayout) findViewById(R.id.transactions);
        balanceDisplay = (TextView) findViewById(R.id.accountBalance);
        try {
            String fileResult = readTransaction();
            tranJSON = new JSONArray(fileResult);
            JSONObject balanceJSON = tranJSON.getJSONObject(0);
            balance = Float.parseFloat(balanceJSON.getString("balance"));
            balanceDisplay.setText("$" + balance);
            for (int i = 1; i <tranJSON.length(); i++) {
                LinearLayout tranLayout = new LinearLayout(this);
                tranLayout.setGravity(Gravity.CENTER);
                JSONObject temp = tranJSON.getJSONObject(i);
                transactionAdder(temp, tranLayout);
            }
        } catch (JSONException e) {
            Log.e("JSON Error", e.toString());
        }

    }

    public void setBalance() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Set Balance");
        LayoutInflater inflater = this.getLayoutInflater();
        alert.setView(inflater.inflate(R.layout.set_balance, null));
        alert.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Set balance Balance
                Dialog f = (Dialog) dialog;
                EditText amountInput = (EditText) f.findViewById(R.id.balance);
                Float ammount = Float.parseFloat(amountInput.getText().toString());
                balance = ammount;
                balanceDisplay.setText("$" + amountInput.getText().toString());

                String initial = "[{\"balance\":\"" + balance + "\"}]";
                writeTransaction(initial);
                System.exit(0);
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                System.exit(0);
            }
        });
        alert.show();
    }

    public void addBalance(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Insert Deposit");
        LayoutInflater inflater = this.getLayoutInflater();
        alert.setView(inflater.inflate(R.layout.add_balance, null));
        alert.setPositiveButton("Add Deposit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Add Balance
                Dialog f = (Dialog) dialog;
                EditText buisnessInput = (EditText) f.findViewById(R.id.buisness);
                String buisness = buisnessInput.getText().toString();

                EditText amountInput = (EditText) f.findViewById(R.id.ammount);
                Float ammount = Float.parseFloat(amountInput.getText().toString());

                DatePicker dateInput = (DatePicker) f.findViewById(R.id.date);
                int month = dateInput.getMonth() + 1;
                int day = dateInput.getDayOfMonth();
                int year = dateInput.getYear();
                String date = month + "/" + day + "/" + year;
                String operation = "+";
                balance += ammount;
                try {
                    JSONObject temp = new JSONObject();
                    temp.put("ammount", ammount.toString());
                    temp.put("buisness", buisness);
                    temp.put("date", date);
                    temp.put("type", operation);
                    tranJSON.put(temp);
                    JSONObject balanceJSON = tranJSON.getJSONObject(0);
                    balanceJSON.put("balance", "" + balance);
                    tranJSON.put(0, balanceJSON);
                    writeTransaction(tranJSON.toString());
                    LinearLayout tranLayout = new LinearLayout(getApplicationContext());
                    tranLayout.setGravity(Gravity.CENTER);
                    transactionAdder(temp, tranLayout);
                } catch (JSONException e) {
                    Log.e("New Transaction Error", e.toString());
                }
                balanceDisplay.setText("$" + (balance));

            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }

    public void removeBalance(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Insert Transaction");
        LayoutInflater inflater = this.getLayoutInflater();
        alert.setView(inflater.inflate(R.layout.add_balance, null));
        alert.setPositiveButton("Add Transaction", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Remove balance
                Dialog f = (Dialog) dialog;
                EditText buisnessInput = (EditText) f.findViewById(R.id.buisness);
                String buisness = buisnessInput.getText().toString();

                EditText amountInput = (EditText) f.findViewById(R.id.ammount);
                Float ammount = Float.parseFloat(amountInput.getText().toString());

                DatePicker dateInput = (DatePicker) f.findViewById(R.id.date);
                int month = dateInput.getMonth() + 1;
                int day = dateInput.getDayOfMonth();
                int year = dateInput.getYear();
                String date = month + "/" + day + "/" + year;
                String operation = "-";
                balance -= ammount;
                try {
                    JSONObject temp = new JSONObject();
                    temp.put("ammount", ammount.toString());
                    temp.put("buisness", buisness);
                    temp.put("date", date);
                    temp.put("type", operation);
                    tranJSON.put(temp);
                    JSONObject balanceJSON = tranJSON.getJSONObject(0);
                    balanceJSON.put("balance", "" + balance);
                    tranJSON.put(0, balanceJSON);
                    writeTransaction(tranJSON.toString());
                    LinearLayout tranLayout = new LinearLayout(getApplicationContext());
                    tranLayout.setGravity(Gravity.CENTER);
                    transactionAdder(temp, tranLayout);
                } catch (JSONException e) {
                    Log.e("New Transaction Error", e.toString());
                }
                balanceDisplay.setText("$" + (balance));
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }

    public void writeTransaction(String string) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getApplicationContext().openFileOutput("transactions.json", Context.MODE_PRIVATE));
            outputStreamWriter.write(string);
            outputStreamWriter.close();
        } catch (Exception e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public String readTransaction() {
        String ret = "";
        try {
            InputStream input = getApplicationContext().openFileInput("transactions.json");

            if (input != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(input);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                input.close();
                ret = stringBuilder.toString();
            }
        } catch (Exception e) {
            Log.e("File not found maybe...", "\t" + e);
            setBalance();
        }
        return ret;
    }

    public void transactionAdder(JSONObject temp, LinearLayout tranLayout) {
        try {
            TextView text0 = new TextView(this);
            text0.setLayoutParams(tranParams);
            text0.setGravity(Gravity.CENTER);
            text0.setText(temp.getString("date"));
            tranLayout.addView(text0);

            TextView text1 = new TextView(this);
            text1.setLayoutParams(tranParams);
            text1.setGravity(Gravity.CENTER);
            text1.setText(temp.getString("buisness"));
            tranLayout.addView(text1);

            TextView text2 = new TextView(this);
            text2.setLayoutParams(tranParams);
            text2.setGravity(Gravity.CENTER);
            text2.setText(temp.getString("type"));
            tranLayout.addView(text2);

            TextView text3 = new TextView(this);
            text3.setLayoutParams(tranParams);
            text3.setGravity(Gravity.CENTER);
            text3.setText(temp.getString("ammount"));
            tranLayout.addView(text3);

            tranContainer.addView(tranLayout,1);
        } catch (Exception e) {
            Log.e("I'm not sure anymore...", e.toString());
        }
    }
}
