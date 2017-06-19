package com.skkk.boiledwaternote;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.skkk.boiledwaternote.CostomViews.RichEditView;

public class TestActivity extends AppCompatActivity {
    private RichEditView richEditView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        richEditView= (RichEditView) findViewById(R.id.rev_edit);

    }
}
