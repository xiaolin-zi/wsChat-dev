/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.lxg.wschat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ChatTOAIDTO
 *
 * @author linxugeng
 * @since 2024/1/3
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatTOAIDTO {
    String userId;
    List<AIMessgaeDTO> messageDTOList;
}
