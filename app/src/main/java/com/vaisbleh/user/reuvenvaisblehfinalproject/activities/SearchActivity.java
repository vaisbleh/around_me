package com.vaisbleh.user.reuvenvaisblehfinalproject.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.vaisbleh.user.reuvenvaisblehfinalproject.R;
import com.vaisbleh.user.reuvenvaisblehfinalproject.model.Constants;
import com.vaisbleh.user.reuvenvaisblehfinalproject.services_asynctasks.SearchService;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener {

    private EditText editKeyword;
    private AlertDialog dialogDeleteAllFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.myTool);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        editKeyword = (EditText) findViewById(R.id.editKeyword);

        findViewById(R.id.btnSearchService).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnSearchService: //find places near user location
                Intent getSearchServiceIntent = new Intent(this, SearchService.class);
                String keyword = editKeyword.getText().toString();
                getSearchServiceIntent.putExtra(Constants.KEYWORD_EXTRA, keyword);
                startService(getSearchServiceIntent);
                Toast.makeText(this, "searching start", Toast.LENGTH_LONG).show();
                finish();
                break;
        }
    }
    // option menu**************************************
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_manu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.settings:
                startActivity(new Intent(this, PrefActivity.class));
                break;

            case R.id.deleteFavorites:

                dialogDeleteAllFavorites = new AlertDialog.Builder(this)
                        .setTitle("Delete all favorites?")
                        .setMessage("Are you sure")
                        .setPositiveButton("YES", this)
                        .setNegativeButton("NO", this)
                        .create();
                dialogDeleteAllFavorites.show();


                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if(dialogInterface == dialogDeleteAllFavorites){
            switch (i){
                case DialogInterface.BUTTON_POSITIVE:
                    getContentResolver().delete(Constants.CONTENT_URI_FAVORITES, null, null);
                    Toast.makeText(this, "favorites deleted", Toast.LENGTH_SHORT).show();
                    break;
            }


        }
    }
    //****************************************************************************

}
