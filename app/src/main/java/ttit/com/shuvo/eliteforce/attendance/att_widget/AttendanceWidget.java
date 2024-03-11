package ttit.com.shuvo.eliteforce.attendance.att_widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import ttit.com.shuvo.eliteforce.R;

public class AttendanceWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId: appWidgetIds) {
            Intent intent = new Intent(context, AttWidgetReceiver.class);
            intent.setAction("COM_TTIT_ATTENDANCE");
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent,0);
            PendingIntent p = PendingIntent.getBroadcast(context, 0, intent,PendingIntent.FLAG_IMMUTABLE);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.attendance_widget);
            views.setViewVisibility(R.id.attendance_widget_progress_bar, View.GONE);
            views.setViewVisibility(R.id.attendance_widget_button, View.VISIBLE);
            views.setOnClickPendingIntent(R.id.attendance_widget_button,p);

            appWidgetManager.updateAppWidget(appWidgetId,views);
        }
    }
}
