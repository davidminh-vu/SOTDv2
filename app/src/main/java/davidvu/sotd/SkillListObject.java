package davidvu.sotd;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import davidvu.sotd.fragment.SkillFragment;
import davidvu.sotd.fragment.MerkFragment;

public class SkillListObject {
    public Bitmap icon;
    public String title;
    public Context con;
    public boolean isMyList;
    public int ProgressBarVisibility = View.VISIBLE;

    public SkillListObject(){
        super();
    }

    public SkillListObject(Context context, String title, boolean isMyList){
        super();
        this.title = title;
        this.con = context;
        this.isMyList = isMyList;
        getImageSkill();

    }

    private void getImageSkill() {
        String s = title;
        while (s.contains(" ")) {
            s = s.replace(" ", "%20");
        }
        ImageRequest imageRequest = new ImageRequest(
                Constants.URL_SIMG + s, // Image URL
                new Response.Listener<Bitmap>() { // Bitmap listener
                    @Override
                    public void onResponse(Bitmap response) {
                        // Do something with response
                        icon = response;
                        ProgressBarVisibility = View.GONE;
                        if(isMyList){
                            MerkFragment.updateSkillList();
                        }else {
                            SkillFragment.updateSkillList();
                        }


                        // Save this downloaded bitmap to internal storage
                        //Uri uri = saveImageToInternalStorage(response);

                        // Display the internal storage saved image to image view
                        //mImageViewInternal.setImageURI(uri);
                    }
                },
                500, // Image width
                500, // Image height
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

        RequestHandler.getInstance(con).addToRequestQueue(imageRequest);

    }
}
