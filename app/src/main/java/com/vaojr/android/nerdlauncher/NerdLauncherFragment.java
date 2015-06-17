package com.vaojr.android.nerdlauncher;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class NerdLauncherFragment extends ListFragment {
    private static final String TAG = "NerdLauncherFragment";
    private List<RunningTaskInfo> mRunningApps;
    private ActivityManager mActivityManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityManager = (ActivityManager)(getActivity().getSystemService(Activity.ACTIVITY_SERVICE));

        try {
            mRunningApps = mActivityManager.getRunningTasks(Integer.MAX_VALUE);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "no running processes", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        Log.i(TAG, "I've found " + mRunningApps.size() + " running activities.");

        ArrayAdapter<RunningTaskInfo> adapter = new ArrayAdapter<RunningTaskInfo>(
                getActivity(), android.R.layout.simple_list_item_1, mRunningApps) {
            public View getView(int pos, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getActivity().getLayoutInflater().inflate(
                            android.R.layout.simple_list_item_1, null);
                }

                RunningTaskInfo rti = getItem(pos);

                // Documentation says that simple_list_view_item_1 is a TextView,
                // so cast it so that you can set its text value
                TextView tv = (TextView)convertView.findViewById(android.R.id.text1);
                tv.setText(rti.baseActivity.getPackageName());
                return convertView;
            }
        };

        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        RunningTaskInfo rti = (ActivityManager.RunningTaskInfo)l.getAdapter().getItem(position);
        mActivityManager.moveTaskToFront(rti.id, 0);
    }
}
