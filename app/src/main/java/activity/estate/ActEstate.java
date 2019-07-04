package activity.estate;

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
import activity.core.ActGetResponseMain;
import butterknife.BindView;
import butterknife.ButterKnife;
import ntk.base.app.R;

public class ActEstate extends AppCompatActivity {

    @BindView(R.id.api_recycler_view)
    RecyclerView apiRecyclerView;
    private String[] articleList = new String[]{"EstateContractType",
            "EstatePropertyAdd",
            "EstatePropertyDetailGroupList",
            "EstatePropertyDetailList",
            "EstatePropertyFavoriteAddOrRemove",
            "EstatePropertyFavoriteList",
            "EstatePropertyList",
            "EstatePropertyType",
            "EstatePropertyAdd"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_estate);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Estate");
        apiRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        apiRecyclerView.setAdapter(new ApiRecyclerViewAdapter(this, articleList));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(this, Main.class));
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(this, Main.class));
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
                            startActivity(new Intent(ActEstate.this, ActContractType.class));
                            break;
                        case 1:
                            startActivity(new Intent(ActEstate.this, ActPropertyAdd.class));
                            break;
                        case 2:
                            startActivity(new Intent(ActEstate.this, ActPropertyDetailGroupList.class));
                            break;
                        case 3:
                            startActivity(new Intent(ActEstate.this, ActPropertyDetailList.class));
                            break;
                        case 4:
                            startActivity(new Intent(ActEstate.this, ActPropertyFavoriteAddOrRemove.class));
                            break;
                        case 5:
                            startActivity(new Intent(ActEstate.this, ActPropertyFavoriteList.class));
                            break;
                        case 6:
                            startActivity(new Intent(ActEstate.this, ActPropertyList.class));
                            break;
                        case 7:
                            startActivity(new Intent(ActEstate.this, ActPropertyType.class));
                            break;
                        case 8:
                            startActivity(new Intent(ActEstate.this, ActPropertyView.class));
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
