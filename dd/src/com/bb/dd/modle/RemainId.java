package com.bb.dd.modle;

public class RemainId {

	private String bikeSiteId;
	private int status;
	public RemainId() {
		
	}
	public String getBikeSiteId() {
		return bikeSiteId;
	}
	public void setBikeSiteId(String bikeSiteId) {
		this.bikeSiteId = bikeSiteId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public RemainId(String bikeSiteId, int status) {
		this.bikeSiteId = bikeSiteId;
		this.status = status;
	}
	@Override
	public boolean equals(Object o) {
		if(null!=o&&o instanceof RemainId){
			RemainId remainId=(RemainId)o;
			if(remainId.getBikeSiteId().equals(this.getBikeSiteId())&&remainId.getStatus()==this.getStatus()){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}


	
}
