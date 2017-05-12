package com.secretbiology.battleship.setup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.secretbiology.battleship.R;
import com.secretbiology.battleship.common.GamePref;
import com.secretbiology.helpers.general.General;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Login extends AppCompatActivity {

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.email)
    TextView email;

    private GamePref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);
        pref = new GamePref(getBaseContext());
        if (pref.isAuthenticated()) {
            startActivity(new Intent(this, GameSetup.class));
        }
    }

    @OnClick(R.id.enter_btn)
    public void enterGame() {
        if (name.getText().toString().trim().length() == 0) {
            General.makeLongToast(getBaseContext(), "Name can not be empty");
        } else if (!General.isValidEmail(email.getText().toString())) {
            General.makeLongToast(getBaseContext(), "Not a valid email ID");
        } else {
            pref.setName(name.getText().toString());
            pref.setEmail(email.getText().toString());
            pref.userLoggedIn();
            startActivity(new Intent(this, GameSetup.class));
        }
    }
}
