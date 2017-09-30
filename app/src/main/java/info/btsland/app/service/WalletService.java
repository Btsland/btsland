package info.btsland.app.service;

import java.util.List;

import info.btsland.app.model.Wallet;

/**
 * author：lw1000
 * function：钱包接口
 * 2017/9/29.
 */

public interface WalletService {
    /**
     *获取钱包数据
     */
    List<Wallet> getwallet();

}
