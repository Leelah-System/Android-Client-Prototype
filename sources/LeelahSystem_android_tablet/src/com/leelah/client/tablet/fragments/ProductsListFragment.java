package com.leelah.client.tablet.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.leelah.client.tablet.R;
import com.leelah.client.tablet.bo.Product;
import com.leelah.client.tablet.service.LeelahServices;
import com.smartnsoft.droid4me.app.AppPublics;
import com.smartnsoft.droid4me.app.AppPublics.GuardedCommand;

public class ProductsListFragment
    extends SmartListFragment
{

  public final static class ProductAdapter
      extends ArrayAdapter<Product>
  {

    private final List<Product> products;

    private final Context context;

    public ProductAdapter(Context context, List<Product> objects)
    {
      super(context, 0, objects);
      this.products = objects;
      this.context = context;
    }

    @Override
    public int getCount()
    {
      return products.size();
    }

    @Override
    public Product getItem(int position)
    {
      if (position < products.size())
      {
        return products.get(position);
      }
      return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
      final View view = (convertView != null) ? convertView : LayoutInflater.from(context).inflate(android.R.layout.simple_expandable_list_item_2, null);

      final TextView text1 = (TextView) view.findViewById(android.R.id.text1);
      final TextView text2 = (TextView) view.findViewById(android.R.id.text2);

      text1.setText(getItem(position).product.name);
      text2.setText(getItem(position).product.description);

      return view;
    }

  }

  private List<Product> products = new ArrayList<Product>();

  @Override
  public void onListItemClick(ListView l, View v, int position, long id)
  {
    final ProductFragment productView = (ProductFragment) getFragmentManager().findFragmentById(R.id.productFragment);
    productView.updateProduct((Product) l.getAdapter().getItem(position));
  }

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    AppPublics.THREAD_POOL.execute(new GuardedCommand(getActivity())
    {
      @Override
      protected void runGuarded()
          throws Exception
      {
        products = LeelahServices.getInstance().getProducts(null);
        getActivity().runOnUiThread(new Runnable()
        {
          public void run()
          {
            setListAdapter(new ProductAdapter(getActivity(), products));
          }
        });
      }
    });
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState)
  {
    super.onViewCreated(view, savedInstanceState);
  }

  public void onRetrieveDisplayObjects()
  {

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

}
