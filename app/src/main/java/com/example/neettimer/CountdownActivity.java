package com.example.neettimer;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CountdownActivity extends AppCompatActivity {

    private TextView daysLeftText;
    private TextView timerText;
    private TextView monthsDaysText;
    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown); // Separate layout for countdown

        daysLeftText = findViewById(R.id.days_left_text);
        timerText = findViewById(R.id.timer_text);
        monthsDaysText = findViewById(R.id.months_days_text);

        updateCountdown();
    }

    private void updateCountdown() {
        Calendar neetDate = Calendar.getInstance();
        neetDate.set(2025, Calendar.MAY, 4, 0, 0, 0); // NEET exam date: May 4, 2025

        runnable = new Runnable() {
            @Override
            public void run() {
                Calendar currentDate = Calendar.getInstance();
                long diffInMillis = neetDate.getTimeInMillis() - currentDate.getTimeInMillis();

                long days = TimeUnit.MILLISECONDS.toDays(diffInMillis);
                long hours = TimeUnit.MILLISECONDS.toHours(diffInMillis) % 24;
                long minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis) % 60;
                long seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis) % 60;

                int[] monthsDays = calculateMonthsAndDaysLeft(currentDate, neetDate);

                daysLeftText.setText(String.format(Locale.getDefault(), "%d Days Left", days));
                timerText.setText(String.format(Locale.getDefault(), "%02d:%02d:%02d Left", hours, minutes, seconds));
                monthsDaysText.setText(String.format(Locale.getDefault(), "%d Months, %d Days Left", monthsDays[0], monthsDays[1]));

                handler.postDelayed(this, 1000);
            }
        };

        handler.post(runnable);
    }

    private int[] calculateMonthsAndDaysLeft(Calendar currentDate, Calendar neetDate) {
        Calendar tempCurrent = (Calendar) currentDate.clone();

        int monthsLeft = 0;
        int daysLeft = 0;

        while (tempCurrent.before(neetDate)) {
            tempCurrent.add(Calendar.MONTH, 1);
            if (tempCurrent.before(neetDate) || tempCurrent.equals(neetDate)) {
                monthsLeft++;
            }
        }

        tempCurrent.add(Calendar.MONTH, -1);
        daysLeft = neetDate.get(Calendar.DAY_OF_MONTH) - tempCurrent.get(Calendar.DAY_OF_MONTH);

        if (daysLeft < 0) {
            daysLeft += tempCurrent.getActualMaximum(Calendar.DAY_OF_MONTH);
            monthsLeft--;
        }

        return new int[]{monthsLeft, daysLeft};
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
