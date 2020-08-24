package com.example.matchesapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matchesapp.Constants.DataBase;
import com.example.matchesapp.Model.MatchesModel;
import com.example.matchesapp.R;

import java.util.ArrayList;

public class SavedMatchesFragment extends Fragment {
    private RecyclerView recyclerview;
    private ArrayList<MatchesModel> campModel = new ArrayList<>();

    DataBase dataBase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.all_matches_fragment, container, false);
        init(view);
        dataBase = new DataBase(getActivity());
        onClick(view);

        //get tha data from database
        getData();
        return view;
    }

    private void onClick(View view) {
    }

    private void init(View view) {
        recyclerview = view.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    public void getData() {
        campModel = dataBase.getFriends();
        campAdapter adapter = new campAdapter();
        recyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recyclerview.setVisibility(View.VISIBLE);
    }


    public class campAdapter extends RecyclerView.Adapter<campAdapter.ViewHolder> {
        ArrayList<MatchesModel> arrayList;

        @NonNull
        @Override
        public campAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_layout, parent, false);
            return new campAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull campAdapter.ViewHolder holder, int position) {
            MatchesModel model = campModel.get(position);
            ((campAdapter.ViewHolder) holder).bind(model, position);

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
                txt_name = itemView.findViewById(R.id.name);
                txt_id = itemView.findViewById(R.id.id);
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
                imageInActive.setVisibility(View.GONE);
                imageActive.setVisibility(View.GONE);
            }

        }
    }

}
