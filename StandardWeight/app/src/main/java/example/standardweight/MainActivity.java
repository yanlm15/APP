package example.standardweight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Info info = new Info();
                if ("".equals(((EditText) findViewById(R.id.editText)).getText().toString())) {
                    Toast.makeText(MainActivity.this, "please input your height!", Toast.LENGTH_SHORT).show();
                    return;
                }

                int stature = Integer.parseInt(((EditText) findViewById(R.id.editText)).getText().toString());
                RadioGroup sex = (RadioGroup) findViewById(R.id.sex);

                for (int i=0;i<sex.getChildCount();i++) {
                    RadioButton rb=(RadioButton)sex.getChildAt(i);
                    if(rb.isChecked()){
                        info.setSex(rb.getText().toString());
                        break;
                    }
                }
                info.setStature(stature);
                Bundle bundle=new Bundle();
                bundle.putSerializable("info",info);
                Intent intent=new Intent(MainActivity.this, example.standardweight.ResultActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);


            }


        });
    }
}



class Info implements Serializable {
    private static final long serialVersionUID = 1L;
    private String sex = "";
    private int stature = 0;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getStature() {
        return stature;
    }

    public void setStature(int stature) {
        this.stature = stature;
    }

}
