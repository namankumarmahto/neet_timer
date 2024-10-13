package com.example.neettimer;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Neet_widget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // Get the remaining days for NEET exam
        int daysLeft = calculateDaysLeft();

        // Update the widget layout
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.neet_widget);
        views.setTextViewText(R.id.neet_days_left, "Days Left: " + daysLeft);

        // Update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private int calculateDaysLeft() {
        // Set the NEET exam date
        Calendar neetDate = Calendar.getInstance();
        neetDate.set(2025, Calendar.MAY, 4); // NEET Date

        // Get the current date
        Calendar today = Calendar.getInstance();

        // Calculate the difference in days
        long diffInMillis = neetDate.getTimeInMillis() - today.getTimeInMillis();
        return (int) (diffInMillis / (1000 * 60 * 60 * 24));
    }
}
