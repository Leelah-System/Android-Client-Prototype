package com.leelah.client.tablet;

import java.util.ArrayList;
import java.util.List;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.smartnsoft.droid4me.LifeCycle.BusinessObjectsRetrievalAsynchronousPolicy;
import com.smartnsoft.droid4me.app.SmartActivity;
import com.smartnsoft.droid4me.framework.Commands;
import com.smartnsoft.droid4me.menu.StaticMenuCommand;

public class HomeActivity
    extends SmartActivity<TitleBar.TitleBarAggregate>
    implements BusinessObjectsRetrievalAsynchronousPolicy, OnClickListener
{

  private Button buttonProducts;

  private Button buttonOrders;

  private Button buttonCalc;

  private Button buttonScan;

  public void onRetrieveDisplayObjects()
  {
    setContentView(R.layout.home);

    buttonProducts = (Button) findViewById(R.id.buttonProducts);
    buttonOrders = (Button) findViewById(R.id.buttonOrders);
    buttonCalc = (Button) findViewById(R.id.buttonCalc);
    buttonScan = (Button) findViewById(R.id.buttonBarcode);
  }

  public void onRetrieveBusinessObjects()
      throws BusinessObjectUnavailableException
  {

  }

  public void onFulfillDisplayObjects()
  {
    buttonProducts.setOnClickListener(this);
    buttonProducts.setOnClickListener(this);
    buttonScan.setOnClickListener(this);
    buttonCalc.setOnClickListener(this);
  }

  public void onSynchronizeDisplayObjects()
  {

  }

  private void startBarCodeScanner()
  {
    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
    intent.setPackage("com.google.zxing.client.android");
    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
    try
    {
      startActivity(intent);
    }
    catch (ActivityNotFoundException exception)
    {
      runOnUiThread(new Runnable()
      {
        public void run()
        {
          Toast.makeText(HomeActivity.this, R.string.barcode_scanner_missing, Toast.LENGTH_LONG).show();
        }
      });
    }
  }

  // Method pour le menu
  @Override
  public List<StaticMenuCommand> getMenuCommands()
  {
    final List<StaticMenuCommand> commands = new ArrayList<StaticMenuCommand>();

    commands.add(new StaticMenuCommand(R.string.Home_scan, '1', 'l', R.drawable.ic_menu_barcode, new Commands.StaticEnabledExecutable()
    {
      @Override
      public void run()
      {
        startBarCodeScanner();
      }

    }));
    commands.add(new StaticMenuCommand(R.string.Home_logout, '1', 'l', R.drawable.ic_menu_exit, new Commands.StaticEnabledExecutable()
    {
      @Override
      public void run()
      {
        getPreferences().edit().clear().commit();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
      }
    }));
    commands.add(new StaticMenuCommand(R.string.Login_menu_about, '2', 'a', android.R.drawable.ic_menu_info_details, new Commands.StaticEnabledExecutable()
    {
      @Override
      public void run()
      {
        startActivity(new Intent(getApplicationContext(), AboutActivity.class));
      }
    }));
    return commands;
  }

  public void onClick(View view)
  {
    if (view == buttonProducts)
    {
      startActivity(new Intent(this, ProductsActivity.class));
    }
    else if (view == buttonOrders)
    {

    }
    else if (view == buttonScan)
    {
      startBarCodeScanner();
    }
  }

}
