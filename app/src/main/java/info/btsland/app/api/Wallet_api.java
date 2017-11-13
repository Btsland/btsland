package info.btsland.app.api;

import android.util.Log;

import com.google.common.primitives.UnsignedInteger;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.btsland.app.BtslandApplication;
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
    private static final String TAG="Wallet_api";

    private Websocket_api mWebsocketApi;
    private sha512_object mCheckSum = new sha512_object();
    private wallet_object mWalletObject;
    private HashMap<types.public_key_type, types.private_key_type> mHashMapPub2Priv = new HashMap<>();
    class wallet_object {
        sha256_object chain_id;
        List<account_object> my_accounts = new ArrayList<>();
        ByteBuffer cipher_keys;
        HashMap<object_id<account_object>, List<types.public_key_type>> extra_keys = new HashMap<>();
        String ws_server = "";
        String ws_user = "";
        String ws_password = "";

        public void update_account(account_object accountObject) {
            boolean bUpdated = false;
            for (int i = 0; i < my_accounts.size(); ++i) {
                if (my_accounts.get(i).id == accountObject.id) {
                    my_accounts.remove(i);
                    my_accounts.add(accountObject);
                    bUpdated = true;
                    break;
                }
            }

            if (bUpdated == false) {
                my_accounts.add(accountObject);
            }
        }
    }
    public Wallet_api() {
        mWebsocketApi = BtslandApplication.getMarketStat().mWebsocketApi;
        initialize();
    }
    public int initialize() {
        int nRet = mWebsocketApi.connect();
        if (nRet == 0) {
            sha256_object sha256Object = null;
            try {
                sha256Object = mWebsocketApi.get_chain_id();
                if (mWalletObject == null) {
                    mWalletObject = new wallet_object();
                    mWalletObject.chain_id = sha256Object;
                } else if (mWalletObject.chain_id != null &&
                        mWalletObject.chain_id.equals(sha256Object) == false) {
                    nRet = -1;
                }
            } catch (NetworkStatusException e) {
                e.printStackTrace();
                nRet = -1;
            }
        }
        return nRet;
    }
    /**
     * 注册帐号
     * @param strAccountName
     * @param strPassword
     * @return
     * @throws NetworkStatusException
     * @throws CreateAccountException
     */
    public int create_account_with_password(String strAccountName,
                                            String strPassword) throws NetworkStatusException, CreateAccountException {
        /*String[] strAddress = {
                "https://bitshares.openledger.info/api/v1/accounts",
                "https://openledger.io/api/v1/accounts",
                "https://openledger.hk/api/v1/accounts"
        };*/
        //https://openledger.io/api/v1/accounts
        String[] strAddress = {"https://openledger.io/api/v1/accounts"};
        Log.i(TAG, "create_account_with_password: ");

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

    public account_object get_account(String strAccountName){
        account_object accountObject=null;
        List<String> names=new ArrayList<>();
        names.add(strAccountName);

        try {
            accountObject = mWebsocketApi.get_account_by_name(strAccountName);
        } catch (NetworkStatusException e) {
            e.printStackTrace();
        }
        return accountObject;
    }



    private int create_account_with_password(String strServerUrl,
                                            String strAccountName,
                                            String strPassword) throws NetworkStatusException, CreateAccountException {
        private_key privateActiveKey = private_key.from_seed(strAccountName + "active" + strPassword);
        private_key privateOwnerKey = private_key.from_seed(strAccountName + "owner" + strPassword);

        types.public_key_type publicActiveKeyType = new types.public_key_type(privateActiveKey.get_public_key());
        types.public_key_type publicOwnerKeyType = new types.public_key_type(privateOwnerKey.get_public_key());

        account_object accountObject = mWebsocketApi.get_account_by_name(strAccountName);
        if (accountObject != null) {
            return ErrorCode.ERROR_ACCOUNT_OBJECT_EXIST;
        }

        create_account_object createAccountObject = new create_account_object();
        createAccountObject.name = strAccountName;
        createAccountObject.active_key = publicActiveKeyType;
        createAccountObject.owner_key = publicOwnerKeyType;
        createAccountObject.memo_key = publicActiveKeyType;
        createAccountObject.refcode = null;
        createAccountObject.referrer = "btsland";
        Gson gson = global_config_object.getInstance().getGsonBuilder().create();

        String strAddress = strServerUrl;
        OkHttpClient okHttpClient = new OkHttpClient();

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json"),
                gson.toJson(createAccountObject)
        );
        Log.i(TAG, "create_account_with_password:requestBody: "+gson.toJson(createAccountObject));
        Request request = new Request.Builder()
                .url(strAddress)
                .addHeader("Accept", "application/json")
                .post(requestBody)
                .build();

        create_account_object.create_account_response createAccountResponse = null;
        try {
            Response response = okHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                String responseBody=response.body().string();
                Log.i(TAG, "create_account_with_password: response:"+responseBody);
                createAccountResponse = gson.fromJson(responseBody, create_account_object.create_account_response.class);
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
    public int set_passwrod(String strPassword) {
        mCheckSum = sha512_object.create_from_string(strPassword);

        return 0;
    }
    public account_object import_account_password(String strAccountName,
                                       String strPassword) throws NetworkStatusException {

        // try the wif key at first time, then use password model. this is from the js code.
        /*int nRet = import_key(strAccountName, strPassword);
        if (nRet == 0) {
            return nRet;
        }*/

        private_key privateActiveKey = private_key.from_seed(strAccountName + "active" + strPassword);
        private_key privateOwnerKey = private_key.from_seed(strAccountName + "owner" + strPassword);

        types.public_key_type publicActiveKeyType = new types.public_key_type(privateActiveKey.get_public_key());
        types.public_key_type publicOwnerKeyType = new types.public_key_type(privateOwnerKey.get_public_key());

        account_object accountObject = mWebsocketApi.get_account_by_name(strAccountName);
        if (accountObject == null) {
            return null;
        }

        if (accountObject.active.is_public_key_type_exist(publicActiveKeyType) == false &&
                accountObject.owner.is_public_key_type_exist(publicActiveKeyType) == false &&
                accountObject.active.is_public_key_type_exist(publicOwnerKeyType) == false &&
                accountObject.owner.is_public_key_type_exist(publicOwnerKeyType) == false){
            return null;
        }

        List<types.public_key_type> listPublicKeyType = new ArrayList<>();
        listPublicKeyType.add(publicActiveKeyType);
        listPublicKeyType.add(publicOwnerKeyType);
        mWalletObject.update_account(accountObject);
        mWalletObject.extra_keys.put(accountObject.toObject_id(accountObject.id), listPublicKeyType);
        mHashMapPub2Priv.put(publicActiveKeyType, new types.private_key_type(privateActiveKey));
        mHashMapPub2Priv.put(publicOwnerKeyType, new types.private_key_type(privateOwnerKey));

        encrypt_keys();

        // 保存至文件

        return accountObject;
    }
    static class plain_keys {
        Map<types.public_key_type, String> keys;
        sha512_object checksum;

        public void write_to_encoder(base_encoder encoder) {
            raw_type rawType = new raw_type();

            rawType.pack(encoder, UnsignedInteger.fromIntBits(keys.size()));
            for (Map.Entry<types.public_key_type, String> entry : keys.entrySet()) {
                encoder.write(entry.getKey().key_data);

                byte[] byteValue = entry.getValue().getBytes();
                rawType.pack(encoder, UnsignedInteger.fromIntBits(byteValue.length));
                encoder.write(byteValue);
            }
            encoder.write(checksum.hash);
        }

        public static plain_keys from_input_stream(InputStream inputStream) {
            plain_keys keysResult = new plain_keys();
            keysResult.keys = new HashMap<>();
            keysResult.checksum = new sha512_object();

            raw_type rawType = new raw_type();
            UnsignedInteger size = rawType.unpack(inputStream);
            try {
                for (int i = 0; i < size.longValue(); ++i) {
                    types.public_key_type publicKeyType = new types.public_key_type();
                    inputStream.read(publicKeyType.key_data);

                    UnsignedInteger strSize = rawType.unpack(inputStream);
                    byte[] byteBuffer = new byte[strSize.intValue()];
                    inputStream.read(byteBuffer);

                    String strPrivateKey = new String(byteBuffer);

                    keysResult.keys.put(publicKeyType, strPrivateKey);
                }

                inputStream.read(keysResult.checksum.hash);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return keysResult;
        }


    }
    private void encrypt_keys() {
        plain_keys data = new plain_keys();
        data.keys = new HashMap<>();
        for (Map.Entry<types.public_key_type, types.private_key_type> entry : mHashMapPub2Priv.entrySet()) {
            data.keys.put(entry.getKey(), entry.getValue().toString());
        }
        data.checksum = mCheckSum;

        datastream_size_encoder sizeEncoder = new datastream_size_encoder();
        data.write_to_encoder(sizeEncoder);
        datastream_encoder encoder = new datastream_encoder(sizeEncoder.getSize());
        data.write_to_encoder(encoder);

        byte[] byteKey = new byte[32];
        System.arraycopy(mCheckSum.hash, 0, byteKey, 0, byteKey.length);
        byte[] ivBytes = new byte[16];
        System.arraycopy(mCheckSum.hash, 32, ivBytes, 0, ivBytes.length);

        ByteBuffer byteResult = aes.encrypt(byteKey, ivBytes, encoder.getData());

        mWalletObject.cipher_keys = byteResult;

        return;

    }
}
