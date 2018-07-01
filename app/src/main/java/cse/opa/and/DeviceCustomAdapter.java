package cse.opa.and;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cse.opa.and.Classes.Agent;

public class DeviceCustomAdapter extends ArrayAdapter<Agent>{

        private class ViewHolder {
            TextView textView1;
            ImageView image;
        }

        private ArrayList<Agent> objects;
        private LayoutInflater inflater;

        public DeviceCustomAdapter(Context context, int resource, ArrayList<Agent> objects) {
            super(context, resource, objects);
            inflater = LayoutInflater.from(context);
            this.objects = objects;
        }

        public int getCount() {
            return objects.size();
        }
        public Agent getItem(int position) {
            return objects.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.devices_list_entry, null);
                holder.textView1 = (TextView) convertView.findViewById(R.id.tv_device_name);
                holder.image = (ImageView) convertView.findViewById(R.id.iv_device_icon);
                convertView.setTag(holder);
            }

            else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView1.setText(objects.get(position).getName());
            if (objects.get(position).getType().equals("Router")){
                Log.v("TYPE","Router");
                holder.image.setBackgroundResource(R.drawable.ic_router1);
            }else if (objects.get(position).getType().equals("Switch")){
                Log.v("TYPE","Switch");
                holder.image.setBackgroundResource(R.drawable.ic_switch1);
            }
            else if (objects.get(position).getType().equals("Host")){
                Log.v("TYPE","Host");
                holder.image.setBackgroundResource(R.drawable.ic_pc1);
            }
            return convertView;
        }

    }

