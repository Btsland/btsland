package info.btsland.app;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

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
//        websocket_api wsapi =new websocket_api("wss://bitshares.dacplay.org/ws");
//        wsapi.connect();
//        wsapi.get_ticker();
            websocket_api websocket_api=new websocket_api("wss://bitshares.dacplay.org/ws");
            websocket_api.connect();
            websocket_api.get_ticker();

    }


//    public static void main(String args[])
//
//    {
//
//        String[] addrs= {"www.baidu.com"};
//
//        if (addrs.length < 1) {
//            System.out.println("syntax Error!");
//        } else {
//            for(int i=0;i<addrs.length;i++){
//                String line = null;
//                try{
//
//                    Process pro = Runtime.getRuntime().exec("ping " + addrs+" -l 1000 -n 4");
//
//                    BufferedReader buf = new BufferedReader(new InputStreamReader(pro.getInputStream()));
//                    while((line = buf.readLine()) != null){
//                        System.out.println(line);
//                        int position=0;
//                        if((position=line.indexOf("Average"))>=0) {
//                            System.out.println(line);
////                            line.substring(position+10,line.lastIndexOf("ms"));
////                            String value=line.substring(position+10,line.lastIndexOf("ms"));
////                            System.out.println("your speed is:"+(1000/Integer.parseInt(value))+"KB");
//                        }
//                    }
//                }catch(Exception ex) {
//                    System.out.println(ex.getMessage());
//                }
//            }
//        }
//    }

    public static void playRunTime(String cmd) throws Exception {
        Process p = Runtime.getRuntime().exec("ping " + cmd+" -l 1000 -n 4");
        InputStream is = p.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        p.waitFor();
        is.close();
        reader.close();
        p.destroy();
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