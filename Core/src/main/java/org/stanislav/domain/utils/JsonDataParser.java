package org.stanislav.domain.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component("jsonParser")
public class JsonDataParser implements ApiDataParser {

    private final ObjectMapper objectMapper;


    public JsonDataParser(@Autowired ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Override
    public <T> T parseObject(String source, Class<T> clas) {
        try {
            String json = extract(source);
            return objectMapper.readValue(json, clas);
        } catch (JsonProcessingException e) {
            //TODO
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public <T> T parseObject(String source, Class<T> clas, String... layers) {
        try {
            String json = extract(source, layers);
            return objectMapper.readValue(json, clas);
        } catch (JsonProcessingException e) {
            //TODO
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public <T> List<T> parseObjectsList(String source, Class<T> clas) {
        try {
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            String json = extract(source);
            return objectMapper.readValue(json, typeFactory.constructCollectionType(List.class, clas));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            //TODO
            return null;
        }
    }

    @Override
    public <T> List<T> parseObjectsList(String source, Class<T> clas, String... layers) {
        try {
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            String json = extract(source, layers);
            return objectMapper.readValue(json, typeFactory.constructCollectionType(List.class, clas));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            //TODO
            return null;
        }
    }

    @Override
    public Map<String, Object> getJsonMap(String source, String... layers) {
        try {
            if (layers != null && layers.length != 0) {
                source = extract(source, layers);

            }
            return objectMapper.readValue(source, new TypeReference<>(){});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            //TODO
            return null;
        }
    }

    @Override
    public String toJson(Object o) {
        try {
            return objectMapper.writer().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            //TODO
            return null;
        }
    }

    private String extract(String source) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(source);
        return jsonNode.toString();
    }

    private String extract(String source, String... layers) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(source);
        for (String s : layers) {
            jsonNode = jsonNode.get(s);
        }
        return jsonNode.toString();
    }
}