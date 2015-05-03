//***************************************************************************************************
//***************************************************************************************************
//      Project name                    		: Alesco Happy
//      Class Name                              : UserServices
//      Author                                  : PurpleTalk, Inc.
//***************************************************************************************************
//      Class Description: Defines API calls relating to User data.
//***************************************************************************************************
//***************************************************************************************************

package com.uconnect.events.network.services;

import android.content.Context;

import com.alesco.suggestionsapp.AppConstants;
import com.alesco.suggestionsapp.model.pojo.UserInfo;
import com.alesco.suggestionsapp.network.CloudAdapter;
import com.alesco.suggestionsapp.network.CloudAdapter.JSONCallback;
import com.alesco.suggestionsapp.network.CloudConstants;
import com.alesco.suggestionsapp.utils.AppPreferences;
import com.alesco.suggestionsapp.utils.LogUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
* Defines API calls relating to User data.
* */
public class UCUserServices extends BaseService implements AppConstants, CloudConstants {

    private static String userId;
    private final Context mContext;
    private final ArrayList<String> listOfCountries = new ArrayList<>();
    private UserInfo mLoggedInUser;

    public UCUserServices(Context context) {
        super(context);
        mContext = context;
    }

    /**
    * To get the Logged in User object.
    * */
    public UserInfo getLoggedInUser() {
        return mLoggedInUser;
    }

    /**
    * To get the list of countries.
    * */
    public ArrayList<String> getListOfCountries() {
        return listOfCountries;
    }

    /**
    * Calling the login API.
    * */
    public void login(String mailId, String authentication, int type, final DataUpdateCallback callback) {
        List<NameValuePair> nameValuePairList = new ArrayList<>();

        nameValuePairList.add(new BasicNameValuePair("email", mailId));
        nameValuePairList.add(new BasicNameValuePair("authentication", authentication));
        nameValuePairList.add(new BasicNameValuePair("snType", String.valueOf(type)));

        mCloudAdapter.postData(Api.API_LOGIN, nameValuePairList, false, new JSONCallback() {
            @Override
            public void getJsonData(byte status, JSONObject data, String statusMsg) {
                if (data != null) {
                    parseLogin(data, callback, statusMsg);
                } else {
                    if (callback != null) {
                        callback.dataLoaded(status, statusMsg, true, null);
                    }
                }
            }
        });
    }

    /**
    * Parsing the Login API result.
    * */
    private void parseLogin(JSONObject data, DataUpdateCallback callback, String statusMsg) {
        if (mCloudAdapter.hasServiceError(data, callback)) {
            return;
        }
        try {
            JSONObject obj = data.getJSONObject("data");
            mLoggedInUser = new UserInfo();
            mLoggedInUser.setUserId(obj.getString("userId"));
            userId = obj.getString("userId");
            mLoggedInUser.setUserAccessToken(obj.getString("accessToken"));
            mLoggedInUser.setLoginRateIt(Boolean.valueOf(obj.getString("rateIt")));

            saveOAuthToken();
            saveUserData(obj);

            if (callback != null) {
                callback.dataLoaded(NetworkStatusConstants.HTTP_SUCCESS, statusMsg, true, null);
            }
        } catch (Exception e) {
            if (callback != null) {
                callback.dataLoaded(NetworkStatusConstants.HTTP_FAILURE, "Exception Occurred", true, null);
            }
            e.printStackTrace();
        }
    }

    /**
    * Checking for the existence of the given social id in the database.
    * */
    public void evaluateFacebookSocialId(String facebookId, final DataUpdateCallback callback) {
        List<NameValuePair> nameValuePairList = new ArrayList<>();

        nameValuePairList.add(new BasicNameValuePair("socialNetwork", facebookId));
        nameValuePairList.add(new BasicNameValuePair("snType", String.valueOf(AppConstants.LOGIN_TYPE_FACEBOOK)));

        mCloudAdapter.postData(Api.API_EVALUATE_FACEBOOK_ID, nameValuePairList, false, new JSONCallback() {
            @Override
            public void getJsonData(byte status, JSONObject data, String statusMsg) {
                if (data != null) {
                    parseEvaluateFacebookSocialId(data, callback);
                } else {
                    if (callback != null) {
                        callback.dataLoaded(status, statusMsg, true, null);
                    }
                }
            }
        });
    }

    /**
    * Parsing the existence of the social id API response.
    * */
    private void parseEvaluateFacebookSocialId(JSONObject data, DataUpdateCallback callback) {
        if (mCloudAdapter.hasServiceError(data, callback)) {
            return;
        }
        try {
            String status;
            JSONObject objData = data.getJSONObject("data");
            if (objData.getBoolean("status")) {
                status = "SUCCESS";
            } else {
                status = "FAILURE";
            }

            if (callback != null) {
                callback.dataLoaded(NetworkStatusConstants.HTTP_SUCCESS, status, true, null);
            }
        } catch (Exception e) {
            if (callback != null) {
                callback.dataLoaded(NetworkStatusConstants.HTTP_FAILURE, "Exception Occurred", true, null);
            }
            e.printStackTrace();
        }
    }

