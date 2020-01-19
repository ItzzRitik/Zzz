package co.beats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompatExtras;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sofakingforever.stars.AnimatedStarsView;

public class LoginActivity extends AppCompatActivity {
    AnimatedStarsView starsView;
    ImageView logo;
    TextView create;

    EditText email,pass;
    Button login;

    SharedPreferences users;
    SharedPreferences.Editor useredit;

    @Override
    protected void onStart() {
        super.onStart();
        starsView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        starsView.onStop();
    }

    public Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    public void openDashboard(){
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        users = getSharedPreferences("users", MODE_PRIVATE);
        useredit = users.edit();
        if(users.getString("currUser", null) != null) {
            openDashboard();
            return;
        }
        else setContentView(R.layout.activity_login);

        starsView = findViewById(R.id.starsView);

        logo = findViewById(R.id.logo);
        int margin = 400;
        int halfMargin = margin / 2;
        int glowRadius = 180; // the glow radius
        int glowColor = Color.rgb(150, 200, 255); // the glow color
        Bitmap src = getBitmapFromVectorDrawable(this, R.drawable.moon_contrast); // The original image to use
        Bitmap alpha = src.extractAlpha(); // extract the alpha from the source image
        Bitmap bmp = Bitmap.createBitmap(src.getWidth() + margin, src.getHeight() + margin, Bitmap.Config.ARGB_8888); // The output bitmap (with the icon + glow)
        Canvas canvas = new Canvas(bmp);  // The canvas to paint on the image
        Paint paint = new Paint();
        paint.setColor(glowColor);
        paint.setMaskFilter(new BlurMaskFilter(glowRadius, BlurMaskFilter.Blur.OUTER)); // outer glow
        canvas.drawBitmap(alpha, halfMargin, halfMargin, paint);
        canvas.drawBitmap(src, halfMargin, halfMargin, null); // original icon
        logo.setImageBitmap(bmp);

        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);

        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailVal = email.getText().toString();
                String passVal = pass.getText().toString();
                String storedPass = users.getString(emailVal, null);
                if(storedPass != null){
                    if(storedPass.equals(passVal)) {
                        Toast.makeText(LoginActivity.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                        useredit.putString("currUser", emailVal);
                        useredit.apply();
                        openDashboard();
                    }
                    else Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(LoginActivity.this, "Account with this email doesn't exist.", Toast.LENGTH_SHORT).show();
            }
        });

        create = findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }
}
