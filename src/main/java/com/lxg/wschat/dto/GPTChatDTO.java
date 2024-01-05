/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.lxg.wschat.dto;

import lombok.Data;

import java.util.List;

/**
 * GPTChatDTO
 *
 * @author linxugeng
 * @since 2024/1/4
 */
@Data
public class GPTChatDTO {
    String model;
    List<AIMessgaeDTO> messageDTOList;
}
