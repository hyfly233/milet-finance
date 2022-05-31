package com.hyfly.milet.seq.codec;

/**
 * @author hyfly
 */
public interface IBodyCodec {

    /**
     * obj --> byte[]
     *
     * @param obj obj
     * @param <T> t
     * @return byte
     * @throws Exception e
     */
    <T> byte[] serialize(T obj) throws Exception;


    /**
     * byte[] --> obj
     *
     * @param bytes bytes
     * @param clazz clazz
     * @param <T>   t
     * @return t
     * @throws Exception e
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception;
}
