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

import java.util.HashMap;
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
import ntk.base.api.ticket.model.TicketingDepartemen;
import ntk.base.api.ticket.model.TicketingSubmitRequest;
import ntk.base.api.ticket.model.TicketingSubmitResponse;
import ntk.base.api.utill.RetrofitManager;
import ntk.base.app.R;
import utill.EasyPreference;

public class ActSetTicketSubmit extends AppCompatActivity {

    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.phoneNo)
    EditText phoneNo;
    @BindView(R.id.uploadName)
    EditText uploadName;
    @BindView(R.id.Priority)
    EditText Priority;
    @BindView(R.id.HtmlBody)
    EditText HtmlBody;
    @BindView(R.id.LinkTicketingDepartemenId)
    EditText LinkTicketingDepartemenId;
    @BindView(R.id.TicketStatus)
    EditText TicketStatus;
    @BindView(R.id.UserIpAddress)
    EditText UserIpAddress;
    @BindView(R.id.DeviceInformation)
    EditText DeviceInformation;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.title)
    EditText title;
    @BindView(R.id.lblLayout)
    TextView lblLayout;
    @BindView(R.id.api_test_submit_button)
    Button apiTestSubmitButton;
    @BindView(R.id.Question)
    EditText Question;
    @BindView(R.id.Answer)
    EditText Answer;
    @BindView(R.id.LinkTicketTypeId)
    EditText LinkTicketTypeId;
    @BindView(R.id.IdDepartemen)
    EditText IdDepartemen;
    @BindView(R.id.DefaultAnswerBodyDepartemen)
    EditText DefaultAnswerBodyDepartemen;
    @BindView(R.id.PriorityDepartemen)
    EditText PriorityDepartemen;
    @BindView(R.id.AccessToChangeTypeDepartemen)
    EditText AccessToChangeTypeDepartemen;
    @BindView(R.id.TitleDepartemen)
    EditText TitleDepartemen;
    @BindView(R.id.IdActSetTicketSubmit)
    EditText Id;
    private ConfigStaticValue configStaticValue = new ConfigStaticValue(this);
    private ConfigRestHeader configRestHeader = new ConfigRestHeader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_ticket_set_ticket_submit);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        lblLayout.setText("TicketingTaskSubmit");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra(ActArticle.LAYOUT_VALUE));
    }

    @OnClick(R.id.api_test_submit_button)
    public void onClick(View v) {
        progressBar.setVisibility(View.VISIBLE);
        getData();
    }

    private void getData() {
        TicketingSubmitRequest request = new TicketingSubmitRequest();
        if (!name.getText().toString().matches("")) {
            request.Name = name.getText().toString();
        }
        if (!email.getText().toString().matches("")) {
            request.Email = email.getText().toString();
        }
        if (!phoneNo.getText().toString().matches("")) {
            request.PhoneNo = phoneNo.getText().toString();
        }
        if (!uploadName.getText().toString().matches("")) {
            request.UploadName = uploadName.getText().toString();
        }
        if (!LinkTicketingDepartemenId.getText().toString().matches("")) {
            request.LinkTicketingDepartemenId = LinkTicketingDepartemenId.getText().toString();
        }
        if (!Id.getText().toString().matches("")) {
            if (Id.getInputType() != InputType.TYPE_CLASS_NUMBER) {
                Id.setError("InValid Info !!");
                progressBar.setVisibility(View.GONE);
                return;
            } else {
                request.Id = Long.valueOf(Priority.getText().toString());
            }
        }
        if (!title.getText().toString().matches("")) {
            request.Title = title.getText().toString();
        }
        if (!Question.getText().toString().matches("")) {
            request.Title = Question.getText().toString();
        }
        if (!Answer.getText().toString().matches("")) {
            request.Title = Answer.getText().toString();
        }
        if (!HtmlBody.getText().toString().matches("")) {
            request.HtmlBody = HtmlBody.getText().toString();
        }
        if (!Priority.getText().toString().matches("")) {
            if (Priority.getInputType() != InputType.TYPE_CLASS_NUMBER) {
                Priority.setError("InValid Info !!");
                progressBar.setVisibility(View.GONE);
                return;
            } else {
                request.Priority = Integer.valueOf(Priority.getText().toString());
            }
        }
        if (!LinkTicketTypeId.getText().toString().matches("")) {
            request.LinkTicketTypeId = TicketStatus.getText().toString();
        }
        if (!TicketStatus.getText().toString().matches("")) {
            if (TicketStatus.getInputType() != InputType.TYPE_CLASS_NUMBER) {
                TicketStatus.setError("InValid Info !!");
                progressBar.setVisibility(View.GONE);
                return;
            } else {
                request.TicketStatus = Integer.valueOf(TicketStatus.getText().toString());
            }
        }
        if (!UserIpAddress.getText().toString().matches("")) {
            request.UserIpAddress = UserIpAddress.getText().toString();
        }
        if (!DeviceInformation.getText().toString().matches("")) {
            request.DeviceInformation = DeviceInformation.getText().toString();
        }
        if (!IdDepartemen.getText().toString().matches("") &&
                !TitleDepartemen.getText().toString().matches("") &&
                !DefaultAnswerBodyDepartemen.getText().toString().matches("") &&
                !PriorityDepartemen.getText().toString().matches("") &&
                !AccessToChangeTypeDepartemen.getText().toString().matches("")) {
            TicketingDepartemen ticketingDepartemen = new TicketingDepartemen();
            ticketingDepartemen.Id = Integer.valueOf(IdDepartemen.getText().toString());
            ticketingDepartemen.Title = TitleDepartemen.getText().toString();
            ticketingDepartemen.DefaultAnswerBody = DefaultAnswerBodyDepartemen.getText().toString();
            ticketingDepartemen.Priority = Integer.valueOf(Priority.getText().toString());
            ticketingDepartemen.AccessToChangeType = Integer.valueOf(AccessToChangeTypeDepartemen.getText().toString());
            request.virtual_Departemen = ticketingDepartemen;
        }
        RetrofitManager manager = new RetrofitManager(ActSetTicketSubmit.this);
        ITicket iTicket = manager.getRetrofit(configStaticValue.ApiBaseUrl).create(ITicket.class);
        Map<String, String> headers = new HashMap<>();
        headers = configRestHeader.GetHeaders(this);
        headers.put("PackageName", EasyPreference.with(this).getString("packageName",""));
        Observable<TicketingSubmitResponse> call = iTicket.SetTicketSubmit(headers, request);
        call.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<TicketingSubmitResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(TicketingSubmitResponse response) {
                        JsonDialog cdd = new JsonDialog(ActSetTicketSubmit.this, response);
                        cdd.setCanceledOnTouchOutside(false);
                        cdd.show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        Log.i("Error", e.getMessage());
                        Toast.makeText(ActSetTicketSubmit.this, "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
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
