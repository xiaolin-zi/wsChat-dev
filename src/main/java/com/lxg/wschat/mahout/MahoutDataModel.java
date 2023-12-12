/**
 * Copyright (c) 2023, CCSSOFT All Rights Reserved.
 */
package com.lxg.wschat.mahout;

import io.swagger.models.auth.In;
import lombok.Data;

/**
 * MahoutDataModel
 *
 * @author linxugeng
 * @since 2023/12/11
 */
@Data
public class MahoutDataModel {
    private Long userId;
    private Long itemId;
    private Integer score;
}
