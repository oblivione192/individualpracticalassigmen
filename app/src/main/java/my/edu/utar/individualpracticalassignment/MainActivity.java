package my.edu.utar.individualpracticalassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;

import my.edu.utar.individualpracticalassignment.comparenumbers.CompareNumbers;
import my.edu.utar.individualpracticalassignment.formnumbers.FormNumbers;
import my.edu.utar.individualpracticalassignment.arrangenumbers.ArrangeNumbers;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView =  findViewById(R.id.listViewOptions);
        String[] options = {"Lets Form Numbers","Lets Compare Numbers","Lets Arrange Numbers"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, options);
        listView.setAdapter(adapter);

        // Handle item clicks
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent;
            if(position == 0) {
                intent = new Intent(MainActivity.this, FormNumbers.class);
                startActivity(intent);
            }
            else if(position == 1){
                intent = new Intent(MainActivity.this, CompareNumbers.class);
                startActivity(intent);
            }
            else if(position == 2){
                intent = new Intent(MainActivity.this, ArrangeNumbers.class);
                startActivity(intent);
            }


        });

        ImageView gifImageView = findViewById(R.id.gifImageView);

        // Load the GIF from drawable or assets
        Glide.with(this)
                .asGif()
                .load(R.drawable.numbers)  // Put your GIF in res/drawable
                .into(gifImageView);
    }
}