package com.example.android.smartalarm;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by seema_suresh on 2/22/15.
 */
public class FragmentEmpty extends Fragment {


        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {

            //Inflate the layout for this fragment

            return inflater.inflate(
                    R.layout.fragment_empty, container, false);
        }



}
