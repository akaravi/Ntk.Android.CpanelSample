package activity.shop;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import ntk.base.app.R;

public class ActShop extends AppCompatActivity {

    public static String LAYOUT_VALUE = "LAYOUT_VALUE";
    @BindView(R.id.api_recycler_view)
    RecyclerView apiRecyclerView;
    private String[] articleList = new String[]{
            "Count"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_shop);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Shop");
        apiRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        apiRecyclerView.setAdapter(new ActShop.ApiRecyclerViewAdapter(this, articleList));
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

    public class ApiRecyclerViewAdapter extends RecyclerView.Adapter<ActShop.ApiRecyclerViewAdapter.ApiViewHolder> {
        private Context context;
        private String[] list;

        ApiRecyclerViewAdapter(Context context, String[] list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public ActShop.ApiRecyclerViewAdapter.ApiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ActShop.ApiRecyclerViewAdapter.ApiViewHolder(LayoutInflater.from(context).inflate(R.layout.button_item, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ActShop.ApiRecyclerViewAdapter.ApiViewHolder apiViewHolder, int i) {
            apiViewHolder.button.setText(list[i]);
            apiViewHolder.button.setId(i);
            apiViewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case 0:
                            startActivity(new Intent(ActShop.this, ActCount.class));
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