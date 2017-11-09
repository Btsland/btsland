package info.btsland.app.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class authority {
    private Integer weight_threshold;
    public HashMap<object_id<account_object>, Integer> account_auths = new HashMap<>();
    private List<List<String>> key_auths =new ArrayList<>();
    private List<List<String>> address_auths =new ArrayList<>();

//    public authority(int nWeightThreshold, types.public_key_type publicKeyType, int nWeightType) {
//        weight_threshold = nWeightThreshold;
//        key_auths.put(publicKeyType, nWeightType);
//    }

//    public boolean is_public_key_type_exist(types.public_key_type publicKeyType) {
//        return key_auths.containsKey(publicKeyType);
//    }
//
//    public List<types.public_key_type> get_keys() {
//        List<types.public_key_type> listKeyType = new ArrayList<>();
//        listKeyType.addAll(key_auths.keySet());
//        return listKeyType;
//    }
}
