package activity.biography;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import activity.Main;
import activity.news.ActGetCommentView;
import activity.news.ActGetNewsContentList;
import activity.news.ActNews;
import activity.news.ActSetComment;
import butterknife.BindView;
import butterknife.ButterKnife;
import ntk.base.app.R;

public class ActBiography extends AppCompatActivity {

    @BindView(R.id.api_recycler_view)
    RecyclerView apiRecyclerView;
    private String[] biographyList = new String[]{"GetContentList",
            "GetContentView",
            "GetTagList",
            "GetCategoryList",
            "GetCategoryTagList",
            "GetContentOtherInfoList",
            "GetCommentList",
            "SetContentFavoriteAddOrRemove",
            "GetContentFavoriteList",
            "BiographyCommentAdd",
            "biographyCommentView",
            "BiographyContentCategoryList",
            "BiographyContentSimilarList",
            "BiographyContentWithDatePeriodEndList",
            "BiographyContentWithDatePeriodStartList",
            "BiographyContentWithSimilarDatePeriodEndList",
            "BiographyContentWithSimilarDatePeriodStartDayAndMonthOfYearList",
            "BiographyContentWithSimilarDatePeriodStartDayOfYearList",
            "BiographyContentWithSimilarDatePeriodStartList",
            "BiographyContentWithSimilarDatePeriodStartMonthOfYearList"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_biography);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Biography");
        apiRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        apiRecyclerView.setAdapter(new ApiRecyclerViewAdapter(this, biographyList));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    public class ApiRecyclerViewAdapter extends RecyclerView.Adapter<ApiRecyclerViewAdapter.ApiViewHolder> {
        private Context context;
        private String[] list;

        ApiRecyclerViewAdapter(Context context, String[] list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public ApiRecyclerViewAdapter.ApiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ApiRecyclerViewAdapter.ApiViewHolder(LayoutInflater.from(context).inflate(R.layout.button_item, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ApiRecyclerViewAdapter.ApiViewHolder apiViewHolder, int i) {
            apiViewHolder.button.setText(list[i]);
            apiViewHolder.button.setId(i);
            apiViewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case 0:
                            startActivity(new Intent(ActBiography.this, ActGetContentList.class));
                            break;
                        case 1:
                            startActivity(new Intent(ActBiography.this, ActGetContentView.class));
                            break;
                        case 2:
                            startActivity(new Intent(ActBiography.this, ActGetTagList.class));
                            break;
                        case 3:
                            startActivity(new Intent(ActBiography.this, ActGetCategoryList.class));
                            break;
                        case 4:
                            startActivity(new Intent(ActBiography.this, ActGetCategoryTagList.class));
                            break;
                        case 5:
                            startActivity(new Intent(ActBiography.this, ActGetContentOtherInfoList.class));
                            break;
                        case 6:
                            startActivity(new Intent(ActBiography.this, ActGetCommentList.class));
                            break;
                        case 7:
                            startActivity(new Intent(ActBiography.this, ActSetContentFavoriteAddOrRemove.class));
                            break;
                        case 8:
                            startActivity(new Intent(ActBiography.this, ActGetContentFavoriteList.class));
                            break;
                        case 9:
                            startActivity(new Intent(ActBiography.this, ActSetComment.class));
                            break;
                        case 10:
                            startActivity(new Intent(ActBiography.this, ActGetCommentView.class));
                            break;
                        case 11:
                            startActivity(new Intent(ActBiography.this, ActContentCategoryList.class));
                            break;
                        case 12:
                            startActivity(new Intent(ActBiography.this, ActContentSimilarList.class));
                            break;
                        case 13:
                            startActivity(new Intent(ActBiography.this, ActContentWithDatePeriodEndList.class));
                            break;
                        case 14:
                            startActivity(new Intent(ActBiography.this, ActContentWithDatePeriodStartList.class));
                            break;
                        case 15:
                            startActivity(new Intent(ActBiography.this, ActContentWithSimilarDatePeriodEndList.class));
                            break;
                        case 16:
                            startActivity(new Intent(ActBiography.this, ActContentWithSimilarDatePeriodStartDayAndMonthOfYearList.class));
                            break;
                        case 17:
                            startActivity(new Intent(ActBiography.this, ActContentWithSimilarDatePeriodStartDayOfYearList.class));
                            break;
                        case 18:
                            startActivity(new Intent(ActBiography.this, ActContentWithSimilarDatePeriodStartList.class));
                            break;
                        case 19:
                            startActivity(new Intent(ActBiography.this, ActContentWithSimilarDatePeriodStartMonthOfYearList.class));
                            break;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.length;
        }

        class ApiViewHolder extends RecyclerView.ViewHolder {
            private Button button;

            ApiViewHolder(@NonNull View itemView) {
                super(itemView);
                button = itemView.findViewById(R.id.button_item);
            }
        }
    }
}
