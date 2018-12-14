package com.haojiale.filesystem.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Table(name = "file_info")
public class FileInfo extends BaseEntity {

    @Column(name = "path")
    private String path;//文件路径

    @Column(name = "size")
    private Long size;//文件大小

    @Column(name = "type")
    private String type;//文件类型

    @Column(name = "name")
    private String name;//文件名称

    @Column(name = "width")
    private BigDecimal width;//文件宽度

    @Column(name = "height")
    private BigDecimal height;//文件高度

    @Column(name = "business_tag")
    private String businessTag;//业务标签

    @Column(name = "business_id")
    private Long businessId;//业务ID

    @Column(name = "meta_data")
    private String metaData;//文件元数据

}
