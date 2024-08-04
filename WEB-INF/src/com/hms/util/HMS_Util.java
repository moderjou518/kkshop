/**
 * 
 */

package com.hms.util;

import java.util.HashMap;
import java.util.Hashtable;

import com.hms.entity.Actions.Action;
import com.hms.entity.HMS_TBLE;
import com.hms.entity.HMS_TROW;

/**
 * @author 290098 採 singleton
 */
public class HMS_Util {

	// 各種資料來源存放地點
	private static Hashtable allHashBean = new Hashtable();

	// 策略．使用靜態屬性(static property)，讓外界取的唯一的 instance。
	private static HMS_Util hmsUtil;

	// 策略．將預設建構子封閉起來，讓外界無法 new() 建立 Instance。
	private HMS_Util() {
	}

	// 外界只能以 getter() 取得類別的 instance
	public static HMS_Util getOne() {

		if (hmsUtil == null) {
			hmsUtil = new HMS_Util();
		}
		return hmsUtil;
	}

	// set dataTable bean
	public void setDTB(String keyID, HMS_TBLE obj) {
		if (this.allHashBean.containsKey(keyID)) {
			this.allHashBean.remove(keyID);
		}
		this.allHashBean.put(keyID, obj);
	}

	// get dataTable bean
	public HMS_TBLE getDTB(String keyID) {
		if (this.allHashBean.containsKey(keyID)) {
			return (HMS_TBLE) this.allHashBean.get(keyID);
		}
		return null;
	}

	// set dataRow bean
	public void setDRB(String keyID, HMS_TROW obj) {
		if (this.allHashBean.containsKey(keyID)) {
			this.allHashBean.remove(keyID);
		}
		this.allHashBean.put(keyID, obj);
	}

	// get dataRow bean
	public HMS_TROW getDRB(String keyID) {

		if (this.allHashBean.containsKey(keyID)) {
			return (HMS_TROW) this.allHashBean.get(keyID);
		}
		return null;
	}

	public void setAction(String keyID, Action act) {
		if (this.allHashBean.containsKey(keyID)) {
			this.allHashBean.remove(keyID);
		}
		this.allHashBean.put(keyID, act);
	}
	
	public Action getAction(String keyID){
		if (this.allHashBean.containsKey(keyID)) {
			return (Action) this.allHashBean.get(keyID);
		}
		return null;
	}

}
