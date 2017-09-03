package example.standardweight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        TextView sex=(TextView)findViewById(R.id.textView10);
        TextView stature=(TextView)findViewById(R.id.textView11);
        TextView weight=(TextView)findViewById(R.id.textView12);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        Info info=(Info)bundle.getSerializable("info");
        sex.setText("you are a "+info.getSex());
        stature.setText("your height is "+info.getStature()+" cm");
        weight.setText("your standard weight is "+getWeight(info.getSex(),info.getStature())+" kg");

    }
    String getWeight(String sex,float stature){
        String weight="";
        if(sex.equals("man"))
            weight=String.valueOf((stature-80)*0.7);
        else
            weight=String.valueOf((stature-70)*0.6);
        return weight;


    }
}
