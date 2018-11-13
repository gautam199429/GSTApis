package com.codecube.gst.entity;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.annotations.ApiModel;

@ApiModel
@Document(collection="products")
public class GSTR1Model {
	String gstin;
	public String getGstin() {
		return gstin;
	}
	public void setGstin(String gstin) {
		this.gstin = gstin;
	}
	public String getFp() {
		return fp;
	}
	public void setFp(String fp) {
		this.fp = fp;
	}
	public String getGt() {
		return gt;
	}
	public void setGt(String gt) {
		this.gt = gt;
	}
	public String getCur_gt() {
		return cur_gt;
	}
	public void setCur_gt(String cur_gt) {
		this.cur_gt = cur_gt;
	}

	String fp;
	String gt;
	String cur_gt;
	
	
	public GSTR1Model(List<GSTR1Model> gstmodel) {
		super();
		
		this.b2b = getB2b();
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("unchecked")
	public List<GSTRN1_b2b> b2b = (List<GSTRN1_b2b>) new GSTRN1_b2b();
	public List<GSTRN1_b2b> getB2b() {
		return b2b;
	}
	public void setB2b(List<GSTRN1_b2b> b2b) {
		this.b2b = b2b;
	}
	/**
	 * B2B DATA
	 * @author gautam kumar sah
	 *
	 */
	public class GSTRN1_b2b
	{
		public String ctin;

		public String getCtin() {
			return ctin;
		}

		public void setCtin(String ctin) {
			this.ctin = ctin;
		}
		@SuppressWarnings("unchecked")
		public List<GSTRN1_b2b_inv> inv = (List<GSTRN1_b2b_inv>) new GSTRN1_b2b_inv();
		
	}
	public class GSTRN1_b2b_inv
	{
		public String inum;
		public String getInum() {
			return inum;
		}
		public void setInum(String inum) {
			this.inum = inum;
		}
		public String getIdt() {
			return idt;
		}
		public void setIdt(String idt) {
			this.idt = idt;
		}
		public int getVal() {
			return val;
		}
		public void setVal(int val) {
			this.val = val;
		}
		public String getPos() {
			return pos;
		}
		public void setPos(String pos) {
			this.pos = pos;
		}
		public String getRchrg() {
			return rchrg;
		}
		public void setRchrg(String rchrg) {
			this.rchrg = rchrg;
		}
		public String getEtin() {
			return etin;
		}
		public void setEtin(String etin) {
			this.etin = etin;
		}
		public String getInv_typ() {
			return inv_typ;
		}
		public void setInv_typ(String inv_typ) {
			this.inv_typ = inv_typ;
		}
		public float getDiff_percentage() {
			return diff_percentage;
		}
		public void setDiff_percentage(float diff_percentage) {
			this.diff_percentage = diff_percentage;
		}
		public String idt;
		public int val;
		public String pos;
		public String rchrg;
		public String etin;
		public String inv_typ;
		public float diff_percentage;	
		@SuppressWarnings("unchecked")
		public List<GSTRN1_b2b_inv_item> itms = (List<GSTRN1_b2b_inv_item>) new GSTRN1_b2b_inv_item();
	}
	public class GSTRN1_b2b_inv_item
	{
		public int num;
		public int getNum() {
			return num;
		}
		public void setNum(int num) {
			this.num = num;
		}
		public GSTRN1_b2b_inv_item_det item_det = new GSTRN1_b2b_inv_item_det();
	}
	public class GSTRN1_b2b_inv_item_det
	{
		public int rt;
		public int txval;
		public int getRt() {
			return rt;
		}
		public void setRt(int rt) {
			this.rt = rt;
		}
		public int getTxval() {
			return txval;
		}
		public void setTxval(int txval) {
			this.txval = txval;
		}
		public int getIamt() {
			return iamt;
		}
		public void setIamt(int iamt) {
			this.iamt = iamt;
		}
		public int getCsamt() {
			return csamt;
		}
		public void setCsamt(int csamt) {
			this.csamt = csamt;
		}
		public int iamt;
		public  int csamt;
	}

}
