/**
 * Copyright (c) 2023, CCSSOFT All Rights Reserved.
 */
package com.lxg.wschat.vo;

import com.lxg.wschat.domain.Group;
import lombok.Data;

/**
 * GroupInfoVO
 *
 * @author linxugeng
 * @since 2023/12/7
 */
@Data
public class GroupInfoVO extends Group {

    /**
     * 群主
     */
    private UserInfoVO owner;

    /**
     * 群成员数量
     */
    private Long memberCount;




}
