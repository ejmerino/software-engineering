package ec.edu.monster.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ec.edu.monster.controller.LoginController;
import ec.edu.monster.R;

public class LoginActivity extends AppCompatActivity {

    EditText usernameField, passwordField;
    Button loginButton;
    TextView errorLabel;
    LoginController loginController = new LoginController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameField = findViewById(R.id.usernameField);
        passwordField = findViewById(R.id.passwordField);
        loginButton = findViewById(R.id.loginButton);
        errorLabel = findViewById(R.id.errorLabel);

        loginButton.setOnClickListener(v -> validateLogin());
    }

    private void validateLogin(){
        String user = usernameField.getText().toString().trim();
        String pass = passwordField.getText().toString().trim();

        if(loginController.validate(user, pass)){
            Toast.makeText(this,"Login exitoso!",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, ConversionActivity.class));
            finish();
        } else {
            errorLabel.setText("Usuario o contraseña incorrectos.");
        }
    }
}