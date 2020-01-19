package co.beats;

import androidx.annotation.NonNull;
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
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.imangazaliev.circlemenu.CircleMenu;
import com.imangazaliev.circlemenu.CircleMenuButton;
import com.sofakingforever.stars.AnimatedStarsView;

public class DashboardActivity extends AppCompatActivity {
    AnimatedStarsView starsView;
    CircleMenu menu;
    ImageView logo;

    SharedPreferences users;
    SharedPreferences.Editor useredit;
    @Override
    protected void onStart() {
        super.onStart();
        starsView.onStart();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                menu.open(true);
            }
        }, 700);
    }

    @Override
    protected void onPause() {
        super.onPause();
        menu.close(false);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        users = getSharedPreferences("users", MODE_PRIVATE);
        useredit = users.edit();

        setContentView(R.layout.activity_dashboard);
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

        menu = findViewById(R.id.menu);
        menu.setEventListener(new CircleMenu.EventListener() {
            @Override public void onMenuOpenAnimationStart() {}
            @Override public void onMenuOpenAnimationEnd() {}
            @Override public void onMenuCloseAnimationStart() {}
            @Override public void onMenuCloseAnimationEnd() {}
            @Override public void onButtonClickAnimationStart(@NonNull CircleMenuButton menuButton) {}

            @Override
            public void onButtonClickAnimationEnd(@NonNull CircleMenuButton menuButton) {
                String buttonHint = menuButton.getHintText();
                Intent intent = new Intent(DashboardActivity.this, VisualizeActivity.class);
                if(buttonHint.equals("When there is no movement")) intent.putExtra("mode", 0);
                else if(buttonHint.equals("When binaural beats are not applied")) intent.putExtra("mode", 1);
                else if(buttonHint.equals("When binaural beats are applied")) intent.putExtra("mode", 2);
                else if(buttonHint.equals("Logout")){
                    useredit.putString("currUser", null);
                    useredit.apply();
                    Intent intent1 = new Intent(DashboardActivity.this, LoginActivity.class);
                    startActivity(intent1);
                    DashboardActivity.this.finish();
                    return;
                }
                startActivity(intent);
            }
        });
    }
}
