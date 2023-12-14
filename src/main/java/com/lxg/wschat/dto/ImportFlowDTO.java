/**
 * Copyright (c) 2023, CCSSOFT All Rights Reserved.
 */
package com.lxg.wschat.dto;

import lombok.Data;

import java.util.List;

/**
 * ImportFlowDTO
 *
 * @author linxugeng
 * @since 2023/12/12
 */
@Data
public class ImportFlowDTO {
    List<String> userIds;
    String userId;

}
