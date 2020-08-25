package com.pwc.modules.input.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pwc.common.utils.excel.annotation.ExcelField;

import java.io.Serializable;
import java.util.Date;

/**
 * 保养
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-08-07 14:55:19
 */
@TableName("input_maintain")
public class InputMaintainEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 序号
	 */
	@TableId
	@ExcelField(title="序号", align=1, sort=1)
	private Integer id;
	/**
	 * 设备编号
	 */
	@ExcelField(title="设备编号", align=1, sort=1)
	private String equipmentNum;
	/**
	 * 安检点编号
	 */
	@ExcelField(title="安检点编号", align=1, sort=1)
	private String checkpointNum;
	/**
	 * 中队
	 */
	@ExcelField(title="中队", align=1, sort=1)
	private String team;
	/**
	 * 线路
	 */
	@ExcelField(title="线路", align=1, sort=1)
	private String line;
	/**
	 * 车站
	 */
	@ExcelField(title="车站", align=1, sort=1)
	private String station;
	/**
	 * 寻呼号
	 */
	@ExcelField(title="寻呼号", align=1, sort=1)
	private String callNum;
	/**
	 * 设备型号
	 */
	@ExcelField(title="设备型号", align=1, sort=1)
	private String model;
	/**
	 * 维保公司
	 */
	private Integer dealer;
	/**
	 * 保养类型0：季保 1：年保
	 */
	@ExcelField(title="设备类型", align=1, sort=1,dictType = "maintainType")
	private Integer type;
	/**
	 * 保养内容
	 */
	@ExcelField(title="保养内容", align=1, sort=1)
	private String content;
	/**
	 * 保养建议
	 */
	@ExcelField(title="保养建议", align=1, sort=1)
	private String advise;
	/**
	 * 保养人id
	 */
	private Integer maintainUserId;
	/**
	 * 保养人
	 */
	@ExcelField(title="保养人", align=1, sort=1)
	private String maintainUser;
	/**
	 * 开始时间
	 */
	@ExcelField(title="开始时间", align=1, sort=1)
	private Date maintainStartTime;
	/**
	 * 结束时间
	 */
	@ExcelField(title="结束时间", align=1, sort=1)
	private Date maintainEndTime;
	/**
	 * 保养图片
	 */
	private String img;
	/**
	 * 状态 0：未审核 1：已审核
	 */
	@ExcelField(title="状态", align=1, sort=1,dictType = "maintainStatus")
	private Integer status;
	/**
	 * 确认人
	 */
	@ExcelField(title="确认人", align=1, sort=1)
	private String confirmUser;
	/**
	 * 确认时间
	 */
	@ExcelField(title="确认时间", align=1, sort=1)
	private Date confirmTime;

	/**
	 * 质保
	 */
	@ExcelField(title="质保情况", align=1, sort=1)
	private String warranty;

	@TableField(exist = false)
	@ExcelField(title="维保公司", align=1, sort=1)
	private String dealerName;

	@TableField(exist = false)
	private Date begin;

	@TableField(exist = false)
	private Date end;

	/**
	 * 设置：序号
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：序号
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：设备编号
	 */
	public void setEquipmentNum(String equipmentNum) {
		this.equipmentNum = equipmentNum;
	}
	/**
	 * 获取：设备编号
	 */
	public String getEquipmentNum() {
		return equipmentNum;
	}
	/**
	 * 设置：安检点编号
	 */
	public void setCheckpointNum(String checkpointNum) {
		this.checkpointNum = checkpointNum;
	}
	/**
	 * 获取：安检点编号
	 */
	public String getCheckpointNum() {
		return checkpointNum;
	}
	/**
	 * 设置：中队
	 */
	public void setTeam(String team) {
		this.team = team;
	}
	/**
	 * 获取：中队
	 */
	public String getTeam() {
		return team;
	}
	/**
	 * 设置：线路
	 */
	public void setLine(String line) {
		this.line = line;
	}
	/**
	 * 获取：线路
	 */
	public String getLine() {
		return line;
	}
	/**
	 * 设置：车站
	 */
	public void setStation(String station) {
		this.station = station;
	}
	/**
	 * 获取：车站
	 */
	public String getStation() {
		return station;
	}
	/**
	 * 设置：寻呼号
	 */
	public void setCallNum(String callNum) {
		this.callNum = callNum;
	}
	/**
	 * 获取：寻呼号
	 */
	public String getCallNum() {
		return callNum;
	}
	/**
	 * 设置：设备型号
	 */
	public void setModel(String model) {
		this.model = model;
	}
	/**
	 * 获取：设备型号
	 */
	public String getModel() {
		return model;
	}
	/**
	 * 设置：维保公司
	 */
	public void setDealer(Integer dealer) {
		this.dealer = dealer;
	}
	/**
	 * 获取：维保公司
	 */
	public Integer getDealer() {
		return dealer;
	}
	/**
	 * 设置：保养类型0：季保 1：年保
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：保养类型0：季保 1：年保
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：保养内容
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 获取：保养内容
	 */
	public String getContent() {
		return content;
	}
	/**
	 * 设置：保养建议
	 */
	public void setAdvise(String advise) {
		this.advise = advise;
	}
	/**
	 * 获取：保养建议
	 */
	public String getAdvise() {
		return advise;
	}
	/**
	 * 设置：保养人id
	 */
	public void setMaintainUserId(Integer maintainUserId) {
		this.maintainUserId = maintainUserId;
	}
	/**
	 * 获取：保养人id
	 */
	public Integer getMaintainUserId() {
		return maintainUserId;
	}
	/**
	 * 设置：保养人
	 */
	public void setMaintainUser(String maintainUser) {
		this.maintainUser = maintainUser;
	}
	/**
	 * 获取：保养人
	 */
	public String getMaintainUser() {
		return maintainUser;
	}
	/**
	 * 设置：开始时间
	 */
	public void setMaintainStartTime(Date maintainStartTime) {
		this.maintainStartTime = maintainStartTime;
	}
	/**
	 * 获取：开始时间
	 */
	public Date getMaintainStartTime() {
		return maintainStartTime;
	}
	/**
	 * 设置：结束时间
	 */
	public void setMaintainEndTime(Date maintainEndTime) {
		this.maintainEndTime = maintainEndTime;
	}
	/**
	 * 获取：结束时间
	 */
	public Date getMaintainEndTime() {
		return maintainEndTime;
	}
	/**
	 * 设置：保养图片
	 */
	public void setImg(String img) {
		this.img = img;
	}
	/**
	 * 获取：保养图片
	 */
	public String getImg() {
		return img;
	}
	/**
	 * 设置：状态 0：未审核 1：已审核
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态 0：未审核 1：已审核
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：确认人
	 */
	public void setConfirmUser(String confirmUser) {
		this.confirmUser = confirmUser;
	}
	/**
	 * 获取：确认人
	 */
	public String getConfirmUser() {
		return confirmUser;
	}
	/**
	 * 设置：确认时间
	 */
	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}
	/**
	 * 获取：确认时间
	 */
	public Date getConfirmTime() {
		return confirmTime;
	}

	public String getWarranty() {
		return warranty;
	}

	public void setWarranty(String warranty) {
		this.warranty = warranty;
	}

	public String getDealerName() {
		return dealerName;
	}

	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	public Date getBegin() {
		return begin;
	}

	public void setBegin(Date begin) {
		this.begin = begin;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
}
