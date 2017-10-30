package info.btsland.app;

import org.junit.Test;

import info.btsland.app.api.websocket_api;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        websocket_api wsapi = new websocket_api("wss://bitshares.openledger.info/ws");
        wsapi.connect();
        wsapi.get_ticker();
    }
}