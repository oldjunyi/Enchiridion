/** FROM: http://ovaraksin.blogspot.co.uk/2011/05/json-with-gson-and-abstract-classes.html **/

package joshie.enchiridion.wiki;
 
import java.lang.reflect.Type;

import joshie.enchiridion.lib.EnchiridionInfo;
import joshie.enchiridion.wiki.elements.Element;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
 
public class WikiAbstractAdapter implements JsonSerializer<Element>, JsonDeserializer<Element> {
    @Override
    public JsonElement serialize(Element src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getSimpleName()));
        result.add("properties", context.serialize(src, src.getClass()));
 
        return result;
    }
 
    @Override
    public Element deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement element = jsonObject.get("properties");
 
        try {
            return context.deserialize(element, Class.forName(EnchiridionInfo.JAVAPATH + "wiki.elements." + type));
        } catch (ClassNotFoundException cnfe) {
            throw new JsonParseException("Unknown element type: " + type, cnfe);
        }
    }
}