package info.btsland.exchange.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/12/13.
 */

public class GsonDateAdapter implements JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonElement json,
                            Type typeOfT,
                            JsonDeserializationContext context) throws JsonParseException {
        Date dateResult =null;
        try {
            dateResult=new Date(Long.decode(json.getAsString()));
        }catch (Exception e){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                dateResult = simpleDateFormat.parse(json.getAsString());
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        }
        return dateResult;
    }
}