    /**
    * Calling the forget password API.
    * */
    public void postForgotPassword(String emailId, final DataUpdateCallback callback) {
        List<NameValuePair> nameValuePairList = new ArrayList<>();

        nameValuePairList.add(new BasicNameValuePair("email", emailId));

        mCloudAdapter.postData(Api.API_FORGOT_PASSWORD, nameValuePairList, false, new JSONCallback() {
            @Override
            public void getJsonData(byte status, JSONObject data, String statusMsg) {
                if (data != null) {
                    parseForgotPassword(data, callback, statusMsg);
                } else {
                    if (callback != null) {
                        callback.dataLoaded(status, statusMsg, true, null);
                    }
                }
            }
        });
    }

    /**
    * Parsing the forget password API result.
    * */
    private void parseForgotPassword(JSONObject data, DataUpdateCallback callback, String statusMsg) {
        if (mCloudAdapter.hasServiceError(data, callback)) {
            return;
        }

        if (callback != null) {
            callback.dataLoaded(NetworkStatusConstants.HTTP_SUCCESS, statusMsg, true, null);
        }
    }

    /**
    * Calling Signup API
    * */
    public void signUp(UserInfo user, int snType, final DataUpdateCallback callback) {
        List<NameValuePair> nameValuePairList = new ArrayList<>();

        nameValuePairList.add(new BasicNameValuePair("email", user.getMailId()));
        nameValuePairList.add(new BasicNameValuePair("authentication", user.getPassword()));
        nameValuePairList.add(new BasicNameValuePair("fullName", user.getName()));
        nameValuePairList.add(new BasicNameValuePair("gender", user.getGender()));
        nameValuePairList.add(new BasicNameValuePair("dateOfBirth", user.getDob()));
        nameValuePairList.add(new BasicNameValuePair("snType", String.valueOf(snType)));


        if (user.getUserCountry() != null) {
            nameValuePairList.add(new BasicNameValuePair("country", user.getUserCountry()));
        } else {
            nameValuePairList.add(new BasicNameValuePair("country", "USA"));
        }

        String url = CloudConstants.Api.API_SIGN_UP + "";

        mCloudAdapter.postData(url, nameValuePairList, false, new JSONCallback() {
            @Override
            public void getJsonData(byte status, JSONObject data, String statusMsg) {
                if (data != null) {
                    parseSignUp(data, callback, statusMsg);
                } else {
                    if (callback != null) {
                        callback.dataLoaded(status, statusMsg, true, null);
                    }
                }
            }
        });
    }

    /**
    * Parsing Signup API response
    * */
    private void parseSignUp(JSONObject data, DataUpdateCallback callback, String statusMsg) {
        try {
            if (mCloudAdapter.hasServiceError(data, callback)) {
                return;
            }

            JSONObject obj = data.getJSONObject("data");
            mLoggedInUser = new UserInfo();
            mLoggedInUser.setUserId(obj.getString("userId"));
            mLoggedInUser.setUserAccessToken(obj.getString("accessToken"));
            mLoggedInUser.setLoginRateIt(obj.getBoolean("rateIt"));

            saveOAuthToken();
            saveUserData(obj);

            if (callback != null) {
                callback.dataLoaded(NetworkStatusConstants.HTTP_SUCCESS, statusMsg, true, null);
            }
        } catch (Exception e) {
            if (callback != null) {
                callback.dataLoaded(NetworkStatusConstants.HTTP_FAILURE, "Exception Occurred", true, null);
            }
            e.printStackTrace();
        }
    }

    /**
    * Fetching the User profile details.
    * */
    public void getUserProfile(final DataUpdateCallback callback) {
        List<NameValuePair> nameValuePairList = new ArrayList<>();
        nameValuePairList.add(new BasicNameValuePair("userId", CloudAdapter.OAUTH_TOKEN.substring(0, CloudAdapter.OAUTH_TOKEN.indexOf(':'))));
        String url = Api.API_GET_USER_PROFILE;
        mCloudAdapter.getData(url, nameValuePairList, true, new JSONCallback() {
            @Override
            public void getJsonData(byte status, JSONObject data, String statusMsg) {
                if (data != null) {
                    parseUserProfile(data, callback, statusMsg);
                } else {
                    if (callback != null) {
                        callback.dataLoaded(status, statusMsg, true, null);
                    }
                }
            }
        });
    }

