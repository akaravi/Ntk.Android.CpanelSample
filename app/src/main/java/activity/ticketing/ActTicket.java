package activity.ticketing;

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

public class ActTicket extends AppCompatActivity {
    public static String LAYOUT_VALUE = "LAYOUT_VALUE";
    @BindView(R.id.pooling_recycler_view)
    RecyclerView poolingRecyclerView;
    private String[] ticketList = new String[]{"Ticketing Answer List",
            "Ticketing Answer Submit",
            "Ticketing Departemen List",
            "Ticketing Faq List",
            "Ticketing Task List",
            "Ticketing Task Submit"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_ticket);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Ticketing");
        poolingRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        poolingRecyclerView.setAdapter(new TicketRecyclerViewAdapter(this,ticketList));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(this, Main.class));
            finish();
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

    public class TicketRecyclerViewAdapter extends RecyclerView.Adapter<TicketRecyclerViewAdapter.TicketViewHolder> {
        private Context context;
        private String[] list;

        TicketRecyclerViewAdapter(Context context, String[] list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public TicketRecyclerViewAdapter.TicketViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new TicketViewHolder(LayoutInflater.from(context).inflate(R.layout.button_item, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull TicketRecyclerViewAdapter.TicketViewHolder viewHolder, int i) {
            viewHolder.button.setText(list[i]);
            viewHolder.button.setId(i);
            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case 0:
                            startActivity(new Intent(ActTicket.this, ActGetTicketAnswerList.class).putExtra(LAYOUT_VALUE, "TicketingAnswerList"));
                            finish();
                            break;
                        case 1:
                            startActivity(new Intent(ActTicket.this, ActGetTicketAnswerSubmit.class).putExtra(LAYOUT_VALUE, "TicketingAnswerSubmit"));
                            finish();
                            break;
                        case 2:
                            startActivity(new Intent(ActTicket.this, ActGetTicketDepartman.class).putExtra(LAYOUT_VALUE, "TicketingDepartemenList"));
                            finish();
                            break;
                        case 3:
                            startActivity(new Intent(ActTicket.this, ActGetTicketFaqList.class).putExtra(LAYOUT_VALUE, "TicketingFaqList"));
                            finish();
                            break;
                        case 4:
                            startActivity(new Intent(ActTicket.this, ActGetTicketList.class).putExtra(LAYOUT_VALUE, "TicketingTaskList"));
                            finish();
                            break;
                        case 5:
                            startActivity(new Intent(ActTicket.this, ActSetTicketSubmit.class).putExtra(LAYOUT_VALUE, "TicketingTaskSubmit"));
                            finish();
                            break;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.length;
        }

        class TicketViewHolder extends RecyclerView.ViewHolder {
            private Button button;

            TicketViewHolder(@NonNull View itemView) {
                super(itemView);
                button = itemView.findViewById(R.id.button_item);
            }
        }
    }
}