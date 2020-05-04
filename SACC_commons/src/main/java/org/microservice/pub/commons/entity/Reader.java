package org.microservice.pub.commons.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Reader implements Serializable {
    private Integer id;
    private Integer userCode;
    private Integer bookCode;
    private Integer viewTimes;
}
