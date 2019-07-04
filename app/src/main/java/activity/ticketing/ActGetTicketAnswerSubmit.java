package activity.ticketing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import activity.article.ActArticle;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import config.ConfigRestHeader;
import config.ConfigStaticValue;
import dialog.JsonDialog;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ntk.base.api.ticket.interfase.ITicket;
import ntk.base.api.ticket.model.TicketingAnswerSubmitRequest;
import ntk.base.api.ticket.model.TicketingAnswerSubmitResponse;
import ntk.base.api.utill.RetrofitManager;
import ntk.base.app.R;
import utill.EasyPreference;

public class ActGetTicketAnswerSubmit extends AppCompatActivity {


    @BindView(R.id.LinkTicketId)
    EditText LinkTicketId;
    @BindView(R.id.api_test_submit_button)
    Button apiTestSubmitButton;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.lblLayout)
    TextView lblLayout;
    @BindView(R.id.HtmlBody)
    EditText HtmlBody;
    @BindView(R.id.LinkTicketingDepartemenId)
    EditText LinkTicketingDepartemenId;
    @BindView(R.id.LinkTypeOperatorId)
    EditText LinkTypeOperatorId;
    @BindView(R.id.LinkFileIds)
    EditText LinkFileIds;
    @BindView(R.id.LinkFileIdsSrc)
    EditText LinkFileIdsSrc;
    @BindView(R.id.btnAddActTicketAnswerSubmit)
    Button btnAdd;
    @BindView(R.id.Id)
    EditText Id;

    private ConfigStaticValue configStaticValue = new ConfigStaticValue(this);
    private ConfigRestHeader configRestHeader = new ConfigRestHeader();
    private List<String> LinkFileIdsSrcList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_ticket_get_answer_submit);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        lblLayout.setText("TicketingAnswerSubmit");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra(ActArticle.LAYOUT_VALUE));
    }

    @OnClick(R.id.api_test_submit_button)
    public void onClick(View v) {
        progressBar.setVisibility(View.VISIBLE);
        getData();
    }

    @OnClick(R.id.btnAddActTicketAnswerSubmit)
    public void onAddClick() {
        if (!LinkFileIdsSrc.getText().toString().matches("")) {
            LinkFileIdsSrcList.add(LinkFileIdsSrc.getText().toString());
            LinkFileIdsSrc.setText("");
        } else {
            LinkFileIdsSrc.setError("Invalid Info !!");
        }
    }

    private void getData() {
        TicketingAnswerSubmitRequest request = new TicketingAnswerSubmitRequest();
        if (!LinkTicketId.getText().toString().matches("")) {
            if (LinkTicketId.getInputType() != InputType.TYPE_CLASS_NUMBER) {
                LinkTicketId.setError("InValid Info !!");
                progressBar.setVisibility(View.GONE);
                return;
            } else {
                request.LinkTicketId = Long.valueOf(LinkTicketId.getText().toString());
            }
        }
        if (!HtmlBody.getText().toString().matches("")) {
            request.HtmlBody = HtmlBody.getText().toString();
        }
        if (!LinkTicketingDepartemenId.getText().toString().matches("")) {
            if (LinkTicketingDepartemenId.getInputType() != InputType.TYPE_CLASS_NUMBER) {
                LinkTicketingDepartemenId.setError("InValid Info !!");
                progressBar.setVisibility(View.GONE);
                return;
            } else {
                //request.LinkTicketingDepartemenId = Long.valueOf(LinkTicketingDepartemenId.getText().toString());
            }
        }
        if (!LinkTypeOperatorId.getText().toString().matches("")) {
            if (LinkTypeOperatorId.getInputType() != InputType.TYPE_CLASS_NUMBER) {
                LinkTypeOperatorId.setError("InValid Info !!");
                progressBar.setVisibility(View.GONE);
                return;
            } else {
                //request.LinkTypeOperatorId = Long.valueOf(LinkTypeOperatorId.getText().toString());
            }
        }
        if (!LinkFileIds.getText().toString().matches("")) {
            request.LinkFileIds = LinkFileIds.getText().toString();
        }
        if (!LinkFileIdsSrcList.isEmpty()) {
            request.LinkFileIds = LinkFileIdsSrcList.toString();
        }
        if (!Id.getText().toString().matches("")) {
            if (Id.getInputType() != InputType.TYPE_CLASS_NUMBER) {
                Id.setError("InValid Info !!");
                progressBar.setVisibility(View.GONE);
                return;
            } else {
                request.LinkTicketId = Long.valueOf(Id.getText().toString());
            }
        }
        RetrofitManager manager = new RetrofitManager(ActGetTicketAnswerSubmit.this);
        ITicket iTicket = manager.getRetrofit(configStaticValue.ApiBaseUrl).create(ITicket.class);
        Map<String, String> headers = new HashMap<>();
        headers = configRestHeader.GetHeaders(this);
        headers.put("PackageName", EasyPreference.with(this).getString("packageName",""));
        Observable<TicketingAnswerSubmitResponse> call = iTicket.GetTicketAnswerSubmit(headers, request);
        call.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<TicketingAnswerSubmitResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(TicketingAnswerSubmitResponse response) {
                        JsonDialog cdd = new JsonDialog(ActGetTicketAnswerSubmit.this, response);
                        cdd.setCanceledOnTouchOutside(false);
                        cdd.show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        Log.i("Error", e.getMessage());
                        Toast.makeText(ActGetTicketAnswerSubmit.this, "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(this, ActTicket.class));
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(this, ActTicket.class));
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
