package info.btsland.app.api;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2017/10/30.
 */
public class websocket_apiTest {
    @Test
    public void get_ticker() throws Exception {
        websocket_api wsapi = new websocket_api("wss://bitshares.openledger.info/ws");
        wsapi.connect();
        wsapi.get_ticker();
    }

}