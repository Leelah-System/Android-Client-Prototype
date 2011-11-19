package com.leelah.client.tablet;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.protocol.HTTP;

import android.webkit.WebView;

import com.leelah.client.tablet.LeelahApplication.BelongsToUserRegistration;
import com.smartnsoft.droid4me.LifeCycle.BusinessObjectsRetrievalAsynchronousPolicy;
import com.smartnsoft.droid4me.app.SmartActivity;
import com.smartnsoft.droid4me.ws.WebServiceCaller;

/**
 * The "about" screen.
 * 
 * @author Jocelyn Girard
 * @since 2011-03-01
 */
public final class AboutActivity
    extends SmartActivity<Void>
    implements BusinessObjectsRetrievalAsynchronousPolicy, BelongsToUserRegistration
{

  private String content;

  private WebView webView;

  public void onRetrieveDisplayObjects()
  {
    setContentView(R.layout.about);
    webView = (WebView) findViewById(R.id.webView);
    webView.getSettings().setSupportMultipleWindows(false);
    webView.getSettings().setSupportZoom(false);
    webView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
  }

  public void onRetrieveBusinessObjects()
      throws BusinessObjectUnavailableException
  {
    final InputStream inputStream = getResources().openRawResource(R.raw.about);
    try
    {
      content = WebServiceCaller.getString(inputStream);
    }
    catch (IOException exception)
    {
      throw new BusinessObjectUnavailableException(exception);
    }
  }

  public void onFulfillDisplayObjects()
  {
    webView.loadDataWithBaseURL("file:///android_asset/", content, "text/html", HTTP.UTF_8, null);
  }

  public void onSynchronizeDisplayObjects()
  {
  }

}
