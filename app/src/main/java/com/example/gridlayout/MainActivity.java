package com.example.gridlayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.os.Handler;
import java.lang.Math.*;
import android.content.Intent;
import android.widget.EditText;




import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private int[][] bombnums= new int[10][8];
    private int clock = 0;
    private boolean running = false;
    private static final int COLUMN_COUNT = 8;
    private boolean pick_mode =true;
    private int num_flags=4;
    public TextView flagView;
    public boolean gameEnd=false;
    public boolean gameEndWin=false;
    public boolean gameEndLoss=false;

    private ArrayList<TextView> cell_tvs;

    private int dpToPixel(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cell_tvs = new ArrayList<TextView>();
        int bomb1x, bomb1y, bomb2x, bomb2y, bomb3x, bomb3y, bomb4x, bomb4y;
        GridLayout grid = (GridLayout) findViewById(R.id.gridLayout01);
        bomb1x=(int)(Math.random() * 7);
        bomb1y=(int)(Math.random() * 9);
        bomb2x=(int)(Math.random() * 7);
        bomb2y=(int)(Math.random() * 9);
        while(bomb2x==bomb1x && bomb2y==bomb1y){
            bomb2x=(int)(Math.random() * 7);
            bomb2y=(int)(Math.random() * 9);
        }
        bomb3x=(int)(Math.random() * 7);
        bomb3y=(int)(Math.random() * 9);
        while((bomb3x==bomb1x && bomb3y==bomb1y) || (bomb3x==bomb2x && bomb3y==bomb2y)){
            bomb3x=(int)(Math.random() * 7);
            bomb3y=(int)(Math.random() * 9);
        }
        bomb4x=(int)(Math.random() * 7);
        bomb4y=(int)(Math.random() * 9);
        while((bomb4x==bomb1x && bomb4y==bomb1y) || (bomb4x==bomb2x && bomb4y==bomb2y) || (bomb4x==bomb3x && bomb4y==bomb3y)){
            bomb4x=(int)(Math.random() * 7);
            bomb4y=(int)(Math.random() * 9);
        }
        LayoutInflater li = LayoutInflater.from(this);
        for (int i = 0; i<=7; i++) {
            for (int j=0; j<=9; j++) {
                int counter=0;
                if((i==bomb1x && j==bomb1y) || (i==bomb2x && j==bomb2y) || (i==bomb3x && j==bomb3y) || (i==bomb4x && j==bomb4y)){
                    counter=5;
                }
                else{
                    if(i>=(bomb1x-1) && i<=(bomb1x+1) && j>=(bomb1y-1) && j<=(bomb1y+1)){
                        counter++;
                    }
                    if(i>=(bomb2x-1) && i<=(bomb2x+1) && j>=(bomb2y-1) && j<=(bomb2y+1)){
                        counter++;
                    }
                    if(i>=(bomb3x-1) && i<=(bomb3x+1) && j>=(bomb3y-1) && j<=(bomb3y+1)){
                        counter++;
                    }
                    if(i>=(bomb4x-1) && i<=(bomb4x+1) && j>=(bomb4y-1) && j<=(bomb4y+1)){
                        counter++;
                    }
                }
                bombnums[j][i]=counter;
                TextView tv = (TextView) li.inflate(R.layout.custom_cell_layout, grid, false);
                tv.setHeight( dpToPixel(30));
                tv.setWidth( dpToPixel(30));
                tv.setText(String.valueOf(counter));
                tv.setTextColor(Color.GREEN);
                tv.setBackgroundColor(Color.GREEN);
                tv.setOnClickListener(this::onClickTV);

                GridLayout.LayoutParams lp = (GridLayout.LayoutParams) tv.getLayoutParams();
                lp.rowSpec = GridLayout.spec(j);
                lp.columnSpec = GridLayout.spec(i);

                grid.addView(tv, lp);

                cell_tvs.add(tv);
            }
        }
        if (savedInstanceState != null) {
            clock = savedInstanceState.getInt("clock");
            running = savedInstanceState.getBoolean("running");
        }

        runTimer();
    }
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("clock", clock);
        savedInstanceState.putBoolean("running", running);
    }
    public void onClickStart(View view) {
        running = true;
    }

