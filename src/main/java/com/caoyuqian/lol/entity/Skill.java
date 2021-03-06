package com.caoyuqian.lol.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 技能
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skill {

    private String content;//技能内容

    private String skillName; //技能名称

    private String imgUrl;  //技能图片


}