    /**
    * Parsing the User Profile details.
    * */
    private void parseUserProfile(JSONObject data, DataUpdateCallback callback, String statusMsg) {
        try {
            if (mCloudAdapter.hasServiceError(data, callback)) {
                return;
            }

            JSONObject obj = data.getJSONObject("data");
            mLoggedInUser = new UserInfo();
//            mLoggedInUser.setPhoneNo(obj.getString("phone"));
            mLoggedInUser.setMailId(obj.getString("email"));
            mLoggedInUser.setUserId(obj.getString("userId"));
            mLoggedInUser.setDob(obj.getString("dob"));
            mLoggedInUser.setGender(obj.getString("gender"));
            mLoggedInUser.setName(obj.getString("fullName"));
            mLoggedInUser.setUserCountry(obj.getString("country"));

            if (callback != null) {
                callback.dataLoaded(NetworkStatusConstants.HTTP_SUCCESS, statusMsg, true, null);
            }
        } catch (Exception e) {
            if (callback != null) {
                callback.dataLoaded(NetworkStatusConstants.HTTP_FAILURE, "Exception Occurred", true, null);
            }
            e.printStackTrace();
        }
    }

    /**
    * Updating the User Profile Details.
    * */
    public void updateUserProfile(UserInfo user, int type, final DataUpdateCallback callback) {
        List<NameValuePair> nameValuePairList = new ArrayList<>();

        nameValuePairList.add(new BasicNameValuePair("userId", CloudAdapter.OAUTH_TOKEN.substring(0, CloudAdapter.OAUTH_TOKEN.indexOf(':'))));
        nameValuePairList.add(new BasicNameValuePair("email", user.getMailId()));
        //if (type == LOGIN_TYPE_NORMAL)
        //nameValuePairList.add(new BasicNameValuePair("authentication", user.getPassword()));
        nameValuePairList.add(new BasicNameValuePair("fullName", user.getName()));
        nameValuePairList.add(new BasicNameValuePair("gender", user.getGender()));
        nameValuePairList.add(new BasicNameValuePair("dateOfBirth", user.getDob()));
        if (user.getUserCountry() != null) {
            nameValuePairList.add(new BasicNameValuePair("country", user.getUserCountry()));
        }
        if (type == LOGIN_TYPE_FACEBOOK)
            nameValuePairList.add(new BasicNameValuePair("faceBookId", user.getFacebookId()));

        String url = Api.API_UPDATE_PROFILE;

        mCloudAdapter.postData(url, nameValuePairList, true, new JSONCallback() {
            @Override
            public void getJsonData(byte status, JSONObject data, String statusMsg) {
                if (data != null) {
                    parseUpdateUserProfile(data, callback, statusMsg);
                } else {
                    if (callback != null) {
                        callback.dataLoaded(status, statusMsg, true, null);
                    }
                }
            }
        });
    }

