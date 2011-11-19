package com.leelah.client.tablet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.Window;

import com.leelah.client.tablet.TitleBar.TitleBarDiscarded;
import com.leelah.client.tablet.bo.User;
import com.leelah.client.tablet.service.LeelahServices;
import com.smartnsoft.droid4me.LifeCycle.BusinessObjectsRetrievalAsynchronousPolicy;
import com.smartnsoft.droid4me.app.SmartSplashScreenActivity;
import com.smartnsoft.droid4me.cache.Values.CacheException;

/**
 * The first activity displayed while the application is loading.
 * 
 * @author Jocelyn Girard
 * @since 2011-03-01
 */
public final class LeelahSplashScreenActivity
    extends SmartSplashScreenActivity
    implements BusinessObjectsRetrievalAsynchronousPolicy, TitleBarDiscarded
{

  private final static int MISSING_SD_CARD_DIALOG_ID = 0;

  @Override
  protected Dialog onCreateDialog(int id)
  {
    if (id == LeelahSplashScreenActivity.MISSING_SD_CARD_DIALOG_ID)
    {
      return new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle(R.string.applicationName).setMessage(
          R.string.LeelahSplashScreen_dialogMessage_noSdCard).setPositiveButton(android.R.string.ok, new OnClickListener()
      {
        public void onClick(DialogInterface dialog, int which)
        {
          finish();
        }
      }).create();
    }
    return super.onCreateDialog(id);
  }

  @Override
  protected boolean requiresExternalStorage()
  {
    return true;
  }

  @Override
  protected void onNoExternalStorage()
  {
    showDialog(LeelahSplashScreenActivity.MISSING_SD_CARD_DIALOG_ID);
  }

  @Override
  protected Class<? extends Activity> getNextActivity()
  {
    return LoginActivity.class;
  }

  @Override
  protected void onRetrieveDisplayObjectsCustom()
  {
    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
    setContentView(LayoutInflater.from(this).inflate(R.layout.leelah_splash_screen, null));
    setProgressBarIndeterminateVisibility(true);
  }

  @Override
  protected void onRetrieveBusinessObjectsCustom()
      throws BusinessObjectUnavailableException
  {
    try
    {
      Thread.sleep(5000);
    }
    catch (InterruptedException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    if (LeelahServices.getInstance().hasParameters(getPreferences()))
    {
      final String login = getPreferences().getString(LoginActivity.USER_LOGIN, null);
      final String password = getPreferences().getString(LoginActivity.USER_PASSWORD, null);
      final String address = getPreferences().getString(LoginActivity.SERVER_ADDRESS, null);
      try
      {
        LeelahServices.getInstance().authenticate(new User(login, password, address));

      }
      catch (CacheException exception)
      {
        // Si ca plante on log et le framework gère l'exception
        if (log.isErrorEnabled())
        {
          log.error("Error while authenticate user '" + login + "' with password '" + password + "' on server address '" + address + "'", exception);
        }
      }
    }
    markAsInitialized();
  }

}
