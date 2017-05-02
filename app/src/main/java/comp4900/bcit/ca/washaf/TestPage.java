package comp4900.bcit.ca.washaf;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TestPage extends AppCompatActivity {

    private TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_page);

        text = (TextView)findViewById(R.id.textView);
    }
}
