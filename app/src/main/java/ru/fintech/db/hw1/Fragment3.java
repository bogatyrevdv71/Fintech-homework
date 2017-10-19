package ru.fintech.db.hw1;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment3.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment3 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private int mParam2;


    public Fragment3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment3.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment3 newInstance(int param1, int param2) {
        Fragment3 fragment = new Fragment3();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_fragment3, container
                ,false);
        Button button_1 = v.findViewById((R.id.button1_f3));
        Button button_2 = v.findViewById((R.id.button2_f3));
        Button button_a1 = v.findViewById((R.id.button_a_1));
        Button button_a2 = v.findViewById((R.id.button_a_2));
        Button button_a3 = v.findViewById((R.id.button_a_3));
        Button button_a4 = v.findViewById((R.id.button_a_4));
        button_a1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container,
                                    Fragment4.newInstance(mParam1, mParam2, 1),
                                    "fourth_fragment")
                        .addToBackStack("fourth_fragment").commit();
            }
        });
        button_a2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,
                                Fragment4.newInstance(mParam1, mParam2, 2),
                                "fourth_fragment").addToBackStack("fourth_fragment").commit();
            }
        });
        button_a3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,
                                Fragment4.newInstance(mParam1, mParam2, 3),
                                "fourth_fragment").addToBackStack("fourth_fragment").commit();
            }
        });
        button_a4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mParam2 == 0) {
                    Toast toast = Toast.makeText(getContext(),
                            "Division by zero? Not today",
                            Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container,
                                    Fragment4.newInstance(mParam1, mParam2, 4),
                                    "fourth_fragment").addToBackStack("fourth_fragment").commit();
                }
            }
        });
        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack("first_fragment", 0);
            }
        });
        button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack("second_fragment", 0);
            }
        });
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
