package com.leelah.client.tablet;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.view.MenuItem;

import com.leelah.client.tablet.TitleBar.TitleBarAggregate;
import com.smartnsoft.droid4me.app.SmartActivity;

public class LeelahSystemActivity
    extends SmartActivity<TitleBarAggregate>
    implements TabListener
{

  public void onRetrieveDisplayObjects()
  {
    getActionBar().setDisplayHomeAsUpEnabled(true);
    getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    getActionBar().addTab(getActionBar().newTab().setText(R.string.Home_button_catalog).setTabListener(this));
    getActionBar().addTab(getActionBar().newTab().setText(R.string.Home_button_products).setTabListener(this));
    getActionBar().addTab(getActionBar().newTab().setText(R.string.Home_button_orders).setTabListener(this));
    getActionBar().addTab(getActionBar().newTab().setText(R.string.Home_button_users).setTabListener(this));
    getActionBar().addTab(getActionBar().newTab().setText(R.string.Home_button_stats).setTabListener(this));
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

  public void onTabReselected(Tab tab, FragmentTransaction ft)
  {

  }

  public void onTabSelected(Tab tab, FragmentTransaction ft)
  {

  }

  public void onTabUnselected(Tab tab, FragmentTransaction ft)
  {

  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    if (item.getItemId() == android.R.id.home)
    {
      finish();
    }
    return super.onOptionsItemSelected(item);
  }

}
