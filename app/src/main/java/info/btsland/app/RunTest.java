package info.btsland.app;


/*
 * 第一步： connect
 * 第二步： login
 * 第三部： 获取 _nDatabaseId, _nHistoryId, _nBroadcastId
 * 第四部： 上面准备好了之后，才能去获取数据
 * 注意：因为用了websocket，通信是异步的，需要同步。
 * 建议使用官方的那个wallet的库，要不然要写很多通信的代码。
 */
public class RunTest {
    public static void main(String[] args) {
        websocket_api wsapi = new websocket_api("wss://bitshares.openledger.info/ws");
        wsapi.connect();
        wsapi.get_ticker();
    }
}

/*
输出:
onOpen
{"id":1,"result":true}
{"id":2,"result":2}
{"id":3,"result":3}
{"id":4,"result":4}
{"id":5,"result":{"base":"CNY","quote":"BTS","latest":"0.35963660303664197","lowest_ask":"0.35954989999999998","highest_bid":"0.35819999789455226","percent_change":"0.45895020103956519","base_volume":"620791.76359999633859843","quote_volume":"1734263.93803999619558454"}}
*/