package hms.admin.gui;

import java.util.Vector;

public class test {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Vector<String> CancelWorkOrederID = new Vector<String>();
		CancelWorkOrederID.add("peding");
		CancelWorkOrederID.add("RGH1464");
		CancelWorkOrederID.add(null);
		CancelWorkOrederID.add("null");
		for(int i=0;i<CancelWorkOrederID.size();i++) {
			if(CancelWorkOrederID.get(i)!=null && CancelWorkOrederID.get(i).toLowerCase().contains("rgh")) {
				System.out.println(i+" yes");
//				continue;
			}
		
			System.out.println(i+" no");
		}
	}

}
