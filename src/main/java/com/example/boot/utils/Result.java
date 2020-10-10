package com.example.boot.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value = "Result", description = "返回结果")
public class Result<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "返回代码", allowableValues = "0,1")
	private int code;

	@ApiModelProperty(value = "返回消息")
	private String msg;

	@ApiModelProperty(value = "返回数据")
	private T data;

	public Result() {
		setCode(Constants.RESULT_TYPE_SUCCESS);
		setMsg(Constants.RESULT_MSG_SUCCESS);
	}
	
	public Result(T data) {
		setCode(Constants.RESULT_TYPE_SUCCESS);
		setMsg(Constants.RESULT_MSG_SUCCESS);
		setData(data);
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
