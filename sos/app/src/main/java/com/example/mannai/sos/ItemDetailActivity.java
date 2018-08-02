package com.example.mannai.sos;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mannai.sos.dummy.DummyContent;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.example.mannai.sos.ItemDetailFragment.ARG_ITEM_ID;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ItemListActivity}.
 */
public class ItemDetailActivity extends AppCompatActivity {

    FloatingActionButton fab;
    DummyContent.DummyItem mItem;
    TextView statusTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        String id = this.getIntent().getStringExtra(ItemDetailFragment.ARG_ITEM_ID);
        mItem = DummyContent.ITEM_MAP.get(id);

        Log.i("LGX","mItem "+mItem.name);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        statusTextView = (TextView) findViewById(R.id.status);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG).setAction("Action", null).show();

            assign(mItem.latitude,mItem.longitude,mItem.req_id);}
        });

        if ( mItem.status.length() >0 && !mItem.status.equalsIgnoreCase("null")){
            fab.setVisibility(View.INVISIBLE);
            statusTextView.setText(mItem.assignee);
            statusTextView.setVisibility(View.VISIBLE);
        }else{

            fab.setVisibility(View.VISIBLE);
            statusTextView.setVisibility(View.INVISIBLE);
        }

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ARG_ITEM_ID,
                    getIntent().getStringExtra(ARG_ITEM_ID));
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit();
        }
    }


    void assign(double lat, double lng, String id){
        //loading = true;
        WSHttpClient client = new WSHttpClient(getApplicationContext());
        RequestParams params = new RequestParams();
        params.put("lat", lat);
        params.put("lng", lng);
        params.put("id", id);


        client.post("assign.php", params, new JsonHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                Log.e("ASX", "onFailure: " + throwable.getMessage() + " - " + response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray search = null;
                JSONArray l;
                Log.i("GFX","response "+response.toString());

                try {

                    String status = response.getString("status");
                    if ( status.equals("INVALID")){
                        Log.i("ASX","assign invalid");
                        //Toast.makeText(UserList.this, R.string.onfailure, Toast.LENGTH_LONG ).show();
                    }else{
                        Log.i("ASX","assign success");

                        String assignee = response.getString("assignee");
                        //Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        Toast.makeText(ItemDetailActivity.this, "Assigned to "+assignee, Toast.LENGTH_LONG ).show();
                        fab.setVisibility(View.INVISIBLE);
                        statusTextView.setText(assignee);
                        statusTextView.setVisibility(View.VISIBLE);
                    }



                }catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("CX",e.getMessage());
                }


            }

        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, ItemListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
