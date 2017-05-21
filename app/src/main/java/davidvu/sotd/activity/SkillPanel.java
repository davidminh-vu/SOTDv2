package davidvu.sotd.activity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import davidvu.sotd.Constants;
import davidvu.sotd.MyListStorage;
import davidvu.sotd.R;
import davidvu.sotd.RequestHandler;

public class SkillPanel extends AppCompatActivity {

    public static String SkillPanelName;
    private String schwierigkeit, kategorie, tutorial;
    private int anzahlb, totalb;
    private double bewertung;

    private TextView skill;
    private ImageView imageView;
    private RatingBar rate;
    private Button submitrating,learntButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_panel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabadd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> al = MyListStorage.getStringArrayPref(getApplicationContext(), Constants.SHARED_MYLIST);
                if(al.contains(SkillPanelName)){
                    Toast.makeText(getApplicationContext(),"Skill is already in your List", Toast.LENGTH_LONG).show();
                }else {
                    al.add(SkillPanelName);
                    Toast.makeText(getApplicationContext(),"Skill has been added to your List", Toast.LENGTH_LONG).show();
                }
                MyListStorage.setStringArrayPref(getApplicationContext(),Constants.SHARED_MYLIST,al);
            }
        });

        skill = (TextView) findViewById(R.id.skill);
        imageView = (ImageView) findViewById(R.id.skillPanelImage);
        rate = (RatingBar) findViewById(R.id.ratingBar);
        submitrating = (Button) findViewById(R.id.submitRating);
        learntButton = (Button) findViewById(R.id.learnButton);

        submitrating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkRating()) {
                    rate(((int) rate.getRating()) + "");
                }else{
                    Toast.makeText(getApplicationContext(),"Skill has already been rated", Toast.LENGTH_LONG).show();
                }
            }
        });

        learntButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyListStorage.getStringArrayPref(getApplicationContext(),Constants.SHARED_MYLEARNT).contains(SkillPanelName)){
                    Toast.makeText(getApplicationContext(),"Skill has already been learnt", Toast.LENGTH_LONG).show();
                }else{
                    ArrayList<String> al = MyListStorage.getStringArrayPref(getApplicationContext(),Constants.SHARED_MYLEARNT);
                    al.add(SkillPanelName);
                    MyListStorage.setStringArrayPref(getApplicationContext(),Constants.SHARED_MYLEARNT,al);
                }
            }
        });

        getSupportActionBar().setTitle(SkillPanelName);
        getBaseInfo();
        getSkillImage();

    }
    private void getBaseInfo () {
        String s = SkillPanelName;
        while (s.contains(" ")) {
            s = s.replace(" ", "%20");
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_SKILL+s,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {


                            JSONObject jsonObject = new JSONObject(response);
                            String s = jsonObject.getString("skill");

                            totalb = Integer.parseInt(s.substring(s.indexOf("\"totalbewertung\"")+18,s.indexOf("\"",s.indexOf("\"totalbewertung\"")+18)));
                            anzahlb = Integer.parseInt(s.substring(s.indexOf("\"abewertung\"")+14,s.indexOf("\"",s.indexOf("\"abewertung\"")+14)));
                            bewertung = ((double)totalb)/((double)anzahlb);

                            schwierigkeit = s.substring(s.indexOf("\"schwierigkeit\"")+17,s.indexOf("\"",s.indexOf("\"schwierigkeit\"")+17));
                            kategorie = s.substring(s.indexOf("\"kategorie\"")+13,s.indexOf("\"",s.indexOf("\"kategorie\"")+13));
                            tutorial = s.substring(s.indexOf("\"tutorial\"")+12,s.length()-3);

                            skill.setText("Bewertung: "+bewertung+"\n"+"Schwierigkeit: "+schwierigkeit+"\n"+"Kategorie: "+kategorie+"\n"+"Tutorial: "+tutorial);

                            //Toast.makeText(getApplicationContext(), jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
    private void getSkillImage () {
        String s = SkillPanelName;
        while (s.contains(" ")) {
            s = s.replace(" ", "%20");
        }
        ImageRequest imageRequest = new ImageRequest(
                Constants.URL_SIMG + s, // Image URL
                new Response.Listener<Bitmap>() { // Bitmap listener
                    @Override
                    public void onResponse(Bitmap response) {
                        // Do something with response
                        imageView.setImageBitmap(response);
                        //ProgressBarVisibility = View.GONE;

                        // Save this downloaded bitmap to internal storage
                        //Uri uri = saveImageToInternalStorage(response);

                        // Display the internal storage saved image to image view
                        //mImageViewInternal.setImageURI(uri);
                    }
                },
                1000, // Image width
                1000, // Image height
                ImageView.ScaleType.CENTER_CROP, // Image scale type
                Bitmap.Config.RGB_565, //Image decode configuration
                new Response.ErrorListener() { // Error listener
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something with error response
                        Log.d("IMG", error.getMessage());
                    }
                }
        );
        // 100 is your custom Size before Downloading the Image.

        RequestHandler.getInstance(this).addToRequestQueue(imageRequest);
    }
    private void rate(final String ratenum){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_RATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                            SharedPreferences sp = getSharedPreferences(Constants.SHARED_RATE,0);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putBoolean(SkillPanelName,false);
                            editor.commit();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("skillname",SkillPanelName);
                params.put("ratenum", ratenum);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    /**
     * Checks if the Skill has already been rated.
     * @return allowed to rate the Skill or not
     */
    public boolean checkRating(){
        SharedPreferences sp = getSharedPreferences(Constants.SHARED_RATE,0);
        if(sp.contains(SkillPanelName)){
            if(sp.getBoolean(SkillPanelName,false)) {
                return true;
            }else{
                return false;
            }
        }else{
            return true;
        }
    }

}
