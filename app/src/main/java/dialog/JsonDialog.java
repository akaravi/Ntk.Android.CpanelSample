package dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.dandan.jsonhandleview.library.JsonViewLayout;
import com.google.gson.Gson;

import org.json.JSONObject;

import ntk.base.api.article.model.ArticleContentResponse;
import ntk.base.app.R;


public class JsonDialog extends Dialog {

    private Object  text;
    public Activity c;

    public JsonDialog(Activity a, Object message) {
        super(a);
        text = message;
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.json_dialog);
        JsonViewLayout jsonViewLayout = findViewById(R.id.txt_dia);
        jsonViewLayout.bindJson(new Gson().toJson(text));
        jsonViewLayout.collapseAll();
        Button closeDia = findViewById(R.id.close_dia);
        closeDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}