package com.hundsun.zjfae.common.http.converter;

import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import com.google.protobuf.Parser;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.aes.FixHeader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.annotations.Nullable;
import okhttp3.ResponseBody;
import retrofit2.Converter;

public class CustomProtoResponseBodyConverter<T extends MessageLite>
        implements Converter<ResponseBody, T> {
    private final Parser<T> parser;
    private final @Nullable
    ExtensionRegistryLite registry;
    private static final int INDEX = 16;

    CustomProtoResponseBodyConverter(Parser<T> parser, @Nullable ExtensionRegistryLite registry) {
        this.parser = parser;
        this.registry = registry;
    }

    @Override public T convert(ResponseBody value) throws IOException {
        byte [] responseByte = value.bytes();
        FixHeader fixHeader = FixHeader.parseFrom(responseByte);
        byte [] byteArray = protoBufByteData(responseByte,fixHeader.getExtsize(),fixHeader.getBodysize());
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        try {
            return parser.parseFrom(inputStream, registry);
        } catch (InvalidProtocolBufferException e) {
            CCLog.e(e.getMessage());
            // Despite extending IOException, this is data mismatch.
            throw new RuntimeException(e);
        } finally {
            value.close();
        }
    }

    private byte[] protoBufByteData(byte [] data, int begin, int count){
        begin = begin+INDEX;
        byte[] bs = new byte[count];
        for (int i=begin; i<begin+count; i++){
            bs[i-begin] = data[i];
        }
        return bs;
    }
}
