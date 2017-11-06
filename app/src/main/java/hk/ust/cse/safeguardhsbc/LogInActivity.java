package hk.ust.cse.safeguardhsbc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yejin on 21/10/2017.
 */

public class LogInActivity extends Activity {





    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        final EditText idEditText = (EditText) findViewById(R.id.login_id);
        final EditText passwordEditText = (EditText) findViewById(R.id.login_password);
        final Button signInButton = (Button) findViewById(R.id.login_signIn);

        signInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final String username = idEditText.getText().toString();
                final String password = passwordEditText.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success){

                                Intent intent = new Intent(LogInActivity.this, ChatClient.class);

                                LogInActivity.this.startActivity(intent);


                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(LogInActivity.this);
                                builder.setMessage("Login Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }

                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(username, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LogInActivity.this);
                queue.add(loginRequest);


            }
        }
        );



    }


}

