package com.graduatioinProject.sensorMonitoring.member.dto;

import com.graduatioinProject.sensorMonitoring.baseUtil.config.DateConfig;
import com.graduatioinProject.sensorMonitoring.member.entity.Member;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class MemberReq {
	@ApiModelProperty(required = true)
	private String userId;
	@ApiModelProperty(required = true)
	private String password;
	@ApiModelProperty(required = true)
	private String employeeNumber;
	@ApiModelProperty(required = true)
	private String phoneNumber;
	@ApiModelProperty(required = true)
	private String name;

	public Member toEntity(String encryptedPw) {
		return Member.builder()
				.userId(userId)
				.password(encryptedPw)
				.employeeNumber(employeeNumber)
				.phoneNumber(phoneNumber)
				.name(name)
				.registDate(new DateConfig().getNowDate())
				.updateDate(new DateConfig().getNowDate())
				.signupType("E")
				.activateYn("Y")
				.build();
	}
}
