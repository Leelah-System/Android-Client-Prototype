package com.leelah.client.tablet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.leelah.client.tablet.LeelahApplication.BelongsToUserRegistration;
import com.leelah.client.tablet.bo.User;
import com.leelah.client.tablet.service.LeelahServices;
import com.smartnsoft.droid4me.app.AppPublics;
import com.smartnsoft.droid4me.app.AppPublics.GuardedCommand;
import com.smartnsoft.droid4me.app.SmartActivity;
import com.smartnsoft.droid4me.cache.Values.CacheException;

/**
 * The starting screen of the application.
 * 
 * @author Jocelyn Girard
 * @since 2011-03-01
 */
public final class LoginActivity
    extends SmartActivity<Void>
    implements BelongsToUserRegistration, OnClickListener
{

  public static final String USER_PASSWORD = "password";

  public static final String USER_LOGIN = "login";

  public static final String SERVER_ADDRESS = "serverAddress";

  private EditText editIpAddress;

  private EditText editLogin;

  private EditText editPassword;

  private Button buttonLogin;

  private ProgressDialog progressDialog;

  public void onRetrieveDisplayObjects()
  {
    // On set le layout de l'activité
    setContentView(R.layout.login);

    // On récup les éléments graphique de la vue
    editIpAddress = (EditText) findViewById(R.id.editIpAddress);
    editLogin = (EditText) findViewById(R.id.editLogin);
    editPassword = (EditText) findViewById(R.id.editPassword);
    buttonLogin = (Button) findViewById(R.id.buttonLogin);
  }

  public void onRetrieveBusinessObjects()
      throws BusinessObjectUnavailableException
  {
    progressDialog = new ProgressDialog(this);
    progressDialog.setIndeterminate(true);
    progressDialog.setMessage(getString(R.string.applicationName));
  }

  public void onFulfillDisplayObjects()
  {
    // Set l'interface de clique au boutton
    buttonLogin.setOnClickListener(this);

    editIpAddress.setText(getPreferences().getString(SERVER_ADDRESS, ""));
    editLogin.setText(getPreferences().getString(USER_LOGIN, ""));
    editPassword.setText(getPreferences().getString(USER_PASSWORD, ""));
  }

  public void onSynchronizeDisplayObjects()
  {

  }

  public void onClick(View view)
  {
    if (view == buttonLogin)
    {
      // On récup les données des EditText de l'interface
      final String login = editLogin.getText().toString();
      final String password = editPassword.getText().toString();
      final String address = editIpAddress.getText().toString();

      // Création de l'objet parametre pour le webservice
      final User user = new User(login, password, address);

      progressDialog.show();

      AppPublics.THREAD_POOL.execute(new GuardedCommand(this)
      {
        @Override
        protected void runGuarded()
            throws Exception
        {
          try
          {
            // On contact le serveur pour s'authentifier
            LeelahServices.getInstance().authenticate(user);

            // Si ca réussi on enregistre les informations dans les préférences
            final Editor editorPreferences = getPreferences().edit();
            editorPreferences.putString(SERVER_ADDRESS, address);
            editorPreferences.putString(USER_LOGIN, login);
            editorPreferences.putString(USER_PASSWORD, password);
            editorPreferences.commit();
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
          }
          catch (CacheException exception)
          {
            // Si ca plante on log et le framework gère l'exception
            if (log.isErrorEnabled())
            {
              log.error("Error while authenticate user '" + login + "' with password '" + password + "' on server address '" + address + "'", exception);
            }
          }
          progressDialog.dismiss();
        }
      });

    }
  }
}
