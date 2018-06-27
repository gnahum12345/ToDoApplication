package me.gnahum12345.todoapplication;

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

public class MainActivity extends AppCompatActivity {

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

        //mock data   IT WORKS
//        items.add("First item!");
//        items.add("Second item!");

        setupListViewListener();


    }

    public void onAddItem(View v) {
        // Obtaining the reference to the Edit Text in the layout
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        // Obtaining the text within the Edit Text
        String itemText = etNewItem.getText().toString();
        //Adding it to the listView
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

