package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: 用户注册信息表
 * @Author: jeecg-boot
 * @Date:   2021-03-10
 * @Version: V1.0
 */
@Data
@TableName("sys_user_register_record")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sys_user_register对象", description="用户注册信息表")
public class SysUserRegisterRecord implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**邀请人id*/
	@Excel(name = "邀请人id", width = 15)
    @ApiModelProperty(value = "邀请人id")
    private String userId;
	/**被邀请人id*/
	@Excel(name = "被邀请人id", width = 15)
    @ApiModelProperty(value = "被邀请人id")
    private String byUserId;
	/**注册类型*/
	@Excel(name = "注册类型", width = 15, dicCode = "register_type")
	@Dict(dicCode = "register_type")
    @ApiModelProperty(value = "注册类型")
    private String registerType;
	/**注册方式*/
	@Excel(name = "注册方式", width = 15, dicCode = "register_mode")
	@Dict(dicCode = "register_mode")
    @ApiModelProperty(value = "注册方式")
    private String registerMode;
	/**注册时间*/
	@Excel(name = "注册时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "注册时间")
    private java.util.Date registerTime;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
}
