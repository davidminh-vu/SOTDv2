package davidvu.sotd.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import davidvu.sotd.Constants;
import davidvu.sotd.R;
import davidvu.sotd.RequestHandler;
import davidvu.sotd.SkillListAdapter;
import davidvu.sotd.SkillListObject;
import davidvu.sotd.activity.SkillPanel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SkillFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SkillFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SkillFragment extends Fragment {
    // Vielleicht hast ja ein plan
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SkillListObject[] skillListObjects;
    private GridView gridView;
    private static SkillListAdapter skillListAdapter;

    //
    //TODO: Einbindung in den Sort Button oben rechts
    //Options:
    //"NA" = Sort By Name Begin with A - Z
    //"NZ" = Sort By Name Begin with Z - A
    //"SS" = Sort By Schwierigkeit Begin with hardest - easiest
    //"SL" = Sort By Schwierigkeit Begin with easiest - hardest
    //"RM" = Sort By Rating Begin with most - least
    //"RL" = Sort By Rating Begin with least - most
    private String sortLike = "NA";

    private OnFragmentInteractionListener mListener;

    public SkillFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SkillFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SkillFragment newInstance(String param1, String param2) {
        SkillFragment fragment = new SkillFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getSkillnames();
        return inflater.inflate(R.layout.fragment_skill, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     *  @Override
    public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnFragmentInteractionListener) {
    mListener = (OnFragmentInteractionListener) context;
    } else {
    throw new RuntimeException(context.toString()
    + " must implement OnFragmentInteractionListener");
    }
    }
     */


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void getSkillnames(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_SLIST+sortLike,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray ja = jsonObject.optJSONArray("skillname");
                            skillListObjects = new SkillListObject[ja.length()];
                            for(int i=0;i<ja.length();i++){
                                String s = ja.getString(i).substring(14,ja.getString(i).length()-2);
                                skillListObjects[i] = new SkillListObject(getContext(),s,false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        createList();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);

    }
    private void createList(){
        gridView = (GridView) getView().findViewById(R.id.SkillList);

        skillListAdapter = new SkillListAdapter(getContext(), R.layout.skilllist_item_row, skillListObjects);
        gridView.setAdapter(skillListAdapter);

        final Intent intent = new Intent(getActivity(),SkillPanel.class);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SkillPanel.SkillPanelName = ((TextView) view.findViewById(R.id.SkillListName)).getText().toString();
                startActivity(intent);
            }
        });

    }
    public static void updateSkillList(){
        skillListAdapter.notifyDataSetChanged();
    }
}
