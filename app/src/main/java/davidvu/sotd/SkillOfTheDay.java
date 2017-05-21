package davidvu.sotd;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Michael Frank on 21.05.2017.
 */

public class SkillOfTheDay {
    private ArrayList<String> LearntList;
    private ArrayList<String> NotLearnt;
    private ArrayList<String> CategoriesLearnt;
    private ArrayList<String> SkillList;
    private ArrayList<String> CategoriesNotLearnt;

    private String SOTD;

    private Context con;
    public SkillOfTheDay(Context con){
        this.con = con;
        this.LearntList = getLearntList();
        NotLearnt = new ArrayList<>();
        CategoriesLearnt = new ArrayList<>();
        SkillList = new ArrayList<>();
        CategoriesNotLearnt = new ArrayList<>();
        for (int i=0;i<LearntList.size();i++){
            getCategories(LearntList.get(i),true);
        }
        getSkillnames();
    }
    public ArrayList<String> getLearntList(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(con);
        if(prefs.contains(Constants.SHARED_MYLEARNT)){
            return MyListStorage.getStringArrayPref(con.getApplicationContext(), Constants.SHARED_MYLEARNT);
        }else {
            return new ArrayList<>();
        }
    }
    private void getCategories(String Skillname, final boolean isLearnt){
        while(Skillname.contains(" ")){
            Skillname = Skillname.replace(" ","%20");
        }
            StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_KATEGORIE+Skillname,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String s = jsonObject.getString("skill");
                                s = s.substring(s.indexOf("\"kategorie\"")+13,s.length()-3);
                                if(isLearnt){
                                    CategoriesLearnt.add(s);
                                }else{
                                    CategoriesNotLearnt.add(s);
                                    if(CategoriesNotLearnt.size() == NotLearnt.size()){calcSOTD2();}
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(con.getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
            RequestHandler.getInstance(con.getApplicationContext()).addToRequestQueue(stringRequest);
    }
    private void getSkillnames(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_SLIST+"NA",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray ja = jsonObject.optJSONArray("skillname");
                            for(int i=0;i<ja.length();i++){
                                SkillList.add(ja.getString(i).substring(14,ja.getString(i).length()-2));
                            }
                            calcSOTD();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(con.getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        RequestHandler.getInstance(con.getApplicationContext()).addToRequestQueue(stringRequest);

    }
    private void calcSOTD(){
        for(int i=0;i<SkillList.size();i++) {
            if (!(LearntList.contains(SkillList.get(i)))) {
                NotLearnt.add(SkillList.get(i));
            }
        }
        if(NotLearnt.size()==0){
            double random = Math.random()*SkillList.size();
            SOTD = SkillList.get((int)random);
        }else{
            for(int i=0;i<NotLearnt.size();i++){
                getCategories(NotLearnt.get(i),false);
            }
        }
    }
    private void calcSOTD2(){
        ArrayList<Integer> position = new ArrayList<>();
        ArrayList<String> Likley = new ArrayList<>();
        for(int i=0;i<CategoriesNotLearnt.size();i++) {
            if (CategoriesLearnt.contains(CategoriesNotLearnt.get(i))) {
                position.add(i);
            }
        }
        Log.d("hier",position.size()+"");
        if(position.size()==0){
            double random = Math.random()*NotLearnt.size();
            SOTD = NotLearnt.get((int)random);
        }else {
            for (int a = 0; a < position.size(); a++) {
                Likley.add(NotLearnt.get(position.get(a)));
            }
            double random = Math.random() * Likley.size();
            SOTD = Likley.get((int) random);
        }
    }
    public String getSOTD(){
        return SOTD;
    }
}