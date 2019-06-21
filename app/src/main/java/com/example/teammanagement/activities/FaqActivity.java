package com.example.teammanagement.activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;


import com.example.teammanagement.R;
import com.example.teammanagement.Utils.Constants;
import com.example.teammanagement.Utils.QuestionsParser;
import com.example.teammanagement.adapters.ExpandableListFaqAdapter;
import com.example.teammanagement.Utils.Question;


import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FaqActivity extends AppCompatActivity implements Serializable {


    private List<String> listQuestions = new ArrayList<>();
    private List<Question> lista=new ArrayList<>();
    private HashMap<String,List<String>> listaAnswers = new HashMap<>();
    private ExpandableListView listView;
    private ExpandableListFaqAdapter listAdapter;
    ArrayList<List<String>> answers= new ArrayList<>();
    TextView tv_question;
    TextView tv_answers;
    Intent intent;
    ImageButton ibtn_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        initComponents();
        initData();

        for(int i=0;i< listQuestions.size();i++){
            listaAnswers.put(listQuestions.get(i),answers.get(i));
        }


        listAdapter =  new ExpandableListFaqAdapter(this,listQuestions,listaAnswers);
        listView.setAdapter(listAdapter);
    }

    private void initComponents(){
        listView=findViewById(R.id.faq_elv_questionlist);
        tv_question=findViewById(R.id.list_group_faq_tv);
        tv_answers=findViewById(R.id.list_item_faq_tv);
        ibtn_back=findViewById(R.id.faq_ibtn_back);

        ibtn_back.setOnClickListener(clickBack());

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_SHAREDPREF,MODE_PRIVATE);
        String s =  sharedPreferences.getString(Constants.FAQ_LIST_HINT,getString(R.string.faq_sp_default_hint));
        try {
            lista= QuestionsParser.fromJson(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void initData(){
        if(lista != null){
            for(int i=0;i< lista.size();i++){
                listQuestions.add(lista.get(i).getQuestionText());
            }
            for(int i=0; i< lista.size();i++){
                answers.add(lista.get(i).getAnswers());
            }
        }
    }

    private View.OnClickListener clickBack() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
            }
        };
    }



}
