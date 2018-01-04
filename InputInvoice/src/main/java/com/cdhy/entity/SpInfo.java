package com.cdhy.entity;

public class SpInfo {

    private int xh;//序号
    private String fphxz="0";//发票行性质
    private String spmc;//商品名称
    private String spsm="";
    private String ggxh="";
    private String dw;
    private String spsl;//商品数量
    private String dj;//单价
    private String hsdj;//含税单价
    
	private String je;//金额
    private String sl;//税率
    private String se;//税额
    private String hsje;//含税金额
	private String hsbz="0";//含税标志
    private String spbm;//商品编码
    private String zxbm="";//纳税人自行编码
    private String yhzcbs="1";//优惠政策标识
    private String lslbs="2";//零税率标识
    private String zzstsgl="免税";//增值税特殊管理
    public int getXh() {
        return xh;
    }
    public void setXh(int xh) {
        this.xh = xh;
    }
    public String getFphxz() {
        return fphxz;
    }
    public void setFphxz(String fphxz) {
        this.fphxz = fphxz;
    }
    public String getSpmc() {
        return spmc;
    }
    public void setSpmc(String spmc) {
        this.spmc = spmc;
    }
    public String getSpsm() {
        return spsm;
    }
    public void setSpsm(String spsm) {
        this.spsm = spsm;
    }
    public String getGgxh() {
        return ggxh;
    }
    public void setGgxh(String ggxh) {
        this.ggxh = ggxh;
    }
    public String getDw() {
        return dw;
    }
    public void setDw(String dw) {
        this.dw = dw;
    }
    public String getSpsl() {
        return spsl;
    }
    public void setSpsl(String spsl) {
        this.spsl = spsl;
    }
    public String getDj() {
        return dj;
    }
    public void setDj(String dj) {
        this.dj = dj;
    }
    public String getHsdj() {
		return hsdj;
	}
	public void setHsdj(String hsdj) {
		this.hsdj = hsdj;
	}
    public String getJe() {
        return je;
    }
    public void setJe(String je) {
        this.je = je;
    }
    public String getSl() {
        return sl;
    }
    public void setSl(String sl) {
        this.sl = sl;
    }
    public String getSe() {
        return se;
    }
    public void setSe(String se) {
        this.se = se;
    }
    public String getHsje() {
		return hsje;
	}
	public void setHsje(String hsje) {
		this.hsje = hsje;
	}
    public String getHsbz() {
        return hsbz;
    }
    public void setHsbz(String hsbz) {
        this.hsbz = hsbz;
    }
    public String getSpbm() {
        return spbm;
    }
    public void setSpbm(String spbm) {
        this.spbm = spbm;
    }
    public String getZxbm() {
        return zxbm;
    }
    public void setZxbm(String zxbm) {
        this.zxbm = zxbm;
    }
    public String getYhzcbs() {
        return yhzcbs;
    }
    public void setYhzcbs(String yhzcbs) {
        this.yhzcbs = yhzcbs;
    }
    public String getLslbs() {
        return lslbs;
    }
    public void setLslbs(String lslbs) {
        this.lslbs = lslbs;
    }
    public String getZzstsgl() {
        return zzstsgl;
    }
    public void setZzstsgl(String zzstsgl) {
        this.zzstsgl = zzstsgl;
    }
	@Override
	public String toString() {
		return "SpInfo [xh=" + xh + ", fphxz=" + fphxz + ", spmc=" + spmc + ", spsm=" + spsm + ", ggxh=" + ggxh
				+ ", dw=" + dw + ", spsl=" + spsl + ", dj=" + dj + ", hsdj=" + hsdj + ", je=" + je + ", sl=" + sl
				+ ", se=" + se + ", hsje=" + hsje + ", hsbz=" + hsbz + ", spbm=" + spbm + ", zxbm=" + zxbm + ", yhzcbs="
				+ yhzcbs + ", lslbs=" + lslbs + ", zzstsgl=" + zzstsgl + "]";
	}
    
    

}
