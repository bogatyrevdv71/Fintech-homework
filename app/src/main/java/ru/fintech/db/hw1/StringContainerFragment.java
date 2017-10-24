package ru.fintech.db.hw1;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class StringContainerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_LIST = "list";
    private static final String ARG_FIN = "fin";
    public static final String TAG = "StringContainerFragment";

    // Saved between usages
    private ArrayList<String> stringList;
    private boolean finished;

    // Per-view
    private ArrayAdapter<String> stringAdapter;
    private StringLoader stringLoader;

    public StringContainerFragment() {
        // Required empty public constructor
        Bundle args = new Bundle();
        args.putBoolean(ARG_FIN, false);
        args.putStringArrayList(ARG_LIST, new ArrayList<String>());
        setArguments(args);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            stringList = getArguments().getStringArrayList(ARG_LIST);
            finished = getArguments().getBoolean(ARG_FIN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_string_container, container,
                false);
        ListView lv = v.findViewById(R.id.stringListView);
        stringAdapter = new ArrayAdapter<>(v.getContext(),
                R.layout.fragment_item_list, stringList);

        lv.setAdapter(stringAdapter);
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!finished) {
            stringLoader = new StringLoader(this);
            stringLoader.execute();
        }
    }

    private void cease() {
        if (stringLoader != null)
            stringLoader.cancel(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        cease();
    }

    @Override
    public void onStop() {
        super.onStop();
        cease();
    }

    @Override
    public void onDetach() {
        super.onDetach();
       cease();
    }

    public void setString(String list) {
        stringLoader = null;
        if (list == null) {
            finished = true;
            getArguments().putBoolean(ARG_FIN, true);
            return;
        }
        stringList.add(list);
        stringAdapter.notifyDataSetChanged();
        getArguments().putStringArrayList(ARG_LIST, stringList);
        stringLoader = new StringLoader(this);
        stringLoader.execute();
    }

}
