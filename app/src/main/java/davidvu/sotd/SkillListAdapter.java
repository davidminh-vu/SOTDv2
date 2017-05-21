package davidvu.sotd;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by micha on 16.05.2017.
 */

public class SkillListAdapter extends ArrayAdapter<SkillListObject> {

    Context context;
    int layoutResourceId;
    SkillListObject data[] = null;

    public SkillListAdapter(Context context, int layoutResourceId, SkillListObject[] data){
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        SkillListHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new SkillListHolder();
            holder.SkillListImage = (ImageView) row.findViewById(R.id.SkillListImage);
            holder.SkillListName = (TextView) row.findViewById(R.id.SkillListName);
            holder.progressBar = (RelativeLayout) row.findViewById(R.id.loadingPanel);

            row.setTag(holder);
        } else {
            holder = (SkillListHolder) row.getTag();
        }

        final SkillListObject skillListObject = data[position];
        Log.d("Test", skillListObject.title);
        holder.SkillListName.setText(skillListObject.title);
        holder.SkillListImage.setImageBitmap(skillListObject.icon);
        holder.progressBar.setVisibility(skillListObject.ProgressBarVisibility);

        return row;
    }
    static class SkillListHolder
    {
        ImageView SkillListImage;
        TextView SkillListName;
        RelativeLayout progressBar;
    }
}
