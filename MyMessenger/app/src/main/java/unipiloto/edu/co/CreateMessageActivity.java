package unipiloto.edu.co;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.widget.EditText;


public class CreateMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onSendMessage(View view){
        EditText messageView= (EditText)findViewById(R.id.message);
        String messageText= messageView.getText().toString();
        Intent intent= new Intent(this,ReceiveMessageActivity.class);
        intent.putExtra(ReceiveMessageActivity.EXTRA_MESSAGE,messageText);
        startActivity(intent);

    }
}