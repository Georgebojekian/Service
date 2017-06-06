package com.example.user.service;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by User on 6/2/2017.
 */

public class FragmentExample extends Fragment {

    private FragmentListener fragmentListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (fragmentListener != null) {
            fragmentListener.onFragmentCreated();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (fragmentListener != null) {
            fragmentListener.onFragmentDestroyed();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentListener = (FragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public interface FragmentListener {
        void onFragmentCreated();
        void onFragmentDestroyed();
    }
}
