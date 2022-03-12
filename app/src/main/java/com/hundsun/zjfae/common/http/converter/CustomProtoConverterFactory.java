package com.hundsun.zjfae.common.http.converter;

import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.MessageLite;
import com.google.protobuf.Parser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import io.reactivex.annotations.Nullable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class CustomProtoConverterFactory extends Converter.Factory {
    public static CustomProtoConverterFactory create() {
        return new CustomProtoConverterFactory(null);
    }

    /** Create an instance which uses {@code registry} when deserializing. */
    public static CustomProtoConverterFactory createWithRegistry(@Nullable ExtensionRegistryLite registry) {
        return new CustomProtoConverterFactory(registry);
    }

    private final @Nullable ExtensionRegistryLite registry;

    private CustomProtoConverterFactory(@Nullable ExtensionRegistryLite registry) {
        this.registry = registry;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {



        if (!(type instanceof Class<?>)) {
            return null;
        }
        Class<?> c = (Class<?>) type;
        if (!MessageLite.class.isAssignableFrom(c)) {
            return null;
        }

        Parser<MessageLite> parser;
        try {
            Method method = c.getDeclaredMethod("parser");
            //noinspection unchecked
            parser = (Parser<MessageLite>) method.invoke(null);

        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
        } catch (NoSuchMethodException | IllegalAccessException ignored) {
            // If the method is missing, fall back to original static field for pre-3.0 support.
            try {
                Field field = c.getDeclaredField("PARSER");
                //noinspection unchecked
                parser = (Parser<MessageLite>) field.get(null);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new IllegalArgumentException("Found a protobuf message but "
                        + c.getName()
                        + " had no parser() method or PARSER field.");
            }
        }
        CustomProtoResponseBodyConverter converter = new CustomProtoResponseBodyConverter<>(parser, registry);
        return converter;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        if (!(type instanceof Class<?>)) {
            return null;
        }
        if (!MessageLite.class.isAssignableFrom((Class<?>) type)) {
            return null;
        }
        return new CustomProtoRequestBodyConverter<>();
    }
}
