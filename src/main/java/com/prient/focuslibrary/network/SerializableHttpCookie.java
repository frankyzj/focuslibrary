package com.prient.focuslibrary.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.HttpCookie;

/**
 * 作者：prient
 * 时间：2018/9/14 10:50
 * 功能介绍：
 * 修改时间：
 * 修改说明：
 */
public class SerializableHttpCookie implements Serializable {
    private static final String TAG = "SerializableHttpCookie";
    private static final long serialVersionUID = 6374381323722046732L;

    //transient 关键词修饰的变量不会被默认序列化
    private transient HttpCookie cookie;

    // Workaround httpOnly: The httpOnly attribute is not accessible so when we
    // serialize and deserialize the cookie it not preserve the same value. We
    // need to access it using reflection
    private Field fieldHttpOnly;

    public SerializableHttpCookie() {
    }

    /**
     * 把cookie编码成十六进制
     */
    public String encode(HttpCookie cookie) {
        if (cookie == null) return null;
        this.cookie = cookie;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
            outputStream.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return byteArrayToHexString(byteArrayOutputStream.toByteArray());
    }

    /**
     * 把编码过的十六进制字符串解析成cookie
     */
    public HttpCookie decode(String encodedCookie) {
        byte[] bytes = hexStringToByteArray(encodedCookie);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        HttpCookie cookie = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            cookie = ((SerializableHttpCookie) objectInputStream.readObject()).cookie;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return cookie;
    }

    private boolean getHttpOnly() {
        try {
            initFieldHttpOnly();
            return (boolean) fieldHttpOnly.get(cookie);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void setHttpOnly(boolean httpOnly){
        try {
            initFieldHttpOnly();
            fieldHttpOnly.set(cookie, httpOnly);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void initFieldHttpOnly() throws NoSuchFieldException {
        fieldHttpOnly = cookie.getClass().getDeclaredField("httpOnly");
        fieldHttpOnly.setAccessible(true);
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(cookie.getName());
        out.writeObject(cookie.getValue());
        out.writeObject(cookie.getComment());
        out.writeObject(cookie.getCommentURL());
        out.writeObject(cookie.getDomain());
        out.writeLong(cookie.getMaxAge());
        out.writeObject(cookie.getPath());
        out.writeObject(cookie.getPortlist());
        out.writeInt(cookie.getVersion());
        out.writeBoolean(cookie.getSecure());
        out.writeBoolean(cookie.getDiscard());
        out.writeBoolean(getHttpOnly());
    }

    private void readObject(ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        String name = (String) in.readObject();
        String value = (String) in.readObject();
        cookie = new HttpCookie(name, value);
        cookie.setComment((String) in.readObject());
        cookie.setCommentURL((String) in.readObject());
        cookie.setDomain((String) in.readObject());
        cookie.setMaxAge(in.readLong());
        cookie.setPath((String) in.readObject());
        cookie.setPortlist((String) in.readObject());
        cookie.setVersion(in.readInt());
        cookie.setSecure(in.readBoolean());
        cookie.setDiscard(in.readBoolean());
        setHttpOnly(in.readBoolean());
    }

    /**
     * Using some super basic byte array &lt;-&gt; hex conversions so we don't
     * have to rely on any large Base64 libraries. Can be overridden if you
     * like!
     *
     * @param bytes byte array to be converted
     * @return string containing hex values
     */
    private String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte element : bytes) {
            int v = element & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString();
    }

    /**
     * Converts hex values from strings to byte array
     *
     * @param hexString string of hex-encoded values
     * @return decoded byte array
     */
    private byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character
                    .digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }
}
