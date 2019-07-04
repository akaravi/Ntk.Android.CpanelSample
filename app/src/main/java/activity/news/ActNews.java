package activity.news;

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
import butterknife.BindView;
import butterknife.ButterKnife;
import ntk.base.app.R;

public class ActNews extends AppCompatActivity {
    public static String LAYOUT_VALUE = "LAYOUT_VALUE";
    @BindView(R.id.api_recycler_view)
    RecyclerView apiRecyclerView;
    private String[] articleList = new String[]{"News Content List",
            "News Content View",
            "News Tag List",
            "News Category List",
            "News Category Tag List",
            "News Content Other Info List",
            "News Comment List",
            "News Comment Add",
            "News Comment View",
            "News Content Favorite Add Or Remove",
            "News Content Favorite List",
            "NewsContentSimilarList",
            "NewsContentCategoryList"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_news);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("News");
        apiRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        apiRecyclerView.setAdapter(new ApiRecyclerViewAdapter(this, articleList));
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
                            startActivity(new Intent(ActNews.this, ActGetNewsContentList.class));
                            break;
                        case 1:
                            startActivity(new Intent(ActNews.this, ActGetContentView.class));
                            break;
                        case 2:
                            startActivity(new Intent(ActNews.this, ActGetTagList.class));
                            break;
                        case 3:
                            startActivity(new Intent(ActNews.this, ActGetCategoryList.class));
                            break;
                        case 4:
                            startActivity(new Intent(ActNews.this, ActGetCategoryTagList.class));
                            break;
                        case 5:
                            startActivity(new Intent(ActNews.this, ActGetContentOtherInfoList.class));
                            break;
                        case 6:
                            startActivity(new Intent(ActNews.this, ActGetCommentList.class));
                            break;
                        case 7:
                            startActivity(new Intent(ActNews.this, ActSetComment.class));
                            break;
                        case 8:
                            startActivity(new Intent(ActNews.this, ActGetCommentView.class));
                            break;
                        case 9:
                            startActivity(new Intent(ActNews.this, ActSetContentFavoriteAddOrRemove.class));
                            break;
                        case 10:
                            startActivity(new Intent(ActNews.this, ActGetContentFavoriteList.class));
                            break;
                        case 11:
                            startActivity(new Intent(ActNews.this, ActContentSimilarList.class));
                            break;
                        case 12:
                            startActivity(new Intent(ActNews.this, ActContentCategoryList.class));
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
