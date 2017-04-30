package com.cputest.sudhanshu.cputest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.math.BigInteger;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class MainActivity extends AppCompatActivity {

    private Button button1, button2;
    private RelativeLayout rLayout;
    private ProgressBar pbWork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        pbWork = (ProgressBar) findViewById(R.id.pbWorking);
        rLayout = (RelativeLayout) findViewById(R.id.loadingPanel);
        button2.setEnabled(false);
        final ExecutorService executorService = Executors.newFixedThreadPool(10);

        button1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                for (int j = 0; j < 10; j++) {
                        final int ID = j;
                        executorService.submit(new Runnable() {
                            public void run() {
                                for (int i=0;i < Integer.MAX_VALUE; i++) {
                                    System.out.println(ID+" worker: "+i + ": " + fib(new BigInteger(String.valueOf(i))));
                                }
                            }
                        });
                }
                pbWork.getIndeterminateDrawable().setColorFilter(Color.parseColor("#ffffff"),android.graphics.PorterDuff.Mode.SRC_ATOP);
                rLayout.setVisibility(View.VISIBLE);
                button1.setEnabled(false);
                button2.setEnabled(true);
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.quicinc.trepn");
                startActivity( intent );
            }
        });

        button2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                for (int i = 0; i < 10; i ++){
                    executorService.shutdownNow();
                }
                rLayout.setVisibility(View.GONE);
                if(Build.VERSION.SDK_INT >= 21){
                    MainActivity.this.finishAndRemoveTask();
                } else{
                    MainActivity.this.finish();
                }
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.quicinc.trepn");
                startActivity( intent );
            }
        });
    }

    public BigInteger fib(BigInteger n){
        if (n.compareTo(BigInteger.ONE) == -1 || n.compareTo(BigInteger.ONE) == 0 ) return n;
        else
            return fib(n.subtract(BigInteger.ONE)).add(fib(n.subtract(BigInteger.ONE).subtract(BigInteger.ONE)));
    }
}
