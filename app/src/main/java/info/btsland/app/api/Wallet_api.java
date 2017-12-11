package info.btsland.app.api;

import android.text.TextUtils;
import android.util.Log;

import com.google.common.primitives.UnsignedInteger;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.bitcoinj.core.ECKey;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import de.bitsharesmunich.graphenej.BrainKey;
import de.bitsharesmunich.graphenej.FileBin;
import de.bitsharesmunich.graphenej.models.backup.LinkedAccount;
import de.bitsharesmunich.graphenej.models.backup.WalletBackup;
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
    private static int buyTimeSec = 5 * 365 * 24 * 60 * 60;
    private static final String TAG="Wallet_api";
    private static final String BTSLAND_FAUCET = "faucet.btsland.info";
    private static final String OPENLEDGER_FAUCET = "openledger.io";

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
        Log.i(TAG, "create_account_with_password: ");
        String[] strAddress = {"https://faucet.btsland.info/api/v1/accounts", "https://openledger.io/api/v1/accounts"};


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
    public boolean is_locked() {
        if(mWalletObject.cipher_keys!=null){
            if (mWalletObject.cipher_keys.array().length > 0 &&
                    mCheckSum.equals(new sha512_object())) {
                return true;
            }
            return false;
        }else {
            return true;
        }

    }
    public int unlock(String strPassword) {
        assert(strPassword.length() > 0);
        sha512_object passwordHash = sha512_object.create_from_string(strPassword);
        byte[] byteKey = new byte[32];
        System.arraycopy(passwordHash.hash, 0, byteKey, 0, byteKey.length);
        byte[] ivBytes = new byte[16];
        System.arraycopy(passwordHash.hash, 32, ivBytes, 0, ivBytes.length);

        ByteBuffer byteDecrypt = aes.decrypt(byteKey, ivBytes, mWalletObject.cipher_keys.array());
        if (byteDecrypt == null || byteDecrypt.array().length == 0) {
            return -1;
        }

        plain_keys dataResult = plain_keys.from_input_stream(
                new ByteArrayInputStream(byteDecrypt.array())
        );

        for (Map.Entry<types.public_key_type, String> entry : dataResult.keys.entrySet()) {
            types.private_key_type privateKeyType = new types.private_key_type(entry.getValue());
            mHashMapPub2Priv.put(entry.getKey(), privateKeyType);
        }

        mCheckSum = passwordHash;
        if (passwordHash.equals(dataResult.checksum)) {
            return 0;
        } else {
            return -1;
        }
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
    public signed_transaction cancel_order(object_id<limit_order_object> id)
            throws NetworkStatusException {
        operations.limit_order_cancel_operation op = new operations.limit_order_cancel_operation();
        op.fee_paying_account = mWebsocketApi.get_limit_order(id).seller;
        op.order = id;
        op.extensions = new HashSet<>();

        operations.operation_type operationType = new operations.operation_type();
        operationType.nOperationType = operations.ID_CANCEL_LMMIT_ORDER_OPERATION;
        operationType.operationContent = op;

        signed_transaction tx = new signed_transaction();
        tx.operations = new ArrayList<>();
        tx.operations.add(operationType);

        tx.extensions = new HashSet<>();
        set_operation_fees(tx, mWebsocketApi.get_global_properties().parameters.current_fees);

        return sign_transaction(tx);
    }


    private int create_account_with_password(String strServerUrl, String strAccountName, String strPassword)
            throws NetworkStatusException, CreateAccountException {

        // Trust All Certificates
        final TrustManager[] trustManagers = new TrustManager[]{new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] x509Certificates = new X509Certificate[0];
                return x509Certificates;
            }

            @Override
            public void checkServerTrusted(final X509Certificate[] chain,
                                           final String authType) throws CertificateException {
                Log.i(TAG, "authType: " + String.valueOf(authType));
            }

            @Override
            public void checkClientTrusted(final X509Certificate[] chain,
                                           final String authType) throws CertificateException {
                Log.i(TAG, "authType: " + String.valueOf(authType));
            }
        }};

        X509TrustManager x509TrustManager = new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] x509Certificates = new X509Certificate[0];
                return x509Certificates;
            }

            @Override
            public void checkServerTrusted(final X509Certificate[] chain,
                                           final String authType) throws CertificateException {
                Log.i(TAG, "authType: " + String.valueOf(authType));
            }

            @Override
            public void checkClientTrusted(final X509Certificate[] chain,
                                           final String authType) throws CertificateException {
                Log.i(TAG, "authType: " + String.valueOf(authType));
            }
        };

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

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        try {
            String PROTOCOL = "SSL";
            SSLContext sslContext = SSLContext.getInstance(PROTOCOL);
            KeyManager[] keyManagers = null;
            SecureRandom secureRandom = new SecureRandom();
            sslContext.init(keyManagers, trustManagers, secureRandom);
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            okHttpClientBuilder.sslSocketFactory(sslSocketFactory, x509TrustManager);
        } catch (Exception e) {
            e.printStackTrace();
        }

        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                Log.i(TAG, "hostname: " + String.valueOf(hostname));
                if (hostname.equals(BTSLAND_FAUCET) || hostname.equals(OPENLEDGER_FAUCET)) {
                    return true;
                }
                return false;
            }
        };

        okHttpClientBuilder.hostnameVerifier(hostnameVerifier);
        OkHttpClient okHttpClient = okHttpClientBuilder.build();

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json"),
                gson.toJson(createAccountObject)
        );
        Log.i(TAG, "create_account_with_password:requestBody: " + gson.toJson(createAccountObject));
        Request request = new Request.Builder()
                .url(strAddress)
                .addHeader("Accept", "application/json")
                .post(requestBody)
                .build();

        create_account_object.create_account_response createAccountResponse;
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
        set_passwrod(strPassword);
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
        mWalletObject.extra_keys.put(accountObject.id, listPublicKeyType);
        mHashMapPub2Priv.put(publicActiveKeyType, new types.private_key_type(privateActiveKey));
        mHashMapPub2Priv.put(publicOwnerKeyType, new types.private_key_type(privateOwnerKey));

        encrypt_keys();

        BtslandApplication.isLogin=false;

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
    public signed_transaction sell_asset(String amountToSell, String symbolToSell,
                                         String minToReceive, String symbolToReceive,
                                         int timeoutSecs, boolean fillOrKill)
            throws NetworkStatusException {
        // 这是用于出售的帐号
        account_object accountObject = BtslandApplication.accountObject;
        operations.limit_order_create_operation op = new operations.limit_order_create_operation();
        op.seller = accountObject.id;

        // 填充数据
        op.amount_to_sell =mWebsocketApi.lookup_asset_symbols(symbolToSell).amount_from_string(amountToSell);
        op.min_to_receive =mWebsocketApi.lookup_asset_symbols(symbolToReceive).amount_from_string(minToReceive);
        if (timeoutSecs > 0) {
            op.expiration = new Date(
                    System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(timeoutSecs));
        } else {
            op.expiration = new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(365));
        }
        op.fill_or_kill = fillOrKill;
        op.extensions = new HashSet<>();

        operations.operation_type operationType = new operations.operation_type();
        operationType.nOperationType = operations.ID_CREATE_LIMIT_ORDER_OPERATION;
        operationType.operationContent = op;

        signed_transaction tx = new signed_transaction();
        tx.operations = new ArrayList<>();
        tx.operations.add(operationType);

        tx.extensions = new HashSet<>();
        set_operation_fees(tx, mWebsocketApi.get_global_properties().parameters.current_fees);

        return sign_transaction(tx);
    }

    /**
     * @param symbolToSell 卖出的货币符号
     * @param symbolToReceive 买入的货币符号
     * @param rate 多少个<t>symbolToReceive</t>可以兑换一个<t>symbolToSell</t>
     * @param amount 要卖出多少个<t>symbolToSell</t>
     * @throws NetworkStatusException
     */
    public signed_transaction sell(String symbolToSell, String symbolToReceive, double rate,
                                   double amount) throws NetworkStatusException {
        return sell_asset(Double.toString(amount), symbolToSell, Double.toString(rate * amount),
                symbolToReceive, buyTimeSec, false);
    }

    public signed_transaction sell(String symbolToSell, String symbolToReceive, double rate,
                                   double amount, int timeoutSecs) throws NetworkStatusException {
        return sell_asset(Double.toString(amount), symbolToSell, Double.toString(rate * amount),
                symbolToReceive, timeoutSecs, false);
    }

    /**
     * @param symbolToReceive 买入的货币符号
     * @param symbolToSell 卖出的货币符号
     * @param rate 多少个<t>symbolToSell</t>可以兑换一个<t>symbolToReceive</t>
     * @param amount 要买入多少个<t>symbolToReceive</t>
     * @throws NetworkStatusException
     */
    public signed_transaction buy(String symbolToReceive, String symbolToSell, double rate,
                                  double amount) throws NetworkStatusException {
        return sell_asset(Double.toString(rate * amount), symbolToSell, Double.toString(amount),
                symbolToReceive, buyTimeSec, false);
    }

    public signed_transaction buy(String symbolToReceive, String symbolToSell, double rate,
                                  double amount, int timeoutSecs) throws NetworkStatusException {
        return sell_asset(Double.toString(rate * amount), symbolToSell, Double.toString(amount),
                symbolToReceive, timeoutSecs, false);
    }
    private void set_operation_fees(signed_transaction tx, fee_schedule feeSchedule) {
        for (operations.operation_type operationType : tx.operations) {
            feeSchedule.set_fee(operationType, price.unit_price(new object_id<asset_object>(0, asset_object.class)));
        }
    }
    private signed_transaction sign_transaction(signed_transaction tx) throws NetworkStatusException {
        // // TODO: 07/09/2017 这里的set应出问题
        signed_transaction.required_authorities requiresAuthorities = tx.get_required_authorities();

        Set<object_id<account_object>> req_active_approvals = new HashSet<>();
        req_active_approvals.addAll(requiresAuthorities.active);

        Set<object_id<account_object>> req_owner_approvals = new HashSet<>();
        req_owner_approvals.addAll(requiresAuthorities.owner);


        for (authority authorityObject : requiresAuthorities.other) {
            for (object_id<account_object> accountObjectId : authorityObject.account_auths.keySet()) {
                req_active_approvals.add(accountObjectId);
            }
        }

        Set<object_id<account_object>> accountObjectAll = new HashSet<>();
        accountObjectAll.addAll(req_active_approvals);
        accountObjectAll.addAll(req_owner_approvals);


        List<object_id<account_object>> listAccountObjectId = new ArrayList<>();
        listAccountObjectId.addAll(accountObjectAll);

        List<account_object> listAccountObject = mWebsocketApi.get_accounts(listAccountObjectId);
        HashMap<object_id<account_object>, account_object> hashMapIdToObject = new HashMap<>();
        for (account_object accountObject : listAccountObject) {
            hashMapIdToObject.put(accountObject.id, accountObject);
        }

        HashSet<types.public_key_type> approving_key_set = new HashSet<>();
        for (object_id<account_object> accountObjectId : req_active_approvals) {
            account_object accountObject = hashMapIdToObject.get(accountObjectId);
            approving_key_set.addAll(accountObject.active.get_keys());
        }

        for (object_id<account_object> accountObjectId : req_owner_approvals) {
            account_object accountObject = hashMapIdToObject.get(accountObjectId);
            approving_key_set.addAll(accountObject.owner.get_keys());
        }

        for (authority authorityObject : requiresAuthorities.other) {
            for (types.public_key_type publicKeyType : authorityObject.get_keys()) {
                approving_key_set.add(publicKeyType);
            }
        }

        // // TODO: 07/09/2017 被简化了
        dynamic_global_property_object dynamicGlobalPropertyObject = mWebsocketApi.get_dynamic_global_properties();
        tx.set_reference_block(dynamicGlobalPropertyObject.head_block_id);

        Date dateObject = dynamicGlobalPropertyObject.time;
        Calendar calender = Calendar.getInstance();
        calender.setTime(dateObject);
        calender.add(Calendar.SECOND, 30);

        dateObject = calender.getTime();

        tx.set_expiration(dateObject);

        for (types.public_key_type pulicKeyType : approving_key_set) {
            types.private_key_type privateKey = mHashMapPub2Priv.get(pulicKeyType);
            if (privateKey != null) {
                tx.sign(privateKey, mWalletObject.chain_id);
            }
        }

        // 发出tx，进行广播，这里也涉及到序列化
            int nRet = mWebsocketApi.broadcast_transaction(tx);
        if (nRet == 0) {
            return tx;
        } else {
            return null;
        }
    }
    public asset calculate_buy_fee(asset_object assetToReceive, asset_object assetToSell,
                                   double rate, double amount,
                                   global_property_object globalPropertyObject) {
        Log.e(TAG, "calculate_buy_fee: rate:"+rate );
        Log.e(TAG, "calculate_buy_fee: amount:"+amount+"Double.toString(rate * amount):"+Double.toString(rate * amount) );
        return calculate_sell_asset_fee(Double.toString(rate * amount), assetToSell,
                Double.toString(amount), assetToReceive, globalPropertyObject);
    }
    public asset calculate_sell_asset_fee(String amountToSell, asset_object assetToSell,
                                          String minToReceive, asset_object assetToReceive,
                                          global_property_object globalPropertyObject) {
        operations.limit_order_create_operation op = new operations.limit_order_create_operation();
        op.amount_to_sell = assetToSell.amount_from_string(amountToSell);
        op.min_to_receive = assetToReceive.amount_from_string(minToReceive);

        operations.operation_type operationType = new operations.operation_type();
        operationType.nOperationType = operations.ID_CREATE_LIMIT_ORDER_OPERATION;
        operationType.operationContent = op;

        signed_transaction tx = new signed_transaction();
        tx.operations = new ArrayList<>();
        tx.operations.add(operationType);

        tx.extensions = new HashSet<>();
        set_operation_fees(tx, globalPropertyObject.parameters.current_fees);

        return op.fee;
    }
    public List<operation_history_object> get_account_history(object_id<account_object> accountId,
                                                              object_id<operation_history_object> startId,
                                                              int nLimit) throws NetworkStatusException {
        return mWebsocketApi.get_account_history(accountId, startId, nLimit);
    }
    public signed_transaction transfer(String strFrom,
                                       String strTo,
                                       String strAmount,
                                       String strAssetSymbol,
                                       String strMemo) throws NetworkStatusException {

        object_id<asset_object> assetObjectId = object_id.create_from_string(strAssetSymbol);
        asset_object assetObject = null;
        if (assetObjectId == null) {
            assetObject = mWebsocketApi.lookup_asset_symbols(strAssetSymbol);
        } else {
            List<object_id<asset_object>> listAssetObjectId = new ArrayList<>();
            listAssetObjectId.add(assetObjectId);
            assetObject = mWebsocketApi.get_assets(listAssetObjectId).get(0);
        }

        account_object accountObjectFrom = get_account(strFrom);
        account_object accountObjectTo = get_account(strTo);
        if (accountObjectTo == null) {
            throw new NetworkStatusException("failed to get account object");
        }

        operations.transfer_operation transferOperation = new operations.transfer_operation();
        transferOperation.from = accountObjectFrom.id;
        transferOperation.to = accountObjectTo.id;
        transferOperation.amount = assetObject.amount_from_string(strAmount);
        transferOperation.extensions = new HashSet<>();
        if (TextUtils.isEmpty(strMemo) == false) {
            transferOperation.memo = new memo_data();
            transferOperation.memo.from = accountObjectFrom.options.memo_key;
            transferOperation.memo.to = accountObjectTo.options.memo_key;

            types.private_key_type privateKeyType = mHashMapPub2Priv.get(accountObjectFrom.options.memo_key);
            if (privateKeyType == null) {
                // // TODO: 07/09/2017 获取失败的问题
                throw new NetworkStatusException("failed to get private key");
            }
            transferOperation.memo.set_message(
                    privateKeyType.getPrivateKey(),
                    accountObjectTo.options.memo_key.getPublicKey(),
                    strMemo,
                    0
            );
            transferOperation.memo.get_message(
                    privateKeyType.getPrivateKey(),
                    accountObjectTo.options.memo_key.getPublicKey()
            );
        }

        operations.operation_type operationType = new operations.operation_type();
        operationType.nOperationType = operations.ID_TRANSER_OPERATION;
        operationType.operationContent = transferOperation;

        signed_transaction tx = new signed_transaction();
        tx.operations = new ArrayList<>();
        tx.operations.add(operationType);
        tx.extensions = new HashSet<>();
        set_operation_fees(tx, mWebsocketApi.get_global_properties().parameters.current_fees);


        //// TODO: 07/09/2017 tx.validate();
        return sign_transaction(tx);
    }

    /**
     * bin登录
     * @param strPassword
     * @param strFilePath
     * @return
     */
    public int import_file_bin(String strPassword,
                               String strFilePath) {
        File file = new File(strFilePath);
        if (file.exists() == false) {
            return ErrorCode.ERROR_FILE_NOT_FOUND;
        }

        int nSize = (int)file.length();

        final byte[] byteContent = new byte[nSize];

        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(byteContent, 0, byteContent.length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return ErrorCode.ERROR_FILE_NOT_FOUND;
        } catch (IOException e) {
            e.printStackTrace();
            return ErrorCode.ERROR_FILE_READ_FAIL;
        }

        WalletBackup walletBackup = FileBin.deserializeWalletBackup(byteContent, strPassword);
        if (walletBackup == null) {
            return ErrorCode.ERROR_FILE_BIN_PASSWORD_INVALID;
        }

        String strBrainKey = walletBackup.getWallet(0).decryptBrainKey(strPassword);
        //LinkedAccount linkedAccount = walletBackup.getLinkedAccounts()[0];

        int nRet = ErrorCode.ERROR_IMPORT_NOT_MATCH_PRIVATE_KEY;
        for (LinkedAccount linkedAccount : walletBackup.getLinkedAccounts()) {
            nRet = import_brain_key(linkedAccount.getName(), strPassword, strBrainKey);
            if (nRet == 0) {
                break;
            }
        }

        return nRet;
    }

    private int import_brain_key(String strAccountNameOrId,
                                String strPassword,
                                String strBrainKey) {
        set_passwrod(strPassword);
        try {
            int nRet = import_brain_key(strAccountNameOrId, strBrainKey);
            if (nRet != 0) {
                return nRet;
            }
        } catch (NetworkStatusException e) {
            e.printStackTrace();
            return ErrorCode.ERROR_NETWORK_FAIL;
        }

       // save_wallet_file();


        return 0;
    }




    private int import_brain_key(String strAccountNameOrId, String strBrainKey) throws NetworkStatusException {
        account_object accountObject = get_account(strAccountNameOrId);
        if (accountObject == null) {
            return ErrorCode.ERROR_NO_ACCOUNT_OBJECT;
        }

        Map<types.public_key_type, types.private_key_type> mapPublic2Private = new HashMap<>();
        for (int i = 0; i < 10; ++i) {
            BrainKey brainKey = new BrainKey(strBrainKey, i);
            ECKey ecKey = brainKey.getPrivateKey();
            private_key privateKey = new private_key(ecKey.getPrivKeyBytes());
            types.private_key_type privateKeyType = new types.private_key_type(privateKey);
            types.public_key_type publicKeyType = new types.public_key_type(privateKey.get_public_key());

            if (accountObject.active.is_public_key_type_exist(publicKeyType) == false &&
                    accountObject.owner.is_public_key_type_exist(publicKeyType) == false &&
                    accountObject.options.memo_key.compare(publicKeyType) == false) {
                continue;
            }
            mapPublic2Private.put(publicKeyType, privateKeyType);
        }

        if (mapPublic2Private.isEmpty() == true) {
            return ErrorCode.ERROR_IMPORT_NOT_MATCH_PRIVATE_KEY;
        }

        mWalletObject.update_account(accountObject);

        List<types.public_key_type> listPublicKeyType = new ArrayList<>();
        listPublicKeyType.addAll(mapPublic2Private.keySet());

        mWalletObject.extra_keys.put(accountObject.id, listPublicKeyType);
        mHashMapPub2Priv.putAll(mapPublic2Private);

        encrypt_keys();

        return 0;
    }




}
