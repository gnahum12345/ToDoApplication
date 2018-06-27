package me.gnahum12345.todoapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import com.transitionseverywhere.*;
public class MainActivity extends AppCompatActivity {

    public final static int EDIT_REQUEST_CODE = 20;
    //key used for passing data between activities;
    public final static String ITEM_TEXT = "itemText";
    public final static String ITEM_POSITION = "itemPosition";

    //Declaring global variables
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //finding the listView in the Layout
        lvItems = (ListView) findViewById(R.id.lvItems);

        //initializing the items list
        readItems();
        //initializing the items Adapter
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        //Wiring the adapter to the current view.
        lvItems.setAdapter(itemsAdapter);

        setupListViewListener();
    }



    public void onAddItem(View v) {
        // Obtaining the reference to the Edit Text in the layout
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        // Obtaining the text within the Edit Text
        String itemText = etNewItem.getText().toString();
        //Adding it to the listView
        if (itemText.equals("")) {
            Toast t = Toast.makeText(getApplicationContext(), "Empty String is not added", Toast.LENGTH_SHORT);
            t.setGravity(0, 0,0 );
            t.show();
            return;
        }
        itemsAdapter.add(itemText);
        //update items to file System.
        writeItems();
        //Resetting the Edit text.
        etNewItem.setText("");
        //Displaying to the user it has been received.
        Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long l) {
                // Animate the background color of clicked item
                ColorDrawable [] color = { new ColorDrawable(Color.parseColor("#000000")), new ColorDrawable(Color.parseColor("#FFFFFF"))};
                TransitionDrawable trans = new TransitionDrawable(color);
                view.setBackground(trans);
                trans.startTransition(2000); // 2 seconds
                //remove the item.

                items.remove(i);
                //notify the adapter that the underlying dataset changed.
                itemsAdapter.notifyDataSetChanged();
                //update to the file system.
                writeItems();
                //Add a log.
                Log.i("MainActivity", "Removed item " + i);
                //return  true to tell the framework that the long click was consumed.
                return true;
            }
        });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int index, long l) {
                //create the new activity
                Intent editIntent = new Intent(MainActivity.this, EditItemActivity.class);
                //pass the data being edited
                editIntent.putExtra(ITEM_TEXT, items.get(index));
                editIntent.putExtra(ITEM_POSITION, index);
                //display activity
                startActivityForResult(editIntent, EDIT_REQUEST_CODE);

            }
        });
    }

    //  handle result from edit Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if edit activity completed ok
        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE) {
            // extract the string
            String updatedItem = data.getExtras().getString(ITEM_TEXT);
            // extract position
            int position = data.getExtras().getInt(ITEM_POSITION);
            // update items.
            items.set(position, updatedItem);
            // update the list view
            itemsAdapter.notifyDataSetChanged();
            // update the file system.
            writeItems();
            // notify the user.
            Toast.makeText(this, "Item updated successfully", Toast.LENGTH_SHORT).show();
        }
    }

    // Returns the data stored in the file.
    private File getDataFile() {
        return new File(getFilesDir(), "todo.txt");
    }

    //read the items from the file system.
    private void readItems() {
        try {
            items = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            //print the error.
            e.printStackTrace();
            //load an empty list
            items = new ArrayList<>();
        }
    }
    //Write items to the file System.
    private void writeItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            //print the error
            e.printStackTrace();
        }
    }


}

