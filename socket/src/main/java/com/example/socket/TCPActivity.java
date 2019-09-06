package com.example.socket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.socket.biz.TCPClientBiz;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TCPActivity extends AppCompatActivity {

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

        udpClientBiz.setListener(new TCPClientBiz.OnMsgComingListener() {
            @Override
            public void onMsgComing(String msg) {
                appendMsgToContent(msg);
            }

            @Override
            public void onError(Exception ex) {
                ex.printStackTrace();
            }
        });

    }


    @OnClick(R.id.btn_send)
    public void onClick() {
        String msg=etClient.getText().toString();
        udpClientBiz.sendMsg(msg);
    }

    private void appendMsgToContent(String s) {
        tvContent.append(s+"\n");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        udpClientBiz.destory();
    }
}
