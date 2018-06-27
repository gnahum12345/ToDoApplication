package me.gnahum12345.todoapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static me.gnahum12345.todoapplication.MainActivity.ITEM_POSITION;
import static me.gnahum12345.todoapplication.MainActivity.ITEM_TEXT;

public class EditItemActivity extends AppCompatActivity {

    // track edit text
    EditText edItemText;
    //position of item in list
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        // resolve edit text from layout
        edItemText = (EditText) findViewById(R.id.etItemText);
        // set edit text value from intent extra
        edItemText.setText(getIntent().getStringExtra(ITEM_TEXT));
        // update position
        position = getIntent().getIntExtra(ITEM_POSITION, 0);
        // Set ActionBar to be something different.
        getSupportActionBar().setTitle("Edit Item");
    }

    //Handle the save button.
    public void onSaveItem(View v) {
        //prepare the result
        Intent result = new Intent();
        //pass update item text as extra
        result.putExtra(ITEM_TEXT, edItemText.getText().toString());
        //pass original as extra
        result.putExtra(ITEM_POSITION, position);
        //Setting the result
        setResult(RESULT_OK, result);
        //close the current activity and redirect to main.
        finish();
    }
}
