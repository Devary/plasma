package services.parsing.TypeService;

import Hierarchy.persistence.types.Link;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class JsonParsingService {
    public void parseLinkToJson(Link link)
    {
        String message;
        JSONObject json = new JSONObject();
        json.put("name",link.getName());
        json.put("collection_type",link.getCollectionType());
        json.put("element_type",link.getName());
        json.put("reference_integrity_check",link.getName());
        json.put("inverse_name",link.getName());
        message = json.toString();
        File file = new File("C:/parsedJson/"+link.getParent()+"_LINK_"+link.getName());
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(message);
    }

}
