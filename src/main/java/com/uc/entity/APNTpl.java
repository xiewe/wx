package com.uc.entity;

public class APNTpl implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int apnId;
	/**
	 * 运营商标识
	 */
	private String oi;
	/**
	 * 网络标识
	 */
	private String ni;
	/**
	 * QCI
	 */
	private int qci;
	/**
	 * ARP 优先级
	 */
	private int ARPPrio;
	/**
	 * ARP抢占标识
	 */
	private int preEmptionCapability;
	/**
	 * ARP运行抢占标识
	 */
	private int preEmptionVulnerablity;
	/**
	 * PGW使用域
	 */
	private int vplmnDynamicAddrAllowed;
	/**
	 * 最大上行带宽
	 */
	private int maxBwUl;
	/**
	 * 最大下行带宽
	 */
	private int maxBwDl;
	/**
	 * PGW分配类型(使用域)
	 */
	private int pgwAllocationType;

	private String ipv4;
	private String ipv6;
	private String MIPHomeAgentHost;
	private String MIPHomeAgentRealm;

	/**
	 * 拜访网络标识
	 */
	private String vni;

	public int getApnId() {
		return apnId;
	}

	public void setApnId(int apnId) {
		this.apnId = apnId;
	}

	public String getOi() {
		return oi;
	}

	public void setOi(String oi) {
		this.oi = oi;
	}

	public String getNi() {
		return ni;
	}

	public void setNi(String ni) {
		this.ni = ni;
	}

	public int getQci() {
		return qci;
	}

	public void setQci(int qci) {
		this.qci = qci;
	}

	public int getPreEmptionCapability() {
		return preEmptionCapability;
	}

	public void setPreEmptionCapability(int preEmptionCapability) {
		this.preEmptionCapability = preEmptionCapability;
	}

	public int getPreEmptionVulnerablity() {
		return preEmptionVulnerablity;
	}

	public void setPreEmptionVulnerablity(int preEmptionVulnerablity) {
		this.preEmptionVulnerablity = preEmptionVulnerablity;
	}

	public int getVplmnDynamicAddrAllowed() {
		return vplmnDynamicAddrAllowed;
	}

	public void setVplmnDynamicAddrAllowed(int vplmnDynamicAddrAllowed) {
		this.vplmnDynamicAddrAllowed = vplmnDynamicAddrAllowed;
	}

	public int getMaxBwUl() {
		return maxBwUl;
	}

	public void setMaxBwUl(int maxBwUl) {
		this.maxBwUl = maxBwUl;
	}

	public int getMaxBwDl() {
		return maxBwDl;
	}

	public void setMaxBwDl(int maxBwDl) {
		this.maxBwDl = maxBwDl;
	}

	public int getPgwAllocationType() {
		return pgwAllocationType;
	}

	public void setPgwAllocationType(int pgwAllocationType) {
		this.pgwAllocationType = pgwAllocationType;
	}

	public String getIpv4() {
		return ipv4;
	}

	public void setIpv4(String ipv4) {
		this.ipv4 = ipv4;
	}

	public String getIpv6() {
		return ipv6;
	}

	public void setIpv6(String ipv6) {
		this.ipv6 = ipv6;
	}

	public String getMIPHomeAgentHost() {
		return MIPHomeAgentHost;
	}

	public void setMIPHomeAgentHost(String mIPHomeAgentHost) {
		MIPHomeAgentHost = mIPHomeAgentHost;
	}

	public String getMIPHomeAgentRealm() {
		return MIPHomeAgentRealm;
	}

	public void setMIPHomeAgentRealm(String mIPHomeAgentRealm) {
		MIPHomeAgentRealm = mIPHomeAgentRealm;
	}

	public String getVni() {
		return vni;
	}

	public void setVni(String vni) {
		this.vni = vni;
	}

	public int getARPPrio() {
		return ARPPrio;
	}

	public void setARPPrio(int aRPPrio) {
		ARPPrio = aRPPrio;
	}

}
