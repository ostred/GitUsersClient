package ru.test.gitusersclient;

import android.app.ListActivity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    public  MyAdapter adapter;
    ProgressBar pblist;
    ListView lists;
    List<User> mUserlist = new ArrayList<User>();
    int mRequestIterator=1;
    Cursor cursor;
    int mCursorCount;

    final Uri CONTACT_URI = Uri
            .parse("content://ru.test.providers.mGitClient/USERs");

    final String USER_NAME = "name";
    final String USER_AVATAR = "avatar";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lists =(ListView)findViewById(R.id.listView);
        pblist=(ProgressBar)findViewById(R.id.progressBar1);
        pblist.setVisibility(View.VISIBLE);

        cursor = getContentResolver().query(CONTACT_URI, null, null,
                null, null);
        startManagingCursor(cursor);

        EventBus.getDefault().register(this);
        if(mRequestIterator==0) {
            reload_async("0");
        }else if(cursor.getCount()>0){
            while(cursor.moveToNext()){
                User temp=new User(cursor.getString(1),cursor.getString(2));
                mUserlist.add(temp);
            }
        }

        adapter = new MyAdapter(this, R.layout.rowitem, mUserlist);
        lists.setAdapter(adapter);

        lists.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                if(scrollState == SCROLL_STATE_IDLE)
                {
                    if(isOnline()) {
                        reload_async(String.valueOf(mRequestIterator * 46));
                       mRequestIterator++;
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }
        });
    }

    public  void reload_async(String mx_id){
        JSONParser asyncTask =new JSONParser();
        asyncTask.execute(mx_id);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent event){
        if(event.getResult()!=null) {
            mCursorCount = cursor.getCount();
            if (mCursorCount < mRequestIterator) {
             //   for (User object : mUserlist) {
                for(int i=0;i<mCursorCount;i++){
                    ContentValues cv = new ContentValues();
                    cv.put(USER_NAME, event.getResult().get(i).getlogin());
                    cv.put(USER_AVATAR, event.getResult().get(i).getavatar());
                    Uri newUri = getContentResolver().insert(CONTACT_URI, cv);
                }
            }
            if (adapter == null) {
                mUserlist = event.getResult();
            } else {
                mUserlist.addAll(event.getResult());
            }
            adapter.notifyDataSetChanged();
            pblist.setVisibility(View.GONE);
        }
    }
}
