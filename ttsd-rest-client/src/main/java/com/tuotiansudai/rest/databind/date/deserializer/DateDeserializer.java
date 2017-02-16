package com.tuotiansudai.rest.databind.date.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.tuotiansudai.rest.utils.DateUtils;

import java.io.IOException;
import java.util.Date;

public class DateDeserializer extends JsonDeserializer<Date> {

    @Override
    public final Date deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        try {
            ObjectCodec codec = jp.getCodec();
            JsonNode tree = codec.readTree(jp);
            return deserializeObject(tree);
        }
        catch (Exception ex) {
            if (ex instanceof IOException) {
                throw (IOException) ex;
            }
            throw new JsonMappingException("Object deserialize error", ex);
        }
    }

    protected Date deserializeObject(JsonNode tree) throws IOException {
        return DateUtils.deserializeDate(tree.textValue());
    }
}
