package com.ymrs.spirit.ffx.pub;

public class EasyUIDragTreeReq {
	private String point;
	private Long targetId;
	private Long targetPid;
	private Integer targetShowOrder;
	private Long sourceId;
	private Long updateUser;

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public Long getTargetPid() {
		return targetPid;
	}

	public void setTargetPid(Long targetPid) {
		this.targetPid = targetPid;
	}

	public Integer getTargetShowOrder() {
		return targetShowOrder;
	}

	public void setTargetShowOrder(Integer targetShowOrder) {
		this.targetShowOrder = targetShowOrder;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public Long getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(Long updateUser) {
		this.updateUser = updateUser;
	}

	@Override
	public String toString() {
		return "EasyUIDragTreeReq [point=" + point + ", targetId=" + targetId + ", targetPid=" + targetPid
				+ ", targetShowOrder=" + targetShowOrder + ", sourceId=" + sourceId + "]";
	}

}
