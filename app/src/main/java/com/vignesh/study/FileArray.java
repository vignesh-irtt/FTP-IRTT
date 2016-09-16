package com.vignesh.study;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FileArray extends ArrayAdapter<File>  {

    private Context c;
    private int id;
    private List<File> items;

    public FileArray(Context context, int textViewResourceId, List<File> objects) {
super(context,textViewResourceId,objects);
        c = context;
        id = textViewResourceId;
        items = objects;
    }
    public File getItem(int i)
    {
        return items.get(i);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(id, null);
        }

        final File o = items.get(position);
        if (o != null) {
            TextView t1 = (TextView) v.findViewById(R.id.TextView01);
            TextView t2 = (TextView) v.findViewById(R.id.TextView02);
            TextView t3 = (TextView) v.findViewById(R.id.TextViewDate);
                       /* Take the ImageView from layout and set the city's image */
            ImageView imageCity = (ImageView) v.findViewById(R.id.fd_Icon1);
            Bitmap dir = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.folder);
            Bitmap dirScaled = Bitmap.createScaledBitmap(dir, 150, 100, true);
            Bitmap file = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.file);
            Bitmap fileScaled = Bitmap.createScaledBitmap(file, 150, 100, true);
            if(t1!=null)
                t1.setText(o.getName());
            imageCity.setImageBitmap(fileScaled);
            if(t2!=null) {
                if (o.getImage() == "directory_up" || o.getImage() == "directory_icon") {
                    t2.setText(o.getData());
                    imageCity.setImageBitmap(dirScaled);
                }
                else if (Integer.parseInt(o.getData()) < 1024)
                    t2.setText(o.getData() + "B");
                else if (Integer.parseInt(o.getData()) / 1024 < 1024)
                    t2.setText(Integer.parseInt(o.getData()) / 1024 + "KB");
                else
                    t2.setText(Integer.parseInt(o.getData()) / 1024/1024 + "MB");
            }
            if(t3!=null)
                t3.setText(o.getDate());
        }
        return v;
    }
}