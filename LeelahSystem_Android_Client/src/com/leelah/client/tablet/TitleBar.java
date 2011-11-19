package com.leelah.client.tablet;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.smartnsoft.droid4me.app.ActivityController;
import com.smartnsoft.droid4me.app.AppPublics;
import com.smartnsoft.droid4me.app.ProgressHandler;

/**
 * @author Jocelyn Girard
 * @since 25-01-2011
 */
public final class TitleBar
{

  static final class TitleBarAttributes
      extends ProgressHandler
  {

    private boolean enabledRefresh;

    private final ImageButton home;

    final TextView headerTitle;

    private final View separator2;

    final ImageButton action;

    private final View separator3;

    final ImageButton refresh;

    private final ProgressBar refreshProgress;

    private final View separator4;

    private final ImageButton search;

    private final FrameLayout refreshLayout;

    public TitleBarAttributes(Activity activity, View view)
    {
      home = (ImageButton) view.findViewById(R.id.home);
      // separator1 = view.findViewById(R.id.separator1);
      headerTitle = (TextView) view.findViewById(R.id.headerTitle);
      setTitle(activity.getTitle());
      separator2 = view.findViewById(R.id.separator2);
      action = (ImageButton) view.findViewById(R.id.action);
      separator3 = view.findViewById(R.id.separator3);
      refreshLayout = (FrameLayout) view.findViewById(R.id.refreshLayout);
      refresh = (ImageButton) view.findViewById(R.id.refresh);
      refreshProgress = (ProgressBar) view.findViewById(R.id.refreshProgress);
      separator4 = view.findViewById(R.id.separator4);
      search = (ImageButton) view.findViewById(R.id.search);
      // home.setOnClickListener(this);
      home.setImageResource(R.drawable.ic_title_home);
      // setShowHome(R.drawable.ic_title_home, null);
      setShowRefresh(null);
      setShowSearch(false);
      setShowAction(-1, null);
    }

    public void setTitle(CharSequence title)
    {
      headerTitle.setText(title);
    }

    private void setShowHome(int iconResourceId, View.OnClickListener onClickListener)
    {
      if (iconResourceId != -1)
      {
        home.setVisibility(View.VISIBLE);
        home.setImageResource(iconResourceId);
      }
      else
      {
        home.setImageDrawable(null);
      }
      home.setOnClickListener(onClickListener);
    }

    public void setShowAction(int iconResourceId, View.OnClickListener onClickListener)
    {
      if (iconResourceId != -1)
      {
        action.setImageResource(iconResourceId);
      }
      else
      {
        action.setImageDrawable(null);
      }
      action.setVisibility(onClickListener != null ? View.VISIBLE : View.GONE);
      separator2.setVisibility(onClickListener != null ? View.VISIBLE : View.GONE);
      action.setOnClickListener(onClickListener);
    }

    private void setShowRefresh(View.OnClickListener onClickListener)
    {
      refreshLayout.setVisibility(onClickListener != null ? View.VISIBLE : View.GONE);
      refresh.setVisibility(onClickListener != null ? View.VISIBLE : View.INVISIBLE);
      separator3.setVisibility(onClickListener != null ? View.VISIBLE : View.INVISIBLE);
      refresh.setOnClickListener(onClickListener);
      enabledRefresh = onClickListener != null;
    }

    private void setShowSearch(boolean value)
    {
      search.setVisibility(value == true ? View.VISIBLE : View.GONE);
      separator4.setVisibility(value == true ? View.VISIBLE : View.GONE);
    }

    public void onProgress(boolean isLoading)
    {
      toggleRefresh(isLoading);
    }

    public void toggleRefresh(boolean isLoading)
    {
      if (enabledRefresh == true)
      {
        refresh.setVisibility(isLoading == true ? View.INVISIBLE : View.VISIBLE);
      }
      refreshProgress.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    protected void dismiss(Activity activity, Object progressExtra)
    {
      toggleRefresh(false);
    }

    @Override
    protected void show(Activity activity, Object progressExtra)
    {
      toggleRefresh(true);
    }

  }

  static interface TitleBarDiscarded
  {
  }

  static interface TitleBarRefreshFeature
  {
    void onTitleBarRefresh();
  }

  static interface TitleBarShowHomeFeature
  {
  }

  static interface TitleBarShowSearchFeature
  {
  }

  static interface TitleBarShowHomeAndSearchFeature
      extends TitleBarShowHomeFeature, TitleBarShowSearchFeature
  {
  }

  public final static class TitleBarAggregate
      extends AppPublics.LoadingBroadcastListener
      implements View.OnClickListener
  {

    private final boolean customTitleSupported;

    private TitleBarAttributes attributes;

    private TitleBarRefreshFeature onRefresh;

    public TitleBarAggregate(Activity activity, boolean customTitleSupported)
    {
      super(activity, true);
      this.customTitleSupported = customTitleSupported;
    }

    TitleBarAttributes getAttributes()
    {
      return attributes;
    }

    private void setOnRefresh(final TitleBarRefreshFeature titleVarRefreshFeature)
    {
      this.onRefresh = titleVarRefreshFeature;
      attributes.setShowRefresh(this);
    }

    @Override
    protected void onLoading(boolean isLoading)
    {
      attributes.toggleRefresh(isLoading);
    }

    public void onClick(View view)
    {
      if (view == attributes.home)
      {
        getActivity().startActivity(new Intent(getActivity(), HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        getActivity().finish();
      }
      else if (view == attributes.refresh)
      {
        onRefresh.onTitleBarRefresh();
      }
      else if (view == attributes.search)
      {
        getActivity().onSearchRequested();
      }
    }

  }

  @SuppressWarnings("unchecked")
  public final static void onLifeCycleEvent(Activity activity, ActivityController.Interceptor.InterceptorEvent event)
  {
    // if (event == ActivityController.Interceptor.InterceptorEvent.onSuperCreateBefore && !(activity instanceof TitleBarDiscarded))
    // {
    // // set theme with Title
    // activity.setTheme(R.style.Theme_Leelah);
    //
    // if (activity.getParent() == null && activity instanceof Droid4mizer<?>)
    // {
    // boolean requestWindowFeature;
    // try
    // {
    // requestWindowFeature = activity.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
    // }
    // catch (AndroidRuntimeException exception)
    // {
    // // This means that the activity does not support custom titles
    // return;
    // }
    // // We test whether we can customize the title bar
    // final TitleBarAggregate titleBarAggregate = new TitleBarAggregate(activity, requestWindowFeature);
    // final AppPublics.CommonActivity<TitleBarAggregate> commonActivity = (AppPublics.CommonActivity<TitleBarAggregate>) activity;
    // commonActivity.setAggregate(titleBarAggregate);
    // }
    // }
    // else if (event == ActivityController.Interceptor.InterceptorEvent.onContentChanged && !(activity instanceof TitleBarDiscarded))
    // {
    // if (activity.getParent() == null && activity instanceof AppPublics.CommonActivity<?>)
    // {
    // final AppPublics.CommonActivity<TitleBarAggregate> commonActivity = (AppPublics.CommonActivity<TitleBarAggregate>) activity;
    // final TitleBarAggregate titleBarAggregate = commonActivity.getAggregate();
    // if (titleBarAggregate != null && titleBarAggregate.customTitleSupported == true && titleBarAggregate.attributes == null)
    // {
    // activity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
    // titleBarAggregate.attributes = new TitleBarAttributes(activity, activity.findViewById(R.id.titleBar));
    // if (activity instanceof TitleBarRefreshFeature)
    // {
    // titleBarAggregate.attributes.setShowRefresh(titleBarAggregate);
    // titleBarAggregate.setOnRefresh((TitleBarRefreshFeature) activity);
    // }
    // else
    // {
    // titleBarAggregate.attributes.setShowRefresh(null);
    // }
    // if (activity instanceof TitleBarShowHomeFeature)
    // {
    // titleBarAggregate.attributes.setShowHome(R.drawable.ic_title_home, titleBarAggregate);
    // }
    // if (activity instanceof TitleBarShowSearchFeature)
    // {
    // titleBarAggregate.attributes.setShowSearch(true);
    // }
    // else
    // {
    // titleBarAggregate.attributes.setShowSearch(false);
    // }
    // commonActivity.registerBroadcastListeners(new AppPublics.BroadcastListener[] { titleBarAggregate });
    // }
    // }
    // }
  }

}
