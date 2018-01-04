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
 * Created by Administrator on 2018/1/3 0003.
 */

public class GsonDateTimeAdaper implements JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dateResult = simpleDateFormat.parse(json.getAsString());

            return dateResult;
        } catch (ParseException e) {
            e.printStackTrace();
            throw new JsonParseException(e.getMessage() + json.getAsString());
        }
    }
}
