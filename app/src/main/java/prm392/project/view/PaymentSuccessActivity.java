package prm392.project.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import prm392.project.R;

public class PaymentSuccessActivity extends AppCompatActivity {

    private Button returnHomeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        returnHomeButton = findViewById(R.id.returnHomeButton);

        returnHomeButton.setOnClickListener(v -> {
            Intent intent = new Intent(PaymentSuccessActivity.this, HomeActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }
}
