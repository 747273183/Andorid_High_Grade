package com.example.socket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.socket.R;
import com.example.socket.biz.UDPClientBiz;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UDPActivity extends AppCompatActivity {

    @BindView(R.id.et_client)
    EditText etClient;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.tv_content)
    TextView tvContent;
    private UDPClientBiz udpClientBiz=new UDPClientBiz();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }


    @OnClick(R.id.btn_send)
    public void onClick() {
        String msg=etClient.getText().toString();
        appendMsgToContent("Client:"+msg);
        udpClientBiz.sendMsg(msg, new UDPClientBiz.OnMsgReturnedListener() {
             @Override
             public void onMsgReturned(String msg) {
                 appendMsgToContent("Server:"+msg);
             }

             @Override
             public void onError(Exception ex) {
                ex.printStackTrace();
             }
         });
    }

    private void appendMsgToContent(String s) {
        tvContent.append(s+"\n");
    }
}
