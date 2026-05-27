package com.wipro.layers.bean;

import org.springframework.stereotype.Component;

@Component
public class Customer {

    private int cid;
    private String cname;

    public Customer() {
        this.cid = 1001;
        this.cname = "Poojitha";
    }

		public int getCid() {
			return cid;
		}
		public void setCid(int cid) {
			this.cid = cid;
		}
		public String getCname() {
			return cname;
		}
		public void setCname(String cname) {
			this.cname = cname;
		}
		

		 @Override
		    public String toString() {
		        return "Customer [cid=" + cid + ", cname=" + cname + "]";
		    }

		
		

}
