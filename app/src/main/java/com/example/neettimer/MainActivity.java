package com.example.neettimer;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private TextView daysLeftText;
    private TextView timerText;
    private TextView monthsDaysText;
    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        daysLeftText = findViewById(R.id.days_left_text);
        timerText = findViewById(R.id.timer_text);
        monthsDaysText = findViewById(R.id.months_days_text);

        // Update the days left and start the countdown timer
        updateCountdown();
    }

    private void updateCountdown() {
        // Get the NEET exam date
        Calendar neetDate = Calendar.getInstance();
        neetDate.set(2025, Calendar.MAY, 4, 0, 0, 0); // NEET date: May 4, 2025

        // Create a runnable to update the countdown every second
        runnable = new Runnable() {
            @Override
            public void run() {
                Calendar currentDate = Calendar.getInstance();
                long diffInMillis = neetDate.getTimeInMillis() - currentDate.getTimeInMillis();

                // Calculate days, hours, minutes, and seconds left
                long days = TimeUnit.MILLISECONDS.toDays(diffInMillis);
                long hours = TimeUnit.MILLISECONDS.toHours(diffInMillis) % 24;
                long minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis) % 60;
                long seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis) % 60;

                // Calculate months and days left
                int[] monthsDays = calculateMonthsAndDaysLeft(currentDate, neetDate);

                // Update the TextViews
                daysLeftText.setText(String.format(Locale.getDefault(), "%d Days Left", days));
                timerText.setText(String.format(Locale.getDefault(), "%02d:%02d:%02d Left", hours, minutes, seconds));
                monthsDaysText.setText(String.format(Locale.getDefault(), "%d Months, %d Days Left", monthsDays[0], monthsDays[1]));

                // Re-run the handler every second
                handler.postDelayed(this, 1000);
            }
        };

        // Start the countdown
        handler.post(runnable);
    }

    private int[] calculateMonthsAndDaysLeft(Calendar currentDate, Calendar neetDate) {
        // Create a copy of the current date for manipulation
        Calendar tempCurrent = (Calendar) currentDate.clone();

        int monthsLeft = 0;
        int daysLeft = 0;

        // Calculate months left by incrementing the current date's month until we reach the target month
        while (tempCurrent.before(neetDate)) {
            tempCurrent.add(Calendar.MONTH, 1);
            if (tempCurrent.before(neetDate) || tempCurrent.equals(neetDate)) {
                monthsLeft++;
            }
        }

        // Reset the tempCurrent back by one month to calculate remaining days
        tempCurrent.add(Calendar.MONTH, -1);

        // Calculate the remaining days after subtracting full months
        daysLeft = neetDate.get(Calendar.DAY_OF_MONTH) - tempCurrent.get(Calendar.DAY_OF_MONTH);
        if (daysLeft < 0) {
            // If negative, we need to adjust for the previous month's days
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
