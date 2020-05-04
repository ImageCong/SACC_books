package org.microservice.pub.commons.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Book implements Serializable {
    private Integer id;
    private Integer bookCode;
    private String bookName;
}
