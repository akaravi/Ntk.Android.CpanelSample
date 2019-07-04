package activity.core;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import ntk.base.app.R;

public class ActCore extends AppCompatActivity {
    @BindView(R.id.api_recycler_view)
    RecyclerView apiRecyclerView;
    private String[] articleList = new String[]{"Main",
            "Core About Us",
            "Core User Register By Mobile",
            "Core Location",
            "Core User Change Password",
            "Core User Email Confirm",
            "Core User Forget Password",
            "Core User Login",
            "Core User Login By Mobile",
            "Core User Mobile Confirm",
            "Core User Register",
            "Core Create Filter Model"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_core);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Core");
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
                            startActivity(new Intent(ActCore.this, ActGetResponseMain.class));
                            break;
                        case 1:
                            startActivity(new Intent(ActCore.this, ActGetAbout.class));
                            break;
                        case 2:
                            startActivity(new Intent(ActCore.this, ActRegisterWithMobile.class));
                            break;
                        case 3:
                            startActivity(new Intent(ActCore.this, ActLocation.class));
                            break;
                        case 4:
                            startActivity(new Intent(ActCore.this, ActUserChangePassword.class));
                            break;
                        case 5:
                            startActivity(new Intent(ActCore.this, ActUserEmailConfirm.class));
                            break;
                        case 6:
                            startActivity(new Intent(ActCore.this, ActUserForgetPassword.class));
                            break;
                        case 7:
                            startActivity(new Intent(ActCore.this, ActUserLogin.class));
                            break;
                        case 8:
                            startActivity(new Intent(ActCore.this, ActUserLoginByMobile.class));
                            break;
                        case 9:
                            startActivity(new Intent(ActCore.this, ActUserMobileConfirm.class));
                            break;
                        case 10:
                            startActivity(new Intent(ActCore.this, ActUserRegister.class));
                            break;
                        case 11:
                            startActivity(new Intent(ActCore.this, ActCreateFilterModel.class));
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
