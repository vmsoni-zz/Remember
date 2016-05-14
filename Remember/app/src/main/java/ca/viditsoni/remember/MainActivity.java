package ca.viditsoni.remember;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends Activity {
    private ArrayList<String> temps;
    private ArrayAdapter<String> itemsAdapter;
    TextView lvNewItem;
    private ListView listItems;
    private final static String STORETEXT="storetext";
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        temps = new ArrayList<String>();
        listItems = (ListView) findViewById(R.id.listViewItems);

        lvNewItem=(EditText)findViewById(R.id.addItem);
        try {
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(openFileInput(STORETEXT)));
            String inputString;
            while ((inputString = inputReader.readLine()) != null) {
                temps.add(inputString);
            }

            setupListViewListener();
            itemsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, temps);
            listItems.setAdapter(itemsAdapter);

        } catch (IOException e) {
        }
    }

    public void addItem(View view) {
        try {
            FileOutputStream fos = openFileOutput(STORETEXT, Context.MODE_APPEND);
            String write_line = (lvNewItem).getText().toString();
            write_line = write_line + "\n";
            fos.write(write_line.getBytes());
            fos.close();
            if (!lvNewItem.getText().toString().isEmpty()) {
                temps.add(lvNewItem.getText().toString());
                listItems.setAdapter(itemsAdapter);
                lvNewItem.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupListViewListener() {
        listItems.setOnItemLongClickListener(
            new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapter,
                                               View item, int pos, long id) {
                    temps.remove(pos);
                    itemsAdapter.notifyDataSetChanged();
                    try {
                        FileOutputStream writer = openFileOutput(STORETEXT,Context.MODE_PRIVATE);
                        writer.write(new String().getBytes());
                        writer.close();
                    }
                    catch (Exception e) {
                    }
                    try {
                        FileOutputStream fos = openFileOutput(STORETEXT, Context.MODE_APPEND);
                        for (int i=0; i< temps.toArray().length; i++){
                            String write_line = temps.get(i).toString();
                            write_line = write_line + "\n";
                            fos.write(write_line.getBytes());
                        }
                        fos.close();
                    } catch (Exception e) {
                        lvNewItem.setText(e.toString());
                        e.printStackTrace();
                    }
                    return true;
                }

            });
    }
}