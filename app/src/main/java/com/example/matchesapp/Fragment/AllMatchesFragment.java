package com.example.matchesapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.matchesapp.Constants.DataBase;
import com.example.matchesapp.Constants.Urls;
import com.example.matchesapp.Model.MatchesModel;
import com.example.matchesapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllMatchesFragment extends Fragment {
    private Button btn_getdata;
    private RecyclerView recyclerview;
    private ArrayList<MatchesModel> campModel = new ArrayList<>();
    private DataBase dataBase;
    private ArrayList<MatchesModel> campModelsqlite = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.all_matches_fragment, container, false);
        init(view);
        onClick(view);
        return view;
    }

    private void onClick(View view) {
        btn_getdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerview.setVisibility(View.VISIBLE);
                campListing();
            }
        });
    }

    private void init(View view) {
//        dialog = new SpotsDialog(getContext(), R.style.Custom);
        btn_getdata = view.findViewById(R.id.btn_getdata);
        campModelsqlite=new ArrayList<>();
        btn_getdata.setVisibility(View.VISIBLE);
        recyclerview = view.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        dataBase = new DataBase(getActivity());

    }


    public void campListing() {
//        dialog.show();
        if (Urls.isNetwork(getContext())) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.getdataUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject object = jsonObject.getJSONObject("response");
                        JSONArray json = object.getJSONArray("venues");
                        for (int i = 0; i < json.length(); i++) {

                            JSONObject packObject = json.getJSONObject(i);
                            MatchesModel model = new MatchesModel();
                            model.setId(packObject.getString("id"));
                            model.setName(packObject.getString("name"));
                            campModelsqlite = dataBase.getFriends();
                            if(campModelsqlite.size()!=0  && campModelsqlite!=null) {
                                for (int j = 0; j < campModelsqlite.size(); j++) {
                                    MatchesModel model1 = campModelsqlite.get(j);
                                    if (model1.getId().equals(packObject.getString("id"))) {
                                        model.setStatus(model1.getStatus());
                                    }
                                }
                            }else {
                                model.setStatus(0);
                            }
                            campModel.add(model);
                        }

                        campAdapter adapter = new campAdapter();
                        recyclerview.setAdapter(adapter);
                        recyclerview.setVisibility(View.VISIBLE);
                        btn_getdata.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
//            dismissDialog();
            Toast.makeText(getContext(), "no internet connection", Toast.LENGTH_SHORT).show();
        }
    }


    public class campAdapter extends RecyclerView.Adapter<campAdapter.ViewHolder> {
        ArrayList<MatchesModel> arrayList;

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            MatchesModel model = campModel.get(position);
            ((ViewHolder) holder).bind(model, position);
        }

        @Override
        public int getItemCount() {
            return campModel.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView txt_name, txt_id;
            private ImageView imageInActive, imageActive;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                txt_id = itemView.findViewById(R.id.id);
                txt_name = itemView.findViewById(R.id.name);
                imageInActive = itemView.findViewById(R.id.img_inactive);
                imageActive = itemView.findViewById(R.id.img_active);
            }


            public void bind(final MatchesModel model, int position) {
                txt_id.setText(model.getId());

                if (model.getName().equals("")) {
                    txt_name.setText("NA");
                } else {
                    txt_name.setText(model.getName());
                }

                if (model.getStatus() == 1) {
                    imageInActive.setVisibility(View.GONE);
                    imageActive.setVisibility(View.VISIBLE);
                } else {
                    imageInActive.setVisibility(View.VISIBLE);
                    imageActive.setVisibility(View.GONE);
                }

                imageInActive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        model.setStatus(1);
                        if (model.getStatus() == 1) {
                            imageInActive.setVisibility(View.GONE);
                            imageActive.setVisibility(View.VISIBLE);
                        } else {
                            imageInActive.setVisibility(View.VISIBLE);
                            imageActive.setVisibility(View.GONE);
                        }


                        if (!dataBase.ifExists((model.getId()))) {
                            dataBase.addFriend(
                                    model.getId(),
                                    model.getName(),
                                    model.getStatus());
                        }
                    }
                });


                imageActive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        model.setStatus(0);
                        if (model.getStatus() == 0) {
                            imageInActive.setVisibility(View.VISIBLE);
                            imageActive.setVisibility(View.GONE);
                        } else {
                            imageInActive.setVisibility(View.GONE);
                            imageActive.setVisibility(View.VISIBLE);
                        }
                        // if (!dataBase.ifExists((model.getId()))) {
                        dataBase.deleteRecord(model.id);
                        notifyDataSetChanged();
                        // }
                    }
                });
            }
        }
    }
}

