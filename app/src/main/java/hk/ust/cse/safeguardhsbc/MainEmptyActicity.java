package hk.ust.cse.safeguardhsbc;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by yejin on 21/10/2017.
 */

public class MainEmptyActicity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent activityIntent;

        // go straight to main if a token is stored
       /* if (Util.getToken() != null) {
            activityIntent = new Intent(this, ChatClient.class);
        } else {
            activityIntent = new Intent(this, LogInActivity.class);
        }*/

        activityIntent = new Intent (this, LogInActivity.class);

        startActivity(activityIntent);
        finish();
    }
}
