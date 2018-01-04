package info.btsland.exchange.scoket;


import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import info.btsland.app.BtslandApplication;
import info.btsland.exchange.entity.Chat;
import info.btsland.exchange.utils.GsonDateAdapter;

/**
 * Created by Administrator on 2018/1/2 0002.
 */

public class IChatOnMessageListener implements C2CScoket.OnMessageListener {



    private Gson gson;

    public IChatOnMessageListener() {
        init();
    }

    public void init(){
        GsonBuilder gsonBuilder=new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class,new GsonDateAdapter());
        gson=gsonBuilder.create();
    }

    @Override
    public void onMessage(String message) {
        Chat chat=gson.fromJson(message,Chat.class);
        Intent intent=new Intent("Chat");
        intent.putExtra("message",message);
        putChat(chat);
        LocalBroadcastManager.getInstance(BtslandApplication.getInstance()).sendBroadcast(intent);
    }

    private Boolean putChat(Chat chat){
        String key = chat.getFromUser()+"-"+chat.getToUser();
        if(chat==null){
            return false;
        }
        if(chat.getFromUser()==null||chat.getToUser()==null){
            return false;
        }
        if(BtslandApplication.chatListMap.get(key)!=null){
            BtslandApplication.chatListMap.get(key).add(chat);
            return true;
        }else {
            List<Chat> chats=new ArrayList<>();
            chats.add(chat);
            BtslandApplication.chatListMap.put(key,chats);
            return true;
        }
    }
}
