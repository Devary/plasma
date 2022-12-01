/*
 * Copyright (c) 2020. Fakher Hammami | Plasma Project
 */

package services.parsing.TypeService;

import hierarchy.persistence.types.Link;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import services.parsing.ParsingService;
import services.processing.VirtualLinkCreationProcess;

import java.io.File;
import java.io.IOException;

public class JsonParsingService extends ParsingService {
    private static Logger logger = Logger.getLogger(JsonParsingService.class);

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
        logger.warn(message);
    }

}
