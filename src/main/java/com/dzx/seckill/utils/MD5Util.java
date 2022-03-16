package com.dzx.seckill.utils;

import com.dzx.seckill.conts.CommonConst;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * MD5工具类
 */
public class MD5Util {

    private static final String SALT = "1a2b3c4d";
    public  static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    /**
     *  加密初始密码，加密方式与前端一致，使得加密后密码与前端传来的密码一致
     * @param passwd: 初始密码
     */
    public static String encryptForm(String passwd){
        return md5("" + SALT.charAt(0) + SALT.charAt(2) + passwd + SALT.charAt(5) + SALT.charAt(4));
    }

    /**
     *加密前端传来的密码，然后存储到数据库中
     * @param passwd: 密码
     */
    public static String encryptDB(String passwd,String salt){
        return md5(salt+passwd );
    }

    /**
     * MD5加密初始密码
     * @param inputPass：密码
     * @param salt：直接使用用户名
     */
    public static String inputPassToDBPass(String inputPass,String salt){
        String fromPass = encryptForm(inputPass);
        return encryptDB(fromPass,salt);
    }

    public static void main(String[] args) {
        System.out.println(inputPassToDBPass("dzx123456","13256773444"));
    }
}
