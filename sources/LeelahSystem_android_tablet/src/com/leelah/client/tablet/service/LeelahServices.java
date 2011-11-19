package com.leelah.client.tablet.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.json.JSONException;

import android.content.SharedPreferences;

import com.leelah.client.tablet.Constants;
import com.leelah.client.tablet.LoginActivity;
import com.leelah.client.tablet.bo.Product;
import com.leelah.client.tablet.bo.ProductResult;
import com.leelah.client.tablet.bo.User;
import com.leelah.client.tablet.bo.UserResult;
import com.leelah.client.tablet.bo.UsersResult;
import com.leelah.client.tablet.bo.WebServiceResult;
import com.smartnsoft.droid4me.cache.Persistence;
import com.smartnsoft.droid4me.cache.Persistence.PersistenceException;
import com.smartnsoft.droid4me.cache.Values.CacheException;
import com.smartnsoft.droid4me.ws.WSUriStreamParser;
import com.smartnsoft.droid4me.ws.WSUriStreamParser.UrlWithCallTypeAndBody;
import com.smartnsoft.droid4me.ws.WebServiceCaller;
import com.smartnsoft.droid4me.wscache.BackedWSUriStreamParser;

/**
 * @author Jocelyn Girard
 * @since 2011-03-01
 */
public final class LeelahServices
    extends WebServiceCaller
{

  private static volatile LeelahServices instance;

  private UserResult user;

  public static LeelahServices getInstance()
  {
    if (instance == null)
    {
      synchronized (LeelahServices.class)
      {
        if (instance == null)
        {
          instance = new LeelahServices();
        }
      }
    }
    return instance;
  }

  private LeelahServices()
  {
  }

  @Override
  protected String getUrlEncoding()
  {
    return Constants.WEBSERVICES_HTML_ENCODING;
  }

  private String serializeObjectToJson(Object object)
  {
    try
    {
      final ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.configure(Feature.WRITE_NULL_MAP_VALUES, false);
      objectMapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
      final String json = objectMapper.writeValueAsString(object);
      if (log.isDebugEnabled())
      {
        log.debug("Converting Object ( " + object.getClass().getSimpleName() + " )to json String: \"" + json + "\"");
      }
      return json;
    }
    catch (JsonGenerationException exception)
    {
      if (log.isErrorEnabled())
      {
        log.error("Error while generating json !", exception);
      }
    }
    catch (JsonMappingException jsonMappingException)
    {
      if (log.isErrorEnabled())
      {
        log.error("Error while mapping json !", jsonMappingException);
      }
    }
    catch (IOException ioException)
    {
      if (log.isErrorEnabled())
      {
        log.error("Input/Output error while reading InputStream !", ioException);
      }
    }
    return null;
  }

  private Object deserializeJson(InputStream inputStream, Class<?> valueType)
  {
    final ObjectMapper objectMapper = new ObjectMapper();
    try
    {
      final String json = getString(inputStream);
      if (log.isDebugEnabled())
      {
        log.debug("Recieving json: " + json);
      }
      if (json.startsWith("{"))
      {
        return objectMapper.readValue(json, valueType);
      }
    }
    catch (JsonParseException jsonParseException)
    {
      if (log.isErrorEnabled())
      {
        log.error("Error while parsing json !", jsonParseException);
      }
    }
    catch (JsonMappingException jsonMappingException)
    {
      if (log.isErrorEnabled())
      {
        log.error("Error while mapping json !", jsonMappingException);
      }
    }
    catch (IOException ioException)
    {
      if (log.isErrorEnabled())
      {
        log.error("Input/Output error while reading InputStream !", ioException);
      }
    }
    return null;
  }

  public boolean hasParameters(SharedPreferences preferences)
  {
    return preferences.contains(LoginActivity.SERVER_ADDRESS) && preferences.contains(LoginActivity.USER_LOGIN) && preferences.contains(LoginActivity.USER_PASSWORD);
  }

  private final WSUriStreamParser<List<Product>, String, JSONException> getProductsStreamParser = new WSUriStreamParser<List<Product>, String, JSONException>(this)
  {

    public UrlWithCallTypeAndBody computeUri(String parameter)
    {
      return new UrlWithCallTypeAndBody(computeUri("http://" + user.result.user.address, "api/" + user.result.user.token + "/product", null));
    }

    public List<Product> parse(String parameter, InputStream inputStream)
        throws JSONException
    {
      final ProductResult products = (ProductResult) deserializeJson(inputStream, ProductResult.class);
      return products.result.products;
    }

  };

  public List<Product> getProducts(String address)
      throws CacheException, CallException
  {
    return getProductsStreamParser.getValue(address);
  }

  private final WSUriStreamParser<List<User>, String, JSONException> getUsersStreamParser = new WSUriStreamParser<List<User>, String, JSONException>(this)
  {

    public UrlWithCallTypeAndBody computeUri(String parameter)
    {
      return new UrlWithCallTypeAndBody(computeUri("http://" + user.result.user.address, "api/" + user.result.user.token + "/users", null));
    }

    public List<User> parse(String parameter, InputStream inputStream)
        throws JSONException
    {
      final UsersResult usersResult = (UsersResult) deserializeJson(inputStream, UsersResult.class);
      return usersResult.result.users;
    }

  };

  public List<User> getUsers(String address)
      throws CacheException, CallException
  {
    return getUsersStreamParser.getValue(address);
  }

  private final WSUriStreamParser<Boolean, String, JSONException> deleteProductStreamParser = new WSUriStreamParser<Boolean, String, JSONException>(this)
  {

    public UrlWithCallTypeAndBody computeUri(String parameter)
    {
      return new UrlWithCallTypeAndBody(computeUri("http://" + user.result.user.address, "api/" + user.result.user.token + "/product/" + parameter, null));
    }

    public Boolean parse(String parameter, InputStream inputStream)
        throws JSONException
    {
      final WebServiceResult products = (WebServiceResult) deserializeJson(inputStream, WebServiceResult.class);
      return products.success;
    }

  };

  public Boolean deleteProduct(String id)
      throws CacheException, CallException
  {
    return deleteProductStreamParser.getValue(id);
  }

  private final WSUriStreamParser<Boolean, String, JSONException> deleteUserStreamParser = new WSUriStreamParser<Boolean, String, JSONException>(this)
  {

    public UrlWithCallTypeAndBody computeUri(String parameter)
    {
      return new UrlWithCallTypeAndBody(computeUri("http://" + user.result.user.address, "api/" + user.result.user.token + "/user/" + parameter, null));
    }

    public Boolean parse(String parameter, InputStream inputStream)
        throws JSONException
    {
      final WebServiceResult products = (WebServiceResult) deserializeJson(inputStream, WebServiceResult.class);
      return products.success;
    }

  };

  public Boolean deleteUser(String id)
      throws CacheException, CallException
  {
    return deleteUserStreamParser.getValue(id);
  }

  private final BackedWSUriStreamParser.BackedUriStreamedMap<UserResult, User, JSONException, PersistenceException> authenticateStreamParser = new BackedWSUriStreamParser.BackedUriStreamedMap<UserResult, User, JSONException, PersistenceException>(Persistence.getInstance(0), this)
  {

    public UrlWithCallTypeAndBody computeUri(User parameter)
    {
      final List<NameValuePair> postParams = new ArrayList<NameValuePair>();
      postParams.add(new BasicNameValuePair("json", serializeObjectToJson(parameter)));
      postParams.add(new BasicNameValuePair("login", parameter.login));
      postParams.add(new BasicNameValuePair("password", parameter.password));

      UrlEncodedFormEntity entity = null;
      try
      {
        entity = new UrlEncodedFormEntity(postParams);
      }
      catch (UnsupportedEncodingException exception)
      {
        if (log.isErrorEnabled())
        {
          log.error("Problems when initializing Entity with post parameters", exception);
        }
      }

      return new UrlWithCallTypeAndBody(computeUri("http://" + parameter.address, "api/authenticate", null), CallType.Post, entity);
    }

    public UserResult parse(User parameter, InputStream inputStream)
        throws JSONException
    {
      final UserResult userResult = (UserResult) deserializeJson(inputStream, UserResult.class);
      return userResult;
    }

  };

  public UserResult authenticate(User u)
      throws CacheException
  {
    user = authenticateStreamParser.backed.getValue(false, null, u);
    user.result.user.address = u.address;
    return user;
  }

}
