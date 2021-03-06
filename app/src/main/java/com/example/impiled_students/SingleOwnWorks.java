package com.example.impiled_students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SingleOwnWorks extends AppCompatActivity {
    private String Works_Id = null;
    ImageView back,pic;
    TextView works_name,works_author,works_content;
    DatabaseReference worksref, userref;
    Button update, delete;
    Query query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_own_works);
        Works_Id = getIntent().getExtras().getString("works_key");
        works_name = (TextView) findViewById(R.id.works_name);
        works_author = (TextView) findViewById(R.id.author);
        works_content = (TextView) findViewById(R.id.content);
        back = (ImageView) findViewById(R.id.backers2);
        pic = (ImageView) findViewById(R.id.pic);
        update = (Button) findViewById(R.id.upda);
        delete = (Button) findViewById(R.id.dele);
        worksref = FirebaseDatabase.getInstance().getReference("journ_post");
        userref = FirebaseDatabase.getInstance().getReference("students");
        final String works = Works_Id;

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SingleOwnWorks.this, UpdateOwnWorks.class);
                i.putExtra("update_id",works);
                startActivity(i);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                worksref.child(Works_Id).removeValue();
                finish();
            }
        });

        worksref.child(Works_Id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String journ_title = (String) dataSnapshot.child("journ_title").getValue();
                String journ_author = (String) dataSnapshot.child("journ_author").getValue();
                String journ_pic = (String) dataSnapshot.child("journ_photo").getValue();
                String journ_content = (String) dataSnapshot.child("journ_content").getValue();

                works_name.setText(journ_title);
                works_content.setText(journ_content);
                Picasso.with(SingleOwnWorks.this).load(journ_pic).into(pic);

                userref.child(journ_author).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String first = (String) dataSnapshot.child("student_fname").getValue();
                        String last = (String) dataSnapshot.child("student_lname").getValue();
                        works_author.setText(first + " " + last);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }
}
