package com.example.mannai.sos.dummy;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    public static  void clearItems(){
        ITEMS.clear();
        ITEM_MAP.clear();
    }

    public static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DummyItem createDummyItem(int position) {
        return new DummyItem(String.valueOf(position), "Name" , "contact", "dob","condition","location");
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
      //  public final String content;
      //  public final String details;
      public final String name;
        public final String contact;
        public final String dob;
        public final String condition;
        public final String location;
        public  String bloodType="";
        public  String medications="";
        public  String allergies="";
        public  String weight="";
        public  String remarks="";
        public String requested_at;
        public double latitude;
        public double longitude;
        public String req_id;
        public  String status;
        public String assignee;


        public DummyItem(String id, String n, String c, String d, String cnd, String l) {
            this.id = id;
            this.name=n;
            this.contact=c;
            this.dob=d;
            this.location=l;
            this.condition=cnd;
            //this.requested_at = Calendar.getInstance().getTime();
        }

        public DummyItem(String id, String n, String c, String d, String cnd, String l,
                         String b, String m, String a, String w, String r,double lt,double ln
                        ,String req, String s, String ag,String ra) {
            this.id = id;
            this.name=n;
            this.contact=c;
            this.dob=d;
            this.location=l;
            this.condition=cnd;
            this.bloodType=b;
            this.medications=m;
            this.allergies=a;
            this.weight=w;
            this.remarks=r;
            this.requested_at = ra;// Calendar.getInstance().getTime();
            this.latitude=lt;
            this.longitude=ln;
            this.req_id=req;
            this.status=s;
            this.assignee=ag;

            Log.i("DBX","init: "+this.dob);
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
