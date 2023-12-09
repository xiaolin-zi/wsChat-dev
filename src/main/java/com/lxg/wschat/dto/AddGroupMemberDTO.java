/**
 * Copyright (c) 2023, CCSSOFT All Rights Reserved.
 */
package com.lxg.wschat.dto;

import lombok.Data;

import java.util.List;

/**
 * AddGroupMemberVo
 *
 * @author linxugeng
 * @since 2023/12/6
 */
@Data
public class AddGroupMemberDTO {
    List<String> userIds;
    String groupId;
}
