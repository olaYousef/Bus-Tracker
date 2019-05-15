package com.example.bustrucker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class RoutsActivity extends AppCompatActivity {


    ListView routsListView ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routs);

        routsListView=findViewById(R.id.routsListView) ;

        final String routsList []={"Amman to Irbid" ,"Irbid to JUST" ,"JUST to Amman"} ;
        ArrayAdapter <String> routsAdapter =new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1 ,routsList) ;

        routsListView.setAdapter(routsAdapter);

        routsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(RoutsActivity.this, routsList[i], Toast.LENGTH_SHORT).show();
            }
        });


    }
}
