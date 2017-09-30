package info.btsland.app.service.Impl;

import java.util.ArrayList;
import java.util.List;

import info.btsland.app.model.Wallet;
import info.btsland.app.service.WalletService;

/**
 * author：
 * function：
 * 2017/9/30.
 */

public class WalletServiceImpl implements WalletService {
    @Override
    public List<Wallet> getwallet() {
        List<Wallet>  walletList=new ArrayList<>();
        walletList.add(new Wallet("0.0002 BTC","10 BTS","0.000001%"));
        walletList.add(new Wallet("28484.032 BTS","28484 BTS","0.0011%"));
        walletList.add(new Wallet("2,326.6224 bitUSD","29,007 BTS","0.0729%"));
        walletList.add(new Wallet("0.32063534 NBT","4 BTS","0.0064%"));
        walletList.add(new Wallet("4,675 BTCX","23 BTS","0.2227%"));
        walletList.add(new Wallet("19,803.2555 bitCNY","35,457 BTS","0.0344%"));
        walletList.add(new Wallet("0.3039ICOO","2 0 BTS","0.0001%"));
        walletList.add(new Wallet("1 WHALESHARE","4BTS","0.0007%"));
        walletList.add(new Wallet("257 OBITS","2,183 BTS","0.0016%"));
        walletList.add(new Wallet("0.1 bitSILVER","22 BTS","0s.0011%"));

        return walletList;
    }

}

