package info.btsland.exchange.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by Administrator on 2017/12/13.
 */

public class GsonDateAdapter implements JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonElement json,
                            Type typeOfT,
                            JsonDeserializationContext context) throws JsonParseException {
        Date dateResult = new Date(Long.decode(json.getAsString()));
        return dateResult;

    }

}
