package com.tuotiansudai.rest.databind.date.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.tuotiansudai.rest.databind.date.utils.DateUtils;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.boot.jackson.JsonObjectDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;

@JsonComponent
public class LocalDateTimeDeserializer extends JsonObjectDeserializer<LocalDateTime> {
    @Override
    protected LocalDateTime deserializeObject(JsonParser jsonParser, DeserializationContext context, ObjectCodec codec, JsonNode tree) throws IOException {
        return DateUtils.deserializeLocalDateTime(tree.textValue());
    }
}
