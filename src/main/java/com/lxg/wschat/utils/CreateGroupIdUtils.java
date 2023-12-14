/**
 * Copyright (c) 2023, CCSSOFT All Rights Reserved.
 */
package com.lxg.wschat.utils;

import java.util.Random;
import java.util.UUID;

/**
 * CreateGroupIdUtils
 *
 * @author linxugeng
 * @since 2023/12/7
 */

public class CreateGroupIdUtils {
    public static Long createGroupId() {
        Random random = new java.util.Random();
        String nanoRandom = System.nanoTime() + "" + random.nextInt(99999);
        int hash = Math.abs(UUID.randomUUID().hashCode());
        int needAdd = 19 - String.valueOf(hash).length() + 1;
        return Long.valueOf(hash + "" + nanoRandom.substring(needAdd));
    }

}
