package ru.test.gitusersclient;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

public class MyAdapter  extends ArrayAdapter< List<User> > {
    private final Context context;
    private final List<User>  names;
    public MyAdapter(Context context, int itemResId, List<User> names) {
        super(context, itemResId);
        this.context = context;
        this.names = names;

    }
    @Override
    public int getCount()
    {
        return names.size();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            rowView = inflater.inflate(R.layout.rowitem, null);
        }
        User mUser = names.get(position);
        TextView singlename = (TextView) rowView.findViewById(R.id.textView6);
        ImageView mcoverView=(ImageView) rowView.findViewById(R.id.imageView);
        singlename.setText(mUser.getlogin());
        Picasso.with(context)
                .load(mUser.getavatar())
                .into(mcoverView);
        return rowView;
    }
}