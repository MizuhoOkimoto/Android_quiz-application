package example.myapplication.assignment3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity<quizData> extends AppCompatActivity {

    Button trueButton, falseButton;
    ProgressBar progressBar;
    TextView question;

    private String rightAnswer; //正解を入れるために使います。
    private int rightAnswerCount; //正解数をカウントするために使います。
    private int quizCount = 1; //何問目を出題しているのかをカウントするために使います。
    private int progressCounter = 0;
    static final private int QUIZ_COUNT = 5;

    Questions questions;
    StorageManager storageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questions = new Questions();
        trueButton = findViewById(R.id.trueButton);
        falseButton = findViewById(R.id.falseButton);
        progressBar = findViewById(R.id.progressBar);
        question = (TextView) findViewById(R.id.textQuestion);

        storageManager = ((myApp) getApplication()).getStorageManager();
        //storageManager.resetHistory(MainActivity.this);

        //int size = 5;

        // quizDataからクイズ出題用のquizArrayを作成する
        for (int i = 0; i < QUIZ_COUNT; i++) {

            // 新しいArrayListを準備
            ArrayList<String> tmpArray = new ArrayList<>();

            // クイズデータを追加
            tmpArray.add(Questions.quizData[i][0]);  // question
            tmpArray.add(Questions.quizData[i][1]);  // correct answer

            // tmpArrayをquizArrayに追加する
            questions.quizArray.add(tmpArray);
        }
        //アプリを起動して MainActivity が呼び出されたときにクイズが出題されるように showNextQuiz() を呼び出します。
        showNextQuiz();
    }

    public void showNextQuiz() {

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        fragment_content firstFragment = new fragment_content();

        // ランダムな数字を取得
        Random random = new Random();
        int randomNum = random.nextInt(questions.quizArray.size());

        // randomNumを使って、quizArrayからクイズを一つ取り出す
        ArrayList<String> quiz = questions.quizArray.get(randomNum);
        Log.d("this is quiz", quiz.get(0));
        // 問題文（都道府県名）を表示
        //question.setText(quiz.get(0));

        Bundle bundle = new Bundle();
        bundle.putString("question", quiz.get(0));
        firstFragment.setArguments(bundle);

        // 正解をrightAnswerにセット
        rightAnswer = quiz.get(1);

        // クイズ配列から問題文（都道府県名）を削除
        quiz.remove(0);

        // 正解と選択肢３つをシャッフル
        Collections.shuffle(quiz);


        // このクイズをquizArrayから削除
        questions.quizArray.remove(randomNum);

        transaction.replace(R.id.add_remove_area, firstFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
    }

    //When users clock true or false button
    public void checkAnswer(View view) {
        progressCounter++;
        progressBar.setProgress(progressCounter);

        TextView question = findViewById(R.id.textQuestion);

        // どの解答ボタンが押されたか
        Button answerBtn = findViewById(view.getId());
        String btnText = answerBtn.getText().toString();

        String alertTitle;
        if (btnText.equals(rightAnswer)) {
            alertTitle = "Correct!";
            question.setBackgroundColor(Color.parseColor("#00FF00"));
            rightAnswerCount++;

        } else {
            alertTitle = "Incorrect";
            question.setBackgroundColor(Color.parseColor("#FF0000"));
        }

        // Create a Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(alertTitle);
        builder.setMessage("Answer : " + rightAnswer);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //when it's 5
                if (quizCount == QUIZ_COUNT) {
                    showResult();
                    return;
                } else {
                    quizCount++;
                    showNextQuiz();
                }
            }
        });

        builder.setCancelable(false);
        builder.show();
    }

    public void showResult(){
        // Create a Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Result");
        builder.setMessage("Result : " + rightAnswerCount);

        builder.setPositiveButton("Save", (dialog, which) -> {

            String history = storageManager.getHistory(MainActivity.this);
            String total = "";
            String correct = "";
            for (int i = 0; i < history.toCharArray().length; i++) {
                if (history.toCharArray()[i] == '$') {
                    correct = history.substring(0, i);
                    total = history.substring(i + 1);
                }
            }

            int totalHistory = Integer.parseInt(total);
            int correctHistory = Integer.parseInt(correct);
            correctHistory += rightAnswerCount;
            totalHistory += 5;
            storageManager.saveHistory(MainActivity.this, totalHistory, correctHistory);
        });

        builder.setNegativeButton("Ignore", (dialog, which) -> {

        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.checkHistory: {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                String history = storageManager.getHistory(MainActivity.this);
                String total = "";
                String correct = "";
                for (int i = 0; i < history.toCharArray().length; i++) {
                    if (history.toCharArray()[i] == '$') {
                        correct = history.substring(0, i);
                        total = history.substring(i + 1);
                    }
                }

                int totalHistory = Integer.parseInt(total);
                int correctHistory = Integer.parseInt(correct);

                builder.setTitle("Check history");
                builder.setMessage(correct + " / " + total);
                builder.setPositiveButton("Save", (dialog, which) -> {
                    storageManager.saveHistory(MainActivity.this, totalHistory, correctHistory);

                });

                builder.setNegativeButton("Ignore", (dialog, which) -> {

                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            }
            case R.id.reset: {
                storageManager.resetHistory(MainActivity.this);
                break;
            }
        }
        return true;
    }

    } //class

