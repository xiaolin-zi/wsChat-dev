/**
 * Copyright (c) 2023, CCSSOFT All Rights Reserved.
 */
package com.lxg.wschat.utils;

import java.util.UUID;

/**
 * CreateGroupIdUtils
 *
 * @author linxugeng
 * @since 2023/12/7
 */

public class CreateGroupIdUtils {
    public static String createGroupId() {
       return "GP"+ UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
}
