package com.example.myfinal;

import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView lv;
    ArrayList<QuestionItem> list = new ArrayList<QuestionItem>();
    ArrayAdapter<QuestionItem> adapter;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        builder = new AlertDialog.Builder(this);

        /*
        this.list.add(new QuestionItem("1","What is the favorite color of your instructor?","red","blue","green","yellow"));
        this.list.add(new QuestionItem("2","What is the favorite color of your instructor?","red","blue","green","yellow"));
        this.list.add(new QuestionItem("3","What is the favorite color of your instructor?","red","blue","green","yellow"));
        this.list.add(new QuestionItem("4","What is the favorite color of your instructor?","red","blue","green","yellow"));
        this.list.add(new QuestionItem("5","What is the favorite color of your instructor?","red","blue","green","yellow"));
        this.list.add(new QuestionItem("6","What is the favorite color of your instructor?","red","blue","green","yellow"));
        */

        //free all threads
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        list = getData("http://127.0.0.1/finals/exam");

        this.lv = this.findViewById(R.id.listview1);
         adapter = new ArrayAdapter<QuestionItem>(this,android.R.layout.simple_list_item_1, list);
        this.lv.setAdapter(adapter);

        this.lv.setOnItemClickListener(this);
    }

    public ArrayList<QuestionItem> getData(String stringUrl){
        ArrayList<QuestionItem> list = new ArrayList<QuestionItem>();
        String data = null;
        try {
            URL url = new URL(stringUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String s = null;
            while ((s = br.readLine())!= null){
                sb.append(s);
            }
            br.close();
            conn.disconnect();
            //connecting to JSON
            JSONObject json = new JSONObject(sb.toString());
            JSONArray jarr = json.getJSONArray("exam");
                for(int i =0; i < jarr.length(); i++){
                    JSONObject obj = jarr.getJSONObject(i);
                    String id = obj.getString("id");
                    String question = obj.getString("question");
                    String opt1 = obj.getString("opt1");
                    String opt2 = obj.getString("opt2");
                    String opt3 = obj.getString("opt3");
                    String opt4 = obj.getString("opt4");

                    QuestionItem item = new QuestionItem(id,question,opt1,opt2,opt3,opt4);
                    list.add(item);
                }




        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        builder.setTitle("Question No:" +list.get(position).getId());
        layout = new LinearLayout(this);
        TextView question = new TextView(this);
        TextView option1 = new TextView(this);
        TextView option2 = new TextView(this);
        TextView option3 = new TextView(this);
        TextView option4 = new TextView(this);
            question.setText(list.get(position).getQuestion());
            question.setTextColor(Color.BLUE);
            option1.setText(list.get(position).getOpt1());
            option2.setText(list.get(position).getOpt2());
            option3.setText(list.get(position).getOpt3());
            option4.setText(list.get(position).getOpt4());
            layout.setPadding(10,10,10,10);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(question);
            layout.addView(option1);
            layout.addView(option2);
            layout.addView(option3);
            layout.addView(option4);
            builder.setView(layout);
            builder.setNeutralButton("okay",null);
            dialog = builder.create();
            dialog.show();
    }
}
