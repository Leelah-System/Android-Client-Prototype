package com.leelah.client.tablet;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartnsoft.droid4me.app.SmartActivity;

/**
 * An activity dialog box, which enables to share the containing application.
 * 
 * @author Jocelyn Girard
 * @since 2011-03-01
 */
public abstract class SharingActivity
    extends SmartActivity<Void>
{

  private static final int SHARE_DIALOG_ID = 0;

  protected CharSequence getSubject()
  {
    return getString(R.string.Sharing_send_subject, getString(R.string.applicationName));
  }

  protected SpannableStringBuilder computeBody()
  {
    final SpannableStringBuilder stringBuilder = new SpannableStringBuilder(Html.fromHtml(getString(R.string.Sharing_send_body1,
        getString(R.string.applicationName))));
    {
      final String applicationAndroidMarketUrl = "http://market.android.com/search?q=pname:" + getPackageName();
      final SpannableString applicationUrlString = new SpannableString(getString(R.string.applicationName));
      applicationUrlString.setSpan(new URLSpan(applicationAndroidMarketUrl), 0, applicationUrlString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
      stringBuilder.append(applicationUrlString);
    }
    {
      stringBuilder.append(new SpannableStringBuilder(Html.fromHtml(getString(R.string.Sharing_send_body2))));
    }
    return stringBuilder;
  }

  protected CharSequence getAndroidMarketRating()
  {
    return getString(R.string.Sharing_rateMe);
  }

  protected CharSequence getAndroidMarketOtherApplications()
  {
    return getString(R.string.Sharing_otherApplications);
  }

  protected String getAndroidMarketDeveloperName()
  {
    return "";
  }

  @Override
  protected Dialog onCreateDialog(int id)
  {
    switch (id)
    {
    case SharingActivity.SHARE_DIALOG_ID:
      return createSharingDialogBox();
    default:
      return super.onCreateDialog(id);
    }
  }

  public void onRetrieveDisplayObjects()
  {
    showDialog(SharingActivity.SHARE_DIALOG_ID);
  }

  public void onRetrieveBusinessObjects()
      throws BusinessObjectUnavailableException
  {
  }

  public void onFulfillDisplayObjects()
  {
  }

  public void onSynchronizeDisplayObjects()
  {
  }

  private Dialog createSharingDialogBox()
  {
    final Intent sendChooseIntent = new Intent(Intent.ACTION_SEND).setType("message/rfc822");
    final List<ResolveInfo> sendResolveInfos;
    {
      sendChooseIntent.putExtra(Intent.EXTRA_SUBJECT, getSubject());
      // The stylish contents is taken from http://developer.android.com/guide/topics/resources/string-resource.html
      final SpannableStringBuilder stringBuilder = computeBody();
      sendChooseIntent.putExtra(Intent.EXTRA_TEXT, stringBuilder);
      sendResolveInfos = getPackageManager().queryIntentActivities(sendChooseIntent, PackageManager.MATCH_DEFAULT_ONLY);
    }
    final List<ResolveInfo> androidMarketResolveInfos;
    {
      final Intent chooseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName()));
      androidMarketResolveInfos = getPackageManager().queryIntentActivities(chooseIntent, PackageManager.MATCH_DEFAULT_ONLY);
    }

    return new AlertDialog.Builder(this).setTitle(R.string.Login_menu_sharing).setAdapter(new BaseAdapter()
    {
      public View getView(int position, View convertView, ViewGroup parent)
      {
        final View view;

        if (convertView == null)
        {
          view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sharing_item, null);
        }
        else
        {
          view = convertView;
        }
        final ImageView icon = (ImageView) view.findViewById(R.id.icon);
        final TextView text1 = (TextView) view.findViewById(android.R.id.text1);
        final TextView text2 = (TextView) view.findViewById(android.R.id.text2);
        if (position < sendResolveInfos.size())
        {
          final ResolveInfo resolveInfo = sendResolveInfos.get(position);
          icon.setImageDrawable(resolveInfo.loadIcon(getPackageManager()));
          text1.setText(resolveInfo.loadLabel(getPackageManager()));
        }
        else if (position < sendResolveInfos.size() + androidMarketResolveInfos.size() * 2)
        {
          final int androidMarketPosition = position - sendResolveInfos.size();
          final ResolveInfo resolveInfo = androidMarketResolveInfos.get(0);
          icon.setImageDrawable(resolveInfo.loadIcon(getPackageManager()));
          text1.setText(androidMarketPosition == 0 ? getAndroidMarketRating() : getAndroidMarketOtherApplications());
        }
        else
        {
          // icon.setImageResource(R.drawable.facebook_application_icon);
          // text1.setText(R.string.dialog_facebook);
        }
        text2.setVisibility(View.GONE);
        return view;
      }

      public long getItemId(int position)
      {
        return position;
      }

      public Object getItem(int position)
      {
        return position;
      }

      public int getCount()
      {
        return sendResolveInfos.size() + androidMarketResolveInfos.size() * 2;// + 1;
      }
    }, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface dialog, int which)
      {
        if (which < sendResolveInfos.size())
        {
          final Intent intent = new Intent(sendChooseIntent);
          intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
          final ActivityInfo activityInfo = sendResolveInfos.get(which).activityInfo;
          intent.setComponent(new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name));
          startActivity(intent);
        }
        else if (which < sendResolveInfos.size() + androidMarketResolveInfos.size() * 2)
        {
          final int androidMarketWitch = which - sendResolveInfos.size();
          final Intent intent = new Intent(Intent.ACTION_VIEW);
          if (androidMarketWitch == 0)
          {
            intent.setData(Uri.parse("market://details?id=" + getPackageName()));
          }
          else
          {
            intent.setData(Uri.parse("market://search?q=pub:" + getAndroidMarketDeveloperName()));
          }
          startActivity(intent);
        }
        finish();
      }
    }).setOnCancelListener(new DialogInterface.OnCancelListener()
    {
      public void onCancel(DialogInterface dialog)
      {
        finish();
      }
    }).create();
  }

}
