package cse.opa.and;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ListOfTopologies extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_topologies);
        ArrayList<CustomObject> objects = new ArrayList<CustomObject>();
        String [] date={"21/2/2017","2/5/2017","5/12/2017","17/8/2017","15/7/2017","9/9/2017","21/12/2017","1/1/2018"};
        int u;
        for(int i=0;i<8;i++){
            u=i+1;
            objects.add(new CustomObject("TopologyActivity"+u,"User"+u,date[i]));
        }

        CustomAdapter customAdapter = new CustomAdapter(this, R.layout.listview, objects);
        ListView lv = (ListView) findViewById(R.id.lv);
        //lv.setAdapter(testadapter);
        lv.setAdapter(customAdapter);
        final ArrayList<CustomObject> finalObjects = objects;
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //String str = ((TextView) arg1).getText().toString();
                // Toast.makeText(getBaseContext(),str, Toast.LENGTH_LONG).show();

                startActivity(new Intent(ListOfTopologies.this,TopologyActivity.class));

            }
        });
    }
}
