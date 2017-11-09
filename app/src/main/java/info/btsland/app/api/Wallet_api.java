package info.btsland.app.api;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import info.btsland.app.exception.CreateAccountException;
import info.btsland.app.exception.NetworkStatusException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/11/9.
 */

public class Wallet_api {

    private Websocket_api mWebsocketApi;

    public Wallet_api() {
        mWebsocketApi = new Websocket_api();
    }


    public int create_account_with_password(String strAccountName,
                                            String strPassword) throws NetworkStatusException, CreateAccountException {
        /*String[] strAddress = {
                "https://bitshares.openledger.info/api/v1/accounts",
                "https://openledger.io/api/v1/accounts",
                "https://openledger.hk/api/v1/accounts"
        };*/
        String[] strAddress = {"https://openledger.io/api/v1/accounts"};

        int nRet = -1;
        for (int i = 0; i < strAddress.length; ++i) {
            try {
                nRet = create_account_with_password(
                        strAddress[i],
                        strAccountName,
                        strPassword
                );
                if (nRet == 0) {
                    break;
                }
            } catch (NetworkStatusException e) {
                e.printStackTrace();
                if (i == strAddress.length - 1) {
                    throw e;
                }
            } catch (CreateAccountException e) {
                e.printStackTrace();
                if (i == strAddress.length - 1) {
                    throw e;
                }
            }
        }
        return nRet;
    }
    public int create_account_with_password(String strServerUrl,
                                            String strAccountName,
                                            String strPassword) throws NetworkStatusException, CreateAccountException {
        private_key privateActiveKey = private_key.from_seed(strAccountName + "active" + strPassword);
        private_key privateOwnerKey = private_key.from_seed(strAccountName + "owner" + strPassword);

        types.public_key_type publicActiveKeyType = new types.public_key_type(privateActiveKey.get_public_key());
        types.public_key_type publicOwnerKeyType = new types.public_key_type(privateOwnerKey.get_public_key());
        List<String> names=new ArrayList<>();
        names.add(strAccountName);

        account_object accountObject = mWebsocketApi.get_account_by_name(names);
        if (accountObject != null) {
            return ErrorCode.ERROR_ACCOUNT_OBJECT_EXIST;
        }

        create_account_object createAccountObject = new create_account_object();
        createAccountObject.name = strAccountName;
        createAccountObject.active_key = publicActiveKeyType;
        createAccountObject.owner_key = publicOwnerKeyType;
        createAccountObject.memo_key = publicActiveKeyType;
        createAccountObject.refcode = null;
        createAccountObject.referrer = "bituniverse";
        Gson gson = new Gson();

        String strAddress = strServerUrl;
        OkHttpClient okHttpClient = new OkHttpClient();

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json"),
                gson.toJson(createAccountObject)
        );

        Request request = new Request.Builder()
                .url(strAddress)
                .addHeader("Accept", "application/json")
                .post(requestBody)
                .build();

        create_account_object.create_account_response createAccountResponse = null;
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                createAccountResponse = gson.fromJson(response.body().string(), create_account_object.create_account_response.class);
            } else {
                if (response.body().contentLength() != 0) {
                    String strResponse = response.body().string();

                    try {
                        create_account_object.response_fail_error error = gson.fromJson(
                                strResponse,
                                create_account_object.response_fail_error.class
                        );
                        for (Map.Entry<String, List<String>> errorEntrySet : error.error.entrySet()) {
                            throw new CreateAccountException(errorEntrySet.getValue().get(0));
                        }
                    } catch (JsonSyntaxException e) {  // 解析失败，直接抛出原有的内容
                        throw new CreateAccountException(strResponse);
                    }
                }

                return ErrorCode.ERROR_SERVER_RESPONSE_FAIL;
            }
        } catch (IOException e) {
            e.printStackTrace();

            return ErrorCode.ERROR_NETWORK_FAIL;
        }

        if (createAccountResponse.account != null) {
            return 0;
        } else {
            if (createAccountResponse.error.base.isEmpty() == false) {
                String strError = createAccountResponse.error.base.get(0);
                throw new CreateAccountException(strError);
            }
            return ErrorCode.ERROR_SERVER_CREATE_ACCOUNT_FAIL;
        }
    }

}
