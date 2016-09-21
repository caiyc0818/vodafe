package com.huawei.vodafone.bean;

import java.io.Serializable;

public class DialogInfo implements Serializable {

	private String channel;

	private int id;

	private String title;

	private String content;

	private String confirm;

	private String cancel;

	public int getId() {
		return id;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getConfirm() {
		return confirm;
	}

	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}

	public String getCancel() {
		return cancel;
	}

	public void setCancel(String cancel) {
		this.cancel = cancel;
	}

}
