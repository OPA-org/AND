package cse.opa.and;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.view.LayoutInflater;
import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<CustomObject> {
    private class ViewHolder {
        TextView textView1;
        TextView textView2;
        TextView textView3;
    }

    private ArrayList<CustomObject> objects;
    private LayoutInflater inflater;

    public CustomAdapter(Context context, int resource, ArrayList<CustomObject> objects) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(context);
        this.objects = objects;
    }

    public int getCount() {
        return objects.size();
    }
    public CustomObject getItem(int position) {
        return objects.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.listview, null);
                holder.textView1 = (TextView) convertView.findViewById(R.id.tv_username);
                holder.textView2 = (TextView) convertView.findViewById(R.id.tv_topology);
                holder.textView3 = (TextView) convertView.findViewById(R.id.tv_date);
                convertView.setTag(holder);

        }

        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView1.setText(objects.get(position).getUsername());
        holder.textView2.setText(objects.get(position).getTopologyname());
        holder.textView3.setText(objects.get(position).getDate());
        return convertView;
    }

}
