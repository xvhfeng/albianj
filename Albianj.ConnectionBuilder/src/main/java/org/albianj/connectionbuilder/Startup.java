package org.albianj.connectionbuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.albianj.security.DESCoder;

public class Startup {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			System.out.println("请输入需要加密的原始字符串(退出请输入：fuckjava回车):");
			String s = null;
			try {

				s = br.readLine();
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

			if (s.equals("fuckjava")) {
				System.out.println("fuck java & will out");
				return;
			}

			try {
				String des = DESCoder.encrypt(s);
				String os = DESCoder.decrypt(des);
				if (os.equals(s)) {
					System.out.println(String.format(
							"加密的字符串是：%1$s,加密后的字符串是:%2$s.", s, des));
				} else {
					System.out.println(String.format("加密的字符串：%1$s无法加密。", s));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}

		}

	}

}
