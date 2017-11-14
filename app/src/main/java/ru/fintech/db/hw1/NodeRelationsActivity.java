package ru.fintech.db.hw1;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Comparator;

public class NodeRelationsActivity extends AppCompatActivity {

    private class MyAdapter extends BaseAdapter {

        private class NodeCell {
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

            public void setColor(boolean color) {
                this.color = color? 1 : 0;
            }
        }


        private NodeRelationsActivity.MyAdapter.NodeCell[] arrayParents;
        private NodeRelationsActivity.MyAdapter.NodeCell[] arrayChildren;
        private boolean flag = false;

        private int[] color = {Color.TRANSPARENT,
                getResources().getColor(R.color.notVeryGreen)};

        public MyAdapter() {
            arrayParents = new NodeRelationsActivity.MyAdapter.NodeCell[0];
            arrayChildren = new NodeRelationsActivity.MyAdapter.NodeCell[0];
        }

        public void changeData() {
            flag=!flag;
            notifyDataSetChanged();

        }
        public void setArray (SparseBooleanArray arrParents, SparseBooleanArray arrChildren) {
            arrayParents = new NodeRelationsActivity.MyAdapter.NodeCell[arrParents.size()];
            arrayChildren = new NodeRelationsActivity.MyAdapter.NodeCell[arrChildren.size()];
            for(int i=0; i<arrChildren.size();i++) {
                NodeRelationsActivity.MyAdapter.NodeCell cellParent = new NodeRelationsActivity.MyAdapter.NodeCell();
                cellParent.setValue(arrParents.keyAt(i));
                cellParent.setColor(arrParents.valueAt(i));
                arrayParents[i]=cellParent;
                NodeRelationsActivity.MyAdapter.NodeCell cellChildren = new NodeRelationsActivity.MyAdapter.NodeCell();
                cellChildren.setValue(arrChildren.keyAt(i));
                cellChildren.setColor(arrChildren.valueAt(i));
                arrayChildren[i]=cellChildren;
            }

            Arrays.sort(arrayParents, new Comparator<NodeRelationsActivity.MyAdapter.NodeCell>(){
                @Override
                public int compare(NodeRelationsActivity.MyAdapter.NodeCell o1, NodeRelationsActivity.MyAdapter.NodeCell o2) {
                    if(o1.getColor()>o2.getColor())
                        return -1;
                    if (o1.getColor()==o2.getColor())
                        return 0;
                    return 1;
                }
            });
            Arrays.sort(arrayChildren, new Comparator<NodeRelationsActivity.MyAdapter.NodeCell>(){
                @Override
                public int compare(NodeRelationsActivity.MyAdapter.NodeCell o1, NodeRelationsActivity.MyAdapter.NodeCell o2) {
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
            if(flag) return arrayParents[position].getValue();
            return arrayChildren[position].getValue();
        }

        @Override
        public int getCount() {
            if(flag) return arrayParents.length;
            return arrayChildren.length;
        }

        @Override
        public Object getItem(int position) {
            if(flag) return arrayParents[position];
            return arrayChildren[position];
        }

        @Override
        public long getItemId(int position) {
            if(flag) return arrayParents[position].getValue();
            return arrayChildren[position].getValue();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item, parent, false);
            }

            TextView tv = ((TextView) convertView.findViewById(R.id.node_id));
            final NodeRelationsActivity.MyAdapter.NodeCell o = (NodeRelationsActivity.MyAdapter.NodeCell) getItem(position);
            tv.setText(Integer.toString(o.getValue()));
            tv.setBackgroundColor(color[o.getColor()]);
            return convertView;
        }


    }
    NodeRelationsActivity.MyAdapter adapter;
    private TextView mTextMessage;
    private int position;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                   adapter.changeData();
                    return true;
                case R.id.navigation_dashboard:
                    adapter.changeData();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_relations);

        adapter = new NodeRelationsActivity.MyAdapter();
        Intent intent = getIntent();
        position = intent.getIntExtra("value",0);;

        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        class DataTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                NodeDatabase db = NodeDatabase.getInstance();
                db.init(getApplicationContext());
                db.fetch();
                adapter.setArray(db.getNodesRelationsForNode(position,true),
                        db.getNodesRelationsForNode(position,false));
                return null;
            }
        }
        new DataTask().execute();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
