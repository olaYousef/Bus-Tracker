package com.example.bustrucker;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class SourceDestination extends AppCompatActivity {

    AutoCompleteTextView sor, des;
    Button sea, call, current;
    TextView timePic, noOFava, bookCancel;
    DatabaseReference fire;
    String msg = "Do You Want to Book A Seat ?";
    String bntName = "Book Seat ";
    boolean bOrc  = true;
    int num;
    boolean b = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_destination);

        fire = FirebaseDatabase.getInstance().getReference().child("Driver").child("vFBazRDHytZB4ClXH8PDOJrKljE3");

/*        Initiation();
        setAdapter();*/


        fire.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                num = Integer.parseInt(dataSnapshot.child("Seat").getValue().toString());
                noOFava.setText(num + " Seat Available");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SourceDestination.this, "Can't get data from seat", Toast.LENGTH_SHORT).show();
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "0791526798", null)));
            }
        });

        sea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLoc(sor.getText().toString(), des.getText().toString());
            }
        });

        current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SourceDestination.this, "Source is Your Current Location ", Toast.LENGTH_SHORT).show();
            }
        });

        timePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(SourceDestination.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timePic.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        bookCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookOrCancel();
            }
        });
    }

/*    void setAdapter() {
        String[] entries = {"مجمع عمان الجديد", "مجمع الشيخ خليل", "مجمع الشمالي",
                "مجمع الاغوار", "مجمع الشمال", "مجمع المحطة", "مجمع الجنوب", "مجمع الأمير راشد", "مجمع الملك عبد الله",
                "مجمع السلط الخارجي", "مجمع البقعة", "مجمع دوار البلدية/ الجامعة الاردنية", "مجمع عمان الشمال",
                "مجمع جرش الجديد", "مجمع عجلون", "مجمع المفرق الشرقي", "مجمع المفرق الشمالي", "مجمع المفرق الغربي",
                "مجمع الكرك الخارجي (واد إطوي)", "مجمع الكرك الداخلي(البركة)", "مجمع معان",
                "المجمع الداخلي", "المجمع الخارجي", "مجمع الطفيلة الداخلي", "مجمع الطفيلة الخارجي"
                , "جامعة العلوم والتكنولوجيا", "جامعة اليرموك", "جامعةالبلقاء التطبيقيه كلية اربد الجامعيه إربد",
                "جامعة جدارا", "جامعة جرش", "جامعة جدارا", "جامعة اربد الاهليه", "الجامعة الاردنيه"
                , "جامعة فيلاديلفيا عمان", "جامعة عمان الاهليه", "جامعة الزيتونه", "الجامعة الالمانيه"
                , "جامعة العلوم التطبيقيه", "جامعة الاسراء", "جامعةالبتراء"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, entries);
        sor.setAdapter(adapter);
        des.setAdapter(adapter);
    }

    void Initiation() {
        sor = findViewById(R.id.source);
        des = findViewById(R.id.dest);
        noOFava = findViewById(R.id.noOfseat);
        bookCancel = findViewById(R.id.bookCancel);
        sea = findViewById(R.id.search);
        call = findViewById(R.id.contact);
        current = findViewById(R.id.current);
        timePic = findViewById(R.id.timePicker);
    }*/

    public void getLoc(String s, String d) {

        String loc = "";
        String loc2 = "";


        switch (s) {
            case "مجمع عمان الجديد":
                loc = "32.535162,35.869613";
                break;
            case "مجمع الشيخ خليل":
                loc = "32.549435,35.855564";
                break;
            case "مجمع الشمالي":
                loc = "32.567917,35.855414";
                break;
            case "مجمع الاغوار":
                loc = "32.552725,35.836445";
                break;
            case "مجمع الشمال":
                loc = "31.998379,35.919788";
                break;
            case "مجمع المحطة":
                loc = "31.962554,35.959571";
                break;
            case "مجمع الجنوب":
                loc = "31.918549,35.930024";
                break;
            case "مجمع الأمير راشد":
                loc = "32.055549,36.093301";
                break;
            case "مجمع الملك عبد الله":
                loc = "21.768208,39.100936";
                break;
            case "مجمع السلط الخارجي":
                loc = "32.035110,35.731078";
                break;
            case "مجمع البقعة":
                loc = "32.078315,35.836213";
                break;
            case "مجمع دوار البلدية/ الجامعة الاردنية":
                loc = "32.075699,35.843508";
                break;
            case "مجمع عمان الشمال":
                loc = "31.994768,35.919612";
                break;
            case "مجمع جرش الجديد":
                loc = "32.285858,35.895092";
                break;
            case "مجمع عجلون":
                loc = "31.994671,35.919496";
                break;
            case "مجمع المفرق الشرقي":
                loc = "32.341543,36.213113";
                break;
            case "مجمع المفرق الشمالي":
                loc = "32.340783,36.213828";
                break;
            case "مجمع المفرق الغربي":
                loc = "32.341961,36.216950";
                break;
            case "مجمع الكرك الخارجي (واد إطوي)":
                loc = "31.165034,35.739951";
                break;
            case "مجمع الكرك الداخلي(البركة)":
                loc = "31.165367,35.739505";
                break;
            case "مجمع معان":
                loc = "30.193529,35.738204";
                break;
            case "المجمع الداخلي":
                loc = "31.987926,35.848123";
                break;
            case "المجمع الخارجي":
                loc = "31.987926,35.848123";
                break;
            case "مجمع الطفيلة الداخلي":
                loc = "30.830770,35.603575";
                break;
            case "مجمع الطفيلة الخارجي":
                loc = "30.830770,35.603575";
                break;
            case "جامعة العلوم والتكنولوجيا":
                loc = "32.495049, 35.991147";
                break;

            case "جامعة اليرموك":
                loc = " 32.537951, 35.855261";
                break;

            case "جامعةالبلقاء التطبيقيه كلية اربد الجامعيه إربد":
                loc = " 32.545716, 35.861101";
                break;

            case "جامعة جدارا":
                loc = " 32.422416, 35.946848";
                break;

            case "جامعة جرش":
                loc = " 32.252126, 35.897774";
                break;

            case "جامعة اربد الاهليه":
                loc = " 32.406384, 35.950224";
                break;

            case "الجامعة الاردنيه":
                loc = " 32.016109, 35.869686";
                break;

            case "جامعة عمان الاهليه":
                loc = " 32.046888, 35.779691";
                break;

            case "جامعة فيلاديلفيا عمان":
                loc = " 32.007403, 35.949589";
                break;

            case "جامعة الزيتونه":
                loc = " 31.832708, 35.892734";
                break;

            case "الجامعة الالمانيه":
                loc = " 31.972365, 35.833943";
                break;

            case "جامعةالبتراء":
                loc = " 31.892698, 35.874615";
                break;

            case "جامعة العلوم التطبيقيه":
                loc = " 32.040026, 35.900420";
                break;

            case "جامعة الاسراء":
                loc = " 31.788956, 35.928890";
                break;


        }

        switch (d) {
            case "مجمع عمان الجديد":
                loc2 = "32.535162,35.869613";
                break;
            case "مجمع الشيخ خليل":
                loc2 = "32.549435,35.855564";
                break;
            case "مجمع الشمالي":
                loc2 = "32.567917,35.855414";
                break;
            case "مجمع الاغوار":
                loc2 = "32.552725,35.836445";
                break;
            case "مجمع الشمال":
                loc2 = "31.998379,35.919788";
                break;
            case "مجمع المحطة":
                loc2 = "31.962554,35.959571";
                break;
            case "مجمع الجنوب":
                loc2 = "31.918549,35.930024";
                break;
            case "مجمع الأمير راشد":
                loc2 = "32.055549,36.093301";
                break;
            case "مجمع الملك عبد الله":
                loc2 = "21.768208,39.100936";
                break;
            case "مجمع السلط الخارجي":
                loc2 = "32.035110,35.731078";
                break;
            case "مجمع البقعة":
                loc2 = "32.078315,35.836213";
                break;
            case "مجمع دوار البلدية/ الجامعة الاردنية":
                loc2 = "32.075699,35.843508";
                break;
            case "مجمع عمان الشمال":
                loc2 = "31.994768,35.919612";
                break;
            case "مجمع جرش الجديد":
                loc2 = "32.285858,35.895092";
                break;
            case "مجمع عجلون":
                loc2 = "31.994671,35.919496";
                break;
            case "مجمع المفرق الشرقي":
                loc2 = "32.341543,36.213113";
                break;
            case "مجمع المفرق الشمالي":
                loc2 = "32.340783,36.213828";
                break;
            case "مجمع المفرق الغربي":
                loc2 = "32.341961,36.216950";
                break;
            case "مجمع الكرك الخارجي (واد إطوي)":
                loc2 = "31.165034,35.739951";
                break;
            case "مجمع الكرك الداخلي(البركة)":
                loc2 = "31.165367,35.739505";
                break;
            case "مجمع معان":
                loc2 = "30.193529,35.738204";
                break;
            case "المجمع الداخلي":
                loc2 = "31.987926,35.848123";
                break;
            case "المجمع الخارجي":
                loc2 = "31.987926,35.848123";
                break;
            case "مجمع الطفيلة الداخلي":
                loc2 = "30.830770,35.603575";
                break;
            case "مجمع الطفيلة الخارجي":
                loc2 = "30.830770,35.603575";
                break;
            case "جامعة العلوم والتكنولوجيا":
                loc2 = "32.495049, 35.991147";
                break;

            case "جامعة اليرموك":
                loc2 = " 32.537951, 35.855261";
                break;

            case "جامعةالبلقاء التطبيقيه كلية اربد الجامعيه إربد":
                loc2 = " 32.545716, 35.861101";
                break;

            case "جامعة جدارا":
                loc2 = " 32.422416, 35.946848";
                break;

            case "جامعة جرش":
                loc2 = " 32.252126, 35.897774";
                break;

            case "جامعة اربد الاهليه":
                loc2 = " 32.406384, 35.950224";
                break;

            case "الجامعة الاردنيه":
                loc2 = " 32.016109, 35.869686";
                break;

            case "جامعة عمان الاهليه":
                loc2 = " 32.046888, 35.779691";
                break;

            case "جامعة فيلاديلفيا عمان":
                loc2 = " 32.007403, 35.949589";
                break;

            case "جامعة الزيتونه":
                loc2 = " 31.832708, 35.892734";
                break;

            case "الجامعة الالمانيه":
                loc2 = " 31.972365, 35.833943";
                break;

            case "جامعةالبتراء":
                loc2 = " 31.892698, 35.874615";
                break;

            case "جامعة العلوم التطبيقيه":
                loc2 = " 32.040026, 35.900420";
                break;

            case "جامعة الاسراء":
                loc2 = " 31.788956, 35.928890";
                break;

        }


        if (!s.equals("") && !d.equals("")) {
            Toast.makeText(this, "Source: " + s + "Destination: " + d, Toast.LENGTH_LONG).show();
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + loc + "&daddr=" + loc2));
            startActivity(i);
        } else if (s.equals("") && !d.equals("")) {
            Toast.makeText(this, "Source: " + "Your Location" + "Destination: " + d, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?daddr=" + loc2));
            startActivity(intent);
        } else
            Toast.makeText(this, "Please Select a location !", Toast.LENGTH_LONG).show();
    }

    void BookOrCancel() {

        AlertDialog.Builder builder = new AlertDialog.Builder(SourceDestination.this);
        builder.setTitle("Options Menu");
        builder.setMessage(msg);
        builder.setPositiveButton( bntName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (bOrc) // Booking
                {
                    msg = "Do You Want to Cancel A Seat ?";
                    bntName = "Cancel A Seat";
                    bOrc = false;
                    if ( !(num <= 0))
                        Book();
                    else
                        Toast.makeText(SourceDestination.this, "The bus is Full, You Can't Book ", Toast.LENGTH_LONG).show();

                } else // Canceling
                if (!bOrc) {
                    msg = "Do You Want to Book A Seat ?";
                    bntName = "Book A Seat";
                    bOrc = true;
                    if (num < 21)
                        Cancel();
                    else
                        Toast.makeText(SourceDestination.this, "The bus is Empty, You Can't Cancel ", Toast.LENGTH_LONG).show();

                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    void Book() { //Book
            fire.child("Seat").setValue(--num).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(SourceDestination.this, "Booked Sucessfully !", Toast.LENGTH_SHORT).show();
                    noOFava.setText(num + " Available");
                }
            });
    }

    void Cancel() {// Cancel
            fire.child("Seat").setValue(++num).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(SourceDestination.this, "Canceled Sucessfully !", Toast.LENGTH_SHORT).show();
                    noOFava.setText(num + " Available");
                }
            });
    }
}
