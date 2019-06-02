package com.example.teammanagement.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuestionsParser implements Serializable {


    public static List<Question> fromJson(String json) throws JSONException {
        if (json == null) {
            return null;
        }
        List<Question> result = new ArrayList<>();
        String answer;

        JSONArray objectArray = new JSONArray(json);

        for (int i = 0; i < objectArray.length(); i++) {
            JSONObject object = objectArray.getJSONObject(i);
            String question = object.getString("question");
            JSONArray answers=object.getJSONArray("answers");
            List<String> listAnswear=new ArrayList<>();
            for(int j=0;j< answers.length();j++){
                JSONObject objectAnswer = answers.getJSONObject(j);
                answer=objectAnswer.getString("answer");
                listAnswear.add(answer);
            }
            Question questionToAdd = new Question(question, listAnswear);
            result.add(questionToAdd);
        }
        return result;
    }
}
