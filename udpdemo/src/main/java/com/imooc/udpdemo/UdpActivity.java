package com.imooc.udpdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.imooc.udpdemo.biz.UdpClientBiz;

public class UdpActivity extends AppCompatActivity {

    private EditText mEdContent;
    private Button mBtnSend;
    private TextView mTvContent;

    private UdpClientBiz mUdpClientBiz = new UdpClientBiz();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String inputMsg = mEdContent.getText().toString();
                mUdpClientBiz.sendMsg(inputMsg,
                        new UdpClientBiz.ReceiveMsgListener() {
                            @Override
                            public void onReceive(String msg) {
                                appendMsgToTvContent("Client:" + inputMsg);
                                appendMsgToTvContent(msg);
                            }
                        });
            }
        });


    }

    private void appendMsgToTvContent(String inputMsg) {
        mTvContent.append(inputMsg + "\n");
    }

    private void initViews() {
        mEdContent = (EditText) findViewById(R.id.id_et_content);
        mBtnSend = (Button) findViewById(R.id.id_btn_send);
        mTvContent = (TextView) findViewById(R.id.id_tv_content);
    }
}
