package com.thecuong064.pointcounter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final String HOME_NAME = "HOME_NAME";
    private final String GUEST_NAME = "GUEST_NAME";

    Button homeInc3, homeInc2, homeInc1,
        homeDec3, homeDec2, homeDec1,
        guestInc3, guestInc2, guestInc1,
        guestDec3, guestDec2, guestDec1;

    Button resetAll;

    TextView homeName, guestName;
    TextView homePointHundred, homePointTen, homePointUnit;
    TextView guestPointHundred, guestPointTen, guestPointUnit;

    long homePointValue, guestPointValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homePointValue = 0;
        guestPointValue = 0;

        initView();

        initViewOnClick();
    }

    private void initView() {
        homeInc3 = findViewById(R.id.btn_home_inc_3);
        homeInc2 = findViewById(R.id.btn_home_inc_2);
        homeInc1 = findViewById(R.id.btn_home_inc_1);
        homeDec3 = findViewById(R.id.btn_home_dec_3);
        homeDec2 = findViewById(R.id.btn_home_dec_2);
        homeDec1 = findViewById(R.id.btn_home_dec_1);
        guestInc3 = findViewById(R.id.btn_guest_inc_3);
        guestInc2 = findViewById(R.id.btn_guest_inc_2);
        guestInc1 = findViewById(R.id.btn_guest_inc_1);
        guestDec3 = findViewById(R.id.btn_guest_dec_3);
        guestDec2 = findViewById(R.id.btn_guest_dec_2);
        guestDec1 = findViewById(R.id.btn_guest_dec_1);

        homeName = findViewById(R.id.tv_home_team_name);
        guestName = findViewById(R.id.tv_guest_team_name);

        homePointHundred = findViewById(R.id.tv_home_point_hundred_row);
        homePointTen = findViewById(R.id.tv_home_point_ten_row);
        homePointUnit = findViewById(R.id.tv_home_point_unit_row);

        guestPointHundred = findViewById(R.id.tv_guest_point_hundred_row);
        guestPointTen = findViewById(R.id.tv_guest_point_ten_row);
        guestPointUnit = findViewById(R.id.tv_guest_point_unit_row);

        resetAll = findViewById(R.id.btn_reset_points);
    }


    private void initViewOnClick() {

        homeInc3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pointButtonOnClick(v);
            }
        });

        homeInc2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pointButtonOnClick(v);
            }
        });

        homeInc1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pointButtonOnClick(v);
            }
        });

        homeDec3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pointButtonOnClick(v);
            }
        });

        homeDec2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pointButtonOnClick(v);
            }
        });

        homeDec1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pointButtonOnClick(v);
            }
        });

        guestInc3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pointButtonOnClick(v);
            }
        });

        guestInc2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pointButtonOnClick(v);
            }
        });

        guestInc1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pointButtonOnClick(v);
            }
        });

        guestDec3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pointButtonOnClick(v);
            }
        });

        guestDec2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pointButtonOnClick(v);
            }
        });

        guestDec1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pointButtonOnClick(v);
            }
        });

        homeName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogEditTeamName(HOME_NAME);
            }
        });

        guestName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogEditTeamName(GUEST_NAME);
            }
        });

        resetAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPoints();
            }
        });
    }

    private void pointButtonOnClick(View v) {

        switch (v.getId()) {
            case R.id.btn_home_inc_3:
                homePointValue += 3;
                break;
            case R.id.btn_home_inc_2:
                homePointValue += 2;
                break;
            case R.id.btn_home_inc_1:
                homePointValue += 1;
                break;
            case R.id.btn_home_dec_3:
                homePointValue -= 3;
                break;
            case R.id.btn_home_dec_2:
                homePointValue -= 2;
                break;
            case R.id.btn_home_dec_1:
                homePointValue -= 1;
                break;
            case R.id.btn_guest_inc_3:
                guestPointValue += 3;
                break;
            case R.id.btn_guest_inc_2:
                guestPointValue += 2;
                break;
            case R.id.btn_guest_inc_1:
                guestPointValue += 1;
                break;
            case R.id.btn_guest_dec_3:
                guestPointValue -= 3;
                break;
            case R.id.btn_guest_dec_2:
                guestPointValue -= 2;
                break;
            case R.id.btn_guest_dec_1:
                guestPointValue -= 1;
                break;
        }

        if (homePointValue < 0) homePointValue = 0;
        if (guestPointValue < 0) guestPointValue = 0;
        
        updatePoints();
    }


    private void updatePoints() {

        // --------------------HOME--------------
        long homePoint = homePointValue;

        if (homePoint < 10) {
            homePointHundred.setVisibility(View.GONE);
            homePointTen.setVisibility(View.GONE);
            homePointUnit.setText(homePoint + "");
        } else if (homePoint < 100) {
            homePointHundred.setVisibility(View.GONE);
            homePointTen.setVisibility(View.VISIBLE);

            homePointTen.setText(homePoint/10 + "");
            homePoint = homePoint - (homePoint/10)*10;
            homePointUnit.setText(homePoint + "");
        } else {
            homePointHundred.setVisibility(View.VISIBLE);
            homePointTen.setVisibility(View.VISIBLE);
            homePointHundred.setText(homePoint/100 + "");
            homePoint = homePoint - (homePoint/100)*100;

            if (homePoint < 10) {
                homePointTen.setText("0");
                homePointUnit.setText(homePoint + "");
            } else {
                homePointTen.setText(homePoint/10 + "");
                homePoint = homePoint - (homePoint/10)*10;
                homePointUnit.setText(homePoint + "");
            }
        }

        // --------------------GUEST--------------
        long guestPoint = guestPointValue;

        if (guestPoint < 10) {
            guestPointHundred.setVisibility(View.GONE);
            guestPointTen.setVisibility(View.GONE);
            guestPointUnit.setText(guestPoint + "");
        } else if (guestPoint < 100) {
            guestPointHundred.setVisibility(View.GONE);
            guestPointTen.setVisibility(View.VISIBLE);

            guestPointTen.setText(guestPoint/10 + "");
            guestPoint = guestPoint - (guestPoint/10)*10;
            guestPointUnit.setText(guestPoint + "");
        } else {
            guestPointHundred.setVisibility(View.VISIBLE);
            guestPointTen.setVisibility(View.VISIBLE);
            guestPointHundred.setText(guestPoint/100 + "");
            guestPoint = guestPoint - (guestPoint/100)*100;

            if (guestPoint < 10) {
                guestPointTen.setText("0");
                guestPointUnit.setText(guestPoint + "");
            } else {
                guestPointTen.setText(guestPoint/10 + "");
                guestPoint = guestPoint - (guestPoint/10)*10;
                guestPointUnit.setText(guestPoint + "");
            }
        }

    }

    private void openDialogEditTeamName(final String team) {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Team name");
        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.input_dialog, null);
        builder.setView(customLayout);
        // add a button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send data from the AlertDialog to the Activity
                EditText editText = customLayout.findViewById(R.id.editText);
                changeTeamName(team, editText.getText().toString());
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void changeTeamName(String team, String newName) {
        if (newName.isEmpty()) return;
        if (team.equals(HOME_NAME)) {
            homeName.setText(newName.toUpperCase());
        } else {
            guestName.setText(newName.toUpperCase());
        }
    }

    private void resetPoints() {
        homePointValue = 0;
        guestPointValue = 0;
        homePointHundred.setVisibility(View.GONE);
        homePointTen.setVisibility(View.GONE);
        guestPointHundred.setVisibility(View.GONE);
        guestPointTen.setVisibility(View.GONE);
        homePointUnit.setText("0");
        guestPointUnit.setText("0");
    }
}