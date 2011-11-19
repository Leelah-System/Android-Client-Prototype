package com.leelah.client.tablet.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leelah.client.tablet.R;
import com.leelah.client.tablet.bo.Product;

public class ProductFragment
    extends SmartFragment
{

  public static final String PRODUCT = "product";

  private TextView productName;

  private TextView productDescription;

  private TextView productDispo;

  private TextView productPrice;

  private TextView productReference;

  private Product product;

  private View productView;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    productView = inflater.inflate(R.layout.product, null);
    productName = (TextView) productView.findViewById(R.id.productName);
    productDispo = (TextView) productView.findViewById(R.id.productDispo);
    productPrice = (TextView) productView.findViewById(R.id.productPrice);
    productReference = (TextView) productView.findViewById(R.id.productReference);
    productDescription = (TextView) productView.findViewById(R.id.productDescription);

    return productView;
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
    if (product != null)
    {
      productName.setText(product.product.name);
      productDescription.setText(product.product.description);
      productPrice.setText(product.product.price + " €");
      productReference.setText("ref. " + product.product.reference);
      productView.setVisibility(View.VISIBLE);
    }
    else
    {
      productView.setVisibility(View.INVISIBLE);
    }

  }

  public void updateProduct(Product item)
  {
    product = item;
    refreshBusinessObjectsAndDisplay(true, null, true);
  }
}