//    public void onClickStop(View view) {
//        running = false;
//    }
//    public void onClickClear(View view) {
//        running = false;
//        clock = 0;
//    }

    private void runTimer() {
        final TextView timeView = (TextView) findViewById(R.id.textView02);
        final Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                int seconds = clock%60;
                if(clock>59){
                    seconds+=60;
                }
                if(clock>119){
                    seconds+=60;
                }
                if(clock>179){
                    seconds+=60;
                }
                if(clock>239){
                    seconds+=60;
                }
                if(clock>299){
                    seconds+=60;
                }
                String time = String.format("%02d", seconds);
                timeView.setText(time);

                if (running) {
                    clock++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }
    public void changeFlag() {
        //Commit 4
        flagView = findViewById(R.id.textViewNumberFlags);
        flagView.setText(String.valueOf(num_flags));
    }


    public void onClickSwitch(View view){
        TextView tv = (TextView) view;
        if(pick_mode==true){
            tv.setText(String.valueOf("\uD83D\uDEA9"));
            pick_mode=false;
        }
        else{
            tv.setText(String.valueOf("\u26CF"));
            pick_mode=true;
        }
    }

    private int findIndexOfCellTextView(TextView tv) {
        for (int n=0; n<cell_tvs.size(); n++) {
            if (cell_tvs.get(n) == tv)
                return n;
        }
        return -1;
    }
    public void gameEndLoss(){
//        for (int n=0; n<cell_tvs.size(); n++) {
//            if (cell_tvs.get(n).getCurrentTextColor() != Color.BLACK && cell_tvs.get(n).getCurrentTextColor()!= Color.GRAY && cell_tvs.get(n).getCurrentTextColor()!= Color.GREEN
//            && !cell_tvs.get(n).getText().equals("\uD83D\uDEA9")) {
//                cell_tvs.get(n).setText(String.valueOf("\uD83D\uDCA3"));
//                cell_tvs.get(n).setTextColor(Color.BLACK);
//                cell_tvs.get(n).setBackgroundColor(Color.GRAY);
//            }
//            if (cell_tvs.get(n).getText().equals("5")){
//                cell_tvs.get(n).setText(String.valueOf("\uD83D\uDCA3"));
//                cell_tvs.get(n).setTextColor(Color.BLACK);
//                cell_tvs.get(n).setBackgroundColor(Color.GRAY);
//            }
//        }
        for (int n=0; n<cell_tvs.size(); n++) {
            int num = findIndexOfCellTextView(cell_tvs.get(n));
            int i = num % 10; //row 0-9
            int j = num / 10;
            if (bombnums[i][j] ==5){
                cell_tvs.get(n).setText(String.valueOf("\uD83D\uDCA3"));
                cell_tvs.get(n).setTextColor(Color.BLACK);
                cell_tvs.get(n).setBackgroundColor(Color.GRAY);
            }
        }

        gameEnd=true;
        gameEndLoss=true;
        running=false;
    }
    public void gameEndWinFlag(){
        int correct_flags=4;
        if(num_flags==0){
            for (int n=0; n<cell_tvs.size(); n++) {
                if (cell_tvs.get(n).getText().equals("\uD83D\uDEA9")) {
                    int num = findIndexOfCellTextView(cell_tvs.get(n));
                    int i = num % 10; //row 0-9
                    int j = num / 10;
                    if (bombnums[i][j] == 5){
                        correct_flags--;
                    }
                    else{
                        correct_flags++;
                    }
                }
            }
        }
        if(correct_flags==0){
            for (int n=0; n<cell_tvs.size(); n++) {
                if (cell_tvs.get(n).getText().equals("\uD83D\uDEA9")) {
                    cell_tvs.get(n).setText(String.valueOf("\uD83D\uDCA3"));
                    cell_tvs.get(n).setTextColor(Color.BLACK);
                    cell_tvs.get(n).setBackgroundColor(Color.GRAY);
                }
            }
            gameEnd=true;
            gameEndWin=true;
            running=false;
        }

    }
    public void gameEndWinOpen(){
        if(!gameEnd) {
            int count = 0;
            for (int n = 0; n < cell_tvs.size(); n++) {
                if (cell_tvs.get(n).getCurrentTextColor() != Color.BLACK && cell_tvs.get(n).getCurrentTextColor() != Color.GRAY) {
                    count++;
                }
            }

            if (count == 4) {
                for (int n=0; n<cell_tvs.size(); n++) {
                    int num = findIndexOfCellTextView(cell_tvs.get(n));
                    int i = num % 10; //row 0-9
                    int j = num / 10;
                    if (bombnums[i][j] ==5){
                        cell_tvs.get(n).setText(String.valueOf("\uD83D\uDCA3"));
                        cell_tvs.get(n).setTextColor(Color.BLACK);
                        cell_tvs.get(n).setBackgroundColor(Color.GRAY);
                    }
                }
                gameEnd = true;
                gameEndWin = true;
                running = false;
            }
        }
    }

    public void DFS(TextView tv){
        int n = findIndexOfCellTextView(tv);
        int i = n % 10; //row 0-9
        int j = n / 10; //column 0-7
        if(bombnums[i][j]==0) {
            if (tv.getCurrentTextColor() != Color.DKGRAY) {
                tv.setBackgroundColor(Color.GRAY);
                tv.setTextColor(Color.GRAY);
                if (j > 0 && cell_tvs.get(n - 10).getCurrentTextColor() != Color.GRAY) {
//                    cell_tvs.get(n-10).setTextColor(Color.GRAY);
                    DFS(cell_tvs.get(n - 10));
                    if (i > 0) {
                        DFS(cell_tvs.get(n - 10 - 1));
                    }
                    if (i < 9) {
                        DFS(cell_tvs.get(n - 10 + 1));
                    }
                }

                if (j < 7 && cell_tvs.get(n + 10).getCurrentTextColor() != Color.GRAY) {
                    DFS(cell_tvs.get(n + 10));
                    if (i > 0) {
                        DFS(cell_tvs.get(n - 1 + 10));
                    }
                    if (i < 9) {
                        DFS(cell_tvs.get(n + 1 + 10));
                    }
                }
                if (i > 0 && cell_tvs.get(n - 1).getCurrentTextColor() != Color.GRAY) {
                    DFS(cell_tvs.get(n - 1));
                }
                if (i < 9 && cell_tvs.get(n + 1).getCurrentTextColor() != Color.GRAY) {
                    DFS(cell_tvs.get(n + 1));
                }
            }
        }
        else if(bombnums[i][j]!=5){
            if(tv.getCurrentTextColor()!=Color.DKGRAY){
                tv.setBackgroundColor(Color.GRAY);
//                tv.setText(bombnums[i][j]);
                tv.setTextColor(Color.BLACK);
            }
        }
    }

    public void onClickTV(View view) {
        // final TextView flagView = (TextView) findViewById(R.id.textViewNumberFlags);
        if(!gameEnd) {
            TextView tv = (TextView) view;
            onClickStart(view);
            int n = findIndexOfCellTextView(tv);
            int i = n % 10; //row 0-9
            int j = n / 10; //column 0-7

            //open green square
            if (pick_mode && tv.getCurrentTextColor() == Color.GREEN) {
                if (bombnums[i][j] == 5) {
                    tv.setText(String.valueOf("\uD83D\uDCA3"));
                    tv.setTextColor(Color.BLACK);
                    tv.setBackgroundColor(Color.GRAY);
                    gameEndLoss();
                } else {
                    if(bombnums[i][j] == 0){
                        tv.setTextColor(Color.GRAY);
                        tv.setBackgroundColor(Color.GRAY);
                        DFS(tv);
                    }
                    else if (bombnums[i][j] != 0) {
                        tv.setText(String.valueOf(bombnums[i][j]));
                        tv.setTextColor(Color.BLACK);
                        tv.setBackgroundColor(Color.GRAY);
                    }
                }
               // tv.setTextColor(Color.BLACK);
                gameEndWinOpen();
            }

            //flag on green space
            else if (!pick_mode && tv.getCurrentTextColor() == Color.GREEN) {
                tv.setTextColor(Color.DKGRAY);
                tv.setBackgroundColor(Color.GREEN);
                tv.setText(String.valueOf("\uD83D\uDEA9"));
                num_flags--;
                changeFlag();
                gameEndWinFlag();

            }
            //retract flag
            else if (!pick_mode && tv.getCurrentTextColor() == Color.DKGRAY) {
                tv.setText(String.valueOf(bombnums[i][j]));
                tv.setTextColor(Color.GREEN);
                tv.setBackgroundColor(Color.GREEN);
                num_flags++;
                changeFlag();
                gameEndWinFlag();

            }


        }
        else {
            if(gameEndWin){

                String message = "Used "+String.valueOf(clock)+" seconds.\nYou won!\nGood job!";
                Intent intent = new Intent(this, DisplayMessageActivity.class);
                intent.putExtra("com.example.sendmessage.MESSAGE", message);
                startActivity(intent);
            }
            else if(gameEndLoss){

                String message = "Used "+String.valueOf(clock)+" seconds.\nYou lost!\nNice try!";
                Intent intent = new Intent(this, DisplayMessageActivity.class);
                intent.putExtra("com.example.sendmessage.MESSAGE", message);
                startActivity(intent);
            }
        }
    }
}