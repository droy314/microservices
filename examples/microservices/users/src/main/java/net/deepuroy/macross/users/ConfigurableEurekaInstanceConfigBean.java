package net.deepuroy.macross.users;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;

@ConfigurationProperties("eureka.instance")
public class ConfigurableEurekaInstanceConfigBean extends
		EurekaInstanceConfigBean {

	private static final Log logger = LogFactory
			.getLog(ConfigurableEurekaInstanceConfigBean.class);

	private String preferIp4Segment;

	private String[] hostInfo;

	private boolean preferIpAddress = false;

	@Override
	public String getHostname() {
		return super.getHostName(false);
	}

	@Override
	public String getHostName(boolean refresh) {
		if (refresh || hostInfo == null) {
			hostInfo = convert(resolveHostInfo());
		}
		return (preferIpAddress) ? hostInfo[1] : hostInfo[0];
	}

	@Override
	public String getIpAddress() {
		if (hostInfo == null) {
			hostInfo = convert(resolveHostInfo());
		}
		return hostInfo[1];
	}

	private String[] convert(InetAddress addr) {
		return new String[] { addr.getHostName(), addr.getHostAddress() };
	}

	private InetAddress resolveHostInfo() {
		logger.info("preferIpAddress=" + preferIpAddress + " preferIp4Segment="
				+ preferIp4Segment);
		InetAddress ipadd = null;
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface
					.getNetworkInterfaces();
			preferIp4Segment = normalize(preferIp4Segment);
			logger.debug("preferIpAddress=" + preferIpAddress
					+ " preferIp4Segment=" + preferIp4Segment);
			if (!preferIp4Segment.isEmpty()) {
				ipadd = getPreferredAddress(networkInterfaces);
			} else {
				ipadd = getFirstNonLoopbackAddress(networkInterfaces);
			}
		} catch (SocketException ex) {
			logger.error("Could not resolve a preferred address", ex);
			ipadd = getLocalHostAddress();
		}
		logger.info("Resolved address " + ipadd.getHostName() + "("
				+ ipadd.getHostAddress() + ")");
		return ipadd;
	}

	private String normalize(String ipSegment) {
		ipSegment = (ipSegment == null) ? "" : ipSegment.trim();
		if (!ipSegment.isEmpty()) {
			// Add separator at the end because we do a startsWith later. 1
			// should not match 11.
			ipSegment = ipSegment + ".";
		}
		return ipSegment;
	}

	private InetAddress getFirstNonLoopbackAddress(
			Enumeration<NetworkInterface> enumNic) throws SocketException {
		for (; enumNic.hasMoreElements();) {
			NetworkInterface ifc = enumNic.nextElement();
			if (ifc.isUp()) {
				for (Enumeration<InetAddress> enumAddr = ifc.getInetAddresses(); enumAddr
						.hasMoreElements();) {
					InetAddress address = enumAddr.nextElement();
					if (address instanceof Inet4Address
							&& !address.isLoopbackAddress()) {
						return address;
					}
				}
			}
		}
		return getLocalHostAddress();
	}

	private InetAddress getLocalHostAddress() {
		try {
			return InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			logger.warn("Unable to retrieve localhost");
		}
		return null;
	}

	private InetAddress getPreferredAddress(Enumeration<NetworkInterface> nics)
			throws SocketException {
		while (nics.hasMoreElements()) {
			NetworkInterface nic = nics.nextElement();
			if (nic.isUp()) {
				for (InterfaceAddress addr : nic.getInterfaceAddresses()) {
					InetAddress ipaddr = addr.getAddress();
					if (ipaddr instanceof Inet4Address
							&& isPreferredIp4Segment(ipaddr)) {
						return ipaddr;
					}
				}
			}
		}
		return getLocalHostAddress();
	}

	private boolean isPreferredIp4Segment(InetAddress ipaddr) {
		String ip = ipaddr.getHostAddress();
		return ip.startsWith(preferIp4Segment);
	}

	public String getPreferIp4Segment() {
		return preferIp4Segment;
	}

	public void setPreferIp4Segment(String preferIp4Segment) {
		this.preferIp4Segment = preferIp4Segment;
	}

	public boolean isPreferIpAddress() {
		return preferIpAddress;
	}

	public void setPreferIpAddress(boolean preferIpAddress) {
		this.preferIpAddress = preferIpAddress;
	}

}
