package com.leelah.client.tablet;

import org.apache.http.protocol.HTTP;

import android.util.Log;
import com.leelah.client.tablet.R;

/**
 * Gathers in one place the constants of the application.
 * 
 * @author Jocelyn Girard
 * @since 2011-03-01
 */
public abstract class Constants
{

  /**
   * The logging level of the application and of the droid4me framework.
   */
  public static final int LOG_LEVEL = Log.DEBUG;

  /**
   * The e-mail that will receive error reports.
   */
  public static final String REPORT_LOG_RECIPIENT_EMAIL = "leelah-system@googlegroups.com";

  /**
   * The encoding used for wrapping the URL of the HTTP requests.
   */
  public static final String WEBSERVICES_HTML_ENCODING = HTTP.ISO_8859_1;

}