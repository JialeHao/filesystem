package com.haojiale.filesystem.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Description TODO
 * @Author haojiale
 * @Date 2018/12/14 18:54
 * @Version 1.0
 **/
@MappedSuperclass
@Data
public abstract class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    @Column(name = "create_user")
    private Long createUser;//创建用户id

    @Column(name = "update_user")
    private Long updateUser;//更新用户id

    @Column(name = "create_time")
    private Timestamp createTime;//创建时间

    @Column(name = "update_time")
    private Timestamp updateTime;//更新时间

}
