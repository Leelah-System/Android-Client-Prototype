package com.leelah.client.tablet;

import java.util.ArrayList;
import java.util.List;

import com.smartnsoft.droid4me.framework.Commands;
import com.smartnsoft.droid4me.menu.StaticMenuCommand;

public class ProductsActivity
    extends LeelahSystemActivity
{

  @Override
  public void onRetrieveDisplayObjects()
  {
    setContentView(R.layout.products);
    super.onRetrieveDisplayObjects();
  }

  @Override
  public void onRetrieveBusinessObjects()
      throws BusinessObjectUnavailableException
  {

  }

  @Override
  public void onFulfillDisplayObjects()
  {

  }

  @Override
  public void onSynchronizeDisplayObjects()
  {

  }

  @Override
  public List<StaticMenuCommand> getMenuCommands()
  {
    final List<StaticMenuCommand> commands = new ArrayList<StaticMenuCommand>();

    commands.add(new StaticMenuCommand(R.string.Action_add_basket, '1', 'l', R.drawable.ic_menu_barcode, new Commands.StaticEnabledExecutable()
    {
      @Override
      public void run()
      {

      }
    }));
    commands.add(new StaticMenuCommand(R.string.Action_modify, '1', 'l', R.drawable.ic_menu_exit, new Commands.StaticEnabledExecutable()
    {
      @Override
      public void run()
      {

      }
    }));
    commands.add(new StaticMenuCommand(R.string.Action_delete, '2', 'a', android.R.drawable.ic_menu_info_details, new Commands.StaticEnabledExecutable()
    {
      @Override
      public void run()
      {

      }
    }));
    return commands;
  }

}
