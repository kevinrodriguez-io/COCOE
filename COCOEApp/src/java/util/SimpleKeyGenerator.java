/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 *
 * @author Kevin Rodriguez
 */
public class SimpleKeyGenerator implements KeyGenerator {
    @Override
    public Key generateKey() {
        String keyString = "F8MI0oWq2VPXTX0Y!@#6RwWyXaBPTc2T";
        Key key = new SecretKeySpec(keyString.getBytes(), 0, keyString.getBytes().length, "DES");
        return key;
    }
}