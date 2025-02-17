package com.bisson2000.portalbuildercreate.portalbuilder;

import com.bisson2000.portalbuildercreate.PortalBuilderCreate;
import com.google.gson.*;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;



public class JsonDeserializerWithOptions<T> implements JsonDeserializer<T>
{
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface FieldRequired {}

    @Override
    public T deserialize(JsonElement json, Type type, JsonDeserializationContext jdc)
            throws JsonParseException {

        // Parsing object as usual.
        T pojo = new Gson().fromJson(json, type);

        // Getting all fields of the class and checking if all required ones were provided.
        checkRequiredFields(pojo.getClass().getDeclaredFields(), pojo);

        // All checks are ok.
        return pojo;
    }

    private void checkRequiredFields(@NonNull Field[] fields, @NonNull Object pojo)
            throws JsonParseException {
        // Checking nested list items too.
        if (pojo instanceof List pojoList) {
            for (final Object pojoListPojo : pojoList) {
                checkRequiredFields(pojoListPojo.getClass().getDeclaredFields(), pojoListPojo);
            }
        }

        for (Field f : fields) {
            // If some field has required annotation.
            if (f.getAnnotation(FieldRequired.class) != null) {
                try {
                    // Trying to read this field's value and check that it truly has value.
                    f.setAccessible(true);
                    Object fieldObject = f.get(pojo);
                    if (fieldObject == null) {
                        // Required value is null - throwing error.
                        String errorString = String.format("%1$s: Required field is missing! : %2$s -> %3$s", PortalBuilderCreate.MOD_ID, pojo.getClass().getSimpleName(), f.getName());
                        PortalBuilderCreate.LOGGER.error(errorString);
                        throw new JsonParseException(errorString);
                    } else {
                        checkRequiredFields(fieldObject.getClass().getDeclaredFields(), fieldObject);
                    }
                }

                // Exceptions while reflection.
                catch (IllegalArgumentException | IllegalAccessException e) {
                    PortalBuilderCreate.LOGGER.error(PortalBuilderCreate.MOD_ID, e);
                }
            }
        }
    }
}
