package com.skkk.boiledwaternote.Views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skkk.boiledwaternote.Modles.Note;
import com.skkk.boiledwaternote.Presenters.NoteList.NoteListPresenter;
import com.skkk.boiledwaternote.R;

import java.util.List;


public class NoteListFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private NoteListPresenter noteListPresenter;

    private String mParam1;
    private String mParam2;



    public static NoteListFragment newInstance(String param1, String param2) {
        NoteListFragment fragment = new NoteListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_note_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        noteListPresenter=new NoteListPresenter(getContext());

        TextView tvTest = (TextView) view.findViewById(R.id.tv_test);
        
        List<Note> list = noteListPresenter.getNotes();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            stringBuffer.append(list.get(0).getNoteImages().get(0).getPath());
            stringBuffer.append("\n");
        }
        tvTest.setText(stringBuffer);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
