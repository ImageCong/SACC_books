package org.microservice.pub.commons.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class RecommendBook implements Serializable {
    private Integer id;
    private Integer userCode;
    private String recommendBook;
    private Float rating;
    private Integer type;
}
