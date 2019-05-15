package com.example.bustrucker;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;


public class path extends AppCompatActivity {

    Button findBtn;
    Spinner fromSpinner ,toSpinner ;

    FirebaseDatabase database ;

    DriverModel drivers ;


    public  ArrayList<DriverModel> driversList ;
    public ArrayList<BusesModel> busesList ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);

        findBtn = findViewById(R.id.findBtn);
        fromSpinner=findViewById(R.id.fromSpinner) ;
        toSpinner=findViewById(R.id.toSpinner) ;


        driversList=new ArrayList<>();
        busesList=new ArrayList<>();


        readDataFromFirebase();



        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int fromPos =fromSpinner.getSelectedItemPosition() ;
                int toPos =toSpinner.getSelectedItemPosition() ;

                if( ( fromPos != 0 && toPos != 0 ) && (fromPos!=toPos) ) {
                    Intent intent = new Intent(path.this, BusesLiveLocationActivity.class);

                    intent.putExtra("fromPos" ,fromPos+"") ;
                    intent.putExtra("toPos" ,toPos+"") ;
                    intent.putParcelableArrayListExtra("busesList" , busesList) ; //maybe not work
                    intent.putParcelableArrayListExtra("driversList" ,driversList) ;
                    startActivity(intent);
                }else{
                    Toast.makeText(path.this, "Please select a correct Root.. !!!", Toast.LENGTH_SHORT).show();
                }

            } });

    }

    public void   readDataFromFirebase()
    {//maybe call function putlocation here


        database = FirebaseDatabase.getInstance();
        DatabaseReference myRefForDivers = database.getReference("Drivers");
        DatabaseReference myRefForBuses = database.getReference("Buses");

        myRefForDivers.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                driversList.add(dataSnapshot.getValue(DriverModel.class));

                Toast.makeText(path.this, "read data Driveres done ..", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(path.this, "read data failed !! ..", Toast.LENGTH_LONG).show();


            }
        });


        myRefForBuses.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                busesList.add(dataSnapshot.getValue(BusesModel.class));

                Toast.makeText(path.this, "read data Buses done ..", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


}
