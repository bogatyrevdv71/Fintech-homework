package ru.fintech.db.hw1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ViewInterface, SwipeRefreshLayout.OnRefreshListener {

    ArrayAdapter<String> listAdapter ;
    PresenterViewInterface presenter;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new Presenter(new Model(), this);
        final ListView listView = findViewById(R.id.list_view);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        listAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.item,
                R.id.node_id);
        listView.setAdapter(listAdapter);
        onRefresh();

        swipeRefreshLayout.setOnRefreshListener(this);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.setSelectionAfterHeaderView();
                Toast.makeText(getApplicationContext(),"Проскроленно вверх",Toast.LENGTH_SHORT).show();
            }
        });
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

    @Override
    public void callback(List<String> s) {
        listAdapter.clear();
        listAdapter.addAll(s);
        listAdapter.notifyDataSetChanged();
    }

    class RefreshNews extends AsyncTask <ViewInterface,Void,Void>{
        @Override
        protected Void doInBackground(ViewInterface... viewInterfaces) {
            presenter.get(viewInterfaces[0]);
            return null;
        }
        @Override
        protected void onPostExecute (Void v) {
            if(swipeRefreshLayout.isRefreshing())
                swipeRefreshLayout.setRefreshing(false);
        }

    }

    @Override
    public void onRefresh() {
        new RefreshNews().execute(this);
    }
}
