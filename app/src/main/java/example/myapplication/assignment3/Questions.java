package example.myapplication.assignment3;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class Questions {

    private int rightAnswerCount;
    private int quizCount = 1;
    //private static Context context;
//    private String q1 = context.getString(R.string.question1);
//    private String q2 = context.getString(R.string.question2);
//    private String q3 = context.getString(R.string.question3);
//    private String q4 = context.getString(R.string.question4);
//    private String q5 = context.getString(R.string.question5);
//    private static String test = q1.toString();

    ArrayList<ArrayList<String>> quizArray = new ArrayList<>();

    public static String[][] quizData = {
            {"Is Ontario capital city in Canada", "False"},
            {"1 + 2 is 4", "False"},
            {"Emus can fly", "False"},
            {"Electrons move faster than the speed of light", "False"},
            {"Peanuts are nuts!", "False"},
//            {context.getString(R.string.question1), "False"},
//            {context.getString(R.string.question2), "False"},
//            {context.getString(R.string.question3), "False"},
//            {context.getString(R.string.question4), "False"},
//            {context.getString(R.string.question5), "False"}
    };

    public Questions() {
        //Questions.context = getApplicationContext();
        quizArray = new ArrayList<>();
        //this.context = context;
    }
}