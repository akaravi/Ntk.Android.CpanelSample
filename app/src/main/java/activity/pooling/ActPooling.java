package activity.pooling;

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

public class ActPooling extends AppCompatActivity {

    @BindView(R.id.pooling_recycler_view)
    RecyclerView poolingRecyclerView;
    public static String LAYOUT_VALUE = "LAYOUT_VALUE";
    private String[] poolingList = new String[]{"Pooling Category List",
            "Pooling Content List",
            "Pooling Vote Submit"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_poolling);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Pooling");
        poolingRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        poolingRecyclerView.setAdapter(new PoolingRecyclerViewAdapter(this, poolingList));
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

    public class PoolingRecyclerViewAdapter extends RecyclerView.Adapter<PoolingRecyclerViewAdapter.PoolingViewHolder> {
        private Context context;
        private String[] list;

        PoolingRecyclerViewAdapter(Context context, String[] list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public PoolingRecyclerViewAdapter.PoolingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new PoolingViewHolder(LayoutInflater.from(context).inflate(R.layout.button_item, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull PoolingRecyclerViewAdapter.PoolingViewHolder viewHolder, int i) {
            viewHolder.button.setText(list[i]);
            viewHolder.button.setId(i);
            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case 0:
                            startActivity(new Intent(ActPooling.this, ActGetCategoryList.class));
                            break;
                        case 1:
                            startActivity(new Intent(ActPooling.this, ActGetContentList.class));
                            break;
                        case 2:
                            startActivity(new Intent(ActPooling.this, ActSetSubmitPooling.class));
                            break;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.length;
        }

        class PoolingViewHolder extends RecyclerView.ViewHolder {
            private Button button;

            PoolingViewHolder(@NonNull View itemView) {
                super(itemView);
                button = itemView.findViewById(R.id.button_item);
            }
        }
    }
}