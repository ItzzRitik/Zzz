package co.beats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DashboardActivity extends AppCompatActivity {
    Button noMovement, binaural, noBinaural;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        noMovement = findViewById(R.id.noMovement);
        noMovement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, VisualizeActivity.class);
                intent.putExtra("mode", 0);
                startActivity(intent);
            }
        });

        binaural = findViewById(R.id.binaural);
        binaural.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, VisualizeActivity.class);
                intent.putExtra("mode", 1);
                startActivity(intent);
            }
        });

        noBinaural = findViewById(R.id.noBinaural);
        noBinaural.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, VisualizeActivity.class);
                intent.putExtra("mode", 2);
                startActivity(intent);
            }
        });
    }
}
