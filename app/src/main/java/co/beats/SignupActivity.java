package co.beats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sofakingforever.stars.AnimatedStarsView;

public class SignupActivity extends AppCompatActivity {
    AnimatedStarsView starsView;
    ImageView logo;
    TextView haveaccount;

    EditText email,pass,conpass;
    Button signup;

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

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        users = getSharedPreferences("users", MODE_PRIVATE);
        useredit = users.edit();

        setContentView(R.layout.activity_signup);

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

        users = getSharedPreferences("users", MODE_PRIVATE);
        useredit = users.edit();

        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        conpass = findViewById(R.id.conpass);

        signup = findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailVal = email.getText().toString();
                String passVal = pass.getText().toString();
                String conpassVal = conpass.getText().toString();

                if(passVal.equals(conpassVal)){
                    if(users.getString(emailVal, null) == null) {
                        useredit.putString(emailVal, conpassVal);
                        useredit.apply();
                        Toast.makeText(SignupActivity.this, "Account Created Successfully!", Toast.LENGTH_SHORT).show();
                        haveaccount.performClick();
                    }
                    else Toast.makeText(SignupActivity.this, "Account with this email already exist!", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(SignupActivity.this, "Confirm Password doesn't match the entered Password.", Toast.LENGTH_SHORT).show();
            }
        });

        haveaccount = findViewById(R.id.haveaccount);
        haveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }
}
