package dps924.adpulse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class LoginActivty extends ActionBarActivity {

    EditText username, password;
    TextView loginError;
    Button loginButton;
    static ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        username = (EditText)findViewById(R.id.loginUsername);
        password = (EditText) findViewById(R.id.loginPassword);
        loginButton = (Button)findViewById(R.id.loginButton);
        loginError = (TextView)findViewById(R.id.loginError);
        loginError.setVisibility(View.GONE);

        progress = new ProgressDialog(this);
        progress.setTitle("Login Status");
        progress.setMessage("Authenticating user...");
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url = "http://adpulse.ca/token";
                progress.show();
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progress.dismiss();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progress.hide();
                                loginError.setVisibility(View.VISIBLE);
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("username", username.getText().toString());
                        params.put("password", password.getText().toString());
                        params.put("grant_type", "password");

                        return params;
                    }
                };
                ApplicationController.getInstance().addToRequestQueue(postRequest);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (progress!=null) {
            if (progress.isShowing()) {
                progress.dismiss();
            }
        }
    }
}