    /**
    * Parsing the updateProfileDetails response.
    * */
    private void parseUpdateUserProfile(JSONObject data, DataUpdateCallback callback, String statusMsg) {
        try {
            if (mCloudAdapter.hasServiceError(data, callback)) {
                return;
            }

            JSONObject obj = data.getJSONObject("data");
            saveUserData(obj);

            if (callback != null) {
                callback.dataLoaded(NetworkStatusConstants.HTTP_SUCCESS, statusMsg, true, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    * Calling the Change password API.
    * */
    public void changeUserPassword(String password, final DataUpdateCallback callback) {
        List<NameValuePair> nameValuePairList = new ArrayList<>();

        nameValuePairList.add(new BasicNameValuePair("userId", CloudAdapter.OAUTH_TOKEN.substring(0, CloudAdapter.OAUTH_TOKEN.indexOf(':'))));
        nameValuePairList.add(new BasicNameValuePair("authentication", password));
        String url = Api.API_CHANGE_PASSWORD;

        mCloudAdapter.postData(url, nameValuePairList, true, new JSONCallback() {
            @Override
            public void getJsonData(byte status, JSONObject data, String statusMsg) {
                if (data != null) {
                    parseChangePassword(data, callback, statusMsg);
                } else {
                    if (callback != null) {
                        callback.dataLoaded(status, statusMsg, true, null);
                    }
                }
            }
        });
    }

    /**
    * Calling the list of countries API.
    * */
    public void getListOfCountriesFromServer(final DataUpdateCallback callback) {
        List<NameValuePair> nameValuePairList = new ArrayList<>();

        String url = CloudConstants.Api.API_GET_COUNTRIES;

        mCloudAdapter.postData(url, nameValuePairList, false, new JSONCallback() {
            @Override
            public void getJsonData(byte status, JSONObject data, String statusMsg) {
                if (data != null) {
                    parseListOfCountries(data, callback);
                } else {
                    if (callback != null) {
                        callback.dataLoaded(status, statusMsg, true, null);
                    }
                }
            }
        });
    }

    /**
    * Parsing the list of countries API result.
    * */
    private void parseListOfCountries(JSONObject data, DataUpdateCallback callback) {
        if (mCloudAdapter.hasServiceError(data, callback)) {
            return;
        }

        try {
            JSONObject dataObj = data.getJSONObject("data");
            JSONArray countriesList = dataObj.getJSONArray("countries");
            for (int i = 0; i < countriesList.length(); i++) {
                JSONObject country = countriesList.getJSONObject(i);
                listOfCountries.add(country.getString("name"));
            }

            if (callback != null) {
                callback.dataLoaded(NetworkStatusConstants.HTTP_SUCCESS, "", true, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
    * Parsing the updateProfileDetails API response.
    * */
    private void parseChangePassword(JSONObject data, DataUpdateCallback callback, String statusMsg) {
        if (mCloudAdapter.hasServiceError(data, callback)) {
            return;
        }

        if (callback != null) {
            callback.dataLoaded(NetworkStatusConstants.HTTP_SUCCESS, statusMsg, true, null);
        }
    }

    /**
    * Sending the list of installed applications to the server.
    * */
    public void postUSerInstalledApplications(String installedApplications, final DataUpdateCallback callback) {
        List<NameValuePair> nameValuePairList = new ArrayList<>();

        nameValuePairList.add(new BasicNameValuePair("userId", CloudAdapter.OAUTH_TOKEN.substring(0, CloudAdapter.OAUTH_TOKEN.indexOf(':'))));
        nameValuePairList.add(new BasicNameValuePair("type", "Android"));
        nameValuePairList.add(new BasicNameValuePair("bundleId", installedApplications));

        String url = Api.API_INSTALLED_APPS;

        mCloudAdapter.postData(url, nameValuePairList, false, new JSONCallback() {
            @Override
            public void getJsonData(byte status, JSONObject data, String statusMsg) {
                if (data != null) {
                    parseUserInstalledApplications(data, callback, statusMsg);
                } else {
                    if (callback != null) {
                        callback.dataLoaded(status, statusMsg, true, null);
                    }
                }
            }
        });
    }

    /**
    * Parsing the updateProfileDetails response.
    * */
    private void parseUserInstalledApplications(JSONObject data, DataUpdateCallback callback, String statusMsg) {
        if (mCloudAdapter.hasServiceError(data, callback)) {
            return;
        }

        if (callback != null) {
            callback.dataLoaded(NetworkStatusConstants.HTTP_SUCCESS, statusMsg, true, null);
        }
    }

    /**
    * Saving the Authentication Token.
    * */
    private void saveOAuthToken() {
        CloudAdapter.OAUTH_TOKEN = mLoggedInUser.getUserId() + ":" + mLoggedInUser.getUserAccessToken();

        AppPreferences appPrefs = AppPreferences.getInstance(mContext);
        appPrefs.savePreference(SharedPreferences.LOGGED_IN_USER_ID, mLoggedInUser.getUserId());
        appPrefs.savePreference(SharedPreferences.OAUTH_ACCESS_TOKEN, mLoggedInUser.getUserAccessToken());
    }

    private void saveUserData(JSONObject obj) {
        /*mLoggedInUser.setName(obj.getString("fullName"));
        mLoggedInUser.setName(obj.getString("gender"));
        mLoggedInUser.setName(obj.getString("email"));
        mLoggedInUser.setName(obj.getString("dob"));
        mLoggedInUser.setName(obj.getString("country"));*/
        try {
            AppPreferences appPrefs = AppPreferences.getInstance(mContext);
            appPrefs.savePreference(SharedPreferences.FULL_NAME, obj.getString("fullName"));
            appPrefs.savePreference(SharedPreferences.GENDER, obj.getString("gender"));
            appPrefs.savePreference(SharedPreferences.EMAIL, obj.getString("email"));
            appPrefs.savePreference(SharedPreferences.DOB, obj.getString("dob"));
            appPrefs.savePreference(SharedPreferences.COUNTRY, obj.getString("country"));
            LogUtil.verbose("Alesco User Details saved successfully...");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UserInfo getUserData() {
        UserInfo user = new UserInfo();
        AppPreferences appPrefs = AppPreferences.getInstance(mContext);
        user.setName(appPrefs.getPreference(SharedPreferences.FULL_NAME, ""));
        user.setGender(appPrefs.getPreference(SharedPreferences.GENDER, ""));
        user.setMailId(appPrefs.getPreference(SharedPreferences.EMAIL, ""));
        user.setDob(appPrefs.getPreference(SharedPreferences.DOB, ""));
        user.setUserCountry(appPrefs.getPreference(SharedPreferences.COUNTRY, ""));

        return user;
    }
}
