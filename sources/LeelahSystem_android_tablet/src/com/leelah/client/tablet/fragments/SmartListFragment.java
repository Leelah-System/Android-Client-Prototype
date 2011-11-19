package com.leelah.client.tablet.fragments;

import java.util.List;

import android.app.ListFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.ListView;

import com.leelah.client.tablet.TitleBar.TitleBarAggregate;
import com.smartnsoft.droid4me.app.AppPublics.BroadcastListener;
import com.smartnsoft.droid4me.app.Droid4mizer;
import com.smartnsoft.droid4me.app.Droid4mizerInterface;
import com.smartnsoft.droid4me.app.SmartableActivity;
import com.smartnsoft.droid4me.framework.ActivityResultHandler.CompositeHandler;
import com.smartnsoft.droid4me.menu.MenuHandler.Composite;
import com.smartnsoft.droid4me.menu.StaticMenuCommand;

public abstract class SmartListFragment
    extends ListFragment
    implements Droid4mizerInterface, SmartableActivity<TitleBarAggregate>
{

  private final Droid4mizer<TitleBarAggregate> droid4mizer = new Droid4mizer<TitleBarAggregate>(getActivity(), this, this);

  @Override
  public void onCreate(final Bundle savedInstanceState)
  {
    droid4mizer.onCreate(new Runnable()
    {
      public void run()
      {
        SmartListFragment.super.onCreate(savedInstanceState);
      }
    }, savedInstanceState);
  }

  @Override
  public void onResume()
  {
    super.onResume();
    droid4mizer.onResume();
  }

  @Override
  public void onSaveInstanceState(Bundle outState)
  {
    super.onSaveInstanceState(outState);
    droid4mizer.onSaveInstanceState(outState);
  }

  @Override
  public void onStart()
  {
    super.onStart();
    droid4mizer.onStart();
  }

  @Override
  public void onPause()
  {
    try
    {
      droid4mizer.onPause();
    }
    finally
    {
      super.onPause();
    }
  }

  @Override
  public void onStop()
  {
    try
    {
      droid4mizer.onStop();
    }
    finally
    {
      super.onStop();
    }
  }

  @Override
  public void onDestroy()
  {
    try
    {
      droid4mizer.onDestroy();
    }
    finally
    {
      super.onPause();
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    return droid4mizer.onOptionsItemSelected(super.onOptionsItemSelected(item), item);
  }

  @Override
  public boolean onContextItemSelected(MenuItem item)
  {
    return droid4mizer.onContextItemSelected(super.onContextItemSelected(item), item);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    super.onActivityResult(requestCode, resultCode, data);
    droid4mizer.onActivityResult(requestCode, resultCode, data);
  }

  /**
   * SmartableActivity implementation.
   */

  public TitleBarAggregate getAggregate()
  {
    return droid4mizer.getAggregate();
  }

  public void setAggregate(TitleBarAggregate aggregate)
  {
    droid4mizer.setAggregate(aggregate);
  }

  public Handler getHandler()
  {
    return droid4mizer.getHandler();
  }

  public void onException(Throwable throwable, boolean fromGuiThread)
  {
    droid4mizer.onException(throwable, fromGuiThread);
  }

  public void registerBroadcastListeners(BroadcastListener[] broadcastListeners)
  {
    droid4mizer.registerBroadcastListeners(broadcastListeners);
  }

  public void onBusinessObjectsRetrieved()
  {
    droid4mizer.onBusinessObjectsRetrieved();
  }

  public int getOnSynchronizeDisplayObjectsCount()
  {
    return droid4mizer.getOnSynchronizeDisplayObjectsCount();
  }

  public boolean isRefreshingBusinessObjectsAndDisplay()
  {
    return droid4mizer.isRefreshingBusinessObjectsAndDisplay();
  }

  public boolean isFirstLifeCycle()
  {
    return droid4mizer.isFirstLifeCycle();
  }

  public boolean shouldKeepOn()
  {
    return droid4mizer.shouldKeepOn();
  }

  public void refreshBusinessObjectsAndDisplay(boolean retrieveBusinessObjects, Runnable onOver, boolean immediatly)
  {
    droid4mizer.refreshBusinessObjectsAndDisplay(retrieveBusinessObjects, onOver, immediatly);
  }

  /**
   * Droid4mizeInterface implementation.
   */

  public void onBeforeRetrievingDisplayObjects()
  {
    droid4mizer.onBeforeRetrievingDisplayObjects();
  }

  public Composite getCompositeActionHandler()
  {
    return droid4mizer.getCompositeActionHandler();
  }

  public CompositeHandler getCompositeActivityResultHandler()
  {
    return droid4mizer.getCompositeActivityResultHandler();
  }

  @Override
  public ListView getListView()
  {
    // TODO Auto-generated method stub
    return super.getListView();
  }

  // --------------

  public SharedPreferences getPreferences()
  {
    return null;
    // return droid4mizer.getPreferences();
  }

  public void onActuallyCreated()
  {

  }

  public void onActuallyDestroyed()
  {

  }

  public List<StaticMenuCommand> getMenuCommands()
  {
    return null;
  }

}
