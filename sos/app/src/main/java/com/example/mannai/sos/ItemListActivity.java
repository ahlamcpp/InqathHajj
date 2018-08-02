package com.example.mannai.sos;


import cz.msebera.android.httpclient.Header;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.mannai.sos.dummy.DummyContent;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    RecyclerView recyclerView;
    ArrayList<DummyContent.DummyItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        items = new ArrayList<DummyContent.DummyItem>();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG) .setAction("Action", null).show();

            }
        });

         recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
       // setupRecyclerView((RecyclerView) recyclerView);
loadRequests();
        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<DummyContent.DummyItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).id);
            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
            //dateFormat.format("yyyy-MM-dd hh:mm:ss a", new java.util.Date());
            String x = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(mValues.get(position).requested_at);
            //holder.mContentView.setText(dateFormat.format(mValues.get(position).requested_at));
            holder.mContentView.setText(x);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ItemDetailActivity.class);
                        intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        Log.i("DBX","click: "+holder.mItem.dob);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public DummyContent.DummyItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }

    void loadRequests(){
        //loading = true;
        WSHttpClient client = new WSHttpClient(getApplicationContext());
        RequestParams params = new RequestParams();
        //params.put("un", un);


        client.post("getHelps.php", params, new JsonHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                Log.e("XX", "onFailure: " + throwable.getMessage() + " - " + response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray search = null;
                DummyContent.DummyItem u[] = null;
                JSONArray l;
                Log.i("GFX","response "+response.toString());

                try {

                    String status = response.getString("status");
                    if ( status.equals("INVALID")){
                        //Toast.makeText(UserList.this, R.string.onfailure, Toast.LENGTH_LONG ).show();
                    }else{
                        l = response.getJSONArray("users");
                        //if ( refresh){ users.clear(); }
                        //if ( l.length()==0 || l.length()< 30){ end = true;  }
                        u = new DummyContent.DummyItem[l.length()];

                        for (int i = 0; i < l.length(); i++) {
                            JSONObject c = l.getJSONObject(i);


                            String name = c.getString("name");
                            String dob = c.getString("dob");
                            String id = c.getString("id");
                            String contact = c.getString("contact");
                            if (contact.equalsIgnoreCase("null"))   contact ="";
                            String condition = c.getString("condition");
                            String location = c.getString("location");
                            //String dob = c.getString("dob");
                            String blood = c.getString("bloodtype");
                            String medications = c.getString("medications");
                            String allergies = c.getString("allergies");
                            String weigt = c.getString("weight");
                            String remarks = c.getString("remarks");
                            String req = c.getString("req_id");
                            String stat = c.getString("status");
                            String assignee = c.getString("assignee");
                            double lat = c.getDouble("latitude");
                            double lng = c.getDouble("longitude");
                            boolean liked = false;
                            Log.i("DBX","load: "+dob);
                            u[i]= new DummyContent.DummyItem(id,name,contact,dob,condition,location,blood,
                                    medications,allergies,weigt,remarks,lat,lng,req,stat,assignee);
                            // new DummyItem(String.valueOf(position), "Name" , "contact", "dob","condition","location");

                            //usr=username;

                            items.add(u[i]);
                            DummyContent.addItem(u[i]);

                        }

                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("CX",e.getMessage());
                }

                //if ( end)  list.removeFooterView(footerView);
                //else if ( list.getFooterViewsCount()==0)
                //	list.addFooterView(footerView);

                //adapter.notifyDataSetChanged();
                recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(items));
                //refresh=false;

                //loading = false;


            }

        });


    }


    void addRequest(double lat,double lng, String id){
        //loading = true;
        WSHttpClient client = new WSHttpClient(getApplicationContext());
        RequestParams params = new RequestParams();
        params.put("lat", lat);
        params.put("lng", lng);
        params.put("id", id);


        client.post("addHelp.php", params, new JsonHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                Log.e("XX", "onFailure: " + throwable.getMessage() + " - " + response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray search = null;
                DummyContent.DummyItem u[] = null;
                JSONArray l;
                Log.i("GFX","response "+response.toString());

                try {

                    String status = response.getString("status");
                    if ( status.equals("INVALID")){
                        //Toast.makeText(UserList.this, R.string.onfailure, Toast.LENGTH_LONG ).show();
                    }else{


                        }



                }catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("CX",e.getMessage());
                }


            }

        });


    }
}
