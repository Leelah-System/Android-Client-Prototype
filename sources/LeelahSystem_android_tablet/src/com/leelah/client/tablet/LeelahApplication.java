package com.leelah.client.tablet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;

import com.leelah.client.tablet.service.LeelahServices;
import com.smartnsoft.droid4me.app.ActivityController;
import com.smartnsoft.droid4me.app.SmartApplication;
import com.smartnsoft.droid4me.bo.Business.InputAtom;
import com.smartnsoft.droid4me.cache.DbPersistence;
import com.smartnsoft.droid4me.cache.Persistence;
import com.smartnsoft.droid4me.download.AdvancedBitmapDownloader;
import com.smartnsoft.droid4me.download.BitmapDownloader;

/**
 * The entry point of the application.
 * 
 * @author Jocelyn Girard
 * @since 2011-03-01
 */
public final class LeelahApplication
    extends SmartApplication
{

  public interface BelongsToUserRegistration
  {

  }

  public static class CacheInstructions
      extends AdvancedBitmapDownloader.AdvancedAbstractInstructions
  {

    @Override
    public InputStream getInputStream(String imageUid, Object imageSpecs, String url, BitmapDownloader.InputStreamDownloadInstructor downloadInstructor)
        throws IOException
    {
      final InputAtom inputAtom = Persistence.getInstance(1).extractInputStream(url);
      return inputAtom == null ? null : inputAtom.inputStream;
    }

    @Override
    public InputStream onInputStreamDownloaded(String imageUid, Object imageSpecs, String url, InputStream inputStream)
    {
      final InputAtom inputAtom = Persistence.getInstance(1).flushInputStream(url, new InputAtom(new Date(), inputStream));
      return inputAtom == null ? null : inputAtom.inputStream;
    }

  }

  public final static BitmapDownloader.Instructions CACHE_IMAGE_INSTRUCTIONS = new LeelahApplication.CacheInstructions();

  @Override
  protected int getLogLevel()
  {
    return Constants.LOG_LEVEL;
  }

  @Override
  protected SmartApplication.I18N getI18N()
  {
    return new SmartApplication.I18N(getText(R.string.problem), getText(R.string.unavailableItem), getText(R.string.unavailableService), getText(R.string.connectivityProblem), getText(R.string.connectivityProblem), getText(R.string.unhandledProblem), getString(R.string.applicationName), getText(R.string.dialogButton_unhandledProblem), getString(R.string.progressDialogMessage_unhandledProblem));
  }

  @Override
  protected String getLogReportRecipient()
  {
    return Constants.REPORT_LOG_RECIPIENT_EMAIL;
  }

  @Override
  public void onCreateCustom()
  {
    super.onCreateCustom();

    // We initialize the persistence
    final String directoryName = getPackageManager().getApplicationLabel(getApplicationInfo()).toString();
    final File contentsDirectory = new File(Environment.getExternalStorageDirectory(), directoryName);
    Persistence.CACHE_DIRECTORY_PATHS = new String[] { contentsDirectory.getAbsolutePath(), contentsDirectory.getAbsolutePath() };
    DbPersistence.FILE_NAMES = new String[] { DbPersistence.DEFAULT_FILE_NAME, DbPersistence.DEFAULT_FILE_NAME };
    DbPersistence.TABLE_NAMES = new String[] { "data", "images" };
    Persistence.INSTANCES_COUNT = 2;
    Persistence.IMPLEMENTATION_FQN = DbPersistence.class.getName();

    // We set the ImageDownloader instances
    BitmapDownloader.IMPLEMENTATION_FQN = AdvancedBitmapDownloader.class.getName();
    BitmapDownloader.INSTANCES_COUNT = 1;
    BitmapDownloader.MAX_MEMORY_IN_BYTES = new long[] { 3 * 1024 * 1024 };
    BitmapDownloader.LOW_LEVEL_MEMORY_WATER_MARK_IN_BYTES = new long[] { 1 * 1024 * 1024 };
    BitmapDownloader.USE_REFERENCES = new boolean[] { false };
    BitmapDownloader.RECYCLE_BITMAP = new boolean[] { false };
  }

  @Override
  protected ActivityController.Redirector getActivityRedirector()
  {
    return new ActivityController.Redirector()
    {
      public Intent getRedirection(Activity activity)
      {
        if (LeelahSplashScreenActivity.isInitialized(LeelahSplashScreenActivity.class) == false)
        {
          // We re-direct to the splash screen activity if the application has not been yet initialized
          if (activity.getComponentName() == null || activity.getComponentName().getClassName().equals(LeelahSplashScreenActivity.class.getName()) == true)
          {
            return null;
          }
          else
          {
            return new Intent(activity, LeelahSplashScreenActivity.class);
          }
        }
        if (LeelahServices.getInstance().hasParameters(getPreferences()) == false)
        {
          if ((activity.getComponentName() == null || activity.getComponentName().getClassName().equals(LoginActivity.class.getName()) == false) && activity instanceof BelongsToUserRegistration == false)
          {
            return new Intent(activity, LoginActivity.class);
          }
          else
          {
            return null;
          }
        }
        return null;
      }
    };
  }

  @Override
  protected ActivityController.Interceptor getActivityInterceptor()
  {
    return new ActivityController.Interceptor()
    {
      public void onLifeCycleEvent(Activity activity, ActivityController.Interceptor.InterceptorEvent event)
      {
        // TitleBar.onLifeCycleEvent(activity, event);
      }
    };
  }

  @Override
  protected ActivityController.ExceptionHandler getExceptionHandler()
  {
    return super.getExceptionHandler();
  }

}
