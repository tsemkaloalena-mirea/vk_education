package com.tsemkalo.homework8;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@SuppressWarnings("NotNullNullableValidation")
@Provider
public final class MyObjectMapperProvider implements ContextResolver<ObjectMapper> {
    final ObjectMapper defaultObjectMapper;

    public MyObjectMapperProvider() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        this.defaultObjectMapper = objectMapper;
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return this.defaultObjectMapper;
    }
}
