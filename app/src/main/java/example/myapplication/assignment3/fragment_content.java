package example.myapplication.assignment3;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;


public class fragment_content extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_questions,container,false);

        TextView question = view.findViewById(R.id.textQuestion);

        Bundle extras = getArguments();
       question.setText(extras.getString("question"));
        return view;
    }
}
