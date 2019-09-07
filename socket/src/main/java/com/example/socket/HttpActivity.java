package com.example.socket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.socket.biz.TCPClientBiz;
import com.example.socket.https.HttpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HttpActivity extends AppCompatActivity {

    @BindView(R.id.et_client)
    EditText etClient;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.tv_content)
    TextView tvContent;
    private TCPClientBiz udpClientBiz=new TCPClientBiz();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }


    @OnClick(R.id.btn_send)
    public void onClick() {
        String url=etClient.getText().toString();
        HttpUtils.doGet(url, new HttpUtils.HttpListener() {
            @Override
            public void onSuccess(final String context) {
                tvContent.post(new Runnable() {
                    @Override
                    public void run() {
                        tvContent.setText(context);
                    }
                });
            }

            @Override
            public void onFail(Exception ex) {
                ex.printStackTrace();
            }
        });
    }




}
