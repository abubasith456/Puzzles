package com.example.puzzle.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.puzzle.BaseActivity;
import com.example.puzzle.R;
import com.example.puzzle.model.Score;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScoreboardActivity extends BaseActivity {

    @BindView(R.id.recyclerViewScoreCard)
    RecyclerView recyclerViewScoreCard;

    ScoreAdapter scoreAdapter;
    ArrayList<Score> scoreArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        try {

        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    @OnClick(R.id.buttonBackArrow)
    void backButtonClick() {
        try {
            finish();
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    public class ScoreAdapter extends RecyclerView.Adapter<ProductsViewHolder> {
        private ArrayList<Score> productsArray;
        private Activity mActivity;

        public ScoreAdapter(ArrayList<Score> productsArray, Activity activity) {
            this.productsArray = productsArray;
            mActivity = activity;
        }

        public void setBookMarksList(ArrayList<Score> clientsArray) {
            this.productsArray = clientsArray;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ProductsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_view_score_details, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {
            holder.bind(productsArray.get(position), mActivity);

        }

        @Override
        public int getItemCount() {
            return productsArray.size();
        }
    }

    public class ProductsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textViewTime)
        TextView textViewTime;
        @BindView(R.id.textViewDate)
        TextView textViewDate;
        @BindView(R.id.textViewScore)
        ImageView textViewScore;

        ProductsViewHolder(View itemView) {
            super(itemView);
            try {
                ButterKnife.bind(this, itemView);
            } catch (Exception exception) {
                Log.e("Error ==> ", "" + exception);
            }
        }

        public void bind(final Score response, final Activity activity) {
            try {
                if (response != null) {


                }
            } catch (Exception exception) {
                Log.e("Error ==> ", "" + exception);
            }
        }
    }


}