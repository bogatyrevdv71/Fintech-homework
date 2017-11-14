package ru.fintech.db.hw1;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    class MyAdapter extends BaseAdapter {

        class NodeCell {
            private int value;
            private int color;

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }

            public int getColor() {
                return color;
            }

            public void setColor(int color) {
                this.color = color;
            }
        }
        NodeCell[] array;
        private int[] color = {Color.TRANSPARENT,
                getResources().getColor(R.color.notVeryYellow),
                getResources().getColor(R.color.notVeryBlue),
                getResources().getColor(R.color.notVeryRed)};

         MyAdapter() {
            array = new NodeCell[0];
        }

        void setArray (SparseIntArray arr) {
            array = new NodeCell[arr.size()];
            for(int i=0; i<arr.size();i++) {
                NodeCell cell = new NodeCell();
                cell.setValue(arr.keyAt(i));
                cell.setColor(arr.valueAt(i));
                array[i]=cell;
            }
            Arrays.sort(array, new Comparator <NodeCell>(){
                @Override
                public int compare(NodeCell o1, NodeCell o2) {
                    if(o1.getColor()>o2.getColor())
                        return -1;
                    if (o1.getColor()==o2.getColor())
                        return 0;
                    return 1;
                }
            });
            notifyDataSetChanged();
        }
        int getValue (int position) {
            return array[position].getValue();
        }

        @Override
        public int getCount() {
            return array.length;
        }

        @Override
        public Object getItem(int position) {
            return array[position];
        }

        @Override
        public long getItemId(int position) {
            return array[position].getValue();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item, parent, false);
            }

            TextView tv = ((TextView) convertView.findViewById(R.id.node_id));
            final NodeCell o = (NodeCell) getItem(position);
            tv.setText(Integer.toString(o.getValue()));
            tv.setBackgroundColor(color[o.getColor()]);
            return convertView;
        }


    }
    MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        adapter = new MyAdapter();

        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        class DataTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                NodeDatabase db = NodeDatabase.getInstance();
                db.init(getApplicationContext());
                db.fetch();
                adapter.setArray(db.getNodes());
                return null;
            }
        }
       new DataTask().execute();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showInputDialog();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                launchNodeActivity(position);
            }
        });
    }
    public void launchNodeActivity (int position) {
        Intent intent = new Intent(this, NodeRelationsActivity.class);
        int quack=((MyAdapter.NodeCell) adapter.getItem(position)).getValue();
        intent.putExtra("value", quack);
        startActivity(intent);
    }
    protected void showInputDialog() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.dialog_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.editValue);
        // setup a dialog window
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final Integer value=Integer.parseInt(editText.getText().toString());
                        class AddNodeTask extends AsyncTask<Void,Void,SparseIntArray>{

                            @Override
                            protected SparseIntArray doInBackground(Void... voids) {
                                NodeDatabase db = NodeDatabase.getInstance();
                                db.init(getApplicationContext());
                                db.fetch();
                                db.createNode(value);
                                return db.getNodes();
                            }
                            protected void onPostExecute (SparseIntArray res) {
                                adapter.setArray(res);
                            }
                        }
                        new AddNodeTask().execute();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
